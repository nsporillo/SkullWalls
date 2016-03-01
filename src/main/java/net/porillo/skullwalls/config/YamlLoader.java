package net.porillo.skullwalls.config;

import java.io.File;
import java.io.IOException;

import net.porillo.skullwalls.SkullWalls;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class YamlLoader {
	protected String			fileName;
	protected File				configFile;
	protected File				dataFolder;
	protected SkullWalls		plugin;
	protected FileConfiguration	config;

	public YamlLoader(SkullWalls plugin, String fileName) {
		this.plugin = plugin;
		this.fileName = fileName;
		this.dataFolder = plugin.getDataFolder();
		this.configFile = new File(this.dataFolder, File.separator + fileName);
		this.config = YamlConfiguration.loadConfiguration(this.configFile);
	}

	protected void addDefaults() {
		this.config.options().copyDefaults(true);
		this.saveConfig();
	}

	protected FileConfiguration getYaml() {
		return this.config;
	}

	public void load() {
		if (!this.configFile.exists()) {
			this.dataFolder.mkdir();
			this.saveConfig();
		}
		this.addDefaults();
		this.loadKeys();
		this.saveIfNotExist();
	}

	protected abstract void loadKeys();

	protected void rereadFromDisk() {
		this.config = YamlConfiguration.loadConfiguration(this.configFile);
	}

	protected void saveConfig() {
		try {
			this.config.save(this.configFile);
		} catch (IOException localIOException) {
		}
	}

	protected void saveIfNotExist() {
		if (!this.configFile.exists() && this.plugin.getResource(this.fileName) != null) {
			this.plugin.saveResource(this.fileName, false);
		}
		this.rereadFromDisk();
	}
}
