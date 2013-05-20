package net.milkycraft.skullwalls.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.milkycraft.skullwalls.SkullWalls;
import net.milkycraft.skullwalls.walls.SkullWall;
import net.milkycraft.skullwalls.walls.Utils;

import static org.bukkit.ChatColor.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author krinsdeath
 */
public class CommandHandler {

	private Map<String, Command> commands = new HashMap<String, Command>();

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

	public void runCommand(final CommandSender sender, final String label,
			final String[] args) {
		if (args.length == 1) {
			if (args[0].equals("test")) {
				for (SkullWall sw : SkullWalls.getWalls()) {
					sender.sendMessage(BLUE + "Wall: " + sw.getName());
					sender.sendMessage(GOLD + "CentDist: "
							+ Utils.getDistanceFromCenter(sw, (Player) sender));
					sender.sendMessage(BLUE + "Transparent: "
							+ sw.isTransparent());
					return;
				}
			}
		}
		if (args.length == 0
				|| this.commands.get(args[0].toLowerCase()) == null) {
			sender.sendMessage(GOLD + "SkullWalls is developed by milkywayz");
			sender.sendMessage(GREEN + "===" + GOLD + " SkullWalls Help "
					+ GREEN + "===");
			for (final Command cmd : this.commands.values()) {
				if (cmd.checkPermission(sender)) {
					cmd.showHelp(sender, label);
				}
			}
			return;
		}
		final List<String> arguments = new ArrayList<String>(
				Arrays.asList(args));
		final Command cmd = this.commands
				.get(arguments.remove(0).toLowerCase());
		if (arguments.size() < cmd.getRequiredArgs()) {
			cmd.showHelp(sender, label);
			return;
		}
		cmd.runCommand(sender, arguments);
	}
}