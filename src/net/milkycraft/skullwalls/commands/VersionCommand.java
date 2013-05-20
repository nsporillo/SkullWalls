package net.milkycraft.skullwalls.commands;

import static org.bukkit.ChatColor.*;
import java.util.List;

import net.milkycraft.skullwalls.SkullWalls;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

public class VersionCommand extends BaseCommand {

	public VersionCommand(final SkullWalls plugin) {
		super(plugin);
		super.setName("version");
	}

	@Override
	public void runCommand(final CommandSender sender, final List<String> args) {
		
		PluginDescriptionFile pdf = plugin.getDescription();
		sender.sendMessage(RED + "[DIAG] [W:" + SkullWalls.getWalls().size() + "]");
		sender.sendMessage(GOLD + "Version: " + pdf.getVersion());
		sender.sendMessage(GOLD + "Author: " + pdf.getAuthors().get(0));
		sender.sendMessage(GOLD + "Website: " + pdf.getWebsite());		
	}

}
