package org.shoper.commons;

/**
 * Created by ShawnShoper on 16/9/6.
 */
public class OSUtil {
	public static String getOS () {
		return System.getProperty("os.name");
	}

	public static String getJavaLibraryPath () {
		return System.getProperty("java.library.path");
	}
	public static void setProperties(String key,String value){
		System.setProperty(key,value);
	}

}
