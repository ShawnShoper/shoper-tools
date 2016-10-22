package org.shoper.commons;

/**
 * @author ShawnShoper
 * @date 16/10/14
 * @sice
 */
public class ClassUtilTest {
	public static void main (String[] args) {
		ClassUtil.NewInstance("").getSubTypesOfClass(A.class).forEach(System.out::println);
	}
}
