package com.hyflicker.MOTD.Commands;

import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;

public abstract class MotdSubCommand extends CommandBase {

    public MotdSubCommand(String name, String description) {
        super(name, description);

        // This keeps the server boot cycle happy across all sub-commands!
        this.requirePermission("motd.use");
    }
}