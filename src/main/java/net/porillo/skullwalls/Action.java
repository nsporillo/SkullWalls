package net.porillo.skullwalls;

import org.bukkit.Bukkit;

public class Action {
	private final String	name;
	private String			cmd;
	private final String	perm;
	private final int		item;

	public Action(String name, String cmd, String perm, int item) {
		this.name = name;
		this.cmd = cmd;
		this.perm = perm;
		this.item = item;
	}

	public String execute(String name, String executor) {
		String command = this.cmd.replace("%p", name).replace("%e", executor);
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

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
}
