package com.hyflicker.MOTD.Commands;

import com.hyflicker.MOTD.Configuration.ModConfig;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;

import javax.annotation.Nonnull;
import java.awt.*;

public class Reload extends CommandBase {
    public Reload() {
        super("reload", "Reloads the MOTD configuration.");
        this.requirePermission(ModConfig.get().commandPermissions);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        try {
            ModConfig.reload();
            ctx.sendMessage(Message.raw("Configs reloaded successfully.").color(Color.green));
        } catch (Exception e) {
            String msg = "Failed to reload MOTD configs.";
            LOGGER.atSevere().withCause(e).log(msg);
            ctx.sendMessage(Message.raw(msg).color(Color.red));
        }
    }
}
