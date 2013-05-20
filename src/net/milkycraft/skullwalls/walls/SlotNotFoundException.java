package net.milkycraft.skullwalls.walls;

@SuppressWarnings("serial")
public class SlotNotFoundException extends RuntimeException {
	
	public SlotNotFoundException(SkullWall w) {
        super("Could not locate available slot in wall " + w.name
				+ ", expand your wall size!");
    }
	
	public SlotNotFoundException(String mess) {
        super(mess);
    }
	public SlotNotFoundException() {
        super("");
    }
}
