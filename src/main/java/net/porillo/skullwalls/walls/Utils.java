package net.porillo.skullwalls.walls;

import net.porillo.skullwalls.SkullWalls;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Double.NaN;

public class Utils {

    public static void gen(SkullWall w, int[] b) {
        w.slots = new ArrayList<SkullSlot>();
        for (int x = Math.min(b[0], b[3]); x <= Math.max(b[0], b[3]); x++) {
            for (int y = Math.min(b[1], b[4]); y <= Math.max(b[1], b[4]); y++) {
                for (int z = Math.min(b[2], b[5]); z <= Math.max(b[2], b[5]); z++) {
                    w.slots.add(new SkullSlot(w, w.getWorld().getBlockAt(x, y, z)));
                }
            }
        }
    }

    public static List<Player> getAllInRadius(SkullWall wall, double radius) {
        List<Player> players = new ArrayList<Player>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (Utils.getDistanceFromCenter(wall, p) < radius) {
                players.add(p);
            }
        }
        return players;
    }

    public static boolean isInRadius(Player p, SkullWall wall, double radius) {
        if (Utils.getDistanceFromCenter(wall, p) > radius) {
            return false;
        }
        return true;
    }

    public static String bind(int[] bounds) {
        String[] a = Arrays.toString(bounds).split("[\\[\\]]")[1].split(", ");
        return Arrays.toString(a);
    }

    public static double getDistanceFromCenter(SkullWall wall, Player p) {
        int slots = wall.getSlots().size();
        int c = 0;
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

    public static double getDistanceFromSlot(Player p, SkullSlot s) {
        Location slot = s.getBlock().getLocation();
        String w1 = p.getLocation().getWorld().getName();
        String w2 = slot.getWorld().getName();
        return w1.equals(w2) ? p.getEyeLocation().distance(slot) : NaN;
    }

    public static List<SkullSlot> offset(List<SkullSlot> l, int i) {
        List<SkullSlot> x = new ArrayList<SkullSlot>();
        List<SkullSlot> y = new ArrayList<SkullSlot>(l.subList(i, l.size()));
        List<SkullSlot> z = new ArrayList<SkullSlot>(l.subList(0, i > 1 ? i : 1));
        x.addAll(y);
        x.addAll(z);
        return x;
    }

    public static boolean check(Block b, Player p, String noperm) {
        for (SkullWall wall : SkullWalls.getWalls()) {
            for (SkullSlot s : wall.getSlots()) {
                if (s.getBlock().equals(b)) {
                    p.sendMessage(noperm);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isSlot(Block b) {
        for (SkullWall wall : SkullWalls.getWalls()) {
            for (SkullSlot s : wall.getSlots()) {
                if (s.getBlock().getLocation().equals(b.getLocation())) {
                    return true;
                }
            }
        }
        return false;
    }
}
