package com.hyflicker.MOTD.Commands.Config.Announcement;

import com.hyflicker.MOTD.Commands.Config.WelcomeBanner.WelcomeBanner;
import com.hyflicker.MOTD.Configuration.ModConfig;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;

import javax.annotation.Nonnull;
import java.awt.*;

public class Announcement extends CommandBase {
    public Announcement() {
        super("announcement", "Gets and Sets the announcement in the config file.");
        this.setAllowsExtraArguments(true);
        this.addSubCommand(new WelcomeBanner());
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        String[] rawCommandMessage = ctx.getInputString().split(" ",5);

        if (rawCommandMessage.length < 4) {
            ctx.sendMessage(Message.raw("Usage: /motd config announcement [isMajor, duration, fadeInDuration, fadeOutDuration, enabled]").color(Color.red));
            return;
        }
        ModConfig.Announcement config = ModConfig.get().announcement;
        String command = rawCommandMessage[3];
        String value = "";
        if (rawCommandMessage.length == 5) {
            value = rawCommandMessage[4];
        }

        switch (command) {
            case "isMajor":
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
                    config.setMajor(Boolean.parseBoolean(value));
                    sendCommandSetMessage(command, value, ctx);
                } else {
                    ctx.sendMessage(
                            Message.join(
                                    Message.raw(command + ": ").color(Color.green),
                                    Message.raw(Boolean.toString(config.isMajor()))
                            )
                    );
                }
                break;
            case "duration":
                if (!value.isBlank()) {
                    if (isNotFloat(value) && !isNumber(value)) {
                        ctx.sendMessage(Message.raw("Please enter a number value in seconds.").color(Color.red));
                        ctx.sendMessage(Message.raw("Examples:").bold(true).color(Color.red));
                        ctx.sendMessage(Message.raw("4"));
                        ctx.sendMessage(Message.raw("2.8"));
                        return;
                    }
                    if (isNumber(value)) {
                        config.setDuration((float) Integer.parseInt(value));
                    } else {
                        config.setDuration(Float.parseFloat(value));
                    }
                    sendCommandSetMessage(command, value, ctx);
                } else {
                    ctx.sendMessage(
                            Message.join(
                                    Message.raw(command + ": ").color(Color.green),
                                    Message.raw(Float.toString(config.getDuration()))
                            )
                    );
                }
                break;
            case "fadeInDuration":
                if (!value.isBlank()) {
                    if (isNotFloat(value) && !isNumber(value)) {
                        ctx.sendMessage(Message.raw("Please enter a number value in seconds.").color(Color.red));
                        ctx.sendMessage(Message.raw("Examples:").bold(true).color(Color.red));
                        ctx.sendMessage(Message.raw("4"));
                        ctx.sendMessage(Message.raw("2.8"));
                        return;
                    }
                    if (isNumber(value)) {
                        config.setFadeInDuration((float) Integer.parseInt(value));
                    } else {
                        config.setFadeInDuration(Float.parseFloat(value));
                    }
                    sendCommandSetMessage(command, value, ctx);
                } else {
                    ctx.sendMessage(
                            Message.join(
                                    Message.raw(command + ": ").color(Color.green),
                                    Message.raw(Float.toString(config.getFadeInDuration()))
                            )
                    );
                }
                break;
            case "fadeOutDuration":
                if (!value.isBlank()) {
                    if (isNotFloat(value) && !isNumber(value)) {
                        ctx.sendMessage(Message.raw("Please enter a number value in seconds.").color(Color.red));
                        ctx.sendMessage(Message.raw("Examples:").bold(true).color(Color.red));
                        ctx.sendMessage(Message.raw("4"));
                        ctx.sendMessage(Message.raw("2.8"));
                        return;
                    }
                    if (isNumber(value)) {
                        config.setFadeOutDuration((float) Integer.parseInt(value));
                    } else {
                        config.setFadeOutDuration(Float.parseFloat(value));
                    }
                    sendCommandSetMessage(command, value, ctx);
                } else {
                    ctx.sendMessage(
                            Message.join(
                                    Message.raw(command + ": ").color(Color.green),
                                    Message.raw(Float.toString(config.getFadeOutDuration()))
                            )
                    );
                }
                break;
            case "permGroup":{
                if(!value.isBlank()){
                    ctx.sendMessage(
                            Message.join(
                                    Message.raw(command + ": ").color(Color.green),
                                    Message.raw(config.getPermGroup())
                            )
                    );
                }else{
                    config.setPermGroup(rawCommandMessage[3]);
                    ctx.sendMessage(
                            Message.join(
                                    Message.raw(command + " was set to: ").color(Color.green),
                                    Message.raw(value)
                            )
                    );
                }
            }
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

    private static boolean isNotFloat(String value) {
        try {
            Float.parseFloat(value);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    private static boolean isNumber(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
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
