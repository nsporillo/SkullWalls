package net.porillo.skullwalls;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.Setter;
import net.porillo.skullwalls.collections.ConcurrentHashSet;
import net.porillo.skullwalls.walls.SkullSlot;
import net.porillo.skullwalls.walls.SkullWall;
import org.apache.commons.io.FileUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class WallHandler {

    @Getter @Setter private WallStore wallStore;
    private final File wallsFile = new File(new File("plugins" + File.separator + "SkullWalls"), "walls.json");

    public WallHandler() {
        loadWalls();
    }

    public void loadWalls() {
        try {
            if (!wallsFile.exists()) {
                FileUtils.write(wallsFile, SkullWalls.getGson().toJson(new WallStore()));
            }

            wallStore = SkullWalls.getGson().fromJson(FileUtils.readFileToString(wallsFile), WallStore.class);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public void saveWalls() {
        try {
            FileUtils.write(wallsFile, SkullWalls.getGson().toJson(wallsFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void remove(SkullWall wall) {
        getWalls().remove(wall);

        saveWalls();
    }

    public void add(SkullWall wall) {
        getWalls().add(wall);

        saveWalls();
    }

    public boolean hasPermission(Block block, Player player, String message) {
        for (SkullWall wall : getWalls()) {
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
        for (SkullWall wall : getWalls()) {
            for (SkullSlot s : wall.getSlots()) {
                if (s.getBlock().getLocation().equals(b.getLocation())) {
                    return wall;
                }
            }
        }
        return null;
    }

    public Set<SkullWall> getReadOnlyWalls() {
        return ImmutableSet.copyOf(getWalls());
    }

    public Set<SkullWall> getWalls() {
        return wallStore.getWalls();
    }

    private static class WallStore {
        @Getter private Set<SkullWall> walls = new ConcurrentHashSet<>();
    }
}
