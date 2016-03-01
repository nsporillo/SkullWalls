package net.porillo.skullwalls;

import net.porillo.skullwalls.commands.CommandHandler;
import net.porillo.skullwalls.config.YamlConfig;
import net.porillo.skullwalls.walls.SkullWall;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SkullWalls extends JavaPlugin {
    private static Set<SkullWall> walls = new HashSet<>();
    private CommandHandler commands;
    private ActionWorker aworker;
    private YamlConfig config;
    private Cuboider sesHandler;

    @Override
    public void onDisable() {
        Serializer.save(walls);
        walls = null;
    }

    @Override
    public void onEnable() {
        this.commands = new CommandHandler(this);
        this.sesHandler = new Cuboider();
        this.aworker = new ActionWorker(this);
        this.config = new YamlConfig(this, "config.yml");
        Bukkit.getPluginManager().registerEvents(new ActionListener(this), this);
        autoUpdate(this.config.getUpdateInterval());
        walls = Serializer.load();
        struct();
    }

    public static Set<SkullWall> getWalls() {
        return walls;
    }

    public ActionWorker getActionWorker() {
        return this.aworker;
    }

    public YamlConfig getConfiguration() {
        return this.config;
    }

    public Cuboider getCuboider() {
        return this.sesHandler;
    }

    public void log(String info) {
        getLogger().info(info);
    }

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        this.commands.runCommand(s, l, a);
        return true;
    }

    public void reload() {
        Serializer.save(walls);
        walls.clear();
        walls = Serializer.load();
        struct();
    }

    public void struct() {
        for (SkullWall w : getWalls()) {
            w.updateWall(getOnlinePlayers());
        }
    }

    public static List<String> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    public void autoUpdate(int interval) {
        log("All walls will update every " + interval + " seconds");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (SkullWall w : SkullWalls.getWalls())
                w.updateWall(SkullWalls.getOnlinePlayers());
        }, 20L, interval * 20);
    }
}
