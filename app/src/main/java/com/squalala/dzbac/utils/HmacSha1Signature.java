package com.squalala.dzbac.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacSha1Signature {
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	private static Formatter formatter;
 
	private static String toHexString(byte[] bytes) {
		formatter = new Formatter();
		
		for (byte b : bytes) {
			formatter.format("%02x", b);
		}
 
		return formatter.toString();
	}
 
	public static synchronized String calculateRFC2104HMAC(String data, String key)
		throws SignatureException, NoSuchAlgorithmException, InvalidKeyException
	{
		Mac mac = null;

		if (key.length() == 0)
			key = "  ";

		try {
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
			mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);
		} catch (NullPointerException e) {
            e.printStackTrace();
		//	calculateRFC2104HMAC(data, key);
		}

       /* // Pour corriger le bug du crash
        if (data == null)
            data = "0";*/


		return toHexString(mac.doFinal(data.getBytes()));
	}
 
}