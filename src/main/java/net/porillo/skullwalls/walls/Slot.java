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
    }

    public void validate() {
        if (isSkull() && skull == null) {
            getSkull().setSkullType(SkullType.PLAYER);
        } else if (!isSkull() && isVisible()) {
            visible = false;
            appear();
        }
    }

    public void appear() {
        if (isVisible()) {
            return;
        }

        visible = true;
        getBlock().setType(Material.SKULL);

        getSkull().setSkullType(SkullType.PLAYER);
        setOwner("Steve");
    }

    public void disappear() {
        visible = false;
        getBlock().setType(Material.AIR);
    }

    public void setOwner(String owner) {
        Skull sk = this.getSkull();
        if(sk != null) {
            sk.setOwner(owner);
            sk.update(true, true);
        }
    }

    public boolean hasOwner() {
        return isVisible() && getSkull().hasOwner() && !skull.getOwner().equals("Steve");
    }

    public Skull getSkull() {
        if (skull == null && isSkull()) {
            skull = (Skull) getBlock().getState();
        }

        return skull;
    }

    public boolean isSkull() {
        return getBlock().getType() == Material.SKULL;
    }

    public Block getBlock() {
        if (parentBlock == null) {
            parentBlock = Bukkit.getWorld(location.getWorld()).getBlockAt(location.getX(), location.getY(), location.getZ());
        }
        return parentBlock;
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
