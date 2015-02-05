package net.porillo.skullwalls.commands;

import java.util.List;

import net.porillo.skullwalls.SkullWalls;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ClearCommand extends BaseCommand {
	public ClearCommand(SkullWalls plugin) {
		super(plugin);
		super.setName("clear");
		super.addUsage(null, null, "Clear your current session");
		super.setPermission("skullwalls.clear");
	}

	public void runCommand(CommandSender sender, List<String> args) {
		if (!this.checkPermission(sender)) {
			this.noPermission(sender);
			return;
		}
		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage(ChatColor.RED + "Console cannot use this command");
			return;
		}
		if (args.size() == 0) {
			this.plugin.getCuboider().clear((Player) sender);
			sender.sendMessage(ChatColor.BLUE + "Cleared your session");
		}
	}
}
