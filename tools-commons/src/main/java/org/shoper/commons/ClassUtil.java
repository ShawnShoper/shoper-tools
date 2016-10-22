package org.shoper.commons;

import org.reflections.Reflections;

import java.util.Set;

/**
 * @author ShawnShoper
 * @date 16/10/14
 * @sice
 */
public class ClassUtil {
	private ClassUtil () {
	}

	Reflections reflection = null;

	public static ClassUtil NewInstance (String pack) {
		ClassUtil classUtil = new ClassUtil();
		classUtil.reflection = new Reflections(pack);
		return classUtil;
	}

	public Set<Class> getSubTypesOfClass (Class clazz) {
		return reflection.getSubTypesOf(clazz);
	}


}
