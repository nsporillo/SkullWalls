package net.porillo.skullwalls.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

public abstract interface Command {
    public abstract boolean checkPermission(CommandSender paramCommandSender);

    public abstract int getRequiredArgs();

    public abstract void runCommand(CommandSender paramCommandSender, List<String> paramList);

    public abstract void showHelp(CommandSender paramCommandSender, String paramString);
}
