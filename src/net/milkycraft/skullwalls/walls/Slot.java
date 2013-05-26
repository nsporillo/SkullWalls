package net.milkycraft.skullwalls.walls;

import java.io.Serializable;

import net.milkycraft.skullwalls.SkullWalls;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;

public class Slot implements Serializable {

	private static final long serialVersionUID = 8074488926217790524L;
	private transient Block b;
	private transient Skull skull;
	private SkullWall parent;
	private SerialLocation loc;
	private final int id;
	private boolean disabled;
	private boolean visible;

	protected Slot(SkullWall parent, Block b, Integer id) {
		this.parent = parent;
		this.loc = new SerialLocation(b.getWorld(), b.getX(), b.getY(), b.getZ());
		this.b = b;
		this.id = id;
		this.disabled = false;
		this.visible = true;
		try {
			skull = (Skull) b.getState();
			this.initSlot();
		} catch (Exception ex) {
			this.disable();
		}
	}

	private void initSlot() {
		if (!isDisabled()) {
			skull.setSkullType(SkullType.PLAYER);
			skull.setOwner("");
			this.visible = true;
		}
	}

	public void validate() {
		Block b = this.getBlock();
		if (this.isDisabled()) {
			return;
		}
		if (this.isVisible()) {
			if (b.getType() != Material.SKULL) {
				this.disable();
			}
		} else {
			return;
		}
		if (skull == null) {
			skull = (Skull) getBlock().getState();
			skull.setSkullType(SkullType.PLAYER);
			skull.setOwner("");
			this.visible = true;
		}
	}

	public void appear() {
		if (isVisible() || isDisabled()) {
			return;
		}
		this.visible = true;
		this.getBlock().setType(Material.SKULL);
		try {
			skull = (Skull) getBlock().getState();
		} catch (Exception ex) {
			System.err.println("Error casting skull to slot! ("+ex.getMessage()+")");
			System.err.println("Slot: " + id + " of wall: " + parent.name + "BT: " + this.getBlock().getType().toString());
			return;
		}
		skull.setSkullType(SkullType.PLAYER);
		this.skull.setOwner("");
		this.skull.update();
	}

	public void disappear() {
		if (!this.isDisabled()) {
			visible = false;
			this.getBlock().setType(Material.AIR);
		}
	}

	public void setOwner(String owner) {
		if (!isDisabled() && isVisible()) {
			skull.setOwner(owner);
			skull.update();
		}
	}

	public String getOwner() {
		if (!isDisabled() && isVisible()) {
			return skull.getOwner();
		}
		return "";
	}

	public boolean hasOwner() {
		if (!isDisabled()) {
			if (!isVisible()) {
				return false;
			}
			if(skull == null) {
				skull = (Skull) getBlock().getState();
			}
			return skull.getOwner().equals("") ? false : true;
		}
		return false;
	}

	public void removeOwner() {
		if (!isDisabled() && isVisible()) {
			this.skull.setOwner("");
			this.skull.update();
			this.visible = false;
		}
	}

	public void setType(SkullType type) {
		if (!isDisabled() && isVisible()) {
			this.skull.setSkullType(type);
			this.skull.update();
		}
	}

	public Skull getSkull() {
		if (!isDisabled() && isVisible()) {
			if (skull == null) {
				if (this.getBlock().getType() == Material.SKULL) {
					skull = (Skull) this.getBlock().getState();
				}
			}
			return this.skull;
		}
		throw new RuntimeException("Slot not enabled!");
	}

	public static Slot getAt(Location l) {
		for (SkullWall w : SkullWalls.getWalls()) {
			for (Slot s : w.getSlots()) {
				if (s.getBlock().getLocation().equals(l)) {
					return s;
				}
			}
		}
		throw new SlotNotFoundException("Slot at " + l.getBlockX() + ", " + l.getBlockY() + ", "
				+ l.getBlockZ() + " not found!");
	}

	public boolean isVisible() {
		return this.visible;
	}

	public void disable() {
		this.disabled = true;
	}

	public boolean isDisabled() {
		return this.disabled;
	}

	public Block getBlock() {
		if (this.b == null) {
			this.b = Bukkit.getWorld(loc.world).getBlockAt(loc.x, loc.y, loc.z);
		}
		return this.b;
	}

	public int getId() {
		return this.id;
	}

	public SkullWall getParent() {
		return this.parent;
	}
}
