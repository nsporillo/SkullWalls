package net.porillo.skullwalls.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.porillo.skullwalls.SkullWalls;
import net.porillo.skullwalls.walls.SkullWall;
import net.porillo.skullwalls.walls.Utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler {
	private Map<String, Command>	commands	= new HashMap();

	public CommandHandler(SkullWalls plugin) {
		this.commands.put("create", new CreateCommand(plugin));
		this.commands.put("wand", new WandCommand(plugin));
		this.commands.put("edit", new WallEditCommand(plugin));
		this.commands.put("action", new ActionCommand(plugin));
		this.commands.put("delete", new DeleteCommand(plugin));
		this.commands.put("list", new ListCommand(plugin));
		this.commands.put("clear", new ClearCommand(plugin));
		this.commands.put("update", new UpdateCommand(plugin));
		this.commands.put("whitelist", new WhitelistCommand(plugin));
		this.commands.put("reload", new ReloadCommand(plugin));
		this.commands.put("sethead", new SetHeadCommand(plugin));
		this.commands.put("rotate", new RotateCommand(plugin));
		this.commands.put("offset", new OffSetCommand(plugin));
		this.commands.put("version", new VersionCommand(plugin));
	}

	public void runCommand(CommandSender sender, String label, String[] args) {
		Iterator localIterator;
		if (args.length == 1 && args[0].equals("test")) {
			localIterator = SkullWalls.getWalls().iterator();
			if (localIterator.hasNext()) {
				SkullWall sw = (SkullWall) localIterator.next();
				sender.sendMessage(ChatColor.BLUE + "Wall: " + sw.getName());
				sender.sendMessage(ChatColor.GOLD + "CentDist: "
						+ Utils.getDistanceFromCenter(sw, (Player) sender));
				sender.sendMessage(ChatColor.BLUE + "Transparent: " + sw.isTransparent());
				return;
			}
		}
		if (args.length == 0 || this.commands.get(args[0].toLowerCase()) == null) {
			sender.sendMessage(ChatColor.GOLD + "SkullWalls is developed by milkywayz");
			sender.sendMessage(ChatColor.GREEN + "===" + ChatColor.GOLD + " SkullWalls Help "
					+ ChatColor.GREEN + "===");
			for (Command cmd : this.commands.values()) {
				if (cmd.checkPermission(sender)) {
					cmd.showHelp(sender, label);
				}
			}
			return;
		}
		List arguments = new ArrayList(Arrays.asList(args));
		Command cmd = this.commands.get(((String) arguments.remove(0)).toLowerCase());
		if (arguments.size() < cmd.getRequiredArgs()) {
			cmd.showHelp(sender, label);
			return;
		}
		cmd.runCommand(sender, arguments);
	}
}
