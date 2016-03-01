package net.porillo.skullwalls.walls;

import java.io.Serializable;

import net.porillo.skullwalls.SkullWalls;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;

public class SkullSlot implements Serializable {
	private static final long	serialVersionUID	= 8074488926217790524L;
	private transient Block		b;
	private transient Skull		skull;
	private SkullWall			parent;
	private SerialLocation		loc;
	private boolean				visible;

	protected SkullSlot(SkullWall parent, Block b) {
		this.parent = parent;
		this.loc = new SerialLocation(b.getWorld(), b.getX(), b.getY(), b.getZ());
		this.b = b;
		this.visible = true;
		try {
			this.skull = (Skull) b.getState();
		} catch (Exception ex) {

		}
	}

	public void validate() {
		if (this.isSkull() && this.skull == null) {
			this.skull = (Skull) this.getBlock().getState();
			this.skull.setSkullType(SkullType.PLAYER);
			this.skull.setOwner("");
			this.skull.update();
		} else if (!this.isSkull() && this.isVisible()) {
			System.out.println("Slot isnt a skull but is visible!");
			this.visible = false;
			this.appear();
		}
		this.setOwner("");
	}

	public void appear() {
		if (this.isVisible()) {
			return;
		}
		this.visible = true;
		this.getBlock().setType(Material.SKULL);
		try {
			this.skull = (Skull) this.getBlock().getState();
		} catch (Exception ex) {
			System.err.println("Error casting skull to slot!");
			return;
		}
		this.skull.setSkullType(SkullType.PLAYER);
		this.skull.setOwner("");
		this.skull.update();
	}

	public void disappear() {
		this.visible = false;
		this.getBlock().setType(Material.AIR);
	}

	public void setOwner(String owner) {
		if (isSkull()) {
			Skull sk = this.getSkull();
			sk.setOwner(owner);
			sk.update();
		}
	}

	public String getOwner() {
		return this.skull.getOwner();
	}

	public boolean hasOwner() {
		if (!this.isVisible()) {
			return false;
		}
		return !this.getSkull().getOwner().equals("");
	}

	public Skull getSkull() {
		Block b = this.getBlock();
		if (this.skull == null) {
			this.skull = (Skull) b.getState();
		}
		return this.skull;
	}

	public static SkullSlot getAt(Location l) {
		for (SkullWall wall : SkullWalls.getWalls()) {
			for (SkullSlot s : wall.getSlots()) {
				if (s.getBlock().getLocation().equals(l)) {
					return s;
				}
			}
		}
		return null;
	}

	public boolean isSkull() {
		return this.getBlock().getType() == Material.SKULL;
	}

	public boolean isVisible() {
		return this.visible;
	}

	public Block getBlock() {
		if (this.b == null) {
			this.b = Bukkit.getWorld(this.loc.world).getBlockAt(this.loc.x, this.loc.y, this.loc.z);
		}
		return this.b;
	}

	public SkullWall getParent() {
		return this.parent;
	}
}
