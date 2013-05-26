package net.milkycraft.skullwalls.walls;

import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.milkycraft.skullwalls.SkullWalls;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class Utils {

	public static void gen(SkullWall w, int[] b) {
		int a = 0;
		w.slots = new ArrayList<Slot>();
		for (int x = min(b[0], b[3]); x <= max(b[0], b[3]); x++) {
			for (int y = min(b[1], b[4]); y <= max(b[1], b[4]); y++) {
				for (int z = min(b[2], b[5]); z <= max(b[2], b[5]); z++) {
					w.slots.add(new Slot(w, w.getWorld().getBlockAt(x, y, z), a++));
				}
			}
		}
	}

	public static void rotate(World w, BlockFace bf, int[] b) {
		for (int x = min(b[0], b[3]); x <= max(b[0], b[3]); x++) {
			for (int y = min(b[1], b[4]); y <= max(b[1], b[4]); y++) {
				for (int z = min(b[2], b[5]); z <= max(b[2], b[5]); z++) {
					Block b0 = w.getBlockAt(x, y, z);
					try {
						b0.setTypeIdAndData(b0.getTypeId(), (byte) getRot(bf), true);
					} catch (Exception ex) {
						continue;
					}
				}
			}
		}
	}
	
	public static void para(Location b, double t) {
		for (double x = -t; x < t; x++) {
			double y = exp(x);
			b.getWorld().getBlockAt((int) (b.getX() + y), (int) (b.getY() + y),
					(int) (b.getZ() + y)).setTypeId(1);
		}
	}

	public static double getAtleastProbability(int x, int y, double z) {
		double d = 0D;
		int a = x - y;
		for (int i = 0; i <= a; i++) {
			d += (f(x) / (f(y + i) * f(x - (y + i))) * pow(z, y + i) * pow(1 - z, (a) - i));
		}
		return r(d * 100, 1);
	}

	public static int f(double y) {
		int r = 1;
		for (int i = 1; i <= y; ++i)
			r *= i;
		return r;
	}

	public static double[] getAngles(int x, int y, int z) {
		return new double[] { ang(y, z, x), ang(z, x, y), ang(x, y, z) };
	}

	public static double ang(double a, double b, double c) {
		double t = (pow(a, 2) + pow(b, 2) - pow(c, 2)) / (2 * a * b);
		if (t >= -1 && t <= 1) {
			return r(toDegrees(acos(t)), 2);
		}
		return Double.NaN;
	}

	public static double r(double v, int p) {
		long f = (long) pow(10, p);
		return (double) round(v * f) / f;
	}

	public static int getRot(BlockFace face) {
		if (face == BlockFace.SELF) {
			return 0;
		} else if (face == BlockFace.NORTH) {
			return 2;
		} else if (face == BlockFace.SOUTH) {
			return 3;
		} else if (face == BlockFace.EAST) {
			return 4;
		} else if (face == BlockFace.WEST) {
			return 5;
		}
		return -1;
	}

	public static List<Player> getAllInRadius(SkullWall wall, double radius) {
		List<Player> players = new ArrayList<Player>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (getDistanceFromCenter(wall, p) < radius) {
				players.add(p);
			}
		}
		return players;
	}

	public static int getTotalSlots() {
		int total = 0;
		for (SkullWall wall : SkullWalls.getWalls()) {
			total += wall.slots.size();
		}
		return total;
	}

	public static boolean isInRadius(Player p, SkullWall wall, double radius) {
		if (getDistanceFromCenter(wall, p) > radius) {
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
		if (slots % 2 == 0) {
			c = slots / 2;
		} else {
			c = (slots / 2) + 1;
		}
		Location slot = wall.getSlots().get(c).getBlock().getLocation();
		Location eye = p.getEyeLocation();
		if (!slot.getWorld().getName().equals(eye.getWorld().getName())) {
			return Double.MAX_VALUE;
		}
		return slot.distance(eye);
	}

	public static double getDistanceFromSlot(Player p, Slot s) {
		return (p.getLocation().getWorld().getName()
				.equals(s.getBlock().getLocation().getWorld().getName()) ? p.getEyeLocation()
				.distance(s.getBlock().getLocation()) : Double.MAX_VALUE);
	}

	public static List<Slot> offset(List<Slot> l, int i) {
		List<Slot> x = new ArrayList<Slot>();
		List<Slot> y = new ArrayList<Slot>(l.subList(i, l.size()));
		List<Slot> z = new ArrayList<Slot>(l.subList(0, max(i, 1)));
		x.addAll(y);
		x.addAll(z);
		return x;
	}

	public static boolean check(Block b, Player p, String noperm) {
		for (SkullWall w : SkullWalls.getWalls()) {
			for (Slot s : w.getSlots()) {
				if (s.getBlock().equals(b)) {
					p.sendMessage(noperm);
					return true;
				}
			}
		}
		return false;
	}

	public static Slot getNextAvailable(SkullWall w) {
		for (Slot slot : w.slots) {
			if (!slot.hasOwner() && !slot.isDisabled()) {
				if (!slot.isVisible()) {
					slot.appear();
				}
				return slot;
			}
		}
		throw new SlotNotFoundException(w);
	}

	public static boolean isSlot(Block b) {
		for (SkullWall w : SkullWalls.getWalls()) {
			for (Slot s : w.getSlots()) {
				if (s.getBlock().getLocation().equals(b.getLocation())) {
					return true;
				}
			}
		}
		return false;
	}
}
