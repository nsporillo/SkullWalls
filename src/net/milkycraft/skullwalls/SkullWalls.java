package net.milkycraft.skullwalls;

import static org.bukkit.Bukkit.getPluginManager;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import net.milkycraft.skullwalls.commands.CommandHandler;
import net.milkycraft.skullwalls.config.YamlConfig;
import net.milkycraft.skullwalls.walls.SkullWall;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitteh.vanish.staticaccess.VanishNoPacket;

import com.earth2me.essentials.Essentials;

public class SkullWalls extends JavaPlugin {

	private static List<SkullWall> walls = new ArrayList<SkullWall>();
	private static Essentials ess;
	private static boolean van;
	private static boolean afk;
	private List<String> trans = new ArrayList<String>();
	private List<String> alerts = new ArrayList<String>();
	private CommandHandler commands;
	private ActionWorker aworker;
	private YamlConfig config;
	private Cuboider sesHandler;

	@Override
	public void onDisable() {
		Serializer.save(walls);
		this.destruct();
		ess = null;
	}

	@Override
	public void onEnable() {
		this.commands = new CommandHandler(this);
		this.sesHandler = new Cuboider();
		this.aworker = new ActionWorker(this);
		this.config = new YamlConfig(this, "config.yml");
		this.loadModules();

		getPluginManager().registerEvents(new ActionListener(this), this);
		autoUpdate(config.getUpdateInterval());
		SkullWalls.walls = Serializer.load();
		this.struct();
	}

	public void debug(String debug) {
		if (this.config.isDebugging()) {
			this.log("Debug: " + debug);
		}
	}

	public void logTrans(String msg) {
		if (config.isFileLogging()) {
			this.log("[Trans] " + msg);
		}
	}

	public static List<SkullWall> getWalls() {
		return SkullWalls.walls;
	}

	public void destruct() {
		for (SkullWall w : SkullWalls.getWalls()) {
			w.wipe();
		}
		SkullWalls.walls = null;
		trans.clear();
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

	public List<String> getTrans() {
		return this.trans;
	}

	public List<String> getAlerts() {
		return this.alerts;
	}

	public void log(String info) {
		this.getLogger().info(info);
	}

	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		this.commands.runCommand(s, l, a);
		return true;
	}

	public void reload() {
		Serializer.save(walls);
		this.destruct();
		SkullWalls.walls = Serializer.load();
		this.struct();
	}

	public void struct() {
		trans.clear();
		for (SkullWall w : SkullWalls.getWalls()) {
			w.recalculate();
		}
	}

	public void autoUpdate(int interval) {
		this.log("All walls will update every " + interval + " seconds");
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				for (SkullWall w : SkullWalls.getWalls()) {
					w.recalculate();
				}
			}
		}, 20L, interval * 20);
	}

	private void loadModules() {
		if (getPluginManager().getPlugin("VanishNoPacket") != null) {
			van = true;
			if (config.isShowingVanished()) {
				log("VanishNoPacket found, but show vanished is enabled");
				van = false;
			}
			if (van) {
				log("VanishNoPacket found and vanished players arent shown on walls");
			}
		} else {
			log("VanishNoPacket not found, vanish support disabled");
			van = false;
		}
		Plugin p = getPluginManager().getPlugin("Essentials");
		if (p != null) {
			afk = true;
			if (config.isShowingAFK()) {
				log("Essentials found, but ShowAFK is disabled");
				afk = false;
			}
			if (afk) {
				ess = (Essentials) p;
				log("Essentials found and AFK players arent shown on walls");
			}
		} else {
			log("Essentials not found, AFK support disabled");
			afk = false;
		}
	}

	public static boolean isVanished(String name) {
		if (!van) {
			return van;
		}
		try {
			return VanishNoPacket.isVanished(name);
		} catch (Exception e) {
			System.err.println("Error checking if " + name
					+ " is vanished - ( Did you /reload? :/ )");
			return false;
		}
	}

	public static boolean isAFK(String name) {
		if (!afk) {
			return afk;
		}
		return ess.getUserMap().getUser(name).isAfk();
	}
}
