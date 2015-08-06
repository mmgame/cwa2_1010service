package com.cwa.util.encryption;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * md5工具
 * 
 * @author mausmars
 *
 */
public class Md5Util {
	private static final Logger logger = LoggerFactory.getLogger(Md5Util.class);

	public static String md5Encode(String sourceStr) {
		try {
			byte[] bytes = sourceStr.getBytes("utf-8");
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(bytes);
			byte[] md5Byte = md5.digest();
			if (md5Byte != null) {
				// signStr = HexBin.encode(md5Byte);
				return Hex.encodeHexString(md5Byte);
			}
		} catch (Exception e) {
			logger.error("Failed to sign str: " + sourceStr, e);
		}
		return null;
	}

	public static void main(String args[]) {
		System.out.println(md5Encode("deee"));
	}
}
