package net.porillo.skullwalls;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.Setter;
import net.porillo.skullwalls.collections.ConcurrentHashSet;
import net.porillo.skullwalls.walls.SkullSlot;
import net.porillo.skullwalls.walls.SkullWall;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Set;

public class WallHandler {

    @Getter @Setter private Set<SkullWall> walls = new ConcurrentHashSet<>();

    public void remove(SkullWall wall) {
        walls.remove(wall);
    }

    public void add(SkullWall wall) {
        walls.add(wall);
    }

    public boolean hasPermission(Block block, Player player, String message) {
        for (SkullWall wall : walls) {
            for (SkullSlot s : wall.getSlots()) {
                if (s.getBlock().equals(block)) {
                    player.sendMessage(message);
                    return true;
                }
            }
        }
        return false;
    }

    public SkullWall getWallFromBlock(Block b) {
        for (SkullWall wall : walls) {
            for (SkullSlot s : wall.getSlots()) {
                if (s.getBlock().getLocation().equals(b.getLocation())) {
                    return wall;
                }
            }
        }
        return null;
    }

    public Set<SkullWall> getReadOnlyWalls() {
        return ImmutableSet.copyOf(walls);
    }
}
