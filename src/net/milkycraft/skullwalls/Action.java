package net.milkycraft.skullwalls;

import org.bukkit.Bukkit;

public class Action {

	private final String name;
	private String cmd;
	private final String perm;
	private final int item;

	public Action(final String name, final String cmd, final String perm,
			final int item) {
		this.name = name;
		this.cmd = cmd;
		this.perm = perm;
		this.item = item;
	}

	public String execute(String name, String executor) {
		String command = cmd.replace("%p", name).replace("%e", executor);
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
		return command;
	}

	public String getCmd() {
		return this.cmd;
	}

	public int getItem() {
		return this.item;
	}

	public String getName() {
		return this.name;
	}

	public String getPerm() {
		return this.perm;
	}

	public void setCmd(final String cmd) {
		this.cmd = cmd;
	}

}
