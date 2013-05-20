package net.milkycraft.skullwalls.commands;

import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

import java.util.List;

import net.milkycraft.skullwalls.SkullWalls;
import net.milkycraft.skullwalls.walls.SkullWall;

import org.bukkit.command.CommandSender;

public class UpdateCommand extends BaseCommand {

	public UpdateCommand(final SkullWalls plugin) {
		super(plugin);
		super.setName("update");
		super.addUsage("[wall]", null, "Recalculates all slots");
		super.setPermission("skullwalls.update");
	}

	@Override
	public void runCommand(final CommandSender sender, final List<String> args) {
		if (!this.checkPermission(sender)) {
			this.noPermission(sender);
			return;
		}
		if (args.size() == 0) {
			sender.sendMessage(RED + "Specify the name of the wall");
		} else if (args.size() == 1) {
			final String arg = args.get(0);
			for (final SkullWall w : SkullWalls.getWalls()) {
				if (w.getName().equalsIgnoreCase(arg)
						|| arg.equalsIgnoreCase("all")) {
					w.recalculate();
				}
			}
			final String wall = arg.equals("all") ? " all walls " : arg + "'s";
			sender.sendMessage(GREEN + "Attempting to update " + GOLD + wall
					+ GREEN + " heads");
		}
	}
}
