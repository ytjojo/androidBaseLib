package com.kerkr.edu.security;


public class SecurityUtils {
	private static final String DES_KEY = "sundayso19880907";// key 必须为8或者8的整数倍
	/*private static final String NET_DES_KEY = "sundayso19880907";// key 必须为8或者8的整数倍*/
	private static final String NET_DES_KEY = "12345678";// key 必须为8或者8的整数倍
	/**
	 * 二维码加密方法
	 * 
	 * 
	 */
	public static String QRencrypt(String content) {
		String result = null;
		try {
			result = DESUtils.encode(DES_KEY, content);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 二维码解密密方法
	 * 
	 * 
	 */
	public static String QRdeciphering(String result) {
		String key = DESUtils.decodeValue(DES_KEY, result);
		return key;
	}
	
	public static String NetEncrypt(String uid,String did){
		String value = "";
		value = "?uid="+uid+"&did="+did;
		try { 
			return DESUtils.encode(NET_DES_KEY, value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	


}
