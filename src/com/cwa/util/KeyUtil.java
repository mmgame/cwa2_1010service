package com.cwa.util;

import java.util.UUID;

public class KeyUtil {
	public static String getUUID() {
		String id = UUID.randomUUID().toString().replace("-", "");
		return id;
	}
}
