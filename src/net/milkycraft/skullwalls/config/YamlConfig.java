package net.milkycraft.skullwalls.config;

import java.util.HashMap;
import java.util.Map;

import net.milkycraft.skullwalls.Action;
import net.milkycraft.skullwalls.SkullWalls;

import org.bukkit.configuration.ConfigurationSection;

public class YamlConfig extends YamlLoader {

	private boolean debug, report, protect, showvan, showafk, logt;
	private String click, name;
	private int tool, updateint;
	private Map<Integer, Action> actionz;

	public YamlConfig(final SkullWalls plugin, final String fileName) {
		super(plugin, fileName);
		super.saveIfNotExist();
		this.actionz = new HashMap<Integer, Action>();
		super.load();
	}

	public Map<Integer, Action> getActionMap() {
		return this.actionz;
	}

	@Override
	protected void loadKeys() {
		this.update();
		debug = config.getBoolean("General.Debug", false);
		name = config.getString("General.Default-Name", "Steve");
		click = config.getString("Messages.Click", "");
		tool = config.getInt("General.Cuboid-Tool", 269);
		protect = config.getBoolean("General.Protect-Walls", true);		
		report = config.getBoolean("General.Suppress-Full-Warnings", false);
		updateint = config.getInt("General.Update-Interval", 10);		
		showvan = config.getBoolean("General.Show-Vanished", false);		
		showafk = config.getBoolean("General.Show-AFK", true);
		logt = config.getBoolean("General.Log-Transfers", true);
		ConfigurationSection actions = super.getYaml().getConfigurationSection(
				"Actions");
		for (String action : actions.getKeys(false)) {
			ConfigurationSection cs = actions.getConfigurationSection(action);
			int tool = cs.getInt("Tool");
			String cmd = cs.getString("Command");
			String perm = cs.getString("Permission");
			this.actionz.put(tool, new Action(cs.getName(), cmd, perm, tool));
		}
	}

	public void reload() {
		super.rereadFromDisk();
		super.load();
	}
	
	public void update() {
		boolean updated = false;
		if(config.get("General.Show-Vanished") == null) {
			config.set("General.Show-Vanished", false);
			updated = true;
		}
		if(config.get("General.Update-Live") != null) {
			config.set("General.Update-Live", null);
			updated = true;
		}
		if(config.get("General.Show-AFK") == null) {
			config.set("General.Show-AFK", true);
			updated = true;
		}		
		if(config.get("General.Log-Transfers") == null) {
			config.set("General.Log-Transfers", true);
			updated = true;
		}
		if(updated) {
			plugin.log("Updated config for latest changes!");	
		}		
		super.saveConfig();
	}
	
	public boolean isDebugging() {
		return debug;
	}
	
	public boolean isFileLogging() {
		return logt;
	}
	
	public boolean isReporting() {
		return report;
	}

	public boolean isProtecting() {
		return protect;
	}

	public boolean isShowingVanished() {
		return showvan;
	}

	public boolean isShowingAFK() {
		return showafk;
	}

	public String getClickMessage() {
		return click;
	}

	public String getName() {
		return name;
	}

	public int getTool() {
		return tool;
	}

	public int getUpdateInterval() {
		return updateint;
	}

	public Map<Integer, Action> getActions() {
		return actionz;
	}
}
