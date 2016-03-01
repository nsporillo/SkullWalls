package net.porillo.skullwalls.commands;

import net.porillo.skullwalls.Action;
import net.porillo.skullwalls.SkullWalls;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map.Entry;

import static org.bukkit.ChatColor.*;

public class ActionCommand extends BaseCommand {

    public ActionCommand(final SkullWalls plugin) {
        super(plugin);
        super.setName("action");
        super.addUsage("[help]", null, "List the action commands");
        super.setPermission("skullwalls.action");
    }

    @Override
    public void runCommand(final CommandSender sender, final List<String> args) {
        if (args.size() == 0) {
            this.displayHelp(sender);
        } else if (args.size() == 1) {
            String one = args.get(0);
            if (one.equalsIgnoreCase("help")) {
                this.displayHelp(sender);
            } else if (one.equalsIgnoreCase("list")) {
                for (Entry<Integer, Action> a : plugin.getConfiguration().getActionMap().entrySet()) {
                    this.displayAction(sender, a.getValue(), a.getKey());
                }
            } else if (one.equalsIgnoreCase("tool")) {
                sender.sendMessage(RED + "Usage: /skull action tool <name>");
            }
        } else if (args.size() == 2) {
            String one = args.get(0);
            String two = args.get(1);
            if (one.equalsIgnoreCase("tool")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("You must be a player to use that command");
                    return;
                }
                Player p = (Player) sender;
                for (Action a : plugin.getConfiguration().getActionMap().values()) {
                    if (a.getName().equalsIgnoreCase(two)) {
                        ItemStack is = new ItemStack(a.getItem(), 1);
                        p.getInventory().addItem(is);
                        p.sendMessage(GOLD + "The action '" + a.getName()
                                + "' wand was added to your inventory");
                        break;
                    }
                }
            }
        }
    }

    public void displayHelp(CommandSender sender) {
        sender.sendMessage(BLUE + "=== " + GOLD + "Action Commands" + BLUE + " ===");
        sender.sendMessage(GREEN + "/skull action list - List all actions");
        sender.sendMessage(GREEN + "/skull action tool <name> - Gives you the tool for the action");
    }

    public void displayAction(CommandSender sender, Action a, int id) {
        sender.sendMessage(BLUE + "Action [Name: " + a.getName() + ", Id: " + id + ", Cmd: " + a.getCmd() + "]");
    }
}
