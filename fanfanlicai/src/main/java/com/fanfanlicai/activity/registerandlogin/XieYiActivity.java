package com.fanfanlicai.activity.registerandlogin;

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.utils.ConstantUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class XieYiActivity extends BaseActivity implements OnClickListener {
	
	private TextView xieyi_back;
	private WebView webview;
	private ProgressBar progressbar;
	String url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_xieyi);

		initView();
		initdata();
	}

	private void initView() {
		xieyi_back = (TextView) findViewById(R.id.xieyi_back);
		xieyi_back.setOnClickListener(this);
		webview = (WebView) findViewById(R.id.webview);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
	}

	private void initdata() {
		url = ConstantUtils.PROTOCOL_URL;
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
		});
		webview.loadUrl(url);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.xieyi_back:
			finish();
			break;

		default:
			break;
		}
	}
}
