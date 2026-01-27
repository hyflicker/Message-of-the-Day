package com.hyflicker.MOTD.Commands;

import com.hyflicker.MOTD.Configuration.ModConfig;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;
import java.awt.*;

import static com.hyflicker.MOTD.onPlayerJoin.OnPlayerJoin.sendMotdTitle;

public class Test extends CommandBase {
    public Test() {
        super("test", "Previews the MOTD title to yourself.");
        this.requirePermission(ModConfig.get().commandPermissions);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        if (ctx.sender() instanceof Player player) {
            // We capture the world and player data here
            var world = player.getWorld();

            // Move the execution to the World Thread to satisfy the Store assertion
            assert world != null;
            world.execute(() -> {
                var ref = player.getReference();
                if (ref != null && ref.isValid()) {
                    var store = ref.getStore();
                    // This call now succeeds because we are in the 'WorldThread'
                    PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());

                    if (playerRef != null) {
                        ModConfig.WelcomeBanner config = ModConfig.get().welcomeBanner;
                        sendMotdTitle(playerRef, config, player);
                        ctx.sendMessage(Message.raw("Previewing MOTD titles...").color(Color.YELLOW));
                    }
                }
            });
        } else {
            ctx.sendMessage(Message.raw("This command can only be run by a player.").color(Color.ORANGE));
        }
    }
}
