package net.porillo.skullwalls.commands;

import net.porillo.skullwalls.SkullWalls;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ClearCommand extends BaseCommand {

    public ClearCommand(SkullWalls plugin) {
        super(plugin);
        super.setName("clear");
        super.addUsage(null, null, "Clear your current session");
        super.setPermission("skullwalls.clear");
        super.setConsoleOnly(true);
    }

    public void runCommand(CommandSender sender, List<String> args) {
        if (args.size() == 0) {
            SkullWalls.getCuboidHandler().clear(sender.getName());
            sender.sendMessage(ChatColor.BLUE + "Cleared your session");
        }
    }
}
