package net.milkycraft.skullwalls;

import java.util.HashMap;
import java.util.Map;

import net.milkycraft.skullwalls.walls.SkullWall;
import net.milkycraft.skullwalls.walls.Utils;
import net.milkycraft.skullwalls.walls.WallType;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class Cuboider {

	private Map<String, Session> sessions = new HashMap<String, Session>();

	public void add(Player player, Block b) {
		if (this.sessions.containsKey(player.getName())) {
			final Session s = this.sessions.get(player.getName());
			if (s.one == null) {
				s.setOne(b);
			} else {
				s.setTwo(b);
			}
		} else {
			Session ses = new Session();
			ses.setOne(b);
			this.sessions.put(player.getName(), ses);
		}
	}

	public void clear(Player player) {
		this.sessions.remove(player.getName());
	}

	public SkullWall createWall(Player p, WallType type, String name) {
		SkullWall wall = null;
		if (this.sessions.containsKey(p.getName())) {
			final Session s = this.sessions.get(p.getName());
			if (s.isComplete()) {
				int[] b = new int[] { s.one.getX(), s.one.getY(), s.one.getZ(),
						s.two.getX(), s.two.getY(), s.two.getZ() };
				wall = new SkullWall(type, name, s.one.getWorld(), b);
			} else {
				return null;
			}
			sessions.remove(p.getName());
		}
		return wall;
	}
	
	public void rotateSkulls(Player p, BlockFace face) {
		if (this.sessions.containsKey(p.getName())) {
			final Session s = this.sessions.get(p.getName());
			if (s.isComplete()) {
				int[] b = new int[] { s.one.getX(), s.one.getY(), s.one.getZ(),
						s.two.getX(), s.two.getY(), s.two.getZ() };
				Utils.rotate(p.getWorld(), face, b);
				p.sendMessage(ChatColor.GREEN + "Applied rotations!");
			} else {
				p.sendMessage(ChatColor.RED + "Your cuboid is not complete!");
			}
			sessions.remove(p.getName());
		}
	}

	public int getStep(final Player p) {
		if (sessions.containsKey(p.getName())) {
			final Session s = sessions.get(p.getName());
			if (s.one == null && s.two == null) {
				return 0;
			} else if (s.one != null && s.two == null) {
				return 1;
			} else if (s.one != null && s.two != null) {
				return 2;
			}
		}
		return -1;
	}
}
