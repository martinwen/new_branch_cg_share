package com.fanfanlicai.sign;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.global.FanFanApplication;

/**
 * 
 * @author jack
 *
 */
public class ProFileUtil {


	public ProFileUtil() {

	}

	/**
	 * 获取文件内容
	 * 
	 * @param pro
	 * @return
	 * @throws Exception
	 */
	public static String getFileStr() throws Exception {
		
		InputStream in;
		in = null;
		//SH测试环境的证书
		//in = FanFanApplication.context.getResources().openRawResource(R.raw.app_private);
		//测试环境的证书
		in = FanFanApplication.context.getResources().openRawResource(R.raw.app_ff_pkcs8_test);
		//正式环境的证书
		//in = FanFanApplication.context.getResources().openRawResource(R.raw.app_ff_pkcs8);

		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(in));
			String readLine = null;

			while ((readLine = br.readLine()) != null) {
				if(readLine.startsWith("--")){
					continue;
				}else{
					sb.append(readLine);
				}
			}
			br.close();
		} catch (IOException e) {
			throw new Exception("私钥数据读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥输入流为空");
		} finally {
			if (null != in)
				in.close();

			if (null != br)
				br.close();
		}
		return sb.toString();
	}

	/**
	 * 读取raw文件里证书的字节
	 * @param pro
	 * @return
	 * @throws IOException
	 */
	public static byte[] getFileByte() throws IOException {
		byte b[];
		InputStream in;
		b = null;
		in = null;
		//SH测试环境的证书
		//in = FanFanApplication.context.getResources().openRawResource(R.raw.service_rsacert);
		//测试环境的证书
		in = FanFanApplication.context.getResources().openRawResource(R.raw.ff_app_public_test);
		//正式环境的证书
		//in = FanFanApplication.context.getResources().openRawResource(R.raw.ff_app_public);
		b = new byte[20480];
		in.read(b);
		if (null != in)
			in.close();
		return b;
	}
}
