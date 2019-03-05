package com.fanfanlicai.activity.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.activity.MainActivity;
import com.fanfanlicai.activity.my.HongBaoActivity;
import com.fanfanlicai.activity.my.JiaXiPiaoActivity;
import com.fanfanlicai.activity.my.TiXianPiaoActivity;
import com.fanfanlicai.activity.registerandlogin.HomeLoginActivity;
import com.fanfanlicai.activity.registerandlogin.HomeRegisterActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.DeviceUtil;
import com.fanfanlicai.utils.LogUtils;

import java.net.URLEncoder;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class BannerWebActivity extends BaseActivity {
	
	private TextView banner_back;
	private TextView banner_title;
	private WebView webview;
	private ProgressBar progressbar;
	String url;
	String title;
	private String inviteCode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bannerweb);

		initView();
		initdata();
	}

	private void initView() {
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		banner_back = (TextView) findViewById(R.id.banner_back);
		banner_title = (TextView) findViewById(R.id.banner_title);
		webview = (WebView) findViewById(R.id.webview);
		
	}

	private void initdata()  {
		//清除cookie，防止两个用户用同一个手机登录参加活动，下一个用户依然显示上一个用户的信息
		CookieSyncManager.createInstance(BannerWebActivity.this);
		CookieManager.getInstance().removeAllCookie();

		inviteCode = CacheUtils.getString(this, CacheUtils.INVITECODE, "");
		String token = CacheUtils.getString(BannerWebActivity.this, "token","");
		
		try {
			url = getIntent().getStringExtra("url");
			if (url.contains("?")) {
				url = url+"&clientType=5" + "&token="+URLEncoder.encode(token, "UTF-8")+
				"&device_id="+DeviceUtil.DEVICE_ID+"&system_type=android"+"&system_version="+
				DeviceUtil.OS_VERSION+"&app_version="+DeviceUtil.APP_VERSIONNAME;
			}else {
				url = url+"?clientType=5" + "&token="+URLEncoder.encode(token, "UTF-8")+
				"&device_id="+DeviceUtil.DEVICE_ID+"&system_type=android"+"&system_version="+
				DeviceUtil.OS_VERSION+"&app_version="+DeviceUtil.APP_VERSIONNAME;
			}
			LogUtils.i("url拼完=="+url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		title = getIntent().getStringExtra("title");
		banner_title.setText(title);
		banner_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (null!=webview&&webview.canGoBack()) {  //表示按返回键时的操作
					webview.goBack();   //后退
				}else{
					//为了防止有些用户在活动中点击登录后返回，此时ConstantUtils.loginflag = 1，这样登录成功后
					//手势密码设置完成后返回到首页不会刷新数据，需要把ConstantUtils.loginflag=0设置在此处
					ConstantUtils.loginflag=0;
					Intent intent=new Intent(BannerWebActivity.this,MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
					startActivity(intent);
					finish();
				}

			}
		});
		
		
		initwebview();
	}


	private void showShare(String shareUrl,String shareTitle,String shareDesc,String shareLogo) {
		
		int end = shareUrl.indexOf("?");
		String subUrl = shareUrl.substring(0, end);
		subUrl = subUrl + "?ukey=" + inviteCode + "&type=userInvite";
		
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		//关闭sso授权
		oks.disableSSOWhenAuthorize(); 
		//分享的标题(微信)
		oks.setTitle(shareTitle);
		
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		 oks.setTitleUrl(subUrl);
		 
		// text是分享文本，所有平台都需要这个字段
		oks.setText(shareDesc);
		
		oks.setImageUrl(shareLogo);

		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(subUrl);
		
		// 启动分享GUI
		oks.show(this);
	}

	private void showShareChristmas(String shareUrl,String shareTitle,String shareDesc,String shareLogo,String groupId) {

		int end = shareUrl.indexOf("?");
		String subUrl = shareUrl.substring(0, end);
		subUrl = subUrl + "?ukey=" + inviteCode + "&type=userInvite"+ "&groupId="+ groupId;

		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		//关闭sso授权
		oks.disableSSOWhenAuthorize();
		//分享的标题(微信)
		oks.setTitle(shareTitle);

		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		 oks.setTitleUrl(subUrl);

		// text是分享文本，所有平台都需要这个字段
		oks.setText(shareDesc);

		oks.setImageUrl(shareLogo);

		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(subUrl);

		// 启动分享GUI
		oks.show(this);
	}

	private void showShareAddRice(String shareUrl,String shareTitle,String shareDesc,String shareLogo,String activityCode) {

		int end = shareUrl.indexOf("?");
		String subUrl = shareUrl.substring(0, end);
		subUrl = subUrl + "?ukey=" + inviteCode + "&type=userInvite"+ "&activityCode="+ activityCode;
		LogUtils.i("subUrl=="+subUrl);

		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		//关闭sso授权
		oks.disableSSOWhenAuthorize();
		//分享的标题(微信)
		oks.setTitle(shareTitle);

		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(subUrl);

		// text是分享文本，所有平台都需要这个字段
		oks.setText(shareDesc);

		oks.setImageUrl(shareLogo);

		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(subUrl);

		// 启动分享GUI
		oks.show(this);
	}

	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	private void initwebview() {
		
		webview.clearCache(true);
		webview.clearHistory();
		
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		//与webview交互
		webview.addJavascriptInterface(new JavaScriptInterface(), "fanfan");
		//没有缓存（防止从第二个界面goback时闪现白屏现象）
		webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);


		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
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
	
	/**
	 * 与webview 交互使用的
	 *
	 * @author lxn
	 *
	 */
	class JavaScriptInterface {


		JavaScriptInterface() {
		}

		@JavascriptInterface
		public void register() {
			LogUtils.i("从活动注册");
			//定义一个flag，用于标记这个登录是活动页面中的登录，与正常登录区分
			ConstantUtils.loginflag = 1;
			Intent intent = new Intent(BannerWebActivity.this,HomeRegisterActivity.class);
			startActivity(intent);
		}
		@JavascriptInterface
		public void login() {
			LogUtils.i("从活动登录");
			//定义一个flag，用于标记这个登录是活动页面中的登录，与正常登录区分
			ConstantUtils.loginflag = 1;
			Intent intent = new Intent(BannerWebActivity.this,HomeLoginActivity.class);
			startActivity(intent);
		}
		@JavascriptInterface
		public void fhinvest() {
			LogUtils.i("从活动进入饭盒投资");
			ConstantUtils.touziflag = 1;
			Intent intent = new Intent(BannerWebActivity.this,MainActivity.class);
			startActivity(intent);
			finish();
		}
		@JavascriptInterface
		public void fwinvest() {
			LogUtils.i("从活动进入饭碗投资");
			ConstantUtils.touziflag = 1;
			ConstantUtils.fanheorfanwanorfantongflag = 1;
			Intent intent = new Intent(BannerWebActivity.this,MainActivity.class);
			startActivity(intent);
			finish();
		}
		@JavascriptInterface
		public void share(String shareUrl,String shareTitle,String shareDesc,String shareLogo) {
			LogUtils.i("从后台拿到的活动分享数据===="+shareUrl+"==="+shareTitle+"==="+shareDesc+"==="+shareLogo);
			if (shareUrl!=null&&shareTitle!=null&&shareDesc!=null&&shareLogo!=null) {
				showShare(shareUrl,shareTitle,shareDesc,shareLogo);
			}
		}
		@JavascriptInterface
		public void shareChristmas(String shareUrl,String shareTitle,String shareDesc,String shareLogo,String groupId) {
			LogUtils.i("从后台拿到的活动分享数据====shareUrl:"+shareUrl+"===shareTitle:"+shareTitle+"===shareDesc:"+shareDesc+"===shareLogo:"+shareLogo+"===groupId:"+groupId);
			if (shareUrl!=null&&shareTitle!=null&&shareDesc!=null&&shareLogo!=null&&groupId!=null) {
				showShareChristmas(shareUrl,shareTitle,shareDesc,shareLogo,groupId);
			}
		}
		@JavascriptInterface
		public void shareAddRice(String shareUrl,String shareTitle,String shareDesc,String shareLogo,String activityCode) {
			LogUtils.i("从后台拿到的活动分享数据==shareUrl:"+shareUrl+"=shareTitle:"+shareTitle+"=shareDesc:"+shareDesc+"=shareLogo:"+shareLogo+"=activityCode:"+activityCode);
			if (shareUrl!=null&&shareTitle!=null&&shareDesc!=null&&shareLogo!=null&&activityCode!=null) {
				showShareAddRice(shareUrl,shareTitle,shareDesc,shareLogo,activityCode);
			}
		}
		@JavascriptInterface
		public void gotojiaxipiao(String code) {
			LogUtils.i("从活动进入加息票界面");
			Intent intent = new Intent(BannerWebActivity.this,JiaXiPiaoActivity.class);
			intent.putExtra("code", code);
			startActivity(intent);
		}
		@JavascriptInterface
		public void gotohongbao(String code) {
			LogUtils.i("从活动进入红包界面");
			Intent intent = new Intent(BannerWebActivity.this,HongBaoActivity.class);
			intent.putExtra("code", code);
			startActivity(intent);
		}
		@JavascriptInterface
		public void gototixianpiao(String code) {
			LogUtils.i("从活动进入提现票界面");
			Intent intent = new Intent(BannerWebActivity.this,TiXianPiaoActivity.class);
			intent.putExtra("code", code);
			startActivity(intent);
		}
		@JavascriptInterface
		public void gotoIndex() {
			LogUtils.i("从活动进入首页");
			//设置标记，其他情况无论在那个界面停留时点击通知栏都进入到首页
			ConstantUtils.touziflag = 0;
			Intent intent = new Intent(BannerWebActivity.this,MainActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
	@Override
	public void onResume() {//登录后回来需要刷新界面
		// TODO Auto-generated method stub
		super.onResume();
		initdata();
	}
	
	@Override
	public void onBackPressed() {//登录后按返回键需要刷新数据
		if (null!=webview&&webview.canGoBack()) {  //表示按返回键时的操作
			webview.goBack();   //后退
		}else{
			Intent intent=new Intent(this,MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
			startActivity(intent);
			finish();
		}

	}
	
}
