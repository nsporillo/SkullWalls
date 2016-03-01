package net.porillo.skullwalls.commands;

import com.google.common.collect.ImmutableSet;
import net.porillo.skullwalls.SkullWalls;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SetHeadCommand extends BaseCommand {

    public SetHeadCommand(SkullWalls plugin) {
        super(plugin);
        super.setName("sethead");
        super.addUsage("[name]", null, "Changes the skull your facings skin");
        super.setPermission("skullwalls.sethead");
    }

    public void runCommand(CommandSender sender, List<String> args) {
        if (!this.checkPermission(sender)) {
            this.noPermission(sender);
            return;
        }

        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ChatColor.RED + "Console cannot use this command");
            return;
        }

        if (args.size() == 1) {
            Block b = ((Player) sender).getTargetBlock(ImmutableSet.of(), 6);
            if (b.getType().equals(Material.SKULL)) {
                Skull skull = (Skull) b.getState();
                skull.setOwner(args.get(0));
                skull.update();
                sender.sendMessage(ChatColor.BLUE + "Set SkullOwner to \"" + ChatColor.GOLD
                        + args.get(0) + ChatColor.BLUE + "\"");
            }
        }
    }
}
