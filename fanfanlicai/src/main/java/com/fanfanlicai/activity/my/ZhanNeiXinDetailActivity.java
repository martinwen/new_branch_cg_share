package com.fanfanlicai.activity.my;

import android.os.Bundle;
import android.view.KeyEvent;
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
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;
import com.fanfanlicai.view.dialog.CustomProgressDialog;

import java.util.Map;

public class ZhanNeiXinDetailActivity extends BaseActivity{
	private CustomProgressDialog progressdialog;
	private WebView webview;
	String url;
	private TextView noticedetail_back;
	private String id_items;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_noticedetail);
		
		progressdialog = new CustomProgressDialog(this, "正在加载数据...");
		progressdialog.show();
		
		initView();
		initData();
		
	}
	
	private void initView() {
		noticedetail_back = (TextView) findViewById(R.id.noticedetail_back);
		noticedetail_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		webview = (WebView) findViewById(R.id.webview);
		
	}

	private void initData() {
		id_items = getIntent().getStringExtra("id_items");
		requestServer(id_items);
	}

	private void requestServer(String id_items) {
		Map<String,String> map=SortRequestData.getmap();//保存提交的form表单参数
		map.put("id",id_items);
		
		String token = CacheUtils.getString(ZhanNeiXinDetailActivity.this, "token",null);
		map.put("token",token);
	   
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign",signData);
		VolleyUtil.sendJsonRequestByPost1(ConstantUtils.STATIONLETTER_DETAIL_URL,
				null,JSON.toJSONString(map) , new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 按下的如果是BACK，同时没有重复
			finish();
			// true 不传递 false 传递
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
}
