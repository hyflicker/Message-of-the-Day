package com.hyflicker.MOTD.Commands.Config;

import com.hyflicker.MOTD.Commands.Config.Announcement.Announcement;
import com.hyflicker.MOTD.Commands.Config.UpdateAvailable.UpdateAvailable;
import com.hyflicker.MOTD.Commands.Config.WelcomeBanner.WelcomeBanner;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;

import javax.annotation.Nonnull;
import java.awt.*;

public class Config extends CommandBase {

    public Config() {
        super("config", "Gets and Sets the config file.");
        this.setAllowsExtraArguments(true);
        this.addSubCommand(new WelcomeBanner());
        this.addSubCommand(new Announcement());
        this.addSubCommand(new UpdateAvailable());
        this.addSubCommand(new CommandPermissions());
    }


    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        String [] rawCommandMessage = ctx.getInputString().split(" ");

        if (rawCommandMessage.length < 3) {
            ctx.sendMessage(Message.raw("Usage: /motd config [welcomeBanner,announcement,updateAvailable,commandPermissions]").color(Color.red));
        }
    }
}
