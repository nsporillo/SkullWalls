package net.porillo.skullwalls.commands;

import com.google.common.collect.ImmutableSet;
import net.porillo.skullwalls.SkullWalls;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static org.bukkit.ChatColor.*;

public class RotateCommand extends BaseCommand {

    public RotateCommand(SkullWalls plugin) {
        super(plugin);
        super.setName("rotate");
        super.addUsage("[blockface]", null, "Rotates skull");
        super.setPermission("skullwalls.rotate");
        super.setConsoleOnly(true);
    }

    public void runCommand(CommandSender sender, List<String> args) {
        if (args.size() > 0) {
            Block b = ((Player) sender).getTargetBlock(ImmutableSet.of(), 6);
            if (b.getType().equals(Material.SKULL)) {
                Skull skull = (Skull) b.getState();
                BlockFace face;
                try {
                    face = BlockFace.valueOf(args.get(0).toUpperCase());
                    int rot = -1;
                    if (face.equals(BlockFace.SELF))
                        rot = 0;
                    else if (face.equals(BlockFace.NORTH))
                        rot = 2;
                    else if (face.equals(BlockFace.SOUTH))
                        rot = 3;
                    else if (face.equals(BlockFace.EAST))
                        rot = 4;
                    else if (face.equals(BlockFace.WEST)) {
                        rot = 5;
                    }
                    if (rot > 0) {
                        b.setTypeIdAndData(b.getTypeId(), (byte) rot, true);
                        sender.sendMessage(BLUE + "Rotation changed to " + GOLD + face.toString().toLowerCase());
                        return;
                    }
                } catch (Exception ex) {
                    sender.sendMessage(RED + "Not a valid block face!");
                    return;
                }
                skull.setRotation(face);
                skull.update();
                sender.sendMessage(BLUE + "Rotation changed to " + GOLD + face.toString().toLowerCase());
            }
        }
    }
}
