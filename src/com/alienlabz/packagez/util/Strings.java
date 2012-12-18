package com.alienlabz.packagez.util;

final public class Strings {

	private Strings() {
	}

	public static boolean isEmpty(final String string) {
		boolean result = false;

		if (string == null || "".equals(string)) {
			result = true;
		}

		return result;
	}

	public static String firstInUpperCase(final String string) {
		String result = string;

		if (result != null && string.length() > 1) {
			result = String.valueOf(string.charAt(0)).toUpperCase() + string.substring(1);
		}

		return result;
	}
}
