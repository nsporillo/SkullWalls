package net.milkycraft.skullwalls.config;

import java.io.File;
import java.io.IOException;

import net.milkycraft.skullwalls.SkullWalls;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class YamlLoader {

	protected String fileName;
	protected File configFile;
	protected File dataFolder;
	protected SkullWalls plugin;
	protected FileConfiguration config;

	public YamlLoader(final SkullWalls plugin, final String fileName) {
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

	/**
	 * Load the keys from this config file.
	 */
	protected abstract void loadKeys();


	protected void rereadFromDisk() {
		this.config = YamlConfiguration.loadConfiguration(this.configFile);
	}

	protected void saveConfig() {
		try {
			this.config.save(this.configFile);
		} catch (final IOException ex) {
			//
		}
	}

	protected void saveIfNotExist() {
		if (!this.configFile.exists()) {
			if (this.plugin.getResource(this.fileName) != null) {
				this.plugin.saveResource(this.fileName, false);
			}
		}
		this.rereadFromDisk();
	}
}