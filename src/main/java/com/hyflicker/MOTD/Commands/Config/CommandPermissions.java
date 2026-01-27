package com.hyflicker.MOTD.Commands.Config;

import com.hyflicker.MOTD.Configuration.ModConfig;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;

import javax.annotation.Nonnull;
import java.awt.*;

public class CommandPermissions extends CommandBase {
    public CommandPermissions() {
        super("commandPermissions", "Gets and Sets the commandPermission in the config file.");
        this.setAllowsExtraArguments(true);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        String[] rawCommandMessage = ctx.getInputString().split(" ",4);
        String command = rawCommandMessage[2];
        if(rawCommandMessage.length == 3){
            ctx.sendMessage(
                    Message.join(
                            Message.raw(command + ": ").color(Color.green),
                            Message.raw(ModConfig.get().getCommandPermissions())
                    )
            );
        }else{
            ModConfig.get().setCommandPermissions(rawCommandMessage[3]);
            ctx.sendMessage(
                    Message.join(
                            Message.raw(command + " was set to: ").color(Color.green),
                            Message.raw(rawCommandMessage[3])
                    )
            );
        }



    }
}
