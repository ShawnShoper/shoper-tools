package org.shoper.dynamiccompile;

import groovy.lang.GroovyObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


/**
 * GroovyLoader tester
 * @author ShawnShoper
 *
 */
public class GroovyLoader {
	public static void compile(String java) throws Exception{
		Class<?> clazz = GroovyCompile.compile(java);
		GroovyObject baseCaller = (GroovyObject)clazz.newInstance();
		baseCaller.invokeMethod("say", null);
	}
	public static void main(String[] args) throws Exception {
		for(;;){
			//File file = new File("/Users/ShawnShoper/Documents/workspace/shoper-util/src/test/java/org/shoper/dynamiccompile/GroovyTester.java");
			File file = new File("/Users/ShawnShoper/Desktop/test/Test.java");
			InputStream inputStream = new FileInputStream(file);
			final byte bs[] = new byte[inputStream.available()];
			inputStream.read(bs);
			inputStream.close();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						compile(new String(bs,0,bs.length));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
			
			Thread.sleep(2000);
		}
	}
}
