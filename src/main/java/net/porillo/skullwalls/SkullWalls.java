package net.porillo.skullwalls;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.porillo.skullwalls.actions.ActionListener;
import net.porillo.skullwalls.actions.ActionWorker;
import net.porillo.skullwalls.commands.CommandHandler;
import net.porillo.skullwalls.config.YamlConfig;
import net.porillo.skullwalls.cuboid.CuboidHandler;
import net.porillo.skullwalls.walls.Wall;
import net.porillo.skullwalls.walls.WallHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.stream.Collectors;

public class SkullWalls extends JavaPlugin {

    @Getter private static Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    @Getter private static CuboidHandler cuboidHandler;
    @Getter private static WallHandler wallHandler;
    @Getter private CommandHandler commandHandler;
    @Getter private ActionWorker actionWorker;
    @Getter private YamlConfig configuration;

    @Override
    public void onDisable() {
        wallHandler.saveWalls();
        wallHandler = null;
    }

    @Override
    public void onEnable() {
        wallHandler = new WallHandler();
        cuboidHandler = new CuboidHandler();
        this.commandHandler = new CommandHandler(this);
        this.actionWorker = new ActionWorker(this);
        this.configuration = new YamlConfig(this, "config.yml");
        Bukkit.getPluginManager().registerEvents(new ActionListener(this), this);
        autoUpdate(this.configuration.getUpdateInterval());
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
        wallHandler.saveWalls();
        wallHandler.loadWalls();
        updateAllWalls();
    }

    public void updateAllWalls() {
        for (Wall w : wallHandler.getWalls()) {
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
