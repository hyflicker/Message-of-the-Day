package com.hyflicker.MOTD;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.util.EventTitleUtil;

import javax.annotation.Nonnull;
import java.awt.*;

public class Command extends CommandBase {

    public Command(String pluginName) {
        super("motd", "Main command for " + pluginName);
        this.requirePermission("hytale.admin");
        this.addSubCommand(new ReloadSub());
        this.addSubCommand(new TestSub());
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        ctx.sendMessage(this.getUsageString(ctx.sender()));
    }

    private static class ReloadSub extends CommandBase {
        public ReloadSub() {
            super("reload", "Reloads the MOTD configuration.");
        }

        @Override
        protected void executeSync(@Nonnull CommandContext ctx) {
            try {
                ModConfig.reload();
                ctx.sendMessage(Message.raw("Configs reloaded successfully.").color(Color.green));
            } catch (Exception e) {
                String msg = "Failed to reload MOTD configs.";
                // Accessing the static LOGGER inherited or defined
                LOGGER.atSevere().withCause(e).log(msg);
                ctx.sendMessage(Message.raw(msg).color(Color.red));
            }
        }
    }
    private static class TestSub extends CommandBase {
        public TestSub() {
            super("test", "Previews the MOTD title to yourself.");
        }

        @Override
        protected void executeSync(@Nonnull CommandContext ctx) {
            if (ctx.sender() instanceof Player player) {
                // We capture the world and player data here
                var world = player.getWorld();

                // Move the execution to the World Thread to satisfy the Store assertion
                assert world != null;
                world.execute(() -> {
                    ModConfig config = ModConfig.get();

                    String title = config.primaryMessage.replace("%player%", player.getDisplayName());
                    String sub = config.secondaryMessage.replace("%player%", player.getDisplayName());

                    var ref = player.getReference();
                    if (ref != null && ref.isValid()) {
                        var store = ref.getStore();
                        // This call now succeeds because we are in the 'WorldThread'
                        PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());

                        if (playerRef != null) {
                            EventTitleUtil.showEventTitleToPlayer(
                                    playerRef,
                                    Message.raw(title),
                                    Message.raw(sub),
                                    true
                            );
                            // It's safe to send messages back to the context from here
                            ctx.sendMessage(Message.raw("Previewing MOTD titles...").color(Color.YELLOW));
                        }
                    }
                });
            } else {
                ctx.sendMessage(Message.raw("This command can only be run by a player.").color(Color.ORANGE));
            }
        }
    }
}
