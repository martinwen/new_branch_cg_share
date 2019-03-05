package com.fanfanlicai.sign;

import java.security.Signature;

import javax.crypto.Cipher;

import com.fanfanlicai.utils.MD5;


/**
 * 签名工具类
 * 
 * @author jack
 *
 */
public class SignUtil {


	/**
	 * 签名算法
	 */
	public static final String SIGN_ALGORITHMS = "MD5WithRSA";

	public SignUtil() {

	}

	/**
	 * 获取签名
	 * 
	 * @param plain
	 *            约定口令 可为空
	 * @return
	 * @throws Exception
	 */
	public static String sign(String plain){
		
		String sign = null;
		try {
			Signature sig = Signature.getInstance(SIGN_ALGORITHMS);
			sig.initSign(PkCertFactory.getPrivateKey());
			sig.update((null == plain ? "" : plain).getBytes("UTF-8"));
			byte signData[] = sig.sign();
		    sign = new String(Base64.encode(signData));
		} catch (Exception e) {
			e.getMessage();
		}
		return sign;
	}

	/**
	 * 签名校验
	 * 
	 * @param sign
	 *            签名
	 * @param plain
	 *            约定口令 可为空
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(String sign, String plain){
		java.security.cert.X509Certificate cert = PkCertFactory.getCert();

		boolean b = false;
		try {
			plain= MD5.getMd5(plain);
			byte signData[] = Base64.decode(sign);
			Signature sig = Signature.getInstance(SIGN_ALGORITHMS);
			sig.initVerify(cert);
			sig.update((null == plain ? "" : plain).getBytes("UTF-8"));
		    b = sig.verify(signData);
		} catch (Exception e) {
			e.getMessage();
		}
		return b;
	}
	
	/**
	 * 加密
	 * @param plainText
	 * @return
	 */
	public static byte[] encrypt(String plainText){
		
		byte[] newPlainText =  null;
		 //获得一个RSA的Cipher类，使用公钥加密    
	       try {
	    	   
	    	byte[] cipherText = plainText.getBytes("UTF-8");

			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");//RSA/ECB/PKCS1Padding
			
			cipher.init(Cipher.ENCRYPT_MODE, PkCertFactory.getCert());
			
			 //用公钥进行加密，返回一个字节流    
			 newPlainText = cipher.doFinal(cipherText);
			
			  //以UTF8格式把字节流转化为String    
			plainText = new String(newPlainText, "UTF-8");    //, "UTF8"
		   
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newPlainText; 
	}
	
    /**
     * 解密
     * @param plainText
     * @return
     */
	public static byte[] decryption(byte[] cipherText){
		
		byte[] newPlainText = null;
		
		try {
//			byte[] cipherText = plainText.getBytes("UTF-8");
			
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");//RSA/ECB/PKCS1Padding
			
			 cipher.init(Cipher.DECRYPT_MODE, PkCertFactory.getPrivateKey());    
			  
			  
		       //用私钥进行解密，返回一个字节流    
		       newPlainText = cipher.doFinal(cipherText);    
		  
//		       plainText = new String(newPlainText,"UTF-8");
		} catch (Exception e) {
			e.getStackTrace();
		}
		return newPlainText;
		
	}
}
