package net.firsthour.util;

import java.io.FileInputStream;
import java.util.Properties;

public class Props {
	
	private static Properties props = null;
	
	private Props() {}
	
	public static String get(String key) {
		if(props == null) {
			props = new Properties();
			try(FileInputStream profile = new FileInputStream(Thread.currentThread().getContextClassLoader().getResource("").getPath() + "profile.properties")) {
				props.load(profile);
			} catch (Exception e) {
				props = null;
			}
			
			if(props == null) {
				props = new Properties();
				try(FileInputStream def = new FileInputStream(Thread.currentThread().getContextClassLoader().getResource("").getPath() + "default.properties")) {
					props.load(def);
				} catch (Exception e) {
					props = null;
					throw new RuntimeException("need to define either default.properties or profile.properties");
				}
			}
		}
		
		Object value = props.get(key);
		if(value == null) {
			return null;
		}
		
		return value.toString();
	}
}
