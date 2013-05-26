package net.milkycraft.skullwalls.commands;

import static org.bukkit.ChatColor.*;
import java.util.List;

import net.milkycraft.skullwalls.SkullWalls;
import net.milkycraft.skullwalls.walls.Utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class VersionCommand extends BaseCommand {

	public VersionCommand(final SkullWalls plugin) {
		super(plugin);
		super.setName("version");
	}

	@Override
	public void runCommand(final CommandSender sender, final List<String> args) {
		Utils.para(((Player)sender).getLocation(), Integer.parseInt(args.get(0)));
		PluginDescriptionFile pdf = plugin.getDescription();
		sender.sendMessage(RED + "[DIAG] [W:" + SkullWalls.getWalls().size() + "]");
		sender.sendMessage(GOLD + "Version: " + pdf.getVersion());
		sender.sendMessage(GOLD + "Author: " + pdf.getAuthors().get(0));
		sender.sendMessage(GOLD + "Website: " + pdf.getWebsite());		
	}

}
