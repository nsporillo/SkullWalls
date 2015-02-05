package net.porillo.skullwalls.commands;

import java.util.ArrayList;
import java.util.List;

import net.porillo.skullwalls.SkullWalls;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public abstract class BaseCommand implements Command {
	protected SkullWalls	plugin;
	protected String		name;
	protected String		permission;
	protected int			required	= 0;
	protected List<String>	usages		= new ArrayList<String>();

	public BaseCommand(SkullWalls plugin) {
		this.plugin = plugin;
	}

	protected final void addUsage(String sub1, String sub2, String description) {
		StringBuilder usage = new StringBuilder().append(ChatColor.BLUE).append(
				String.format("%1$-8s", new Object[] { this.name }));
		if (sub1 != null) {
			usage.append(ChatColor.YELLOW);
			usage.append(String.format("%1$-8s", new Object[] { sub1 }));
		} else {
			usage.append(String.format("%1$-8s", new Object[] { "" }));
		}
		if (sub2 != null) {
			usage.append(ChatColor.AQUA);
			usage.append(String.format("%1$-8s", new Object[] { sub2 }));
		} else {
			usage.append(String.format("%1$-8s", new Object[] { "" }));
		}
		usage.append(ChatColor.GREEN);
		usage.append(description);
		this.usages.add(usage.toString());
	}

	public boolean checkPermission(CommandSender sender) {
		if (this.permission == null) {
			return true;
		}
		return sender.hasPermission(this.permission);
	}

	public int getRequiredArgs() {
		return this.required;
	}

	protected void noPermission(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
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
			sender.sendMessage(ChatColor.GRAY + String.format("%1$-10s", new Object[] { label })
					+ usage);
	}
}
