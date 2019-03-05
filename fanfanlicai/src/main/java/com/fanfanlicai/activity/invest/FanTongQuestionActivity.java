
package com.fanfanlicai.activity.invest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.activity.MainActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.DeviceUtil;
import com.fanfanlicai.utils.LogUtils;

import java.net.URLEncoder;

public class FanTongQuestionActivity extends BaseActivity implements OnClickListener {

	private TextView tv_title;// 标题
	private TextView get_back;// 返回
	private WebView webview;
	private ProgressBar progressbar;
	private String minBookAmount;
	private String maxBookAmount;
	private String minInvestAmount;
	String url;
	private double baseRate;
	private double addRate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fantongquestion);

		initView();
		initData();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.tv_title);// 标题
		get_back = (TextView) findViewById(R.id.get_back);// 返回
		get_back.setOnClickListener(this);
		webview = (WebView) findViewById(R.id.webview);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
	}

	private void initData() {
		Intent intent = getIntent();
		minBookAmount = intent.getStringExtra("minBookAmount");
		maxBookAmount = intent.getStringExtra("maxBookAmount");
		minInvestAmount = intent.getStringExtra("minInvestAmount");
		baseRate = intent.getDoubleExtra("baseRate", 0);
		addRate = intent.getDoubleExtra("addRate", 0);

		String token = CacheUtils.getString(this, "token", null);
		url = ConstantUtils.TOANSWERPAGE_URL;
		try {
			if (url.contains("?")) {
				url = url + "&clientType=5" + "&token=" + URLEncoder.encode(token, "UTF-8") +
						"&device_id=" + DeviceUtil.DEVICE_ID + "&system_type=android" + "&system_version=" +
						DeviceUtil.OS_VERSION + "&app_version=" + DeviceUtil.APP_VERSIONNAME;
			} else {
				url = url + "?clientType=5" + "&token=" + URLEncoder.encode(token, "UTF-8") +
						"&device_id=" + DeviceUtil.DEVICE_ID + "&system_type=android" + "&system_version=" +
						DeviceUtil.OS_VERSION + "&app_version=" + DeviceUtil.APP_VERSIONNAME;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		initwebview();

	}

	@SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
	private void initwebview() {
		webview.clearCache(true);
		webview.clearHistory();

		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		//与webview交互
		webview.addJavascriptInterface(new JavaScriptInterface(), "fanfan");
		//开启本地dom缓存
		webSettings.setDomStorageEnabled(false);
		webSettings.setAppCacheEnabled(false);


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
			/**
			 * 网页加载标题回调
			 * @param view
			 * @param title 网页标题
			 */
			@Override
			public void onReceivedTitle(WebView view, String title) {
				tv_title.setText(title);
			}



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
			case R.id.get_back:
				finish();
				break;
			default:
				break;
		}
	}

	class JavaScriptInterface {


		JavaScriptInterface() {
		}

		@JavascriptInterface
		public void yuyue() {
			LogUtils.i("回答正确进入到饭桶预约界面");
			Intent intent = new Intent(FanTongQuestionActivity.this, FanTongYuYueActivity.class);
			intent.putExtra("minBookAmount", minBookAmount);
			intent.putExtra("maxBookAmount", maxBookAmount);
			intent.putExtra("minInvestAmount", minInvestAmount);
			intent.putExtra("baseRate", baseRate);
			intent.putExtra("addRate", addRate);
			startActivity(intent);
			finish();
		}

		@JavascriptInterface
		public void ftinvest() {
			LogUtils.i("三次答题机会用完了就会调用此方法，跳到首页");
			Intent intent = new Intent(FanTongQuestionActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
