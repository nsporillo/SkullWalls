package net.porillo.skullwalls;

import net.porillo.skullwalls.walls.SkullWall;
import net.porillo.skullwalls.walls.WallType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Cuboider {
    private Map<String, Session> sessions = new HashMap<>();

    public void add(Player player, Block b) {
        if (this.sessions.containsKey(player.getName())) {
            Session s = this.sessions.get(player.getName());
            if (s.one == null)
                s.setOne(b);
            else
                s.setTwo(b);
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
            Session s = this.sessions.get(p.getName());
            if (s.isComplete()) {
                int[] b = {s.one.getX(), s.one.getY(), s.one.getZ(), s.two.getX(), s.two.getY(),
                        s.two.getZ()};
                wall = new SkullWall(type, name, s.one.getWorld(), b);
            } else {
                return null;
            }
            this.sessions.remove(p.getName());
        }
        return wall;
    }

    public int getStep(Player p) {
        if (this.sessions.containsKey(p.getName())) {
            Session s = this.sessions.get(p.getName());
            if (s.one == null && s.two == null)
                return 0;
            if (s.one != null && s.two == null)
                return 1;
            if (s.one != null) {
                return 2;
            }
        }
        return -1;
    }
}
