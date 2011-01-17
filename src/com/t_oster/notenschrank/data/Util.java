package com.t_oster.notenschrank.data;

public class Util {
	public enum OS{
		WINDOWS,
		LINUX,
		MAC,
		UNKNOWN
	}
	
	public static OS getOS(){
		if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
			return OS.WINDOWS;
		} else if (System.getProperty("os.name").toLowerCase().indexOf("linux") > -1) {
			return OS.LINUX;
		} else if (System.getProperty("os.name").toLowerCase().indexOf("mac") > -1) {
			return OS.MAC;
		}
		else{
			return OS.UNKNOWN;
		}

	}
}
