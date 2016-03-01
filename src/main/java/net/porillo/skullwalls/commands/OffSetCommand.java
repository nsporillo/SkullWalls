package net.porillo.skullwalls.commands;

import net.porillo.skullwalls.SkullWalls;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class OffSetCommand extends BaseCommand {

    public OffSetCommand(SkullWalls plugin) {
        super(plugin);
        super.setName("offset");
        super.addUsage("[wall]", "[newStart]", "Changes the start block of a wall");
        super.setPermission("skullwalls.offset");
        super.setConsoleOnly(true);
    }

    public void runCommand(CommandSender sender, List<String> args) {
        if (args.size() == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /skull offset <wall> <number>");
        } else if (args.size() == 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /skull offset <wall> <number>");
        } else if (args.size() == 2) {
            try {
                // Find wall and offset by user-input value
                SkullWalls.getWalls().stream().filter(sw -> sw.getName().equalsIgnoreCase(args.get(0))).forEach(sw -> sw.offSet(Integer.parseInt(args.get(1))));
            } catch (Exception ex) {
                sender.sendMessage(ChatColor.RED + "Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}
