package net.porillo.skullwalls.walls;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;

import java.io.Serializable;

public class SkullSlot implements Serializable {
    private static final long serialVersionUID = 8074488926217790524L;
    private transient Block parentBlock;
    private transient Skull skull;
    private SkullWall parent;
    private SerialLocation loc;
    private boolean visible;

    protected SkullSlot(SkullWall parent, Block block) {
        this.parent = parent;
        this.loc = new SerialLocation(block);
        this.parentBlock = block;
        this.visible = true;

        try {
            this.skull = (Skull) block.getState();
        } catch (Exception ex) {
            ex.printStackTrace();
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
        return this.isVisible() && !this.getSkull().getOwner().equals("");
    }

    public Skull getSkull() {
        if (this.skull == null) {
            this.skull = (Skull) this.getBlock().getState();
        }

        return this.skull;
    }

    public boolean isSkull() {
        return this.getBlock().getType() == Material.SKULL;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public Block getBlock() {
        if (this.parentBlock == null) {
            this.parentBlock = Bukkit.getWorld(loc.getWorld()).getBlockAt(loc.getX(), loc.getY(), loc.getZ());
        }
        return this.parentBlock;
    }

    public SkullWall getParent() {
        return this.parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SkullSlot skullSlot = (SkullSlot) o;

        return loc.equals(skullSlot.loc);
    }

    @Override
    public int hashCode() {
        return loc.hashCode();
    }
}
