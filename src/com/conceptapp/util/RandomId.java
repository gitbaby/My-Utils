package com.conceptapp.util;

import java.security.SecureRandom;
import org.apache.commons.codec.binary.Base64;

public class RandomId {

    private static final SecureRandom random = new SecureRandom();

    private RandomId() {}

    /*
     * byteLength 8 will result in 11 character id.
     * byteLength 16 will result in 22 character id.
     * Bigger byteLength, better collision rate (birthday paradox).
     */
    public static String getRandomUrlSafeId(int byteLength) {
        byte bytes[] = new byte[byteLength];
        random.nextBytes(bytes);
        return new String(Base64.encodeBase64(bytes, false, true));
    }

}