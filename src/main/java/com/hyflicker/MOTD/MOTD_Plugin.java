package com.hyflicker.MOTD;

import com.hyflicker.MOTD.Commands.Motd;
import com.hyflicker.MOTD.Configuration.ModConfig;
import com.hyflicker.MOTD.onPlayerJoin.OnPlayerJoin;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

public class MOTD_Plugin extends JavaPlugin {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public MOTD_Plugin(JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("MOTD Plugin %s initialized.", this.getManifest().getVersion());
    }

    @Override
    protected void setup() {
        ModConfig.get();

        UpdateAvailable.loadVersionFromManifest();

        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, this::onPlayerReady);
        this.getCommandRegistry().registerCommand(new Motd(this.getName()));
    }

    public void onPlayerReady(PlayerReadyEvent event) {
        OnPlayerJoin.welcomeBanner(event);
        UpdateAvailable.checkOnJoin(event);
    }
}