package net.milkycraft.skullwalls.commands;

import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

import java.util.List;

import net.milkycraft.skullwalls.Serializer;
import net.milkycraft.skullwalls.SkullWalls;
import net.milkycraft.skullwalls.walls.SkullWall;
import net.milkycraft.skullwalls.walls.WallType;

import org.bukkit.command.CommandSender;

public class WallEditCommand extends BaseCommand {

	public WallEditCommand(final SkullWalls plugin) {
		super(plugin);
		super.setName("edit");
		super.addUsage("[wall]", "[option]", "Edits a wall");
		super.setPermission("skullwalls.edit");
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
			sender.sendMessage(RED + "Must specify a valid option {trans, radius, world}");
		} else if (args.size() > 1) {
			try {
				Serializer.save(SkullWalls.getWalls());
				for (SkullWall w : SkullWalls.getWalls()) {
					if (w.getName().equalsIgnoreCase(args.get(0))) {
						if (args.get(1).equalsIgnoreCase("transparency")
								|| args.get(1).equalsIgnoreCase("trans")) {
							if (args.size() == 3) {
								try {
									w.setTransparent(Boolean.valueOf(args.get(2)));
								} catch (Exception ex) {
									sender.sendMessage(RED
											+ "You must use a boolean! {true or false}");
									return;
								}
							}
							boolean bool = w.isTransparent();
							w.setTransparent(!bool);
							sender.sendMessage(GREEN + "Set " + w.getName()
									+ "'s transparency state to " + !bool);
							sender.sendMessage(GREEN
									+ "If true, skulls with no owners aren't visible");
						} else if (args.get(1).equalsIgnoreCase("radius")
								|| args.get(1).equalsIgnoreCase("r")) {
							if (args.size() == 3) {
								if (w.getType() != WallType.RADIUS) {
									sender.sendMessage(RED
											+ "Wall is not a Radius wall, setting radius does nothing!");
								}
								int rad = 0;
								try {
									rad = Integer.parseInt(args.get(2));
								} catch (Exception ex) {
									sender.sendMessage(RED
											+ "Usage: /skull edit <wall> radius <num>");
									return;
								}
								w.setRadius(rad);
								sender.sendMessage(GREEN + "Set " + w.getName() + "'s radius to "
										+ rad);
							} else {
								sender.sendMessage(RED + "Usage: /skull edit <wall> radius <num>");
							}
						} else if (args.get(1).equalsIgnoreCase("world")
								|| args.get(1).equalsIgnoreCase("w")) {
							if (args.size() == 3) {
								if (w.getType() != WallType.RADIUS) {
									sender.sendMessage(RED
											+ "Wall is not a World wall, setting world does nothing!");
								}
								w.setWallWorld(args.get(2));
								sender.sendMessage(GREEN + "Set " + w.getName() + "'s world to "
										+ args.get(2));
							} else {
								sender.sendMessage(RED
										+ "Usage: /skull edit <wall> world <world_name>");
							}
						}
						break;
					}
				}
			} catch (Exception ex) {
				sender.sendMessage(RED + "Error: " + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}
}
