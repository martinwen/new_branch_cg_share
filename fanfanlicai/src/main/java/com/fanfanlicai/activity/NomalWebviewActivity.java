package com.fanfanlicai.activity;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.fanfanlicai.fanfanlicai.R;

/**
 * @author lijinliu
 * @date 20180206
 * 打开网页-获取网页标题
 */
public class NomalWebviewActivity extends BaseActivity implements OnClickListener {
    // 返回
    private TextView mTvBtnBack;
    private WebView webview;
    private ProgressBar progressbar;
    private TextView mTvTtile;
    private String mStrTitle;
    private String mStrUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_webview);

        initView();
        initData();
    }

    private void initView() {
        mTvBtnBack = (TextView) findViewById(R.id.back);
        mTvBtnBack.setOnClickListener(this);
        webview = (WebView) findViewById(R.id.webview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        mTvTtile = (TextView) findViewById(R.id.tv_title);
    }

    private void initData() {
        mStrTitle = getIntent().getStringExtra("title");
        mStrUrl = getIntent().getStringExtra("url");
        //LogUtils.i("url=="+mStrUrl);
        mTvTtile.setText(mStrTitle);
        initwebview();
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
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
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                progressbar.setVisibility(View.GONE);
                webview.setVisibility(View.GONE);
                super.onReceivedError(view, errorCode, description, failingUrl);
                Toast.makeText(NomalWebviewActivity.this, "网络出错，请稍后重试！", Toast.LENGTH_SHORT).show();
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
                mTvTtile.setText(view.getTitle());
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
            }

        });
        webview.loadUrl(mStrUrl);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                if (null != webview && webview.canGoBack()) {
                    webview.goBack();
                } else {
                    finish();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (null != webview && webview.canGoBack()) {
            webview.goBack();
        } else {
            finish();
        }
    }
}
