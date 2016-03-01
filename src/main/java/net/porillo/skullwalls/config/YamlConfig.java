package net.porillo.skullwalls.config;

import net.porillo.skullwalls.Action;
import net.porillo.skullwalls.SkullWalls;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class YamlConfig extends YamlLoader {

    private Map<Integer, Action> actionz;
    private boolean protect;
    private boolean showvan;
    private boolean showafk;
    private String click;
    private String name;
    private int tool;
    private int updateint;

    public YamlConfig(SkullWalls plugin, String fileName) {
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
        this.name = this.config.getString("General.Default-Name", "Steve");
        this.click = this.config.getString("Messages.Click", "");
        this.tool = this.config.getInt("General.Cuboid-Tool", 269);
        this.protect = this.config.getBoolean("General.Protect-Walls", true);
        this.updateint = this.config.getInt("General.Update-Interval", 10);
        this.showvan = this.config.getBoolean("General.Show-Vanished", false);
        this.showafk = this.config.getBoolean("General.Show-AFK", true);
        ConfigurationSection actions = super.getYaml().getConfigurationSection("Actions");
        for (String action : actions.getKeys(false)) {
            ConfigurationSection cs = actions.getConfigurationSection(action);
            int tool = cs.getInt("Tool");
            String cmd = cs.getString("Command");
            String perm = cs.getString("Permission");
            this.actionz.put(Integer.valueOf(tool), new Action(cs.getName(), cmd, perm, tool));
        }
    }

    public void reload() {
        super.rereadFromDisk();
        super.load();
    }

    public void update() {
        boolean updated = false;
        if (this.config.get("General.Show-Vanished") == null) {
            this.config.set("General.Show-Vanished", Boolean.valueOf(false));
            updated = true;
        }
        if (this.config.get("General.Show-AFK") == null) {
            this.config.set("General.Show-AFK", Boolean.valueOf(true));
            updated = true;
        }
        if (updated) {
            this.plugin.log("Updated config for latest changes!");
            super.saveConfig();
        }
    }

    public boolean isProtecting() {
        return this.protect;
    }

    public boolean isShowingVanished() {
        return this.showvan;
    }

    public boolean isShowingAFK() {
        return this.showafk;
    }

    public String getClickMessage() {
        return this.click;
    }

    public String getName() {
        return this.name;
    }

    public int getTool() {
        return this.tool;
    }

    public int getUpdateInterval() {
        return this.updateint;
    }

    public Map<Integer, Action> getActions() {
        return this.actionz;
    }
}
