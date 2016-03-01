package net.porillo.skullwalls.walls;

import lombok.Getter;
import net.porillo.skullwalls.SerialLocation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;

public class Slot {

    @Getter private SerialLocation location;
    @Getter private boolean visible;
    private transient Block parentBlock;
    private transient Skull skull;

    public Slot(Block block) {
        this.location = new SerialLocation(block);
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

    public Block getBlock() {
        if (this.parentBlock == null) {
            this.parentBlock = Bukkit.getWorld(location.getWorld()).getBlockAt(location.getX(), location.getY(), location.getZ());
        }
        return this.parentBlock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Slot skullSlot = (Slot) o;

        return location.equals(skullSlot.location);
    }

    @Override
    public int hashCode() {
        return location.hashCode();
    }
}
