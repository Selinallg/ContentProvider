package com.nolovr.nolohome.statistics.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DES {
	private static final String T = "0";
	private static byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };

	public static String encrypt(String message, String key) throws Exception {  
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");  
  
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));  
  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES"); 
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);  
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));  
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);  
  
        return toHexString(cipher.doFinal(message.getBytes("UTF-8")));  
    }  
  
	public static String decrypt(String message, String key) throws Exception {  
		  
        byte[] bytesrc = convertHexString(message);  
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");  
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);  
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));  
  
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);  
  
        byte[] retByte = cipher.doFinal(bytesrc);  
        return new String(retByte);  
    } 
	public static String toHexString(byte b[]) {  
	        StringBuffer hexString = new StringBuffer();  
	        for (int i = 0; i < b.length; i++) {  
	            String plainText = Integer.toHexString(0xff & b[i]);  
	            if (plainText.length() < 2)  
	                plainText = "0" + plainText;  
	            hexString.append(plainText);  
	        }  
	  
	        return hexString.toString();  
	    } 
	
	 
	
	 public static byte[] convertHexString(String ss) {  
	        byte digest[] = new byte[ss.length() / 2];  
	        for (int i = 0; i < digest.length; i++) {  
	            String byteString = ss.substring(2 * i, 2 * i + 2);  
	            int byteValue = Integer.parseInt(byteString, 16);  
	            digest[i] = (byte) byteValue;  
	        }  
	  
	        return digest;  
	    }  


	
	public static void main(String[] args) {
		String digest="de39658c5850a3023f835928dae7f028554eb091b1172bdcb43c8e6919bb01310796b62bef3bd4265ad673ae4d3968f0";
		try {
			String key = "ae8d%13&";
			String str = "{\"channelCode\":\"001\",\"transType\":\"1001\",\"messageId\":\"20170531170556\",\"channelUserId\":\"0123x1\",\"userName\":\"whpgary\",\"storeId\":\"034q12\"}";
			System.out.println("key      :" + key);
			System.out.println("待加密文本:" + str);
			String d = DES.encrypt(str, key);
			System.out.println("加密后          :" + d);
		//	System.out.println("解密后          :" + DES.decryptDES(d, key));
			//System.out.println();

			
	//	System.out.println(DES.decryptDES(digest, key));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}