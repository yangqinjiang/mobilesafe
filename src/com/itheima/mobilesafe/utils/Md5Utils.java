package com.itheima.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {
/**
 * 对文本进行md5加密
 * @param text
 * @return
 */
	public static String encode(String text){
		StringBuffer sb = new StringBuffer();
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			//加密后的结果
			byte[] result = digest.digest(text.getBytes());
			
			for(byte b:result){
				int number = b & 0xff;
				String hex=Integer.toHexString(number);
				if(hex.length() ==1){
					sb.append(0);
				}
				sb.append(hex);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return sb.toString();
				
	}
}
