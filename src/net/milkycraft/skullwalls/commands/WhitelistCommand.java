package net.milkycraft.skullwalls.commands;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

import java.util.List;

import net.milkycraft.skullwalls.SkullWalls;
import net.milkycraft.skullwalls.walls.SkullWall;
import net.milkycraft.skullwalls.walls.WallType;

import org.bukkit.command.CommandSender;

public class WhitelistCommand extends BaseCommand {

	public WhitelistCommand(final SkullWalls plugin) {
		super(plugin);
		super.setName("whitelist");
		super.addUsage("[wall]", "[player]", "Toggles whitelist state");
		super.setPermission("skullwalls.recalculate");
		super.setRequiredArgs(1);
	}

	@Override
	public void runCommand(final CommandSender s, final List<String> args) {
		if (!this.checkPermission(s)) {
			this.noPermission(s);
			return;
		}
		if (args.size() == 0) {
			s.sendMessage(RED + "Specify the name of the wall");
		} else if (args.size() == 1) {
			for (SkullWall w : SkullWalls.getWalls()) {
				if (w.getType().equals(WallType.CUSTOM)) {
					if (w.getName().equalsIgnoreCase(args.get(0))) {
						s.sendMessage(GREEN + "===" + GRAY + w.getName()
								+ "'s whitelist" + GREEN + "===");
						for (String str : w.getWhitelist()) {
							s.sendMessage(AQUA + "- " + str);
						}
					}
				}
			}
		} else if (args.size() == 2) {
			String name = args.get(1);
			for (SkullWall w : SkullWalls.getWalls()) {
				String x = w.getName();
				if (x.equalsIgnoreCase(args.get(0))) {
					if (w.getType().equals(WallType.CUSTOM)) {
						if (w.getWhitelist().contains(name)) {
							s.sendMessage(RED + "Removed " + name + " from "
									+ x + "'s whitelist");
						} else {
							w.getWhitelist().add(name);
							s.sendMessage(RED + "Added " + name + " to " + x
									+ "'s whitelist");
						}
					}
					w.recalculate();
				}
			}
		}
	}
}
