package net.milkycraft.skullwalls.commands;

import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.RED;

import java.util.List;

import net.milkycraft.skullwalls.Serializer;
import net.milkycraft.skullwalls.SkullWalls;
import net.milkycraft.skullwalls.walls.SkullWall;

import org.bukkit.command.CommandSender;

public class DeleteCommand extends BaseCommand {

	public DeleteCommand(final SkullWalls plugin) {
		super(plugin);
		super.setName("delete");
		super.addUsage("[name]", null, "Deletes wall by name");
		super.setPermission("skullwalls.delete");
	}

	@Override
	public void runCommand(final CommandSender sender, final List<String> args) {
		if (!this.checkPermission(sender)) {
			this.noPermission(sender);
			return;
		}
		if (args.size() == 0) {
			sender.sendMessage(RED + "Specify the name of the wall");
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
					sw.wipe();
					SkullWalls.getWalls().remove(sw);
					Serializer.delete(sw);
					this.plugin.reload();
				} else {
					throw new RuntimeException("Wall does not exist!");
				}
				
			} catch (final Exception ex) {
				sender.sendMessage(RED + "Error: " + ex.getMessage());
				return;
			}
			sender.sendMessage(RED + "Sucessfully removed wall " + GOLD
					+ args.get(0));
		}
	}

}
