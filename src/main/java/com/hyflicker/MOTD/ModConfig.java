package com.hyflicker.MOTD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.Strictness;
import com.google.gson.stream.JsonReader;
import com.hypixel.hytale.logger.HytaleLogger;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ModConfig {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static final String FOLDER_NAME = "Message of the Day Config";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public String primaryMessage = "Welcome %player%";
    public String secondaryMessage = "Glad you are here.";

    private static ModConfig instance;
    private static Path configPath;

    public static ModConfig get() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    // Call this from a reload command to refresh the config!
    public static void reload() {
        instance = load();
    }

    private static Path resolvePath() {
        if (configPath != null) return configPath;
        try {
            Path jarPath = Paths.get(ModConfig.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
            configPath = jarPath.resolve(FOLDER_NAME).resolve("config.json");
            return configPath;
        } catch (URISyntaxException e) {
            return Paths.get("mods", FOLDER_NAME, "config.json");
        }
    }

    private static ModConfig load() {
        Path path = resolvePath();
        try {
            Files.createDirectories(path.getParent());

            if (!Files.exists(path)) {
                String defaultContent =
                        """
                                // INSTRUCTIONS:
                                // Use %player% to insert the player's name.
                                
                                // EXAMPLES:
                                // "Happy Spooky Season, %player%!"
                                // "Merry Christmas, %player%!"
                                
                                {
                                  "primaryMessage": "Welcome To The Server",
                                  "secondaryMessage": "%player% has joined"
                                }""";

                Files.writeString(path, defaultContent, StandardCharsets.UTF_8);
                LOGGER.atInfo().log("Created new config with comments at: " + path.toAbsolutePath());
            }

            // Modern Lenient Loading:
            try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8);
                 JsonReader reader = new JsonReader(br)) {

                // Use Strictness instead of setLenient
                reader.setStrictness(Strictness.LENIENT);

                return GSON.fromJson(reader, ModConfig.class);
            }

        } catch (Exception e) {
            LOGGER.atSevere().withCause(e).log("Failed to load config from " + path);
            return new ModConfig();
        }
    }
}