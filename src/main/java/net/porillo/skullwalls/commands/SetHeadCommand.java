package net.porillo.skullwalls.commands;

import net.porillo.skullwalls.SkullWalls;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;

public class SetHeadCommand extends BaseCommand {

    public SetHeadCommand(SkullWalls plugin) {
        super(plugin);
        super.setName("sethead");
        super.addUsage("[name]", null, "Changes the skull your facings skin");
        super.setPermission("skullwalls.sethead");
        super.setConsoleOnly(true);
    }

    public void runCommand(CommandSender sender, List<String> args) {
        if (args.size() == 1) {
            Block b = ((Player) sender).getTargetBlock((HashSet<Byte>)null, 6);
            if (b.getType().equals(Material.SKULL)) {
                Skull skull = (Skull) b.getState();
                skull.setSkullType(SkullType.PLAYER);
                skull.setOwner(args.get(0));
                skull.update();
                sender.sendMessage(ChatColor.BLUE + "Set SkullOwner to \"" + ChatColor.GOLD + args.get(0) + ChatColor.BLUE + "\"");
            }
        }
    }
}
