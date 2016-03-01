package net.porillo.skullwalls.walls;

import com.google.common.base.Joiner;

public enum WallType {
    GLOBAL, CUSTOM, BANNED;

    public static String getTypes() {
        return Joiner.on(",").join(values());
    }
}
