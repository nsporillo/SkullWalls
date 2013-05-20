package net.milkycraft.skullwalls.commands;

import static org.bukkit.ChatColor.RED;

import java.util.List;

import net.milkycraft.skullwalls.SkullWalls;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WandCommand extends BaseCommand {

	public WandCommand(final SkullWalls plugin) {
		super(plugin);
		super.setName("wand");
		super.addUsage(null, null, "Gives you the wall cuboid tool");
		super.setPermission("skullwalls.wand");
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
			Player p = (Player)sender;
			int id = plugin.getConfiguration().getTool();
			if(p.getInventory().contains(id)) {
				p.sendMessage(RED + "You already have the cuboid tool");
			} else {
				ItemStack is = new ItemStack(id, 1);
				p.getInventory().addItem(is);
				p.sendMessage(RED + "You now have a " + is.getType().name().toLowerCase());
			}
		}
	}

}