package org.shoper.commons;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	// 全局数组
	private final static String[] strDigits = {"0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

	public MD5Util () {
	}

	// 返回形式为数字跟字符串
	private static String byteToArrayString (byte bByte) {
		int iRet = bByte;
		// System.out.println("iRet="+iRet);
		if (iRet < 0) {
			iRet += 256;
		}
		int iD1 = iRet / 16;
		int iD2 = iRet % 16;
		return strDigits[iD1] + strDigits[iD2];
	}

	// 返回形式只为数字
	public static String byteToNum (byte bByte) {
		int iRet = bByte;
		System.out.println("iRet1=" + iRet);
		if (iRet < 0) {
			iRet += 256;
		}
		return String.valueOf(iRet);
	}

	// 转换字节数组为16进制字串
	private static String byteToString (byte[] bByte) {
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < bByte.length; i++) {
			sBuffer.append(byteToArrayString(bByte[i]));
		}
		return sBuffer.toString();
	}

	public static String GetMD5Code (String strObj) {
		return byteToString(GetMD5(strObj));
	}

	public static byte[] GetMD5 (String strObj) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// md.digest() 该函数返回值为存放哈希值结果的byte数组
		return md.digest(strObj.getBytes());
	}
}
