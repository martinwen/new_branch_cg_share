package com.fanfanlicai.activity.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
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

import com.fanfanlicai.activity.MainActivity;
import com.fanfanlicai.activity.registerandlogin.HomeLoginActivity;
import com.fanfanlicai.activity.registerandlogin.HomeRegisterActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.DeviceUtil;
import com.fanfanlicai.utils.LogUtils;

import java.net.URLEncoder;



/**
 * 发现模块的网页url
 *
 * @author lxn
 */
public class FanJuActivity extends FragmentActivity {

    private TextView back;
    private TextView tv_title;
    private WebView webview;
    private ProgressBar progressbar;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_webview);

        initView();
        initdata();
    }

    private void initView() {
        back = (TextView) findViewById(R.id.back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        webview = (WebView) findViewById(R.id.webview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null!=webview&&webview.canGoBack()) {  //表示按返回键时的操作
                    webview.goBack();   //后退
                }else{
                    finish();
                }
            }
        });

    }

    public void initdata() {

        //清除cookie，防止两个用户用同一个手机登录参加活动，下一个用户依然显示上一个用户的信息
        CookieSyncManager.createInstance(FanJuActivity.this);
        CookieManager.getInstance().removeAllCookie();

        String token = CacheUtils.getString(this,"token", "");
        try {
            url = ConstantUtils.DAYDINNERINDEX_URL;

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
        LogUtils.i("url=="+url);
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

    /**
     * 与webview 交互使用的
     *
     * @author lxn
     */
    class JavaScriptInterface {


        JavaScriptInterface() {
        }

        @JavascriptInterface
        public void register() {
            LogUtils.i("从活动注册");
            //定义一个flag，用于标记这个登录是活动页面中的登录，与正常登录区分
            ConstantUtils.loginflag = 1;
            Intent intent = new Intent(FanJuActivity.this,HomeRegisterActivity.class);
            startActivity(intent);
        }
        @JavascriptInterface
        public void login() {
            LogUtils.i("从活动登录");
            //定义一个flag，用于标记这个登录是活动页面中的登录，与正常登录区分
            ConstantUtils.loginflag = 1;
            Intent intent = new Intent(FanJuActivity.this,HomeLoginActivity.class);
            startActivity(intent);
        }

        @JavascriptInterface
        public void fhinvest() {
            LogUtils.i("从活动进入饭盒投资");
            ConstantUtils.touziflag = 1;
            Intent intent = new Intent(FanJuActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
            startActivity(intent);
        }
        @JavascriptInterface
        public void fwinvest() {
            LogUtils.i("从活动进入饭碗投资");
            ConstantUtils.touziflag = 1;
            ConstantUtils.fanheorfanwanorfantongflag = 1;
            Intent intent = new Intent(FanJuActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
            startActivity(intent);
        }

        @JavascriptInterface
        public void shareFanJu(String shareUrl, String shareTitle, String shareDesc, String shareLogo) {
            LogUtils.i("从后台拿到的活动分享数据====" + shareUrl + "===" + shareTitle + "===" + shareDesc + "===" + shareLogo);
            if (shareUrl != null && shareTitle != null && shareDesc != null && shareLogo != null) {

                FragmentManager fm = getSupportFragmentManager();
                ShareFragFanJu shareFragFanJu = new ShareFragFanJu();
                shareFragFanJu.show(fm, null);
                shareFragFanJu.setData(shareUrl, shareTitle, shareLogo, shareDesc);
            }
        }

        @JavascriptInterface
        public void gotoIndex() {
            LogUtils.i("从活动进入首页");
            //设置标记，其他情况无论在那个界面停留时点击通知栏都进入到首页
//            ConstantUtils.touziflag = 0;
            Intent intent = new Intent(FanJuActivity.this, MainActivity.class);
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
    public void onBackPressed() {//webview进入二级界面按物理返回键的处理
        if (null != webview && webview.canGoBack()) {  //表示按返回键时的操作
            webview.goBack();   //后退
        } else {
            finish();
        }
    }
}
