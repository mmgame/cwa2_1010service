package com.cwa.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtil {
	protected static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

	public static String base64ToStr(byte[] data) {
		return new String(Base64.encodeBase64URLSafeString(data));
	}

	public static byte[] strToBase64(String str) {
		return Base64.decodeBase64(str.getBytes());
	}

	public static String changeStringPosition(String str, int position, String changeStr, String separator) {
		if (str == null || str.equals("")) {
			return "";
		}
		String[] strs = str.split(separator);
		if (position > strs.length - 1) {
			return "";
		}

		strs[position] = changeStr;

		StringBuilder sb = new StringBuilder();
		int lastIndex = strs.length - 1;
		for (int i = 0; i <= lastIndex; i++) {
			sb.append(strs[i]);
			if (i < lastIndex) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	public static List<String> str2StringList(String str, String separator) {
		if (str == null || str.isEmpty()) {
			return new LinkedList<String>();
		}
		List<String> nums = null;
		try {
			String[] strs = str.split(separator);
			nums = new ArrayList<String>(strs.length);
			for (String s : strs) {
				nums.add(s.trim());
			}
			return nums;
		} catch (Exception e) {
			logger.error("", e);
			nums = new LinkedList<String>();
		}
		return nums;
	}

	public static List<Integer> str2IntList(String str, String separator) {
		if (str == null || str.isEmpty()) {
			return new ArrayList<Integer>();
		}
		List<Integer> nums = null;
		try {
			String[] strs = str.split(separator);
			nums = new ArrayList<Integer>(strs.length);
			for (String s : strs) {
				nums.add(Integer.parseInt(s));
			}
			return nums;
		} catch (Exception e) {
			logger.error("", e);
			nums = new LinkedList<Integer>();
		}
		return nums;
	}

	public static List<Long> str2IongList(String str, String separator) {
		List<Long> nums = null;
		try {
			String[] strs = str.split(separator);
			nums = new ArrayList<Long>(strs.length);
			for (String s : strs) {
				nums.add(Long.parseLong(s));
			}
			return nums;
		} catch (Exception e) {
			logger.error("", e);
			nums = new LinkedList<Long>();
		}
		return nums;
	}

	public static String list2String(List<?> list, String separator) {
		StringBuilder sb = new StringBuilder();
		for (Iterator<?> it = list.iterator(); it.hasNext();) {
			sb.append(String.valueOf(it.next()));
			if (it.hasNext()) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	public static int getStringUTF8Leng(String str) {
		try {
			byte[] b = str.getBytes("utf-8");
			return b.length;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String bytes2String(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append((char) b);
		}
		return sb.toString();
	}

	public static byte[] string2Bytes(String content) {
		int size = content.length();

		byte[] bytes = new byte[size];
		for (int i = 0; i < size; i++) {
			bytes[i] = (byte) content.charAt(i);
		}
		return bytes;
	}

	public static void main(String args[]) throws IOException {
//		ByteArrayOutput output = new ByteArrayOutput();
//		int commondId = 112030;
//		output.writeInt(commondId);
//
//		byte[] snsMessageBytes = output.toByteArray();
//		printBytes(snsMessageBytes);
//
//		String str = bytes2String(snsMessageBytes);
//
//		byte[] snsMessageBytes2 = string2Bytes(str);
//		printBytes(snsMessageBytes2);
		
//		
		List<Integer>  a=new ArrayList<Integer>();
		a=str2IntList("",",");
		System.err.println(a.size());
		String s="";
	}

	private static void printBytes(byte[] bytes) {
		for (byte b : bytes) {
			System.out.print(b);
			System.out.print(",");
		}
		System.out.println();
	}
}
