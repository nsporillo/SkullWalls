package net.porillo.skullwalls.commands;

import java.util.List;

import net.porillo.skullwalls.SkullWalls;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WandCommand extends BaseCommand {
	public WandCommand(SkullWalls plugin) {
		super(plugin);
		super.setName("wand");
		super.addUsage(null, null, "Gives you the wall cuboid tool");
		super.setPermission("skullwalls.wand");
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
			Player p = (Player) sender;
			int id = this.plugin.getConfiguration().getTool();
			if (p.getInventory().contains(id)) {
				p.sendMessage(ChatColor.RED + "You already have the cuboid tool");
			} else {
				ItemStack is = new ItemStack(id, 1);
				p.getInventory().addItem(new ItemStack[] { is });
				p.sendMessage(ChatColor.RED + "You now have a " + is.getType().name().toLowerCase());
			}
		}
	}
}
