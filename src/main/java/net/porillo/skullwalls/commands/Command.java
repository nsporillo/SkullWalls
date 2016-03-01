package net.porillo.skullwalls.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface Command {
    boolean checkPermission(CommandSender paramCommandSender);

    int getRequiredArgs();

    void runCommand(CommandSender paramCommandSender, List<String> paramList);

    void showHelp(CommandSender paramCommandSender, String paramString);
}
