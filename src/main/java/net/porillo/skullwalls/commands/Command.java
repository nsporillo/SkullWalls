package net.porillo.skullwalls.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface Command {

    boolean checkPermission(CommandSender sender);

    int getRequiredArgs();

    boolean isConsoleOnly();

    void runCommand(CommandSender sender, List<String> paramList);

    void showHelp(CommandSender sender, String paramString);

}
