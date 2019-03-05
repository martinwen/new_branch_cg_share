package com.fanfanlicai.activity.my;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;
import com.fanfanlicai.view.dialog.CustomProgressDialog;

import java.util.Map;

public class HeTongActivity extends BaseActivity {
	private CustomProgressDialog progressdialog;
	private WebView webview;
	String url;
	private TextView hetong_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_hetong);
		
		progressdialog = new CustomProgressDialog(this, "正在加载数据...");
		progressdialog.show();
		
		initView();
		initData();
	}

	private void initView() {
		
		hetong_back = (TextView) findViewById(R.id.hetong_back);//返回
		hetong_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		webview = (WebView) findViewById(R.id.webview);
	}

	private void initData() {
		String id = getIntent().getStringExtra("id");
		String proCode = getIntent().getStringExtra("proCode");
		requestServer(id,proCode);
	}






	private void requestServer(String id, String proCode) {
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(this, "token", null);
		map.put("proCode", proCode);
		map.put("id", id);
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost1(ConstantUtils.CONTRACT_URL,
				null,JSON.toJSONString(map) , new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						LogUtils.i("string=="+string);
						webview.loadDataWithBaseURL(null, string, "text/html", "utf-8", null);
						progressdialog.dismiss();
					}

					@Override
					public void onError(VolleyError error) {
						// TODO Auto-generated method stub
						progressdialog.dismiss();
						ToastUtils.toastshort("网络出错！");
					}

				});
	}
	
}
