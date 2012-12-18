package com.alienlabz.packagez.util;

/**
 * Reflection Helper.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
final public class Reflection {

	public static <T> T instantiate(Class<T> cls) {
		T o = null;
		try {
			o = cls.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		return o;
	}

}