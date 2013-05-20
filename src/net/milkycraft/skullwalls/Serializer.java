package net.milkycraft.skullwalls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

import net.milkycraft.skullwalls.walls.SkullWall;

public class Serializer {

	private static File dir = new File("plugins" + File.separator + "SkullWalls"
			+ File.separator + "walls");
	
	public static void save(Set<SkullWall> walls) {
		if (!dir.exists()) {
			dir.mkdir();
		}
		for (SkullWall c : walls) {
			File f = new File(dir, c.getName() + ".wall");
			try {
				if (f.exists()) {
					f.delete();
					f.createNewFile();
				}
				f.createNewFile();
				FileOutputStream fileOut = new FileOutputStream(f);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(c);
				out.close();
				fileOut.close();
			} catch (IOException i) {
				i.printStackTrace();
			}
		}
	}

	public static void delete(SkullWall c) {
		for (File f : dir.listFiles()) {
			if (f.getName().startsWith(c.getName())) {
				f.delete();
			}
		}
	}
	
	public static void deleteAll() {
		for (File f : dir.listFiles()) {
			if (f.getName().endsWith(".wall")) {
				f.delete();
			}
		}
	}

	public static Set<SkullWall> load() {
		Set<SkullWall> local = new HashSet<SkullWall>();
		if (!dir.exists()) {
			dir.mkdir();
		}
		for (File f : dir.listFiles()) {
			if (f.getName().endsWith(".wall")) {
				try {
					SkullWall c = null;
					FileInputStream fileIn = new FileInputStream(f);
					ObjectInputStream in = new ObjectInputStream(fileIn);
					c = (SkullWall) in.readObject();
					in.close();
					fileIn.close();
					local.add(c);
				} catch (Exception i) {
					i.printStackTrace();
				}
			}
		}
		return local;
	}

	public static void setWorkingDir(File dir) {
		Serializer.dir = dir;
	}
}
