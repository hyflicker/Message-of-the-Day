package com.hyflicker.MOTD.Commands;

import com.hyflicker.MOTD.Commands.Config.Config;
import com.hyflicker.MOTD.Commands.Players.Player;
import com.hypixel.hytale.server.core.command.system.CommandContext;

import javax.annotation.Nonnull;

public class Motd extends MotdSubCommand {

    public Motd(String pluginName) {
        super("motd", "Main command for " + pluginName);
        this.addSubCommand(new Reload());
        this.addSubCommand(new Test());
        this.addSubCommand(new Announcement());
        this.addSubCommand(new Config());
        this.addSubCommand(new Player());
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        ctx.sendMessage(this.getUsageString(ctx.sender()));
    }
}
