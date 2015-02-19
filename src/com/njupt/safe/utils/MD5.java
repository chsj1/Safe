package com.njupt.safe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	public static String getData(String str){
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] data = digest.digest(str.getBytes());
			StringBuilder sb = new StringBuilder();
			for(int i = 0 ; i < data.length ; ++i){
				String result = Integer.toHexString(data[i] & 0xff);
				String temp = null;
				if(result.length() == 1){
					temp = "0" + result;
				}else{
					temp = result;
				}
				
				sb.append(temp);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
