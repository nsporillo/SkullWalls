package net.porillo.skullwalls.config;

import lombok.Getter;
import net.porillo.skullwalls.Action;
import net.porillo.skullwalls.SkullWalls;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class YamlConfig extends YamlLoader {

    @Getter private Map<Integer, Action> actionz;
    @Getter private boolean protecting;
    @Getter private String clickMessage;
    @Getter private String name;
    @Getter private int tool;
    @Getter private int updateInterval;

    public YamlConfig(SkullWalls plugin, String fileName) {
        super(plugin, fileName);
        super.saveIfNotExist();
        this.actionz = new HashMap<>();
        super.load();
    }

    public Map<Integer, Action> getActionMap() {
        return this.actionz;
    }

    @Override
    protected void loadKeys() {
        this.update();
        this.name = this.config.getString("General.Default-Name", "Steve");
        this.clickMessage = this.config.getString("Messages.Click", "");
        this.tool = this.config.getInt("General.Cuboid-Tool", 269);
        this.protecting = this.config.getBoolean("General.Protect-Walls", true);
        this.updateInterval = this.config.getInt("General.Update-Interval", 10);

        ConfigurationSection actions = super.getYaml().getConfigurationSection("Actions");

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

        if (this.config.get("General.Show-Vanished") == null) {
            this.config.set("General.Show-Vanished", false);
            updated = true;
        }

        if (this.config.get("General.Show-AFK") == null) {
            this.config.set("General.Show-AFK", true);
            updated = true;
        }

        if (updated) {
            this.plugin.log("Updated config for latest changes!");
            super.saveConfig();
        }
    }
}
