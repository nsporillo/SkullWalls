package net.porillo.skullwalls.walls;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Double.NaN;

public class Utils {

    public static void gen(SkullWall w, int[] b) {
        w.slots = new ArrayList<>();
        for (int x = Math.min(b[0], b[3]); x <= Math.max(b[0], b[3]); x++) {
            for (int y = Math.min(b[1], b[4]); y <= Math.max(b[1], b[4]); y++) {
                for (int z = Math.min(b[2], b[5]); z <= Math.max(b[2], b[5]); z++) {
                    w.slots.add(new SkullSlot(w, w.getWorld().getBlockAt(x, y, z)));
                }
            }
        }
    }

    public static boolean isInRadius(Player p, SkullWall wall, double radius) {
        return Utils.getDistanceFromCenter(wall, p) <= radius;
    }

    public static String bind(int[] bounds) {
        String[] a = Arrays.toString(bounds).split("[\\[\\]]")[1].split(", ");
        return Arrays.toString(a);
    }

    public static double getDistanceFromCenter(SkullWall wall, Player p) {
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

    public static double getDistanceFromSlot(Player p, SkullSlot s) {
        Location slot = s.getBlock().getLocation();
        String w1 = p.getLocation().getWorld().getName();
        String w2 = slot.getWorld().getName();
        return w1.equals(w2) ? p.getEyeLocation().distance(slot) : NaN;
    }

    public static List<SkullSlot> offset(List<SkullSlot> l, int i) {
        List<SkullSlot> x = new ArrayList<>();
        List<SkullSlot> y = new ArrayList<>(l.subList(i, l.size()));
        List<SkullSlot> z = new ArrayList<>(l.subList(0, i > 1 ? i : 1));
        x.addAll(y);
        x.addAll(z);
        return x;
    }
}
