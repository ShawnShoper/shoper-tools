package org.shoper.dynamiccompile;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.shoper.commons.VolatileObject;

import sun.misc.ClassLoaderUtil;

public class ClassLoaderPool
{
	private static VolatileObject<Map<String, ClassLoaderHoloder>> classLoaders = new VolatileObject<Map<String, ClassLoaderHoloder>>(
			new ConcurrentHashMap<>());
	public static void destoryClassLoader(String key) throws IOException
	{
		if (classLoaders.getObject().containsKey(key))
		{
			ClassLoaderHoloder clh = classLoaders.getObject().get(key);
			URLClassLoader classLoader = clh.getClassLoader();
			classLoader.close();
			clh.getHolderCount().decrementAndGet();
			ClassLoaderUtil.releaseLoader(classLoader);
		}
	}
	public static URLClassLoader getClassLoader(String key, URL[] urls)
	{
		URLClassLoader classLoader = null;
		if (classLoaders.getObject().containsKey(key))
		{
			ClassLoaderHoloder clh = classLoaders.getObject().get(key);
			clh.setClassLoader(URLClassLoader.newInstance(urls));
			classLoader = clh.getClassLoader();
			clh.getHolderCount().incrementAndGet();
		}
		return classLoader;
	}

}
