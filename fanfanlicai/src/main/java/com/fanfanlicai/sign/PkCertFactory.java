package com.fanfanlicai.sign;

import java.io.ByteArrayInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;

import android.util.Log;

/**
 * 
 * @author jack
 *
 */
public class PkCertFactory {

	private static X509Certificate umpCert;

	public PkCertFactory() {
	}

	static {
		try {
			byte b[] = ProFileUtil.getFileByte();
			umpCert = getCert(b);
		} catch (Exception ex) {
			RuntimeException rex = new RuntimeException(ex.getMessage());
			rex.setStackTrace(ex.getStackTrace());
			throw rex;
		}
	}

	/**
	 * 获取签名私钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey() throws Exception {
		String privateStr = ProFileUtil.getFileStr();
//		Log.i("lixiaonan", "这次privateStr==="+privateStr);
		PKCS8EncodedKeySpec peks = new PKCS8EncodedKeySpec(
				Base64.decode(privateStr));
//		Log.i("lixiaonan", "peks==="+peks);
		PrivateKey pk;
		KeyFactory kf = KeyFactory.getInstance("RSA");
		//KeyFactory kf = KeyFactory.getInstance("RSA", "BC");
		pk = kf.generatePrivate(peks);
//		Log.i("lixiaonan", "pk==="+pk);
		return pk;
	}

	/**
	 * 获取证书文件
	 * 
	 * @return
	 */
	public static X509Certificate getCert() {
		return umpCert;
	}

	private static X509Certificate getCert(byte b[]) throws Exception {
		X509Certificate x509Certificate;
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		x509Certificate = (X509Certificate) cf.generateCertificate(bais);
		return x509Certificate;
	}
}
