package com.conceptapp.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateFactory;
import java.security.PrivateKey;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;

public final class EncryptDecrypt {

	// openssl req -nodes -new -x509 -keyout misc.key -out misc.crt -days 36500
	private static String crtPath = "WEB-INF/openssl/misc.crt";

	// openssl pkcs8 -nocrypt -topk8 -inform PEM -outform DER -in misc.key -out misc.key.pkcs8
	private static String keyPath = "WEB-INF/openssl/misc.key.pkcs8";

	private EncryptDecrypt() {}

	public static String encrypt(String crtPath, String plain) {
		try {
			// Load certificate file
			InputStream is = new FileInputStream(crtPath);
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate)cf.generateCertificate(is);
			is.close();
			// Read public key
			RSAPublicKey pubKey = (RSAPublicKey) cert.getPublicKey();
			// Encrypt
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, pubKey, SecureRandom.getInstance("SHA1PRNG"));
			byte[] secret = cipher.doFinal(plain.getBytes());
			// Return base64 encoded string
			return new String(Base64.encodeBase64(secret));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String encrypt(String plain) {
		return encrypt(crtPath, plain);
	}

	public static String decrypt(String keyPath, String secret) {
		try {
			// Load private key file  
			InputStream is = new FileInputStream(keyPath);
			byte[] key = new byte[is.available()];
			is.read(key);
			is.close();
			// Read private key
			KeyFactory kf = KeyFactory.getInstance("RSA");
			PrivateKey privKey = (RSAPrivateKey) kf.generatePrivate(new PKCS8EncodedKeySpec(key));
			// Decrypt
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");    
			cipher.init(Cipher.DECRYPT_MODE, privKey, SecureRandom.getInstance("SHA1PRNG")); 
			byte[] plain = cipher.doFinal(Base64.decodeBase64(secret));     
			return new String(plain);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String decrypt(String secret) {
		return decrypt(keyPath, secret);
	}

}