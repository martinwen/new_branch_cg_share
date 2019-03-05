package com.fanfanlicai.activity.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.utils.LogUtils;


/**
 * 确认投资完成之后进入徽商密码输入的网页
 *
 * @author lxn
 */
public class CGAuthenticationActivity extends BaseActivity {

    private WebView webview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cgauthentication);


        initView();
        initData();

    }

    private void initView() {
        TextView bank_back = (TextView) findViewById(R.id.bank_back);
        bank_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        webview = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new JavaScriptInterface(), "bankCard");

    }

    private void initData() {
        final String result = getIntent().getStringExtra("result");
        LogUtils.i("result=" + result);


        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                LogUtils.i("加载=onPageFinished===="+url);
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                LogUtils.i("加载==onPageStarted==="+url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtils.i("加载===shouldOverrideUrlLoading=="+url);
                webview.loadUrl(url);
                return true;
            }
        });
        webview.loadDataWithBaseURL(null, result, "text/html", "utf-8", null);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 按下的如果是BACK，同时没有重复
//            if (null!=webview&&webview.canGoBack()) {  //表示按返回键时的操作
//                webview.goBack();   //后退
//            }else{
                finish();
//            }
            // true 不传递 false 传递
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
        public void handleBindCardResult(String result) {//2取消  1成功  0失败
            LogUtils.i("result====" + result);

            if("2".equals(result)){
                finish();
            }else{
                Intent intent = new Intent(CGAuthenticationActivity.this, CGBankQianYueResultActivity.class);
                intent.putExtra("result", result);
                startActivity(intent);
                finish();
            }

        }
    }
}
