package net.porillo.skullwalls.walls;

import org.bukkit.World;

import java.io.Serializable;

public class SerialLocation implements Serializable {
    private static final long serialVersionUID = -645101824628385024L;
    public String world;
    public int x;
    public int y;
    public int z;

    public SerialLocation(World w, int x, int y, int z) {
        this.world = w.getName();
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
