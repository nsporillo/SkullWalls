package net.milkycraft.skullwalls.commands;

import static org.bukkit.ChatColor.RED;

import java.util.List;

import net.milkycraft.skullwalls.SkullWalls;
import net.milkycraft.skullwalls.walls.SkullWall;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class OffSetCommand extends BaseCommand {

	public OffSetCommand(final SkullWalls plugin) {
		super(plugin);
		super.setName("offset");
		super.addUsage("[wall]", "[newStart]", "Changes the start block of a wall");
		super.setPermission("skullwalls.offset");
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
		if(args.size() == 0) {
			sender.sendMessage(RED + "Usage: /skull offset <wall> <number>");
		} else if(args.size() == 1) {
			sender.sendMessage(RED + "Usage: /skull offset <wall> <number>");
		} else if(args.size() == 2) {
			try {
			for(SkullWall sw : SkullWalls.getWalls()) {
				if(sw.getName().equalsIgnoreCase(args.get(0))) {
					sw.offSet(Integer.parseInt(args.get(1)));
				}
			}
			} catch(Exception ex) {
				sender.sendMessage(RED + "Error: " + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}

}
