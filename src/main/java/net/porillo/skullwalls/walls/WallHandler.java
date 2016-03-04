package net.porillo.skullwalls.walls;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.Setter;
import net.porillo.skullwalls.SkullWalls;
import net.porillo.skullwalls.collections.ConcurrentHashSet;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.lang.Double.NaN;

public class WallHandler {

    @Getter @Setter private WallStore wallStore;
    @Getter private final static Set<String> banCache = new ConcurrentHashSet<>();
    private final File wallsFile = new File(new File("plugins" + File.separator + "SkullWalls"), "walls.json");

    public WallHandler() {
        loadWalls();

        new BukkitRunnable() {
            @Override
            public void run() {
                if(wallStore.contains(WallType.BANNED)) {
                    banCache.clear();

                    for(OfflinePlayer op : Bukkit.getOfflinePlayers()) {
                        if(op.isBanned()) {
                            banCache.add(op.getName());
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(SkullWalls.getInstance(), 20L, 1200L); // Update ban cache
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
            FileUtils.write(wallsFile, SkullWalls.getGson().toJson(wallStore));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void remove(Wall wall) {
        getWalls().remove(wall);

        saveWalls();
    }

    public void add(Wall wall) {
        getWalls().add(wall);

        saveWalls();
    }

    public boolean exists(Wall wall) {
        for (Wall w : getWalls()) {
            if (Objects.equals(w.getName(), wall.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPermission(Block block, Player player, String message) {
        for (Wall wall : getWalls()) {
            for (Slot s : wall.getSlots()) {
                if (s.getBlock().equals(block)) {
                    player.sendMessage(message);
                    return true;
                }
            }
        }
        return false;
    }

    public Wall getWallFromBlock(Block b) {
        for (Wall wall : getWalls()) {
            for (Slot s : wall.getSlots()) {
                if (s.getBlock().getLocation().equals(b.getLocation())) {
                    return wall;
                }
            }
        }
        return null;
    }

    public List<Slot> getSlotsFromBounds(World world, int[] b) {
        List<Slot> slots = new ArrayList<>();
        for (int x = Math.min(b[0], b[3]); x <= Math.max(b[0], b[3]); x++) {
            for (int y = Math.min(b[1], b[4]); y <= Math.max(b[1], b[4]); y++) {
                for (int z = Math.min(b[2], b[5]); z <= Math.max(b[2], b[5]); z++) {
                    slots.add(new Slot(world.getBlockAt(x, y, z)));
                }
            }
        }
        return slots;
    }

    public double getDistanceFromCenter(Wall wall, Player p) {
        int slots = wall.getSlots().size();
        int c;

        if (slots % 2 == 0)
            c = slots / 2;
        else {
            c = (slots / 2) + 1;
        }

        Location slot = wall.getSlots().get(c).getBlock().getLocation();
        Location eye = p.getEyeLocation();

        if (!slot.getWorld().getName().equals(eye.getWorld().getName())) {
            return NaN;
        }
        return slot.distance(eye);
    }

    public static List<Slot> offset(List<Slot> l, int i) {
        List<Slot> x = new ArrayList<>();
        List<Slot> y = new ArrayList<>(l.subList(i, l.size()));
        List<Slot> z = new ArrayList<>(l.subList(0, i > 1 ? i : 1));
        x.addAll(y);
        x.addAll(z);
        return x;
    }


    public Set<Wall> getReadOnlyWalls() {
        return ImmutableSet.copyOf(getWalls());
    }

    public Set<Wall> getWalls() {
        return wallStore.getWalls();
    }

    private static class WallStore {
        @Getter private Set<Wall> walls = new ConcurrentHashSet<>();

        public boolean contains(WallType type) {
            for (Wall wall : walls) {
                if (wall.getType() == type) {
                    return true;
                }
            }
            return false;
        }
    }
}
