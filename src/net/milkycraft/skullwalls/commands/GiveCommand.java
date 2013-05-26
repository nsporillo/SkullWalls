package net.milkycraft.skullwalls.commands;

import static org.bukkit.ChatColor.*;

import java.util.List;

import net.milkycraft.skullwalls.SkullWalls;
import org.bukkit.command.CommandSender;

public class GiveCommand extends BaseCommand {

	public GiveCommand(SkullWalls plugin) {
		super(plugin);
		super.setName("give");
		super.addUsage("<alerts>", null, "Toggles your item transfer mode");
	}

	@Override
	public void runCommand(CommandSender sender, List<String> args) {
		if (args.size() == 0) {
			if (sender.hasPermission("skullwalls.transfer.toggle")) {
				if (plugin.getTrans().contains(sender.getName())) {
					plugin.getTrans().remove(sender.getName());
					sender.sendMessage(GREEN + "Your transfer mode is toggled off");
				} else {
					plugin.getTrans().add(sender.getName());
					sender.sendMessage(RED + "Your transfer mode is toggled on");
				}
			}
		} else if(args.size() == 1) {
			if(args.get(0).equalsIgnoreCase("alerts")) {
				if (plugin.getAlerts().contains(sender.getName())) {
					plugin.getAlerts().remove(sender.getName());
					sender.sendMessage(GREEN + "Your alert mode is toggled off");
				} else {
					plugin.getAlerts().add(sender.getName());
					sender.sendMessage(RED + "Your alert mode is toggled on");
				}
			} else {
				sender.sendMessage(GREEN + "To disable your alerts, use /skull give alerts");
			}
		}
	}
}
