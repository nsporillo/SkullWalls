package net.porillo.skullwalls.commands;

import net.porillo.skullwalls.SkullWalls;
import net.porillo.skullwalls.walls.Wall;
import org.bukkit.command.CommandSender;

import java.util.List;

import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

public class WallEditCommand extends BaseCommand {

    public WallEditCommand(SkullWalls plugin) {
        super(plugin);
        super.setName("edit");
        super.addUsage("[wall]", "[option]", "Edits a wall");
        super.setPermission("skullwalls.edit");
        super.setConsoleOnly(false);
    }

    public void runCommand(CommandSender sender, List<String> args) {
        if (args.size() == 0)
            sender.sendMessage(RED + "Specify the name of the wall");
        else if (args.size() == 1)
            sender.sendMessage(RED + "Must specify a valid option {trans, radius, world}");
        else if (args.size() > 1)
            try {
                Serializer.save(SkullWalls.getWallHandler().getWalls());
                for (Wall w : SkullWalls.getWallHandler().getWalls())
                    if (w.getName().equalsIgnoreCase(args.get(0))) {
                        if (args.get(1).equalsIgnoreCase("transparency") || args.get(1).equalsIgnoreCase("trans")) {
                            if (args.size() == 3) {
                                try {
                                    w.setTransparent(Boolean.valueOf(args.get(2)));
                                } catch (Exception ex) {
                                    sender.sendMessage(RED + "You must use a boolean! {true or false}");
                                    return;
                                }
                            }

                            boolean bool = w.isTransparent();
                            w.setTransparent(!bool);
                            sender.sendMessage(GREEN + "Set " + w.getName() + "'s transparency state to " + !bool);
                            sender.sendMessage(GREEN + "If true, skulls with no owners aren't visible");
                            break;
                        }
                    }
            } catch (Exception ex) {
                sender.sendMessage(RED + "Error: " + ex.getMessage());
                ex.printStackTrace();
            }
    }
}
