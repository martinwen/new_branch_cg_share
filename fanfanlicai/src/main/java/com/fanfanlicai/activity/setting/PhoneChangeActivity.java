package com.fanfanlicai.activity.setting;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.fanfanlicai.R;

public class PhoneChangeActivity extends BaseActivity implements View.OnClickListener {

    private TextView bankchange_back;
    private WebView webview;
    private ProgressBar progressbar;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_change);
        initView();
        initData();
    }

    private void initView() {
        bankchange_back = (TextView) findViewById(R.id.bankchange_back);
        bankchange_back.setOnClickListener(this);
        webview = (WebView) findViewById(R.id.webview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
    }

    private void initData() {
//        url = ConstantUtils.CARDREPLACE_URL;
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
            case R.id.bankchange_back:
                finish();
                break;
            default:
                break;
        }
    }
}
