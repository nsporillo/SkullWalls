package net.porillo.skullwalls.commands;

import java.util.List;

import net.porillo.skullwalls.Serializer;
import net.porillo.skullwalls.SkullWalls;
import net.porillo.skullwalls.walls.SkullWall;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class DeleteCommand extends BaseCommand {
	public DeleteCommand(SkullWalls plugin) {
		super(plugin);
		super.setName("delete");
		super.addUsage("[name]", null, "Deletes wall by name");
		super.setPermission("skullwalls.delete");
	}

	public void runCommand(CommandSender sender, List<String> args) {
		if (!this.checkPermission(sender)) {
			this.noPermission(sender);
			return;
		}
		if (args.size() == 0) {
			sender.sendMessage(ChatColor.RED + "Specify the name of the wall");
		} else if (args.size() == 1) {
			SkullWall sw = null;
			try {
				Serializer.save(SkullWalls.getWalls());
				for (SkullWall w : SkullWalls.getWalls()) {
					if (w.getName().equalsIgnoreCase(args.get(0))) {
						sw = w;
						break;
					}
				}
				if (sw != null) {
					SkullWalls.getWalls().remove(sw);
					Serializer.delete(sw);
					this.plugin.reload();
				} else {
					throw new RuntimeException("Wall does not exist!");
				}
			} catch (Exception ex) {
				sender.sendMessage(ChatColor.RED + "Error: " + ex.getMessage());
				return;
			}
			sender.sendMessage(ChatColor.RED + "Sucessfully removed wall " + ChatColor.GOLD
					+ args.get(0));
		}
	}
}
