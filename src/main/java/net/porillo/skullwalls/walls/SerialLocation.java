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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SerialLocation that = (SerialLocation) o;

        return x == that.x && y == that.y && z == that.z && world.equals(that.world);

    }

    @Override
    public int hashCode() {
        int result = world.hashCode();
        result = 31 * result + x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }
}
