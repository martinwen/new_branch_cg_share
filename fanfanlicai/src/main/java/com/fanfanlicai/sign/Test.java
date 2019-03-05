package com.fanfanlicai.sign;


public class Test {

	public static void main(String[] args) throws Exception {
		String aaa="device_id=C6CF9560-DF86-47DF-88F3-7EE3D1D25983&password=123abc&phone=13260131837";
		String sign = SignUtil.sign(aaa);
		
		System.out.println(sign);
		boolean b = SignUtil.verify(sign, null);
		System.out.println(b);
		
		/*String str = "爱钱帮爱钱帮爱钱帮爱钱帮爱钱帮爱钱帮爱钱帮爱钱帮爱钱帮";
		
		byte[] encrypt = SignUtil.encrypt(str);
		
		System.out.println("解密原文："+str);
		System.out.println("加密后："+new String(encrypt,"UTF-8"));
		
		byte[] decryption = SignUtil.decryption(encrypt);	
		System.out.println("解密后："+new String(decryption,"UTF-8"));*/
	}

}
