/** 
 * @ClassName: UUIDUtils.java
 * @author: jiajie_liang
 * @date: 2015年7月1日 下午5:46:25
 * @copyright:  ©2011-2014 youpo.net
 * @Description:
 *
 */
package com.ewing.order.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

/**
 * @author jiajie_liang
 *
 */
public class UUIDUtils {

	/**
	 * 获取UUID
	 * 
	 * @return
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 获取指定长度的UUID
	 * 
	 * @param index
	 * @return
	 */
	public static String getUUID(int index) {
		String uuid = UUID.randomUUID().toString();
		uuid = StringUtils.replace(uuid, "-", "");
		return uuid.substring(0, index);
	}

	/**
	 * 获取数字UUID
	 * 
	 * @return
	 */
	public static String getNumUUID() {
		String uuid = UUID.randomUUID().toString();
		uuid = StringUtils.replace(uuid, "-", "");
		String result = "";
		if (uuid != null && !uuid.equals("")) {
			for (int i = 0; i < uuid.length(); i += 4) {
				result += Integer.valueOf(uuid.substring(i, 4 + i), 16);
			}
		}
		return result;
	}

	/**
	 * 获取指定长度的数字UUID
	 * 
	 * @param index
	 * @return
	 */
	public static String getNumUUID(int index) {
		return getNumUUID().substring(0, index);
	}

	/**
	 * uuid转换数字 ***注意下标越界
	 * 
	 * @param uuid
	 * @return
	 */
	public static String generateNumber(String uuid, int startIndex, int length) {
		// String uuid = UUID.randomUUID().toString();
		uuid = StringUtils.replace(uuid, "-", "");
		String result = "";
		if (uuid != null && !uuid.equals("")) {
			for (int i = 0; i < uuid.length(); i += 4) {
				result += Integer.valueOf(uuid.substring(i, 4 + i), 16);
			}
		}
		return result.substring(startIndex, startIndex + length);
	}

	/**
	 * 获取流水号：日期+8位随机数
	 * @return
	 */
	public static String getBizCode() {
		String DAY_FORMAT = "yyyyMMddHHmmss";
		SimpleDateFormat sdf = new SimpleDateFormat(DAY_FORMAT);
		String s = sdf.format(new Date());

		String ranStr = getNumUUID(8);

		return s + ranStr;

	}

	public static void main(String[] args) {
		// 64933168342210204014500750923189230757
		// String uuid = "FDA50693A4E24FB1AFCFC6EB07647825";
		// System.out.println(uuid.length());
		// System.out.println(UUIDUtils.generateNumber(uuid, 0, 12));
		// System.out.println(uuid.substring(50, 51));
		/*
		 * System.out.println(UUIDUtils.getUUID(16));
		 * System.out.println(UUIDUtils.getNumUUID().length());
		 * System.out.println(UUIDUtils.getNumUUID(16));
		 */
		getBizCode();
	}
}
