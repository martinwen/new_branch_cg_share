package com.fanfanlicai.fragment.fanwandetail.biddetail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.fanfanlicai.activity.MainActivity;
import com.fanfanlicai.activity.invest.FanWanDetailActivity;
import com.fanfanlicai.activity.my.HongBaoActivity;
import com.fanfanlicai.activity.my.JiaXiPiaoActivity;
import com.fanfanlicai.activity.my.TiXianPiaoActivity;
import com.fanfanlicai.activity.registerandlogin.HomeLoginActivity;
import com.fanfanlicai.activity.registerandlogin.HomeRegisterActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.fragment.BaseFragment;
import com.fanfanlicai.global.FanFanApplication;
import com.fanfanlicai.pagers.WebviewActivity;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author lijinliu
 * @date 20180129
 * 合同模板
 */

public class ContractTempFragment extends BaseFragment {

    private String mBid;
    private String mProductDataShowType;
    private String mMatchId;
    private boolean isBuyBefore = false;

    private WebView mWebview;
    private ProgressBar mProgressBar;
    private String mUrl;


    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_contract_temp, null);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        mWebview = (WebView) view.findViewById(R.id.webview);
        return view;
    }

    @Override
    public void initData() {
        if (isBuyBefore) {
            if ("zt".equals(mProductDataShowType)) {
                mUrl = ConstantUtils.DEBTCONSULTATIONZT_URL;
            } else {
                mUrl = ConstantUtils.DEBTCONSULTATIONFW_URL;
            }
        } else {
            String token = CacheUtils.getString(mActivity, "token", null);
            Map<String, String> map = SortRequestData.getmap();
            map.put("token", token);
            map.put("id", mBid);
            map.put("proCode", "fw");
            map.put("clientType", "5");
            try {
                if ("zt".equals(mProductDataShowType)) {
                    map.put("isZhitou", "true");
                    String requestData = SortRequestData.sortString(map);
                    String signData = SignUtil.sign(requestData);
                    mUrl = ConstantUtils.BORROWCONTRACTNEW_URL + "?token=" + URLEncoder.encode(token, "UTF-8") + "&sign=" + signData + "&id=" + mMatchId + "&proCode=fw&isZhitou=true" + "&clientType=5";
                } else {
                    map.put("isZhitou", "false");
                    String requestData = SortRequestData.sortString(map);
                    String signData = SignUtil.sign(requestData);
                    mUrl = ConstantUtils.BORROWCONTRACTNEW_URL + "?token=" + URLEncoder.encode(token, "UTF-8") + "&sign=" + signData + "&id=" + mMatchId + "&proCode=fw&isZhitou=false" + "&clientType=5";
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
Log.i("借款协议-mUrl:",mUrl+"");
        initwebview();
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    private void initwebview() {
        mWebview.clearCache(true);
        mWebview.clearHistory();

        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //与webview交互
        mWebview.addJavascriptInterface(new JavaScriptInterface(), "fanfan");
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
                mProgressBar.setVisibility(View.VISIBLE);
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

    public void setData(boolean isBuyBefore, String bid, String productDataShowType, String matchId) {
        this.isBuyBefore = isBuyBefore;
        this.mBid = bid;
        this.mProductDataShowType = productDataShowType;
        this.mMatchId = matchId;
    }
}
