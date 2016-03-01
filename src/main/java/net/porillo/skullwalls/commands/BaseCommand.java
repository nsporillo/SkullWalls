package net.porillo.skullwalls.commands;

import net.porillo.skullwalls.SkullWalls;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.ChatColor.*;

public abstract class BaseCommand implements Command {
    protected final SkullWalls plugin;
    protected String name;
    protected String permission;
    protected int required = 0;
    protected List<String> usages = new ArrayList<>();

    public BaseCommand(SkullWalls plugin) {
        this.plugin = plugin;
    }

    protected final void addUsage(String sub1, String sub2, String description) {
        StringBuilder usage = new StringBuilder().append(BLUE).append(String.format("%1$-8s", new Object[]{this.name}));

        if (sub1 != null) {
            usage.append(YELLOW);
            usage.append(String.format("%1$-8s", new Object[]{sub1}));
        } else {
            usage.append(String.format("%1$-8s", new Object[]{""}));
        }

        if (sub2 != null) {
            usage.append(AQUA);
            usage.append(String.format("%1$-8s", new Object[]{sub2}));
        } else {
            usage.append(String.format("%1$-8s", new Object[]{""}));
        }

        usage.append(GREEN);
        usage.append(description);
        this.usages.add(usage.toString());
    }

    public boolean checkPermission(CommandSender sender) {
        return this.permission == null || sender.hasPermission(this.permission);
    }

    public int getRequiredArgs() {
        return this.required;
    }

    protected void noPermission(CommandSender sender) {
        sender.sendMessage(RED + "You do not have permission to use that command!");
    }

    protected final void setName(String name) {
        this.name = name;
    }

    protected final void setPermission(String perm) {
        this.permission = perm;
    }

    protected final void setRequiredArgs(int req) {
        this.required = req;
    }

    public void showHelp(CommandSender sender, String label) {
        for (String usage : this.usages)
            sender.sendMessage(GRAY + String.format("%1$-10s", label) + usage);
    }
}
