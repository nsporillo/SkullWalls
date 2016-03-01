package net.porillo.skullwalls.walls;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.World;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class SerialLocation implements Serializable {

    private static final long serialVersionUID = -645101824628385024L;
    private String world;
    private int x;
    private int y;
    private int z;

    public SerialLocation(World w, int x, int y, int z) {
        this(w.getName(), x, y, z);
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
