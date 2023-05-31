package fr.actionrpg3d;

public class Utils {
	
	public static String getOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "windows";
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            return "linux";
        } else if (os.contains("mac")) {
            return "mac";
        } else if (os.contains("sunos")) {
            return "solaris";
        }
        return "unknow";
    }
	
	public static int parseInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public static float parseFloat(String str) {
		try {
			return Float.parseFloat(str);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
}
