package net.porillo.skullwalls.walls;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.porillo.skullwalls.SkullWalls;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class SkullWall implements Serializable {
	private static final long		serialVersionUID	= 8086068912212564853L;
	private transient List<String>	knownBanned			= new ArrayList<String>();
	private transient List<String>	cachedPlayers		= new ArrayList<String>();
	protected transient World		worldObj;
	protected String				world;
	protected WallType				type;
	protected Set<String>			list;
	protected List<SkullSlot>		slots;
	protected String				name;
	protected int[]					bounds;
	protected boolean				transparent;

	public SkullWall(WallType type, String name, World world, int[] bounds) {
		this.transparent = false;
		this.name = name;
		this.type = type;
		this.worldObj = world;
		this.world = this.worldObj.getName();
		this.bounds = bounds;
		this.list = new HashSet<String>();
		Utils.gen(this, bounds);
		if (type == WallType.BANNED) {
			this.banUpdate();
		} else {
			this.updateWall(cachedPlayers);
		}
	}

	/*
	 * Find the next available slot
	 */
	public SkullSlot getNextAvailable() {
		for (SkullSlot slot : this.slots) {
			if (!slot.hasOwner()) {
				if (!slot.isVisible()) {
					slot.appear();
				}
				return slot;
			}
		}
		return null;
	}

	private List<String> a(List<String> players) {
		List<String> toUse = null;
		if (players == null) {
			toUse = this.cachedPlayers;
		} else {
			toUse = players;
		}
		this.cachedPlayers = toUse;
		return toUse;
	}

	public void updateWall(List<String> players) {
		if (this.slots == null) {
			Utils.gen(this, this.bounds);
		}
		for (SkullSlot slot : this.slots) {
			slot.validate();
		}
		for (String player : a(players)) {
			if (!SkullWalls.isVanished(player) && !SkullWalls.isAFK(player)) {
				SkullSlot s = this.getNextAvailable();
				if (s == null)
					continue;
				if (this.type == WallType.GLOBAL) {
					s.setOwner(player);
					continue;
				}
				Player p = Bukkit.getPlayerExact(player);
				if (this.type == WallType.CUSTOM) {
					if (p.hasPermission("skullwalls.wall." + this.name)
							|| this.list.contains(p.getName()))
						s.setOwner(player);
				} else if (this.type == WallType.BANNED) {
					if (this.knownBanned == null) {
						this.knownBanned = new ArrayList<String>();
					}
					if (this.knownBanned.size() < Bukkit.getBannedPlayers().size())
						this.banUpdate();
				}
			}
		}
		this.verifyState();
	}

	private void banUpdate() {
		int max = this.slots.size();
		int tot = 0;
		for (OfflinePlayer op : Bukkit.getBannedPlayers())
			if (tot < max - 1) {
				this.getNextAvailable().setOwner(op.getName());
				this.knownBanned.add(op.getName());
				tot++;
			} else {
				System.err.println("Wall has " + max
						+ " max slots, theres too many banned players! ("
						+ Bukkit.getBannedPlayers().size() + ")");
				break;
			}
	}

	public void setTransparent(boolean value) {
		this.transparent = value;
		if (value) {
			for (SkullSlot s : this.slots) {
				if (!s.hasOwner())
					s.disappear();
			}
		} else
			for (SkullSlot s : this.slots)
				if (!s.hasOwner())
					s.appear();
	}

	public void offSet(int num) {
		if (this.slots.size() < num) {
			return;
		}
		// this.slots = Utils.offset(this.slots, num);
		if (this.type == WallType.BANNED) {
			this.banUpdate();
		}
		this.updateWall(null);
	}

	public void verifyState() {
		for (SkullSlot s : this.slots) {
			if (this.transparent) {
				if (!s.hasOwner()) {
					s.disappear();
				}
			} else if (!s.hasOwner()) {
				s.appear();
			}
		}
	}

	public World getWorld() {
		if (this.worldObj == null) {
			this.worldObj = Bukkit.getWorld(this.world);
			this.banUpdate();
		}
		return this.worldObj;
	}

	public Block getBlock(int i) {
		if (i == 1)
			return this.getWorld().getBlockAt(this.bounds[0], this.bounds[1], this.bounds[2]);
		if (i == 2) {
			return this.getWorld().getBlockAt(this.bounds[3], this.bounds[4], this.bounds[5]);
		}
		return null;
	}

	public WallType getType() {
		return this.type;
	}

	public List<SkullSlot> getSlots() {
		return this.slots;
	}

	public int[] getBounds() {
		return this.bounds;
	}

	public Set<String> getWhitelist() {
		return this.list;
	}

	public boolean isTransparent() {
		return this.transparent;
	}

	public String getName() {
		return this.name;
	}

	public String getStringBounds() {
		return Utils.bind(this.bounds);
	}
}
