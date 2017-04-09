package com.briefsuits.httptools.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MD5Util {

	private static ThreadLocal<MessageDigest> messageDigestHolder = new ThreadLocal<MessageDigest>();

	static Logger log = LoggerFactory.getLogger(MD5Util.class);

	// 用来将字节转换成 16 进制表示的字符
	static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	static {
		try {
			MessageDigest message = java.security.MessageDigest.getInstance("MD5");
			messageDigestHolder.set(message);
		} catch (NoSuchAlgorithmException e) {
			log.error("初始化java.security.MessageDigest失败:" + StackTraceUtil.getStackTrace(e), e);
		}
	}

	/***
	 * 
	 * @Title: getMD5Format
	 * @Description: 计算MD5并转换为32字节明文显示串
	 * @author wujl
	 * @param data
	 * @return String 返回类型
	 */
	public static String getMD5Format(String data) {
		try {
			MessageDigest message = messageDigestHolder.get();
			if (message == null) {
				message = java.security.MessageDigest.getInstance("MD5");
				messageDigestHolder.set(message);
			}
			message.update(data.getBytes());
			byte[] b = message.digest();

			String digestHexStr = "";
			for (int i = 0; i < 16; i++) {
				digestHexStr += byteHEX(b[i]);
			}

			return digestHexStr;
		} catch (Exception e) {
			log.error("MD5格式化时发生异常[{}]: {}", data, StackTraceUtil.getStackTrace(e));
			return null;
		}
	}

	public static String getMD5Format(byte[] data) {
		try {
			MessageDigest message = messageDigestHolder.get();
			if (message == null) {
				message = java.security.MessageDigest.getInstance("MD5");
				messageDigestHolder.set(message);
			}

			message.update(data);
			byte[] b = message.digest();

			String digestHexStr = "";
			for (int i = 0; i < 16; i++) {
				digestHexStr += byteHEX(b[i]);
			}

			return digestHexStr;
		} catch (Exception e) {
			return null;
		}
	}

	/***
	 * 
	 * @Title: byteHEX
	 * @Description:
	 * @author wujl
	 * @param ib
	 * @return String 返回类型
	 */
	private static String byteHEX(byte ib) {
		char[] ob = new char[2];
		ob[0] = hexDigits[(ib >>> 4) & 0X0F];
		ob[1] = hexDigits[ib & 0X0F];
		String s = new String(ob);
		return s;
	}
}
