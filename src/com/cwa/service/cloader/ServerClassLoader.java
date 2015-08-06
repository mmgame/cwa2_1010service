package com.cwa.service.cloader;

import java.util.HashMap;

/**
 * 类加载器
 * 
 * @author mausmars
 * 
 */
public class ServerClassLoader {
	private static final HashMap<String, Class<?>> CLASSNAME_TO_CLASS = new HashMap<String, Class<?>>();

	private static ClassLoader classLoader;

	static {
		classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader == null) {
			classLoader = ServerClassLoader.class.getClassLoader();
		}
	}

	public static synchronized Class<?> getClass(String className) {
		Class<?> c = CLASSNAME_TO_CLASS.get(className);
		try {
			if (c == null) {
				c = Class.forName(className, true, classLoader);
			}
			if (c == null) {
				System.err.println("className:" + className);
				throw new RuntimeException();
			}
		} catch (ClassNotFoundException e) {
			System.err.println("className:" + className);
			throw new RuntimeException(e);
		}
		return c;
	}
}
