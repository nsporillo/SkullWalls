package net.porillo.skullwalls.walls;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class SerialLocation implements Serializable {

    private static final long serialVersionUID = -645101824628385024L;
    private final String world;
    private final int x;
    private final int y;
    private final int z;

    public SerialLocation(Block block) {
        this(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
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
