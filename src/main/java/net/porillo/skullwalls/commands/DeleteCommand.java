package net.porillo.skullwalls.commands;

import net.porillo.skullwalls.SkullWalls;
import net.porillo.skullwalls.walls.Wall;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Set;

public class DeleteCommand extends BaseCommand {

    public DeleteCommand(SkullWalls plugin) {
        super(plugin);
        super.setName("delete");
        super.addUsage("[name]", null, "Deletes wall by name");
        super.setPermission("skullwalls.delete");
        super.setConsoleOnly(false);
    }

    public void runCommand(CommandSender sender, List<String> args) {
        if (args.size() == 0) {
            sender.sendMessage(ChatColor.RED + "Specify the name of the wall");
        } else if (args.size() == 1) {
            Wall sw = null;

            try {
                Set<Wall> wallsCopy = SkullWalls.getWallHandler().getReadOnlyWalls();
                for (Wall w : wallsCopy) {
                    if (w.getName().equalsIgnoreCase(args.get(0))) {
                        sw = w;
                        break;
                    }
                }

                if (sw != null) {
                    SkullWalls.getWallHandler().remove(sw);
                    plugin.reload(); // saves, loads, then resets all walls
                } else {
                    throw new RuntimeException("Wall does not exist!");
                }
            } catch (Exception ex) {
                sender.sendMessage(ChatColor.RED + "Error: " + ex.getMessage());
                return;
            }

            sender.sendMessage(ChatColor.RED + "Sucessfully removed wall " + ChatColor.GOLD + args.get(0));
        }
    }
}
