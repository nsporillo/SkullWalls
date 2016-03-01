package net.porillo.skullwalls.walls;

import lombok.Getter;
import lombok.Setter;
import net.porillo.skullwalls.SkullWalls;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;

import java.util.*;

public class Wall {

    private transient List<String> knownBanned = new ArrayList<>();
    private transient List<String> cachedPlayers = new ArrayList<>();
    private transient World worldObj;
    @Getter @Setter private transient List<Slot> slots;
    @Getter private String worldName;
    @Getter private WallType type;
    @Getter private Set<String> whitelist;
    @Getter private String name;
    @Getter private int[] bounds;
    @Getter private boolean transparent;

    public Wall(WallType type, String name, World world, int[] bounds) {
        this.transparent = false;
        this.name = name;
        this.type = type;
        this.worldObj = world;
        this.worldName = this.worldObj.getName();
        this.bounds = bounds;
        this.whitelist = new HashSet<>();

        setSlots(SkullWalls.getWallHandler().getSlotsFromBounds(worldObj, this.bounds));

        if (type == WallType.BANNED) {
            this.banUpdate();
        } else {
            updateWall(SkullWalls.getOnlinePlayers());
        }
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

            setSlots(SkullWalls.getWallHandler().getSlotsFromBounds(worldObj, this.bounds));
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
            banUpdate();
            copy = knownBanned;
        }

        for (Slot s : slots) {
            s.validate();
            if (copy.size() > 0) {
                s.setOwner(copy.remove(0));
            }
        }

        this.verifyState();
    }

    private void banUpdate() {
        int max = this.slots.size();
        int tot = 0;
        for (OfflinePlayer op : Bukkit.getBannedPlayers())
            if (tot < max - 1) {
                this.knownBanned.add(op.getName());
                tot++;
            } else {
                System.err.println("Wall has " + max + " max slots, theres too many banned players! (" + Bukkit.getBannedPlayers().size() + ")");
                break;
            }
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
        // this.slots = Utils.offset(this.slots, num);
        if (this.type == WallType.BANNED) {
            this.banUpdate();
        }
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
