package com.hyflicker.MOTD;

import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.EventTitleUtil;

public class OnPlayerJoin{
    public static void onPlayerReady(PlayerReadyEvent event) {
        Player player = event.getPlayer();
        var ref = event.getPlayerRef();
        ModConfig config = ModConfig.get();
        String primary = config.primaryMessage.replace("%player%", player.getDisplayName());
        String secondary = config.secondaryMessage.replace("%player%", player.getDisplayName());
        Store<EntityStore> store = ref.getStore();
        PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());
        assert playerRef != null;
        EventTitleUtil.showEventTitleToPlayer(
                playerRef,
                Message.raw(primary),
                Message.raw(secondary),
                true
        );
    }
}