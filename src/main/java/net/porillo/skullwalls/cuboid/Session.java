package net.porillo.skullwalls.cuboid;

import org.bukkit.block.Block;

public class Session {
    public Block one;
    public Block two;

    public boolean isComplete() {
        return this.one != null && this.two != null;
    }

    public void setOne(Block one) {
        this.one = one;
    }

    public void setTwo(Block two) {
        this.two = two;
    }
}
