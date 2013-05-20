package net.milkycraft.skullwalls.walls;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.milkycraft.skullwalls.SkullWalls;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class Utils {

	private static boolean benching = false;

	public static void bench(long lo) {
		if (benching) {
			System.out.println("Recalculation took " + ((System.nanoTime() - lo) / 1000)
					+ " microseconds");
		}
	}

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

	public static List<String> genRandomNames(int b) {
		List<String> names = new ArrayList<String>(b);
		Random r = new Random();
		for (int i = 0; i < b; i++) {
			char[] w = new char[r.nextInt(8) + 4];
			for (int j = 0; j < w.length; j++) {
				w[j] = (char) ('a' + r.nextInt(26));
			}
			names.add(new String(w));			
		}
		return names;
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
