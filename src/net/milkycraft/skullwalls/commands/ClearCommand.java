package net.milkycraft.skullwalls.commands;

import static org.bukkit.ChatColor.BLUE;
import static org.bukkit.ChatColor.RED;

import java.util.List;

import net.milkycraft.skullwalls.SkullWalls;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ClearCommand extends BaseCommand {

	public ClearCommand(final SkullWalls plugin) {
		super(plugin);
		super.setName("clear");
		super.addUsage(null, null, "Clear your current session");
		super.setPermission("skullwalls.clear");
	}

	@Override
	public void runCommand(final CommandSender sender, final List<String> args) {
		if (!this.checkPermission(sender)) {
			this.noPermission(sender);
			return;
		}
		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage(RED + "Console cannot use this command");
			return;
		}
		if (args.size() == 0) {
			super.plugin.getCuboider().clear((Player) sender);
			sender.sendMessage(BLUE + "Cleared your session");
		}
	}

}
