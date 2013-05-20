package net.milkycraft.skullwalls;

import static java.lang.System.getProperty;
import static java.lang.System.out;

public class ErrorLogger {

	private static String[] p;
	static {
		p = new String[4];
		p[0] = getProperty("java.version");
		p[1] = getProperty("os.arch");
		p[2] = getProperty("os.name");
		p[3] = getProperty("os.version");
	}

	public static void report(Exception ex) {
		out.println("=====================================");
		out.println("== SkullWalls encountered an error ==");
		out.println("------------ Stack Trace ------------");
		ex.printStackTrace();
		out.println("---------- End Stack Trace ----------");
		out.println("======== System Properties ========");
		out.println("JV: " + p[0] + "OS: " + p[1] + ", " + p[2] + ", " + p[3]);
		out.println("Report this error to the SkullWalls ticket tracker!");
		out.println("=====================================");
	}

	public static void report(Exception ex, String event) {
		out.println("=====================================");
		out.println("== SkullWalls encountered an error ==");
		out.println("------------ Stack Trace ------------");
		out.println("Occured on event: " + event);
		ex.printStackTrace();
		out.println("---------- End Stack Trace ----------");
		out.println("======== System Properties ========");
		out.println("JV: " + p[0] + "OS: " + p[1] + ", " + p[2] + ", " + p[3]);
		out.println("Report this error to the SkullWalls ticket tracker!");
		out.println("=====================================");
	}
}
