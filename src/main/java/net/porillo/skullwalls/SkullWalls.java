package net.porillo.skullwalls;

import lombok.Getter;
import net.porillo.skullwalls.commands.CommandHandler;
import net.porillo.skullwalls.config.YamlConfig;
import net.porillo.skullwalls.walls.SkullWall;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.stream.Collectors;

public class SkullWalls extends JavaPlugin {

    @Getter private static WallHandler wallHandler;
    @Getter private CommandHandler commandHandler;
    @Getter private ActionWorker actionWorker;
    @Getter private YamlConfig configuration;
    @Getter private Cuboider cuboider;

    @Override
    public void onDisable() {
        Serializer.save(wallHandler.getWalls());
        wallHandler = null;
    }

    @Override
    public void onEnable() {
        wallHandler = new WallHandler();
        this.commandHandler = new CommandHandler(this);
        this.cuboider = new Cuboider();
        this.actionWorker = new ActionWorker(this);
        this.configuration = new YamlConfig(this, "config.yml");
        Bukkit.getPluginManager().registerEvents(new ActionListener(this), this);
        autoUpdate(this.configuration.getUpdateInterval());
        wallHandler.setWalls(Serializer.load());
        updateAllWalls();
    }

    public void log(String info) {
        getLogger().info(info);
    }

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        this.commandHandler.runCommand(s, l, a);
        return true;
    }

    public void reload() {
        Serializer.save(wallHandler.getWalls());
        wallHandler.setWalls(Serializer.load());
        updateAllWalls();
    }

    public void updateAllWalls() {
        for (SkullWall w : wallHandler.getWalls()) {
            w.updateWall(getOnlinePlayers());
        }
    }

    public static List<String> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    public void autoUpdate(int interval) {
        log("All walls will update every " + interval + " seconds");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::updateAllWalls, 20L, interval * 20);
    }
}
