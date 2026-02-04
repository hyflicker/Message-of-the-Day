package com.hyflicker.MOTD.Commands.Players;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hyflicker.MOTD.Configuration.ModConfig;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.util.EventTitleUtil;

import javax.annotation.Nonnull;
import java.awt.*;

public class Player extends CommandBase {
    public Player() {
        super("player", "Returns a list of players");
        this.requirePermission(ModConfig.get().commandPermissions);
        this.setAllowsExtraArguments(true);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        try {
            String[] rawCommandMessage = ctx.getInputString().split(" ",4);

            if (rawCommandMessage.length < 3) {
                int count = Universe.get().getPlayerCount();

                if (count == 0) {
                    ctx.sendMessage(Message.raw("No players are currently online.").color(Color.YELLOW));
                    return;
                }
                ctx.sendMessage(Message.join(
                        Message.raw("Players online (" + count + "): ").color(Color.WHITE),
                        Message.raw(getPlayerListString()).color(Color.CYAN)
                ));
            } else if (rawCommandMessage.length < 4) {
                String playerName = rawCommandMessage[2];
                PlayerRef target = Universe.get().getPlayers().stream()
                        .filter(p -> p.getUsername().equalsIgnoreCase(playerName))
                        .findFirst()
                        .orElse(null);
                if (target == null) {
                    ctx.sendMessage(Message.raw("Player '" + playerName + "' is not online.").color(Color.RED));
                } else {
                    ctx.sendMessage(Message.raw("Player " + target.getUsername() + " is online!").color(Color.GREEN));
                    ctx.sendMessage(Message.raw("(Use /motd player [player name] [primaryTitle] or [primaryTitle] | [secondaryTitle])").color(Color.WHITE));
                }
            } else {
                String primaryTitle;
                String secondaryTitle;
                String message = rawCommandMessage[3];
                String playerName = rawCommandMessage[2];
                PlayerRef target = Universe.get().getPlayers().stream()
                        .filter(p -> p.getUsername().equalsIgnoreCase(playerName))
                        .findFirst()
                        .orElse(null);
                if (target == null) {
                    ctx.sendMessage(Message.raw("Player '" + playerName + "' is not online.").color(Color.RED));
                    ctx.sendMessage(Message.raw("Use \"/motd player\" to get a list of online players."));
                    return;
                }
                if (message.contains("|")) {
                    String[] messageParts = message.split("\\|", 2); // Split into max 2 parts
                    primaryTitle = messageParts[0].trim();
                    secondaryTitle = messageParts[1].trim();
                } else {
                    primaryTitle = message.trim();
                    secondaryTitle = "Message To You";
                }
                var config = ModConfig.get().announcement;

                if (config.enabled) {
                    EventTitleUtil.showEventTitleToPlayer(
                            target,
                            Message.raw(primaryTitle),
                            Message.raw(secondaryTitle),
                            config.isMajor,
                            null, // No specific group
                            config.duration,
                            config.fadeInDuration,
                            config.fadeOutDuration
                    );
                }else{
                    ctx.sendMessage(Message.raw("Announcements are currently disabled in the config.").color(Color.yellow));
                }
            }


        } catch (Exception e) {
            String msg = "Failed to get player data.";
            LOGGER.atSevere().withCause(e).log(msg);
            ctx.sendMessage(Message.raw(msg).color(Color.RED));
        }
    }

    private static String getPlayerListString() {
        return Universe.get().getPlayers().stream()
                .map(PlayerRef::getUsername)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
    }
}
