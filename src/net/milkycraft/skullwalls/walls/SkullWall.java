package net.milkycraft.skullwalls.walls;

import static net.milkycraft.skullwalls.walls.Utils.bind;
import static net.milkycraft.skullwalls.walls.Utils.gen;
import static net.milkycraft.skullwalls.walls.Utils.getNextAvailable;
import static net.milkycraft.skullwalls.walls.Utils.isInRadius;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.milkycraft.skullwalls.SkullWalls;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class SkullWall implements Serializable {

	private static final long serialVersionUID = 8086068912212564853L;
	private transient List<String> knownBanned = new ArrayList<String>();
	protected transient World worldObj;
	protected WallType type;
	protected Set<String> list;
	protected List<Slot> slots;
	protected String name;
	protected String world;
	protected int[] bounds;
	protected boolean transparent;
	protected int radius;
	protected String typeworld;

	public SkullWall(WallType type, String name, World world, int[] bounds) {
		this.transparent = false;
		this.name = name;
		this.type = type;
		this.worldObj = world;
		this.world = worldObj.getName();
		this.typeworld = this.world;
		this.bounds = bounds;
		this.list = new HashSet<String>();
		this.radius = 15;
		gen(this, bounds);
		if (type == WallType.BANNED) {
			this.updateBans();
		} else {
			this.init();
		}
	}

	private void init() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (SkullWalls.isVanished(p.getName()) || SkullWalls.isAFK(p.getName())) {
				continue;
			}
			Slot s = getNextAvailable(this);
			if (type == WallType.GLOBAL) {
				s.setOwner(p.getName());
			} else if (type == WallType.CUSTOM) {
				if (p.hasPermission("skullwalls.wall." + name) || list.contains(p.getName())) {
					s.setOwner(p.getName());
				}
			} else if (type == WallType.RADIUS) {
				if (isInRadius(p, this, radius)) {
					s.setOwner(p.getName());
				}
			} else if (type == WallType.WORLD) {
				if (p.getWorld().getName().equals(this.typeworld)) {
					s.setOwner(p.getName());
				}
			}
		}
	}

	private void updateBans() {
		Set<OfflinePlayer> banned = Bukkit.getBannedPlayers();
		if(knownBanned == null) {
			knownBanned = new ArrayList<String>();
		}
		if(knownBanned.size() < banned.size()) {
			a(banned);
		} else if(knownBanned.size() > banned.size()) {			
			a(banned);
		} else {
			return;
		}
		
	}
	
	private void a(Set<OfflinePlayer> banned) {
		this.wipe();
		int max = this.slots.size();
		int tot = 0;
		this.knownBanned.clear();
		for (OfflinePlayer op : banned) {
			if (tot < (max - 1)) {
				getNextAvailable(this).setOwner(op.getName());
				knownBanned.add(op.getName());
				tot++;
			} else {
				break;
			}
		}
	}

	public void setTransparent(boolean value) {
		this.transparent = value;
		if (value) {
			for (Slot s : slots) {
				if (!s.hasOwner()) {
					s.disappear();
				}
			}
		} else {
			for (Slot s : slots) {
				if (!s.hasOwner()) {
					s.appear();
				}
			}
		}
	}

	public void recalculate() {
		if (slots == null) {
			gen(this, bounds);
		}
		if (type == WallType.ONLINE) {
			this.type = WallType.GLOBAL;
		}
		if (type != WallType.BANNED) {
			this.wipe();
		}
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (SkullWalls.isVanished(p.getName()) || SkullWalls.isAFK(p.getName())) {
				continue;
			}
			Slot s = getNextAvailable(this);
			if (type == WallType.GLOBAL) {
				s.setOwner(p.getName());
			} else if (type == WallType.CUSTOM) {
				if (p.hasPermission("skullwalls.wall." + name) || list.contains(p.getName())) {
					s.setOwner(p.getName());
				}
			} else if (type == WallType.RADIUS) {
				if (isInRadius(p, this, radius)) {
					s.setOwner(p.getName());
				}
			} else if (type == WallType.WORLD) {
				if (p.getWorld().getName().equals(this.typeworld)) {
					s.setOwner(p.getName());
				}
			} else if (type == WallType.BANNED) {
				this.updateBans();
			}
		}
		this.verifyState();
	}

	public void offSet(int num) {
		if (slots.size() < num) {
			return;
		}
		this.wipe();
		this.slots = Utils.offset(slots, num);
		if (type == WallType.BANNED) {
			this.updateBans();
		}
		this.recalculate();
	}

	public void wipe() {
		for (Slot b : slots) {
			b.validate();
			b.removeOwner();
		}
	}

	public void verifyState() {
		for (Slot s : slots) {
			if (this.transparent) {
				if (!s.hasOwner()) {
					s.disappear();
				}
			} else {
				if (!s.hasOwner()) {
					s.appear();
				}
			}
		}
	}

	public World getWorld() {
		if (worldObj == null) {
			worldObj = Bukkit.getWorld(world);
			this.updateBans();
		}
		return worldObj;
	}

	public Block getBlock(int i) {
		if (i == 1) {
			return getWorld().getBlockAt(bounds[0], bounds[1], bounds[2]);
		} else if (i == 2) {
			return getWorld().getBlockAt(bounds[3], bounds[4], bounds[5]);
		}
		return null;
	}

	public int getRadius() {
		return this.radius;
	}

	public void setRadius(int rad) {
		this.radius = rad;
		this.recalculate();
	}

	public void setWallWorld(String name) {
		this.world = name;
		worldObj = Bukkit.getWorld(world);
		this.recalculate();
	}

	public WallType getType() {
		return this.type;
	}

	public List<Slot> getSlots() {
		return this.slots;
	}

	public int[] getBounds() {
		return this.bounds;
	}

	public Set<String> getWhitelist() {
		return this.list;
	}

	public boolean isTransparent() {
		return transparent;
	}

	public String getName() {
		return this.name;
	}

	public String getStringBounds() {
		return bind(bounds);
	}
}