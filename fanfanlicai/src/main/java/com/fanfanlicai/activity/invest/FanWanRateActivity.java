
package com.fanfanlicai.activity.invest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.global.FanFanApplication;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class FanWanRateActivity extends BaseActivity implements OnClickListener {

	private TextView get_back;// 返回
	private TextView tv_title;
	private WebView webview;
	private ProgressBar progressbar;
	String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fanwanrate);

		initView();
		initData();
	}

	private void initView() {
		get_back = (TextView) findViewById(R.id.get_back);// 返回
		get_back.setOnClickListener(this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		webview = (WebView) findViewById(R.id.webview);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);

	}

	private void initData() {
		Intent intent = getIntent();
		String token = intent.getStringExtra("token");
		String investDays = intent.getStringExtra("investDays");
		String proCode = intent.getStringExtra("proCode");
		try {
			url = ConstantUtils.FWRATE_URL+"?token="+ URLEncoder.encode(token, "UTF-8")+"&investDeadline="+investDays
					+"&productType="+proCode+"&actFwInterestId="+FanFanApplication.jiaxi_ID+"&clientType=5";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		LogUtils.i("url=="+url);
		initwebview();
	}

	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	private void initwebview() {
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		//开启本地dom缓存
		webSettings.setDomStorageEnabled(true);
		

		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				webview.loadUrl(url);

				return true;
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
										String description, String failingUrl) {
				progressbar.setVisibility(View.GONE);
				webview.setVisibility(View.GONE);
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				progressbar.setVisibility(View.VISIBLE);
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				progressbar.setVisibility(View.GONE);
			}
		});
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				progressbar.setProgress(newProgress);
				super.onProgressChanged(view, newProgress);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				//设置标题
				tv_title.setText(title);
			}
		});
		webview.loadUrl(url);
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_back:
			finish();
			break;
		}
	}
}
