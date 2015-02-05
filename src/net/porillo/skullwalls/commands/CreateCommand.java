package net.porillo.skullwalls.commands;

import static org.bukkit.ChatColor.BLUE;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.WHITE;

import java.util.List;

import net.porillo.skullwalls.SkullWalls;
import net.porillo.skullwalls.walls.SkullWall;
import net.porillo.skullwalls.walls.WallType;

import org.apache.commons.lang.WordUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CreateCommand extends BaseCommand {
	public CreateCommand(SkullWalls plugin) {
		super(plugin);
		super.setName("create");
		super.addUsage("[name]", "[type]", "Creates a new skull wall");
		super.setPermission("skullwalls.create");
	}

	public void runCommand(CommandSender sender, List<String> args) {
		if (!this.checkPermission(sender)) {
			this.noPermission(sender);
			return;
		}
		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage(RED + "Console cannot use this command");
			return;
		}
		if (args.size() == 0) {
			sender.sendMessage(RED + "Specify the name of the wall");
		} else if (args.size() == 1) {
			sender.sendMessage(RED
					+ "Specify the wall type: {GLOBAL, CUSTOM, BANNED, WORLD, RADIUS}");
		} else if (args.size() > 1) {
			SkullWall wall = null;
			WallType type = null;
			try {
				type = WallType.valueOf(args.get(1).toUpperCase());
				wall = this.plugin.getCuboider().createWall((Player) sender, type,
						WordUtils.capitalize(args.get(0)));
				for (SkullWall w : SkullWalls.getWalls()) {
					if (wall.getName() == w.getName()) {
						sender.sendMessage(RED + "Error: That wall already exists!");
						return;
					}
				}
				SkullWalls.getWalls().add(wall);
			} catch (NullPointerException ex) {
				sender.sendMessage(RED + "Error: Your cuboid is not complete!");
				return;
			} catch (IllegalArgumentException exx) {
				sender.sendMessage(RED
						+ "Specify the wall type: {GLOBAL, CUSTOM, BANNED, WORLD, RADIUS}");
				return;
			}
			sender.sendMessage(GREEN + "Successfully created a SkullWall!");
			sender.sendMessage(BLUE + "Name: " + WHITE + WordUtils.capitalize(wall.getName()));
			sender.sendMessage(BLUE + "Type: " + WHITE
					+ WordUtils.capitalize(wall.getType().toString().toLowerCase()));
			sender.sendMessage(BLUE + "Size: " + WHITE + wall.getSlots().size() + " slots");
		}
	}
}
