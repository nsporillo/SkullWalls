package net.porillo.skullwalls.commands;

import java.util.List;

import net.porillo.skullwalls.SkullWalls;
import net.porillo.skullwalls.walls.SkullWall;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class UpdateCommand extends BaseCommand {
	public UpdateCommand(SkullWalls plugin) {
		super(plugin);
		super.setName("update");
		super.addUsage("[wall]", null, "Recalculates all slots");
		super.setPermission("skullwalls.update");
	}

	public void runCommand(CommandSender sender, List<String> args) {
		if (!this.checkPermission(sender)) {
			this.noPermission(sender);
			return;
		}
		if (args.size() == 0) {
			sender.sendMessage(ChatColor.RED + "Specify the name of the wall");
		} else if (args.size() == 1) {
			String arg = args.get(0);
			for (SkullWall w : SkullWalls.getWalls()) {
				if (w.getName().equalsIgnoreCase(arg) || arg.equalsIgnoreCase("all")) {

					w.updateWall(null);
				}
			}
			String wall = arg + "'s";
			sender.sendMessage(ChatColor.GREEN + "Attempting to update " + ChatColor.GOLD + wall
					+ ChatColor.GREEN + " heads");
		}
	}
}
