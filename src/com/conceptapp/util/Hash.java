package com.conceptapp.util;

import org.apache.commons.codec.binary.Base64;

public class Hash extends FNV164 {

	public String getHashUrlSafe(String value) {
		update(value);
		return new String(Base64.encodeBase64(getHashByteArray(), false, true));
	}

	public byte[] getHashByteArray() {
		return longToByteArray(getHash());
	}

	public static byte[] longToByteArray(long value) {
		byte[] buf = new byte[8];
		int tmp = (int) (value >>> 32);
		buf[0] = (byte) (tmp >>> 24);
		buf[1] = (byte) ((tmp >>> 16) & 0x0ff);
		buf[2] = (byte) ((tmp >>> 8) & 0x0ff);
		buf[3] = (byte) tmp;
		tmp = (int) value;
		buf[4] = (byte) (tmp >>> 24);
		buf[5] = (byte) ((tmp >>> 16) & 0x0ff);
		buf[6] = (byte) ((tmp >>> 8) & 0x0ff);
		buf[7] = (byte) tmp;
		return buf;
	}

}
