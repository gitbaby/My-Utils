package com.conceptapp.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1 {
	private static final String hexes = "0123456789ABCDEF";

	public static String hex(byte[] raw) {
		if (raw == null) return null;
		final StringBuilder hex = new StringBuilder(2 * raw.length);
		for (final byte b : raw) hex.append(hexes.charAt((b & 0xF0) >> 4))
			.append(hexes.charAt((b & 0x0F)));
		return hex.toString();
	}

	public static String hash(String text)
	throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(text.getBytes("UTF-8"));
		return hex(md.digest());
	}
}