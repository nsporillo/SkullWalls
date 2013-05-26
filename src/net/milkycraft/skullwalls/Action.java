package net.milkycraft.skullwalls;

import org.bukkit.Bukkit;

public class Action {

	private String name;
	private String cmd;
	private String perm;
	private int item;

	public Action(String name, String cmd, String perm, int item) {
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
