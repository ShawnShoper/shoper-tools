package org.shoper.dynamiccompile;

import groovy.lang.GroovyClassLoader;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dynamic Compile and load java.... Call  method</br>
 * Groovy动态编译工具类.</br>
 * 注:以下内容 必看!!!!!!!</br>
 * <pre>
 * 	Class&lt;?&gt; clazz = GroovyCompile.compile(java);
 *	GroovyObject baseCaller = (GroovyObject)clazz.newInstance();
 *	baseCaller.invokeMethod("call", null);
 * </pre>
 * 注意:在编写java文件的groovy脚本时候不能出现以下语法：</br>
 * 1.变量名不能重复，不管再任何代码块中.</br>
 * 2.不能出现代码块</br>
 * <pre>
 * {
 * 	int a = 0;
 * 	......
 * }
 * </pre>
 * </br>
 * 3.不能出现$符号</br>
 * 4.字符串拼接不要换行</br>
 * 其余部分尚未发现...后续再补充
 * 
 * @author ShawnShoper
 *
 */
public class GroovyCompile {
	private static Logger logger = LoggerFactory.getLogger(GroovyCompile.class);
	//private static final String USER_DIR = System.getProperty("user.dir");
	//private static final String SUB_DIR = "/tmp";
	//private static final String CLASSDIR = USER_DIR + SUB_DIR;
	static GroovyClassLoader loader;
	// checking compile output directory....
	static {
		init();
	}
	/**
	 * 初始化,检测系统..以及初始化GrovvyClassLoader..
	 */
	private static void init() {
		logger.info("Checking OS.....");
		String osName = System.getProperties().getProperty("os.name");
		logger.info("Current OS is {}",osName);
		loader = new GroovyClassLoader(GroovyCompile.class.getClassLoader());
	}

	/**
	 * Compile java file by java file String.
	 * 
	 * @param java
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Class<?> compile(String java) {
		if(java==null||java.isEmpty()){return null;}
		logger.info("Preparing to compile java file.....");
		logger.info("Preparing Write java string to file...");
		String className = parseClassName(java);
		String packageName = parsePackageName(java);
		try {
			loader.parseClass(java);
		} catch (Exception e) {
			logger.error(
					"Compliing JAVA file failed...please check the java file content or user has permission to access or file is exists...{}",e);
		}
		Class<?> clazz = null;
		try {
			clazz = loader.loadClass(packageName.isEmpty() ? ""
					: (packageName + ".")+ className);
		} catch (ClassNotFoundException e) {
			logger.error("Compliing JAVA file failed...please check the java class file is exists...,message is {}",e);
		}
		// close loader....
//		try {
//			loader.close();
//		} catch (IOException e) {
//			logger.error("URLClassLoader close failed...message is {}",
//					e);
//		}
		return clazz;
	}

	/**
	 * 
	 * parse package name
	 *
	 **/
	private static String parsePackageName(String java) {
		java = java.replaceAll("(?>/\\*)[^(\\*/)]*\\*/", "");
		String packageName = java.substring(java.indexOf("package") + 8,
				java.indexOf(";"));
		return packageName;
	}

	/**
	 * Parse string to get className
	 * 解析java文件类名
	 * @param java
	 * @return
	 */
	public static String parseClassName(String java) {
		logger.info("Step 1:Parse java string to get className...");
		int prefix_index = java.indexOf(" class ") + 7;
		int stufix_index1 = java.substring(prefix_index).indexOf(" ");
		int stufix_index2 = java.substring(prefix_index).indexOf("{");
		int stufix_index = stufix_index2 > stufix_index1 ? stufix_index1
				: stufix_index2;
		String className = java.substring(prefix_index, prefix_index
				+ stufix_index);

		className = className.replaceAll("\\s*", "");
		return className;
	}

}
