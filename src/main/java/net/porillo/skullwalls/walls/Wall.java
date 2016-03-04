package net.porillo.skullwalls.walls;

import lombok.Getter;
import net.porillo.skullwalls.SkullWalls;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * This class is serialized to file, so we specify what fields to not serialize
 */
public class Wall {

    private transient List<String> cachedPlayers = new ArrayList<>();
    private transient World worldObj;
    @Getter private transient List<Slot> slots;

    @Getter private Set<String> whitelist = new HashSet<>();
    @Getter private WallType type;
    @Getter private String worldName;
    @Getter private String name;
    @Getter private int[] bounds;
    @Getter private boolean transparent;

    public Wall(WallType type, String name, World world, int[] bounds) {
        this.transparent = false;
        this.name = name;
        this.type = type;
        this.worldObj = world;
        this.worldName = world.getName();
        this.bounds = bounds;
        this.whitelist = new HashSet<>();

        slots = SkullWalls.getWallHandler().getSlotsFromBounds(world, bounds);

        updateWall(SkullWalls.getOnlinePlayers());
    }

    private List<String> getCachedNames(List<String> players) {
        if (players != null) {
            this.cachedPlayers = players;
        }
        return cachedPlayers;
    }

    public void updateWall(List<String> players) {
        if (this.slots == null) {
            if(worldObj == null) {
                worldObj = Bukkit.getWorld(worldName);
            }

            slots = SkullWalls.getWallHandler().getSlotsFromBounds(worldObj, bounds);
        }

        List<String> copy = getCachedNames(players);

        if (type == WallType.CUSTOM) {
            for (String name : copy) {
                Player p = Bukkit.getPlayerExact(name);
                if (this.type == WallType.CUSTOM) {
                    if (!p.hasPermission("skullwalls.wall." + this.name) && !this.whitelist.contains(p.getName())) {
                        copy.remove(name);
                    }
                }
            }
        } else if (type == WallType.BANNED) {
            copy = new ArrayList<>();
            copy.addAll(WallHandler.getBanCache());

            if(slots.size() < copy.size()) {
                copy = copy.subList(0, slots.size());
            }
        }

        for (Slot s : slots) {
            s.validate();
            if (copy.size() > 0) {
                s.setOwner(copy.remove(0));
            }
        }

        this.verifyState();
    }

    public void setTransparent(boolean value) {
        this.transparent = value;
        if (value) {
            this.slots.stream().filter(s -> !s.hasOwner()).forEach(Slot::disappear);
        } else
            this.slots.stream().filter(s -> !s.hasOwner()).forEach(Slot::appear);
    }

    public void offSet(int num) {
        if (this.slots.size() < num) {
            return;
        }

        this.slots = WallHandler.offset(this.slots, num);
        this.updateWall(null);
    }

    public void verifyState() {
        for (Slot s : this.slots) {
            if (this.transparent) {
                if (!s.hasOwner()) {
                    s.disappear();
                }
            } else if (!s.hasOwner()) {
                s.appear();
            }
        }
    }

    public String getStringBounds() {
        return Arrays.toString(Arrays.toString(bounds).split("[\\[\\]]")[1].split(", "));
    }
}
