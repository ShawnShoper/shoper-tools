package org.shoper.dynamiccompile;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class ClassLoaderHandler_Test {
	@Test
	public void jdkCompile_test () throws Exception {
		FileInputStream in = new FileInputStream(
				new File("/Users/ShawnShoper/Desktop/Test.java"));
		StringBuilder sb = new StringBuilder();
		int len = 0;
		byte[] b = new byte[2 << 10];
		while ((len = in.read(b)) > 0) {
			sb.append(new String(b, 0, len));
		}
		in.close();
		ClassLoaderHandler clh =
				ClassLoaderHandler.newInstance(new URL(
						"http://192.168.100.177:50075/webhdfs/v1/user/cloudera/jar/mysql-connectors-java-5.1.6-bin.jar?op=OPEN&namenoderpcaddress=192.168.100.178:8020&offset=0"));
		Class<?> clazz = clh.getClassFromJavaCode(sb.toString());
		Object obj = clazz.newInstance();
		Method m = clazz.getMethod("say", null);
		m.invoke(obj, null);
		clh.close();
	}

	@Test
	public void jdkCompile_releaseClass () throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, InterruptedException {
		FileInputStream in = new FileInputStream(
				new File("/Users/ShawnShoper/Desktop/Test.java"));
		StringBuilder sb = new StringBuilder();
		int len = 0;
		byte[] b = new byte[2 << 10];
		while ((len = in.read(b)) > 0) {
			sb.append(new String(b, 0, len));
		}
		in.close();
		ClassLoaderHandler clh =
				ClassLoaderHandler.newInstance();
		Class<?> clazz = clh.getClassFromJavaCode(sb.toString());
		Object obj = clazz.newInstance();
		Method m = clazz.getMethod("say", null);
		m.invoke(obj, null);
		clh.close();
		clh = null;
		clazz = null;
		m = null;
	}
}
