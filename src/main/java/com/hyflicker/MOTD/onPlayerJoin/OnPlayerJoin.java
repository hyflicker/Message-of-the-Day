package com.hyflicker.MOTD.onPlayerJoin;

import com.hyflicker.MOTD.Configuration.ModConfig;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.util.EventTitleUtil;

import java.awt.*;

public class OnPlayerJoin{
    public static void welcomeBanner(PlayerReadyEvent event){

        ModConfig.WelcomeBanner bannerConfig = ModConfig.get().welcomeBanner;
        if (bannerConfig.enabled) {
            Player player = event.getPlayer();
            PlayerRef playerRef = event.getPlayerRef().getStore().getComponent(event.getPlayerRef(),PlayerRef.getComponentType());

            sendMotdTitle(playerRef, bannerConfig, player);
        }
    }

    public static Message formatMessage(String template, Player player) {
        if (template == null || template.isEmpty()) {
            return Message.raw("");
        }

        String processed = template.replace("%player%", player.getDisplayName());
        return Message.raw(processed);
    }

    public static void sendMotdTitle(PlayerRef playerRef, ModConfig.WelcomeBanner bannerConfig, Player player) {
        EventTitleUtil.showEventTitleToPlayer(
                playerRef,
                formatMessage(bannerConfig.getPrimaryTitle(), player),
                formatMessage(bannerConfig.getSecondaryTitle(), player),
                bannerConfig.isMajor,
//                (String)null,
                null,
                bannerConfig.duration,
                bannerConfig.fadeInDuration,
                bannerConfig.fadeOutDuration
        );
    }
}