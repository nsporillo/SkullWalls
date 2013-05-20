package net.milkycraft.skullwalls.commands;

import static org.bukkit.ChatColor.BLUE;

import java.util.List;

import net.milkycraft.skullwalls.SkullWalls;
import net.milkycraft.skullwalls.walls.SkullWall;

import org.bukkit.command.CommandSender;

public class ReloadCommand extends BaseCommand {

	public ReloadCommand(final SkullWalls plugin) {
		super(plugin);
		super.setName("reload");
		super.addUsage(null, null, "Reloads plugin");
		super.setPermission("skullwalls.reload");
	}

	@Override
	public void runCommand(final CommandSender sender, final List<String> args) {
		if (!this.checkPermission(sender)) {
			this.noPermission(sender);
			return;
		}
		if (args.size() == 0) {
			sender.sendMessage(BLUE + "Reloading configuration and walls!");
			this.plugin.getConfiguration().reload();
			this.plugin.reload();
			for (SkullWall w : SkullWalls.getWalls()) {
				w.recalculate();
			}
		}
	}
}
