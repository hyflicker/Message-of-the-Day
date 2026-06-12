package com.hyflicker.MOTD.Commands;

import com.hyflicker.MOTD.Configuration.ModConfig;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;

public abstract class MotdSubCommand extends CommandBase {

    public MotdSubCommand(String name, String description) {
        super(name, description);

        // This keeps the server boot cycle happy across all sub-commands!
        this.requirePermission(ModConfig.get().commandPermissions);
    }

    @Override
    protected boolean canGeneratePermission(){
        return false;
    }

    @Override
    public boolean hasPermission(@Nonnull CommandSender sender){
        String requiredPerm = ModConfig.get().commandPermissions;

        if(sender instanceof PlayerRef playerRef){
            return playerRef.hasPermission(requiredPerm)
                    || playerRef.hasPermission("hytale.admin")
                    || playerRef.hasPermission("hytale.command.op");
        }
        return true;
    }
}