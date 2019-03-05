package com.fanfanlicai.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.MainActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.StringUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;
import com.fanfanlicai.view.dialog.NormalDialog.OnNormalDialogDismissListener;

import java.util.Map;

public class FanTongYuYueDialog extends Dialog implements
		android.view.View.OnClickListener {

	private CustomProgressDialog progressdialog;
	private String money;

	public FanTongYuYueDialog(Context context) {
		super(context);
	}
	
	public FanTongYuYueDialog(Context context, int theme, String money) {
		super(context, theme);
		this.money = money;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_fantongyuyue);
		
		progressdialog = new CustomProgressDialog(getContext(), "正在加载数据...");
		initDialog();
	}
	
	
  
	private void initDialog() {
		TextView btn_cancel = (TextView) findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);
		TextView btnOk = (TextView) findViewById(R.id.btn_ok);
		btnOk.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
			 dismiss();
			 break;
		case R.id.btn_ok:
			 dismiss();
			 requestBook();
			 break;

		default:
			break;
		}
	}

	private void requestBook() {

		if (!progressdialog.isShowing()) {
			progressdialog.show();
		}
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(getContext(), "token", null);
		map.put("token", token);
		map.put("type", "ft");
		map.put("amount", money);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.BOOK_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						LogUtils.i("饭桶预约==="+string);
						// TODO Auto-generated method stub
						progressdialog.dismiss();
						JSONObject json = JSON.parseObject(string);
						String code = json.getString("code");

						if ("0".equals(code)) {
							String datastr = json.getString("data");
							if (StringUtils.isBlank(datastr)) {
								// datastr为空不验签
							} else {
								String sign = json.getString("sign");
								boolean isSuccess = SignUtil.verify(sign,
										datastr);
								if (isSuccess) {// 验签成功
									
									NormalDialog normalDialog = new NormalDialog(getContext(), R.style.YzmDialog, "预约成功！审核通过后我们将以短信形式通知您购买时间，请注意查收。");
									normalDialog.show();
									normalDialog.setOnNormalDialogDismissListener(new OnNormalDialogDismissListener() {
										
										@Override
										public void OnNormalDialogDismiss() {
											// TODO Auto-generated method stub
											//产品要求饭桶预约成功后跳转到首页
											ConstantUtils.touziflag = 0;
											ConstantUtils.fanheorfanwanorfantongflag = 0;
											Intent intent = new Intent(getContext(),MainActivity.class);
											intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
											getContext().startActivity(intent);
										}
									});
									
								} else {
									ToastUtils.toastshort("加载数据异常！");
								}
							}
						} else {
							String msg = json.getString("msg");
							ToastUtils.toastshort(msg);
						}
					}

					@Override
					public void onError(VolleyError error) {
						progressdialog.dismiss();
						ToastUtils.toastshort("网络请求失败");
					}
				});
	}
}

