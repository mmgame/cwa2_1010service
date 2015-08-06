package com.cwa.util.prototype;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StringUtil {
	protected static final Logger logger = LoggerFactory.getLogger(StringUtil.class);
	/**
	 * 字符串首字符大写
	 * 
	 * @param str
	 * @return
	 */
	public static String upperFirstString(String str) {
		if (str == null || str.equals("")) {
			return str;
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public static String lowerFirstString(String str) {
		if (str == null || str.equals("")) {
			return str;
		}
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	// 将protobuf的API返回的类型（形如：STRING）转化成java中的类型
	public static String toJavaType(String str, String isList) {
		if (str.equals("STRING")) {
			return "String";
		}
		if (isList.equals("true") && str.equals("INT")) {
			return "Integer";
		}
		if (str.equals("DOUBLE")) {
			return "Double";
		}
		if (str.equals("FLOAT")) {
			return "Float";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			String temp = String.valueOf(str.charAt(i));
			sb.append(temp.toLowerCase());
		}
		return sb.toString();
	}

	/**
	 * 读一个json数组字符串返回一个json的list
	 */
	public static List<String> getJsonList(String fileContent) {
//		System.out.println(fileContent);
		List<String> jsonList = new ArrayList<String>();
		Pattern p = Pattern.compile("\\{(.*?)\\}");
		Matcher m = p.matcher(fileContent);
		while (m.find()) {
			String aJson = m.group(0);
			jsonList.add(aJson);
		}
		return jsonList;
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
}
