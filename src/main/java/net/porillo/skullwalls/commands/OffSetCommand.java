package net.porillo.skullwalls.commands;

import net.porillo.skullwalls.SkullWalls;
import net.porillo.skullwalls.walls.SkullWall;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.List;

public class OffSetCommand extends BaseCommand {
    public OffSetCommand(SkullWalls plugin) {
        super(plugin);
        super.setName("offset");
        super.addUsage("[wall]", "[newStart]", "Changes the start block of a wall");
        super.setPermission("skullwalls.offset");
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
        if (args.size() == 0)
            sender.sendMessage(ChatColor.RED + "Usage: /skull offset <wall> <number>");
        else if (args.size() == 1)
            sender.sendMessage(ChatColor.RED + "Usage: /skull offset <wall> <number>");
        else if (args.size() == 2)
            try {
                for (SkullWall sw : SkullWalls.getWalls())
                    if (sw.getName().equalsIgnoreCase(args.get(0)))
                        sw.offSet(Integer.parseInt(args.get(1)));
            } catch (Exception ex) {
                sender.sendMessage(ChatColor.RED + "Error: " + ex.getMessage());
                ex.printStackTrace();
            }
    }
}
