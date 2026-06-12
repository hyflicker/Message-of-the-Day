package com.hyflicker.MOTD.Commands;

import com.hyflicker.MOTD.Commands.Config.Config;
import com.hyflicker.MOTD.Commands.Players.Player;
import com.hyflicker.MOTD.Configuration.ModConfig;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;
import java.awt.*;

public class Motd extends CommandBase {

    public Motd(String pluginName) {
        super("motd", "Main command for " + pluginName);
        this.requirePermission("motd.use");
        this.addSubCommand(new Reload());
        this.addSubCommand(new Test());
        this.addSubCommand(new Announcement());
        this.addSubCommand(new Config());
        this.addSubCommand(new Player());
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        String requiredPerm = ModConfig.get().commandPermissions;

        if (ctx.sender() instanceof PlayerRef playerRef) {
            boolean hasAccess = playerRef.hasPermission(requiredPerm)
                    || playerRef.hasPermission("hytale.admin")
                    || playerRef.hasPermission("hytale.command.op");

            if (!hasAccess) {
                ctx.sendMessage(Message.raw("You do not have permission to use this command.")
                        .color("#FF0000"));
                return;
            }
        }
        ctx.sendMessage(this.getUsageString(ctx.sender()));
    }
}
