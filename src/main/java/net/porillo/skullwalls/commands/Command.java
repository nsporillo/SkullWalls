package net.porillo.skullwalls.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

public abstract interface Command {
	public abstract boolean checkPermission(CommandSender paramCommandSender);

	public abstract int getRequiredArgs();

	public abstract void runCommand(CommandSender paramCommandSender, List<String> paramList);

	public abstract void showHelp(CommandSender paramCommandSender, String paramString);
}
