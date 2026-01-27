package com.hyflicker.MOTD.Commands;

import com.hyflicker.MOTD.Configuration.ModConfig;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.util.EventTitleUtil;

import javax.annotation.Nonnull;
import java.awt.*;

public class Announcement extends CommandBase {
    public Announcement() {
        super("announcement", "Announces to all players the text provided");
        this.setAllowsExtraArguments(true);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        String[] rawCommandMessage = ctx.getInputString().split(" ",3);

        String primaryTitle;
        String secondaryTitle;

        if (rawCommandMessage.length < 3) {
            ctx.sendMessage(Message.raw("Usage: /motd announcement [primaryTitle] or [primaryTitle] | [secondaryTitle]").color(Color.red));
            return;
        }

        String message = rawCommandMessage[2];

        if (message.contains("|")) {
            String[] messageParts = message.split("\\|", 2); // Split into max 2 parts
            primaryTitle = messageParts[0].trim();
            secondaryTitle = messageParts[1].trim();
        } else {
            primaryTitle = message.trim();
            secondaryTitle = "Server Wide Announcement"; // Default fallback
        }
        var config = ModConfig.get().announcement;

        if (config.enabled) {
            EventTitleUtil.showEventTitleToUniverse(
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
}
