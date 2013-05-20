package net.milkycraft.skullwalls.commands;

import static org.bukkit.ChatColor.BLUE;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.RED;

import java.util.List;

import net.milkycraft.skullwalls.SkullWalls;
import net.milkycraft.skullwalls.walls.Utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class RotateCommand extends BaseCommand {

	public RotateCommand(SkullWalls plugin) {
		super(plugin);
		super.setName("rotate");
		super.addUsage("[blockface]", null, "Rotates skull");
		super.setPermission("skullwalls.rotate");
	}

	@Override
	public void runCommand(CommandSender sender, List<String> args) {
		if (!this.checkPermission(sender)) {
			this.noPermission(sender);
			return;
		}
		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage(RED + "Console cannot use this command");
			return;
		}
		if(args.size() == 0) {
			sender.sendMessage(RED + "Usage: /skull rotate <blockface>");
		} else if(args.size() == 1) {
			final Block b = ((Player) sender).getTargetBlock(null, 6);
			if (b.getType().equals(Material.SKULL)) {
				final Skull skull = ((Skull) b.getState());
				BlockFace face = null;
				try {
					face = BlockFace.valueOf(args.get(0).toUpperCase());
					int rot = Utils.getRot(face);
					if (rot > 0) {
						b.setTypeIdAndData(b.getTypeId(), (byte) rot, true);	
						sender.sendMessage(BLUE + "Rotation changed to " + GOLD
								+ face.toString().toLowerCase());
						return;
					}
				} catch (Exception ex) {
					sender.sendMessage(RED + "Not a valid block face!");
					return;
				}
				skull.setRotation(face);
				skull.update();
				sender.sendMessage(BLUE + "Rotation changed to " + GOLD
						+ face.toString().toLowerCase());
			}
		} else if(args.size() == 2) {
			if(args.get(1).equalsIgnoreCase("cuboid")) {
				BlockFace face = null;
				try {
					face = BlockFace.valueOf(args.get(0).toUpperCase());
					int rot = Utils.getRot(face);
					if (rot > 0) {
						plugin.getCuboider().rotateSkulls((Player)sender, face);
						return;
					}
				} catch (Exception ex) {
					sender.sendMessage(RED + "Not a valid block face!");
					return;
				}			
			}
		}
	}
}
