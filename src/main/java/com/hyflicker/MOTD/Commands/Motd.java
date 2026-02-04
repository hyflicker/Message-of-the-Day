package com.hyflicker.MOTD.Commands;

import com.hyflicker.MOTD.Commands.Config.Config;
import com.hyflicker.MOTD.Commands.Players.Player;
import com.hyflicker.MOTD.Configuration.ModConfig;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;

import javax.annotation.Nonnull;
import java.awt.*;

public class Motd extends CommandBase {

    public Motd(String pluginName) {
        super("motd", "Main command for " + pluginName);
        this.requirePermission(ModConfig.get().commandPermissions);
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
