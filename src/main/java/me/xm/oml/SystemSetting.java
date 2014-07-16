package me.xm.oml;

import java.util.HashMap;
import java.util.List;

public class SystemSetting {
	private static SystemSetting instance = new SystemSetting();

	public static SystemSetting getInstance() {
		return instance;
	}

	private HashMap<String, String> paths;
	private String dataFolder;
	private String dataPath;

	protected SystemSetting() {
		paths = new HashMap<String,String>();
		paths.put(String.valueOf("/Volumes/Seagate Expansion Drive/Classical Music HD/Baroque".hashCode()),
				"/Volumes/Seagate Expansion Drive/Classical Music HD/Baroque");
		paths.put(String.valueOf("/Volumes/Seagate Expansion Drive/Classical Music HD/Classical".hashCode()),
				"/Volumes/Seagate Expansion Drive/Classical Music HD/Classical");
		paths.put(String.valueOf("/Volumes/Seagate Expansion Drive/Classical Music HD/Romantic".hashCode()),
				"/Volumes/Seagate Expansion Drive/Classical Music HD/Romantic");
		paths.put(String.valueOf("/Volumes/Seagate Expansion Drive/Classical Music HD/20century".hashCode()),
				"/Volumes/Seagate Expansion Drive/Classical Music HD/20century");
		paths.put(String.valueOf("/Volumes/Seagate Expansion Drive/Classical Music HD/Misc".hashCode()),
				"/Volumes/Seagate Expansion Drive/Classical Music HD/Misc");
		
		dataFolder = "/usr/local/apache-tomcat-7.0.40/webapps/ROOT/omldata/";
		//dataFolder = "C:/Program Files/Apache Software Foundation/apache-tomcat-6.0.36/webapps/ROOT/omldata/";
		
		dataPath = "/omldata/";
	}

	public HashMap<String, String> getPaths() {
		return paths;
	}

	public String getDataPath() {
		return dataPath;
	}

	public String getDataFolder() {
		return dataFolder;
	}

	
}
