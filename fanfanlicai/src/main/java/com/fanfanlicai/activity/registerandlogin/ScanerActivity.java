package com.fanfanlicai.activity.registerandlogin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.covics.zxingscanner.OnDecodeCompletionListener;
import com.covics.zxingscanner.ScannerView;
import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.fanfanlicai.R;

/**
 * 二维码扫描页面
 * 
 * @author 蔡有飞 E-mail: caiyoufei@looip.cn
 * @version 创建时间：2014-2-10 下午5:31:40
 * 
 */
public class ScanerActivity extends BaseActivity implements
		OnDecodeCompletionListener {
	private ScannerView cScannerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scaner);
		init();
	}

	/**
	 * 初始化
	 * 
	 * @version 更新时间：2014-2-10 下午5:40:59
	 */
	private void init() {
		cScannerView = (ScannerView) findViewById(R.id.scanner_view);
		cScannerView.setOnDecodeListener(this);
	}

	/**
	 * 扫描结果
	 * 
	 * @version 更新时间：2014-2-10 下午5:44:11
	 * @param barcodeFormat
	 * @param barcode
	 * @param bitmap
	 */
	@Override
	public void onDecodeCompletion(String barcodeFormat, String barcode,
			Bitmap bitmap) {
		if (barcode != null) {
			Intent intent = new Intent();
			intent.putExtra("result", barcode);
			setResult(0x02, intent);
			this.finish();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		cScannerView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		cScannerView.onPause();
	}
}
