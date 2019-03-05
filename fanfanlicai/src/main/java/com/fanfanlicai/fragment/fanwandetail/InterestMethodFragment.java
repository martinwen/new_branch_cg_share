package com.fanfanlicai.fragment.fanwandetail;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.fragment.BaseFragment;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.ConstantUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author lijinliu
 * @date 20180129
 * 计息方式
 */

public class InterestMethodFragment extends BaseFragment {

    private String mToken = "";
    private WebView mWebview;
    private ProgressBar mProgressBar;
    private String mUrl;
    private String mStrInfo;
    private TextView mTvInfo;
    //是否是直投
    private String mProductShowType;

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_interest, null);
        mTvInfo = (TextView) view.findViewById(R.id.tv_info);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        mProgressBar.setVisibility(View.GONE);
        mWebview = (WebView) view.findViewById(R.id.webview);
        return view;
    }

    @Override
    public void initData() {
        mTvInfo.setText(mStrInfo + "");
        if ("zt".equals(mProductShowType))
        {
            mUrl = ConstantUtils.INTERESTRATEMETHODZT_URL;
        } else {
            mUrl = ConstantUtils.INTERESTRATEMETHODFW_URL;
        }
        try {
            Map<String, String> map = SortRequestData.getmap();
            map.put("token", mToken);
            //直投开关
            String requestData = SortRequestData.sortString(map);
            String signData = SignUtil.sign(requestData);
            mUrl = mUrl + "?token=" + URLEncoder.encode(mToken, "UTF-8") + "&sign=" + signData;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        initwebview();
    }

    public void setStrMsg(String token, String msg, String projectShowType) {
        this.mToken = token;
        this.mStrInfo = msg;
        this.mProductShowType = projectShowType;
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    private void initwebview() {
        mWebview.clearCache(true);
        mWebview.clearHistory();

        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //与webview交互
        mWebview.addJavascriptInterface(new InterestMethodFragment.JavaScriptInterface(), "fanfan");
        //没有缓存（防止从第二个界面goback时闪现白屏现象）
        mWebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebview.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                mProgressBar.setVisibility(View.GONE);
                mWebview.setVisibility(View.GONE);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mProgressBar.setVisibility(View.GONE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
            }
        });
        mWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });

        mWebview.loadUrl(mUrl);
    }

    class JavaScriptInterface {
        JavaScriptInterface() {
        }

        @JavascriptInterface
        public void test() {
        }
    }
}
