package com.hyflicker.MOTD;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hyflicker.MOTD.Configuration.ModConfig;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;

import java.awt.*;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class UpdateAvailable {

    private static final String VERSION_API_URL = "https://github.hyflicker.com/api/v1/mod-version";
    private static final String currentVersion = loadVersionFromManifest();

    public static String loadVersionFromManifest() {
        try (var is = UpdateAvailable.class.getResourceAsStream("/manifest.json")) {
            if (is != null) {
                JsonObject json = JsonParser.parseReader(new InputStreamReader(is, StandardCharsets.UTF_8)).getAsJsonObject();
                return json.get("Version").getAsString();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static void checkOnJoin(PlayerReadyEvent event) {
        ModConfig.UpdateAvailable config = ModConfig.get().updateAvailable;
        if (!config.enabled) return;

        Player player = event.getPlayer();
        if (player.hasPermission(config.getPermGroup())) {
            fetchVersionFromMiddleware().thenAccept(latest -> {
                if (latest != null) {
                    // Normalize: strip the 'v' prefix if it exists
                    String normalizedLatest = latest.toLowerCase().startsWith("v")
                            ? latest.substring(1)
                            : latest;

                    // Use the helper instead of !equalsIgnoreCase
                    assert currentVersion != null;
                    if (isUpdateAvailable(currentVersion, normalizedLatest)) {
                        sendUpdateNotice(player, latest);
                    }
                }
            });
        }
    }

    private static CompletableFuture<String> fetchVersionFromMiddleware() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpClient client = HttpClient.newHttpClient();

                String jsonBody = String.format(
                        "{\"version\": \"%s\", \"modId\": \"1443319\"}",
                        currentVersion
                );
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(VERSION_API_URL))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println("FETCH RESPONSE: " + response);
                if (response.statusCode() == 200) {
                    JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
                    return json.get("version").getAsString();
                }
                return null;
            } catch (Exception e) {
                return null;
            }
        });
    }

    private static void sendUpdateNotice(Player player, String latest) {
        player.sendMessage(Message.raw("[MOTD] A new update is available!")
                .color("#00FFFF").bold(true));
        assert currentVersion != null;

        // Ensure the latest version shown also has a 'v' if it doesn't already
        String displayLatest = latest.toLowerCase().startsWith("v") ? latest : "v" + latest;

        player.sendMessage(Message.join(
                Message.raw("Running: "),
                // Add "v" before currentVersion here
                Message.raw("v" + currentVersion).color(Color.red),
                Message.raw(" | Latest: "),
                Message.raw(displayLatest).color(Color.green)
        ));
    }

    private static boolean isUpdateAvailable(String current, String latest) {
        String[] currentParts = current.split("\\.");
        String[] latestParts = latest.split("\\.");
        int length = Math.max(currentParts.length, latestParts.length);

        for (int i = 0; i < length; i++) {
            int curr = i < currentParts.length ? Integer.parseInt(currentParts[i]) : 0;
            int lat = i < latestParts.length ? Integer.parseInt(latestParts[i]) : 0;
            if (lat > curr) return true;
            if (lat < curr) return false;
        }
        return false;
    }
}