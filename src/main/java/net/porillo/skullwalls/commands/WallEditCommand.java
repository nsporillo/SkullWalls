package net.porillo.skullwalls.commands;

import net.porillo.skullwalls.Serializer;
import net.porillo.skullwalls.SkullWalls;
import net.porillo.skullwalls.walls.SkullWall;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class WallEditCommand extends BaseCommand {
    public WallEditCommand(SkullWalls plugin) {
        super(plugin);
        super.setName("edit");
        super.addUsage("[wall]", "[option]", "Edits a wall");
        super.setPermission("skullwalls.edit");
    }

    public void runCommand(CommandSender sender, List<String> args) {
        if (!this.checkPermission(sender)) {
            this.noPermission(sender);
            return;
        }
        if (args.size() == 0)
            sender.sendMessage(ChatColor.RED + "Specify the name of the wall");
        else if (args.size() == 1)
            sender.sendMessage(ChatColor.RED + "Must specify a valid option {trans, radius, world}");
        else if (args.size() > 1)
            try {
                Serializer.save(SkullWalls.getWalls());
                for (SkullWall w : SkullWalls.getWalls())
                    if (w.getName().equalsIgnoreCase(args.get(0))) {
                        if (args.get(1).equalsIgnoreCase("transparency")
                                || args.get(1).equalsIgnoreCase("trans")) {
                            if (args.size() == 3) {
                                try {
                                    w.setTransparent(Boolean.valueOf(args.get(2)).booleanValue());
                                } catch (Exception ex) {
                                    sender.sendMessage(ChatColor.RED
                                            + "You must use a boolean! {true or false}");
                                    return;
                                }
                            }
                            boolean bool = w.isTransparent();
                            w.setTransparent(!bool);
                            sender.sendMessage(ChatColor.GREEN + "Set " + w.getName()
                                    + "'s transparency state to " + !bool);
                            sender.sendMessage(ChatColor.GREEN
                                    + "If true, skulls with no owners aren't visible");
                            break;
                        }
                    }
            } catch (Exception ex) {
                sender.sendMessage(ChatColor.RED + "Error: " + ex.getMessage());
                ex.printStackTrace();
            }
    }
}
