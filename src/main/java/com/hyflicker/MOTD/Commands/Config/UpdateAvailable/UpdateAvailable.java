package com.hyflicker.MOTD.Commands.Config.UpdateAvailable;

import com.hyflicker.MOTD.Configuration.ModConfig;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;

import javax.annotation.Nonnull;
import java.awt.*;

public class UpdateAvailable extends CommandBase {
    public UpdateAvailable(){
        super("updateAvailable", "Gets and Sets the updateAvailable in the config file.");
        this.setAllowsExtraArguments(true);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        String[] rawCommandMessage = ctx.getInputString().split(" ",5);

        if (rawCommandMessage.length < 4) {
            ctx.sendMessage(Message.raw("Usage: /motd config updateAvailable [group, enabled]").color(Color.red));
            return;
        }
        ModConfig.UpdateAvailable config = ModConfig.get().updateAvailable;
        String command = rawCommandMessage[3];
        String value = "";
        if (rawCommandMessage.length == 5) {
            value = rawCommandMessage[4];
        }

        switch (command) {
            case "permGroup":
                if (!value.isBlank()) {
                    config.setPermGroup(value);
                    sendCommandSetMessage(command, value, ctx);
                } else {
                    ctx.sendMessage(
                            Message.join(
                                    Message.raw(command + ": ").color(Color.green),
                                    Message.raw(config.getPermGroup())
                            )
                    );
                }
                break;
            case "enabled":
                if (!value.isBlank()) {
                    if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
                        ctx.sendMessage(Message.join(
                                Message.raw("Please enter ").color(Color.red),
                                Message.raw("true "),
                                Message.raw("or ").color(Color.red),
                                Message.raw("false"),
                                Message.raw("!").color(Color.red)
                        ));
                        return;
                    }
                    config.setEnabled(Boolean.parseBoolean(value));
                    sendCommandSetMessage(command, value, ctx);
                } else {
                    ctx.sendMessage(
                            Message.join(
                                    Message.raw(command + ": ").color(Color.green),
                                    Message.raw(Boolean.toString(config.isEnabled()))
                            )
                    );
                }
                break;
            default:
                ctx.sendMessage(Message.raw("Command \"" + command + "\" is not an option.").color(Color.red));
        }
    }

    private static void sendCommandSetMessage(String command, String value, CommandContext ctx) {
        ctx.sendMessage(
                Message.join(
                        Message.raw(command + " was set to: ").color(Color.green),
                        Message.raw(value.toLowerCase())
                )
        );
    }
}
