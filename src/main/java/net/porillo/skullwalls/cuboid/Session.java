package net.porillo.skullwalls.cuboid;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;

@Data
@RequiredArgsConstructor
public class Session {
    public final Block one;
    public Block two;

    public boolean isComplete() {
        return this.one != null && this.two != null;
    }

    public void setTwo(Block two) {
        this.two = two;
    }
}
