package net.milkycraft.skullwalls.commands;

import static org.bukkit.ChatColor.BLUE;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.WHITE;

import java.util.List;

import net.milkycraft.skullwalls.SkullWalls;
import net.milkycraft.skullwalls.walls.SkullWall;

import org.bukkit.command.CommandSender;

public class ListCommand extends BaseCommand {

	public ListCommand(final SkullWalls plugin) {
		super(plugin);
		super.setName("list");
		super.addUsage(null, null, "Lists all SkullWalls");
		super.setPermission("skullwalls.list");
	}

	@Override
	public void runCommand(final CommandSender sender, final List<String> args) {
		if (!this.checkPermission(sender)) {
			this.noPermission(sender);
			return;
		}
		if (args.size() == 0) {
			final SkullWall[] walls = SkullWalls.getWalls().toArray(
					new SkullWall[SkullWalls.getWalls().size()]);
			sender.sendMessage(BLUE + "Listing the info for "
					+ SkullWalls.getWalls().size() + " SkullWalls");
			for (int i = 0; i < walls.length; i++) {
				final SkullWall w = walls[i];
				sender.sendMessage(BLUE + "[" + GOLD + (i + 1) + BLUE
						+ "] Name: " + WHITE + w.getName() + GOLD + " Type: "
						+ WHITE + w.getType() + GOLD + " Bounds: " + WHITE
						+ w.getStringBounds());
			}
		}
	}
}
