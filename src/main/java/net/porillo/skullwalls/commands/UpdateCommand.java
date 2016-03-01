package net.porillo.skullwalls.commands;

import net.porillo.skullwalls.SkullWalls;
import org.bukkit.command.CommandSender;

import java.util.List;

import static org.bukkit.ChatColor.*;

public class UpdateCommand extends BaseCommand {

    public UpdateCommand(SkullWalls plugin) {
        super(plugin);
        super.setName("update");
        super.addUsage("[wall]", null, "Recalculates all slots");
        super.setPermission("skullwalls.update");
        super.setConsoleOnly(false);
    }

    public void runCommand(CommandSender sender, List<String> args) {
        if (args.size() == 0) {
            sender.sendMessage(RED + "Specify the name of the wall");
        } else if (args.size() == 1) {
            String arg = args.get(0);

            SkullWalls.getWallHandler().getWalls().stream().filter(w -> w.getName().equalsIgnoreCase(arg) || arg.equalsIgnoreCase("all")).forEach(w -> w.updateWall(null));

            sender.sendMessage(GREEN + "Attempting to update " + GOLD + arg + "'s" + GREEN + " heads");
        }
    }
}
