package net.porillo.skullwalls.commands;

import net.porillo.skullwalls.SkullWalls;
import net.porillo.skullwalls.walls.Wall;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.*;

import static org.bukkit.ChatColor.*;

public class CommandHandler {
    private Map<String, Command> commands = new HashMap<>();

    public CommandHandler(SkullWalls plugin) {
        this.commands.put("create", new CreateCommand(plugin));
        this.commands.put("wand", new WandCommand(plugin));
        this.commands.put("edit", new WallEditCommand(plugin));
        this.commands.put("action", new ActionCommand(plugin));
        this.commands.put("delete", new DeleteCommand(plugin));
        this.commands.put("list", new ListCommand(plugin));
        this.commands.put("clear", new ClearCommand(plugin));
        this.commands.put("update", new UpdateCommand(plugin));
        this.commands.put("whitelist", new WhitelistCommand(plugin));
        this.commands.put("reload", new ReloadCommand(plugin));
        this.commands.put("sethead", new SetHeadCommand(plugin));
        this.commands.put("rotate", new RotateCommand(plugin));
        this.commands.put("offset", new OffSetCommand(plugin));
        this.commands.put("version", new VersionCommand(plugin));
    }

    public void runCommand(CommandSender sender, String label, String[] args) {
        if (args.length == 1 && args[0].equals("test")) {
            Iterator localIterator = SkullWalls.getWallHandler().getReadOnlyWalls().iterator();
            if (localIterator.hasNext()) {
                Wall sw = (Wall) localIterator.next();
                sender.sendMessage(BLUE + "Wall: " + sw.getName());
                sender.sendMessage(GOLD + "CentDist: " + SkullWalls.getWallHandler().getDistanceFromCenter(sw, (Player) sender));
                sender.sendMessage(BLUE + "Transparent: " + sw.isTransparent());
                return;
            }
        }

        if (args.length == 0 || this.commands.get(args[0].toLowerCase()) == null) {
            sender.sendMessage(GOLD + "SkullWalls is developed by milkywayz");
            sender.sendMessage(GREEN + "===" + GOLD + " SkullWalls Help " + GREEN + "===");

            this.commands.values().stream().filter(cmd -> cmd.checkPermission(sender)).forEach(cmd -> cmd.showHelp(sender, label));
            return;
        }

        List arguments = new ArrayList(Arrays.asList(args));
        Command cmd = this.commands.get(((String) arguments.remove(0)).toLowerCase());
        if (arguments.size() < cmd.getRequiredArgs()) {
            cmd.showHelp(sender, label);
            return;
        }

        if (!cmd.checkPermission(sender)) {
            sender.sendMessage(RED + "You do not have permission to use that command!");
            return;
        }

        if (sender instanceof ConsoleCommandSender && cmd.isConsoleOnly()) {
            sender.sendMessage(ChatColor.RED + "Console cannot use this command");
            return;
        }

        cmd.runCommand(sender, arguments);
    }
}
