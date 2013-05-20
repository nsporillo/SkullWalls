package net.milkycraft.skullwalls.commands;

import static org.bukkit.ChatColor.*;

import java.util.List;

import net.milkycraft.skullwalls.SkullWalls;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SetHeadCommand extends BaseCommand {

	public SetHeadCommand(final SkullWalls plugin) {
		super(plugin);
		super.setName("sethead");
		super.addUsage("[name]", null, "Changes the skull your facings skin");
		super.setPermission("skullwalls.sethead");
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
		if (args.size() == 1) {
			final Block b = ((Player) sender).getTargetBlock(null, 6);
			if (b.getType().equals(Material.SKULL)) {
				final Skull skull = ((Skull) b.getState());
				skull.setOwner(args.get(0));
				skull.update();
				sender.sendMessage(BLUE + "Set SkullOwner to \"" + GOLD
						+ args.get(0) + BLUE + "\"");
			}
		}
	}

}
