package net.porillo.skullwalls;

import org.bukkit.block.Block;

public class Session {
	public Block	one;
	public Block	two;

	public boolean isComplete() {
		if (this.one != null && this.two != null) {
			return true;
		}
		return false;
	}

	public void setOne(Block one) {
		this.one = one;
	}

	public void setTwo(Block two) {
		this.two = two;
	}
}
