package com.fanfanlicai.activity.setting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.activity.MainActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.DeviceUtil;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.view.dialog.CustomProgressDialog;

import java.net.URLEncoder;


public class SettingForCePingActivity extends BaseActivity{

    private String url;
    private CustomProgressDialog progressdialog;
    private WebView webview;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_for_ce_ping);

        progressdialog = new CustomProgressDialog(this, "");
        initView();
        initdata();
    }

    private void initView() {

        TextView invest_back = (TextView) findViewById(R.id.invest_back);
        invest_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != webview && webview.canGoBack()) {  //表示按返回键时的操作
                    webview.goBack();   //后退
                } else {
                    finish();
                }
            }
        });
        tv_title = (TextView) findViewById(R.id.tv_title);
        webview = (WebView) findViewById(R.id.webview);

    }

    private void initdata() {
        //清除cookie，防止两个用户用同一个手机登录参加活动，下一个用户依然显示上一个用户的信息
        CookieSyncManager.createInstance(this);
        CookieManager.getInstance().removeAllCookie();

        String token = CacheUtils.getString(this,"token", "");
        try {
            url = ConstantUtils.EVALUATION_URL;
            if (url.contains("?")) {
                url = url + "&clientType=5" + "&token=" + URLEncoder.encode(token, "UTF-8") + "&device_id=" + DeviceUtil.DEVICE_ID + "&system_type=android"
                        + "&system_version=" + DeviceUtil.OS_VERSION + "&app_version=" + DeviceUtil.APP_VERSIONNAME;
            } else {
                url = url + "?clientType=5" + "&token=" + URLEncoder.encode(token, "UTF-8") + "&device_id=" + DeviceUtil.DEVICE_ID + "&system_type=android"
                        + "&system_version=" + DeviceUtil.OS_VERSION + "&app_version=" + DeviceUtil.APP_VERSIONNAME;
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
        //没有缓存（防止从第二个界面goback时闪现白屏现象）
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);


        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webview.loadUrl(url);

                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                progressdialog.dismiss();
                webview.setVisibility(View.GONE);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressdialog.show();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressdialog.dismiss();
            }
        });
        webview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                tv_title.setText(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressdialog.show();
                super.onProgressChanged(view, newProgress);
            }
        });

        webview.loadUrl(url);
    }


    @Override
    public void onBackPressed() {//webview进入二级界面按物理返回键的处理
        if (null != webview && webview.canGoBack()) {  //表示按返回键时的操作
            webview.goBack();   //后退
        } else {
            finish();
        }
    }


    /**
     * 与webview 交互使用的
     *
     * @author lxn
     */
    class JavaScriptInterface {


        JavaScriptInterface() {
        }

        @JavascriptInterface
        public void gotoHome() {
            LogUtils.i("去首页");
            ConstantUtils.touziflag = 0;
            Intent intent = new Intent(SettingForCePingActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        @JavascriptInterface
        public void gotoInvest() {
            LogUtils.i("去投资页");
            ConstantUtils.touziflag = 1;
            Intent intent = new Intent(SettingForCePingActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
