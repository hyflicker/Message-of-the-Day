package com.hyflicker.MOTD.Commands;

import com.hyflicker.MOTD.Configuration.ModConfig;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.UUID;

import static com.hyflicker.MOTD.onPlayerJoin.OnPlayerJoin.sendMotdTitle;

public class Test extends CommandBase {
    public Test() {
        super("test", "Previews the MOTD title to yourself.");
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        if (ctx.sender() instanceof PlayerRef playerRef) {

            UUID worldUuid = playerRef.getWorldUuid();
            if (worldUuid == null) return;

            var world = Universe.get().getWorld(worldUuid);
            if (world == null) return;

            world.execute(() -> {
                var ref = playerRef.getReference();
                if (ref != null && ref.isValid()) {
                    var store = ref.getStore();
                    Player player = store.getComponent(ref, Player.getComponentType());

                    if (player != null) {
                        ModConfig.WelcomeBanner config = ModConfig.get().welcomeBanner;
                        String playerName = playerRef.getUsername();
                        sendMotdTitle(playerRef, config, player, playerName);
                        ctx.sendMessage(Message.raw("Previewing MOTD titles...").color(Color.YELLOW));
                    } else {
                        ctx.sendMessage(Message.raw("Error: Active player world-component not found.").color(Color.RED));
                    }
                }
            });
        } else {
            ctx.sendMessage(Message.raw("This command can only be run by a player.").color(Color.ORANGE));
        }
    }
}