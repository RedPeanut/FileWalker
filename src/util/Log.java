package util;

import main.Config;

public class Log {
	
	public static void d(String tag, String msg) {
		if (Config.LOG) System.out.println("[" + tag + "] " + msg);
	}
}
