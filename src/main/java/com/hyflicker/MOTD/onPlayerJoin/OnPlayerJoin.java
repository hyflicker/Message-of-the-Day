package com.hyflicker.MOTD.onPlayerJoin;

import com.hyflicker.MOTD.Configuration.ModConfig;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.EventTitleUtil;

public class OnPlayerJoin{
    public static void welcomeBanner(PlayerReadyEvent event){
        ModConfig.WelcomeBanner bannerConfig = ModConfig.get().welcomeBanner;
        Player player = event.getPlayer();
        Ref<EntityStore> entityRef = event.getPlayerRef();
        PlayerRef playerRef = entityRef.getStore().getComponent(entityRef, PlayerRef.getComponentType());

        if (playerRef == null) return;
        String playerName = playerRef.getUsername();
        if (bannerConfig.enabled || bannerConfig.firstJoin.enabled) {
            sendMotdTitle(playerRef, bannerConfig, player, playerName);
        }
        if(player.isFirstSpawn() && bannerConfig.firstJoin.serverAnnounce.enabled){
            sendMotdTitleUniverse(bannerConfig, playerName);
        }
    }

    public static Message formatMessage(String template, String playerName) {
        if (template == null || template.isEmpty()) {
            return Message.raw("");
        }
        String processed = template.replace("%player%", playerName);
        return Message.raw(processed);
    }

    public static void sendMotdTitle(PlayerRef playerRef, ModConfig.WelcomeBanner bannerConfig, Player player, String playerName) {
        //These get set by WelcomeBanner primary/secondary titles if randomized is turned on it returns randomized titles.
        Message primaryTitle = formatMessage(bannerConfig.getPrimaryTitle(), playerName);
        Message secondaryTitle = formatMessage(bannerConfig.getSecondaryTitle(), playerName);
        if(player.isFirstSpawn() && bannerConfig.firstJoin.enabled){
            primaryTitle = formatMessage(bannerConfig.firstJoin.getPrimaryTitle(bannerConfig.getPrimaryTitle()), playerName);
            secondaryTitle = formatMessage(bannerConfig.firstJoin.getSecondaryTitle(bannerConfig.getSecondaryTitle()), playerName);
        }

        EventTitleUtil.showEventTitleToPlayer(
                playerRef,
                primaryTitle,
                secondaryTitle,
                bannerConfig.isMajor,
//                (String)null,
                null,
                bannerConfig.duration,
                bannerConfig.fadeInDuration,
                bannerConfig.fadeOutDuration
        );
    }
    public static void sendMotdTitleUniverse(ModConfig.WelcomeBanner bannerConfig, String playerName) {
        EventTitleUtil.showEventTitleToUniverse(
                formatMessage(bannerConfig.firstJoin.serverAnnounce.getPrimaryTitle(), playerName),
                formatMessage(bannerConfig.firstJoin.serverAnnounce.getSecondaryTitle(), playerName),
                bannerConfig.firstJoin.serverAnnounce.isMajor,
//                (String)null,
                null,
                bannerConfig.firstJoin.serverAnnounce.duration,
                bannerConfig.firstJoin.serverAnnounce.fadeInDuration,
                bannerConfig.firstJoin.serverAnnounce.fadeOutDuration
        );
    }
}