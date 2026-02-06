package com.hyflicker.MOTD.Configuration;

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

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

public class ModConfig {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static final String FOLDER_NAME = "Message of the Day Config";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // --- Data Structure ---
    public WelcomeBanner welcomeBanner = new WelcomeBanner();
    public Announcement announcement = new Announcement();
    public UpdateAvailable updateAvailable = new UpdateAvailable();
    public String commandPermissions;

    public String getCommandPermissions() {
        return commandPermissions;
    }

    public void setCommandPermissions(String commandPermissions) {
        this.commandPermissions = commandPermissions;
        ModConfig.save();
    }

    public static class WelcomeBanner {
        public String primaryTitle = "Welcome %player% to MOTD";
        public String secondaryTitle = "Fully Customizable";
        public boolean isMajor = true;
        public float duration = 5.0f;
        public float fadeInDuration = 1.5f;
        public float fadeOutDuration = 1.5f;
        public boolean randomizeTitle = false;
        public String permGroup = "%commandPermissions%";
        public boolean enabled = true;
        public FirstJoin firstJoin = new FirstJoin();

        private static final String RANDOMIZER_FILE = "randomEventTitles.json";
        private static final Random RANDOM = new Random();

        // Getters
        public String getPrimaryTitle() {
            if (!randomizeTitle) {
                return primaryTitle;
            }
            return getRandomValueFromJson("primaryTitle", primaryTitle);
        }

        public String getSecondaryTitle() {
            if (!randomizeTitle) {
                return secondaryTitle;
            }
            return getRandomValueFromJson("secondaryTitle", secondaryTitle);
        }

        public boolean isMajor() {
            return isMajor;
        }

        public float getDuration() {
            return duration;
        }

        public float getFadeInDuration() {
            return fadeInDuration;
        }

        public float getFadeOutDuration() {
            return fadeOutDuration;
        }

        public boolean getRandomizeTitle() {
            return randomizeTitle;
        }

        public String getPermGroup() {
            if ("%commandPermissions%".equals(permGroup)) {
                return ModConfig.get().getCommandPermissions();
            }
            return permGroup;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public FirstJoin getFirstJoin() {
            return firstJoin;
        }

        // Setters
        public void setPrimaryTitle(String title) {
            this.primaryTitle = title;
            ModConfig.save();
        }

        public void setSecondaryTitle(String title) {
            this.secondaryTitle = title;
            ModConfig.save();
        }

        public void setMajor(boolean major) {
            this.isMajor = major;
            ModConfig.save();
        }

        public void setDuration(float duration) {
            this.duration = duration;
            ModConfig.save();
        }

        public void setFadeInDuration(float fadeInDuration) {
            this.fadeInDuration = fadeInDuration;
            ModConfig.save();
        }

        public void setFadeOutDuration(float fadeOutDuration) {
            this.fadeOutDuration = fadeOutDuration;
            ModConfig.save();
        }

        public void setRandomizeTitle(boolean randomizeTitle) {
            this.randomizeTitle = randomizeTitle;
            ModConfig.save();
        }

        public void setPermGroup(String permGroup) {
            this.permGroup = permGroup;
            ModConfig.save();
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
            ModConfig.save();
        }

        public static class FirstJoin {
            public String primaryTitle = "%welcomeBanner%";
            public String secondaryTitle = "%welcomeBanner%";
            public boolean enabled = true;
            public ServerAnnouncement serverAnnounce = new ServerAnnouncement();

            public ServerAnnouncement getServerAnnounce() {
                return serverAnnounce;
            }

            public String getPrimaryTitle(String parentFallback) {
                if ("%welcomeBanner%".equals(primaryTitle)) {
                    return parentFallback;
                }
                return primaryTitle;
            }

            public String getSecondaryTitle(String parentFallback) {
                if ("%welcomeBanner%".equals(secondaryTitle)) {
                    return parentFallback;
                }
                return secondaryTitle;
            }

            public boolean isEnabled() { return enabled; }

            public void setPrimaryTitle(String title) {
                this.primaryTitle = title;
                ModConfig.save();
            }

            public void setSecondaryTitle(String title) {
                this.secondaryTitle = title;
                ModConfig.save();
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
                ModConfig.save();
            }

            public static class ServerAnnouncement{
                public String primaryTitle = "%player% has joined the server";
                public String secondaryTitle = "New User Has Joined";
                public boolean isMajor = false;
                public float duration = 5.0f;
                public float fadeInDuration = 1.5f;
                public float fadeOutDuration = 1.5f;
                public boolean enabled = false;


                public String getPrimaryTitle() {
                    return primaryTitle;
                }

                public void setPrimaryTitle(String primaryTitle) {
                    this.primaryTitle = primaryTitle;
                    ModConfig.save();
                }

                public String getSecondaryTitle() {
                    return secondaryTitle;
                }

                public void setSecondaryTitle(String secondaryTitle) {
                    this.secondaryTitle = secondaryTitle;
                    ModConfig.save();
                }

                public boolean isEnabled() {
                    return enabled;

                }

                public void setEnabled(boolean enabled) {
                    this.enabled = enabled;
                    ModConfig.save();
                }

                public boolean isMajor() {
                    return isMajor;
                }

                public void setMajor(boolean major) {
                    isMajor = major;
                }

                public float getDuration() {
                    return duration;
                }

                public void setDuration(float duration) {
                    this.duration = duration;
                }

                public float getFadeInDuration() {
                    return fadeInDuration;
                }

                public void setFadeInDuration(float fadeInDuration) {
                    this.fadeInDuration = fadeInDuration;
                }

                public float getFadeOutDuration() {
                    return fadeOutDuration;
                }

                public void setFadeOutDuration(float fadeOutDuration) {
                    this.fadeOutDuration = fadeOutDuration;
                }
            }
        }

        private String getRandomValueFromJson(String key, String fallback) {
            // Resolve path relative to the "Message of the Day Config" folder
            Path path = ModConfig.resolvePath().getParent().resolve(RANDOMIZER_FILE);

            if (!Files.exists(path)) {
                return fallback;
            }

            try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                Type type = new TypeToken<java.util.Map<String, List<String>>>() {
                }.getType();
                java.util.Map<String, List<String>> data = GSON.fromJson(reader, type);

                if (data != null && data.containsKey(key)) {
                    List<String> options = data.get(key);
                    if (options != null && !options.isEmpty()) {
                        return options.get(RANDOM.nextInt(options.size()));
                    }
                }
            } catch (Exception e) {
                LOGGER.atSevere().withCause(e).log("Failed to parse randomizer file: " + RANDOMIZER_FILE);
            }
            return fallback;
        }
    }

    public static class Announcement {
        public boolean isMajor = true;
        public float duration = 5.0f;
        public float fadeInDuration = 1.5f;
        public float fadeOutDuration = 1.5f;
        public String permGroup = "%commandPermissions%";
        public boolean enabled = true;

        //Getters
        public boolean isMajor() {
            return isMajor;
        }

        public float getDuration() {
            return duration;
        }

        public float getFadeInDuration() {
            return fadeInDuration;
        }

        public float getFadeOutDuration() {
            return fadeOutDuration;
        }

        public String getPermGroup() {
            if ("%commandPermissions%".equals(permGroup)) {
                return ModConfig.get().getCommandPermissions();
            }
            return permGroup;
        }

        public boolean isEnabled() {
            return enabled;
        }

        //Setters
        public void setMajor(boolean major) {
            this.isMajor = major;
            ModConfig.save();
        }

        public void setDuration(float duration) {
            this.duration = duration;
            ModConfig.save();
        }

        public void setFadeInDuration(float fadeInDuration) {
            this.fadeInDuration = fadeInDuration;
            ModConfig.save();
        }

        public void setFadeOutDuration(float fadeOutDuration) {
            this.fadeOutDuration = fadeOutDuration;
            ModConfig.save();
        }

        public void setPermGroup(String permGroup) {
            this.permGroup = permGroup;
            ModConfig.save();
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
            ModConfig.save();
        }
    }

    public static class UpdateAvailable {
        public String permGroup = "%commandPermissions%";
        public boolean enabled = true;

        public String getPermGroup() {
            if ("%commandPermissions%".equals(permGroup)) {
                return ModConfig.get().getCommandPermissions();
            }
            return permGroup;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setPermGroup(String group) {
            this.permGroup = group;
            ModConfig.save();
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
            ModConfig.save();
        }
    }

    // --- Persistence Logic ---

    private static ModConfig instance;
    private static Path configPath;

    public static ModConfig get() {
        if (instance == null) instance = load();
        return instance;
    }

    public static void save() {
        Path path = resolvePath();
        try {
            Files.createDirectories(path.getParent());
            String json = GSON.toJson(get());
            Files.writeString(path, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.atSevere().withCause(e).log("Failed to save config!");
        }
    }

    private static ModConfig load() {
        Path path = resolvePath();
        Path configFolder = path.getParent();
        try {
            Files.createDirectories(configFolder);

            Path defaultJson = configFolder.resolve("randomEventTitles.json");
            if (!Files.exists(defaultJson)) {
                String jsonContent = """
                        {
                          "primaryTitle": ["Welcome %player%!", "Greetings!", "Hello!"],
                          "secondaryTitle": ["Glad you are here", "Enjoy the game", "Welcome back"]
                        }""";
                Files.writeString(defaultJson, jsonContent, StandardCharsets.UTF_8);
            }

            if (!Files.exists(path)) {
                // No file at all? Create a fresh one with all current defaults.
                instance = new ModConfig();
                save();
                return instance;
            }

            try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8);
                JsonReader reader = new JsonReader(br)) {
                reader.setStrictness(Strictness.LENIENT);
                ModConfig loaded = GSON.fromJson(reader, ModConfig.class);

                if (loaded == null) return new ModConfig();
                if (loaded.welcomeBanner == null) loaded.welcomeBanner = new WelcomeBanner();
                if (loaded.welcomeBanner.firstJoin == null) loaded.welcomeBanner.firstJoin = new WelcomeBanner.FirstJoin();
                if(loaded.welcomeBanner.firstJoin.serverAnnounce == null) loaded.welcomeBanner.firstJoin.serverAnnounce = new WelcomeBanner.FirstJoin.ServerAnnouncement();
                if (loaded.announcement == null) loaded.announcement = new Announcement();
                if (loaded.updateAvailable == null) loaded.updateAvailable = new UpdateAvailable();
                if (loaded.commandPermissions == null) loaded.commandPermissions = "hytale:admin";
                instance = loaded;
                save();

                return loaded;
            }
        } catch (Exception e) {
            LOGGER.atSevere().withCause(e).log("Failed to load/repair config: " + path);
            return new ModConfig();
        }
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

    public static void reload() {
        instance = null;
        get();
    }
}