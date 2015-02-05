package net.porillo.skullwalls.commands;

import java.util.List;

import net.porillo.skullwalls.SkullWalls;
import net.porillo.skullwalls.walls.SkullWall;
import net.porillo.skullwalls.walls.WallType;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class WhitelistCommand extends BaseCommand {
	public WhitelistCommand(SkullWalls plugin) {
		super(plugin);
		super.setName("whitelist");
		super.addUsage("[wall]", "[player]", "Toggles whitelist state");
		super.setPermission("skullwalls.recalculate");
		super.setRequiredArgs(1);
	}

	public void runCommand(CommandSender s, List<String> args) {
		if (!this.checkPermission(s)) {
			this.noPermission(s);
			return;
		}
		if (args.size() == 0) {
			s.sendMessage(ChatColor.RED + "Specify the name of the wall");
		} else {
			if (args.size() == 1) {
				for (SkullWall w : SkullWalls.getWalls()) {
					if (!w.getType().equals(WallType.CUSTOM)
							|| !w.getName().equalsIgnoreCase(args.get(0)))
						s.sendMessage(ChatColor.GREEN + "===" + ChatColor.GRAY + w.getName()
								+ "'s whitelist" + ChatColor.GREEN + "===");
					for (String str : w.getWhitelist()) {
						s.sendMessage(ChatColor.AQUA + "- " + str);
					}

				}
			} else if (args.size() == 2) {
				String name = args.get(1);
				for (SkullWall w : SkullWalls.getWalls()) {
					String x = w.getName();
					if (x.equalsIgnoreCase(args.get(0))) {
						if (w.getType().equals(WallType.CUSTOM)) {
							if (w.getWhitelist().contains(name)) {
								s.sendMessage(ChatColor.RED + "Removed " + name + " from " + x
										+ "'s whitelist");
							} else {
								w.getWhitelist().add(name);
								s.sendMessage(ChatColor.RED + "Added " + name + " to " + x
										+ "'s whitelist");
							}
						}
						w.updateWall(null);
					}
				}
			}
		}
	}
}
