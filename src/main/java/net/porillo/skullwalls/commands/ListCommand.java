package net.porillo.skullwalls.commands;

import net.porillo.skullwalls.SkullWalls;
import net.porillo.skullwalls.walls.SkullWall;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ListCommand extends BaseCommand {
    public ListCommand(SkullWalls plugin) {
        super(plugin);
        super.setName("list");
        super.addUsage(null, null, "Lists all SkullWalls");
        super.setPermission("skullwalls.list");
    }

    public void runCommand(CommandSender sender, List<String> args) {
        if (!this.checkPermission(sender)) {
            this.noPermission(sender);
            return;
        }
        if (args.size() == 0) {
            SkullWall[] walls = (SkullWall[]) SkullWalls.getWalls().toArray(
                    new SkullWall[SkullWalls.getWalls().size()]);
            sender.sendMessage(ChatColor.BLUE + "Listing the info for "
                    + SkullWalls.getWalls().size() + " SkullWalls");
            for (int i = 0; i < walls.length; i++) {
                SkullWall w = walls[i];
                sender.sendMessage(ChatColor.BLUE + "[" + ChatColor.GOLD + (i + 1) + ChatColor.BLUE
                        + "] Name: " + ChatColor.WHITE + w.getName() + ChatColor.GOLD + " Type: "
                        + ChatColor.WHITE + w.getType() + ChatColor.GOLD + " Bounds: "
                        + ChatColor.WHITE + w.getStringBounds());
            }
        }
    }
}
