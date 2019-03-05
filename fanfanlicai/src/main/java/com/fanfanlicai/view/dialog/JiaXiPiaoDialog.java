package com.fanfanlicai.view.dialog;

import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;

public class JiaXiPiaoDialog extends Dialog implements
		android.view.View.OnClickListener {

	private CustomProgressDialog progressdialog;
	private String ticketId;

	public JiaXiPiaoDialog(Context context) {
		super(context);
	}
	
	public JiaXiPiaoDialog(Context context, int theme, String ticketId) {
		super(context, theme);
		this.ticketId = ticketId;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_jiaxipiao);
		
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
			 useJiaXiPiao();
			 break;

		default:
			break;
		}
	}

	protected void useJiaXiPiao() {


		if (!progressdialog.isShowing()) {
			progressdialog.show();
		}
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(getContext(), "token", null);
		map.put("ticketId", ticketId);
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.USEFHRATECOUPON_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						LogUtils.i("点击使用饭盒加息票==="+string);
						// TODO Auto-generated method stub
						progressdialog.dismiss();
						JSONObject json = JSON.parseObject(string);
						String code = json.getString("code");

						if ("0".equals(code)) {
							JiaXiPiaoMsgDialog jiaXiPiaoMsgDialog = new JiaXiPiaoMsgDialog(getContext(), R.style.YzmDialog);
							jiaXiPiaoMsgDialog.show();
						} else {
							String msg = json.getString("msg");
							JiaXiPiaoMsgDialog jiaXiPiaoMsgDialog = new JiaXiPiaoMsgDialog(getContext(), R.style.YzmDialog, msg);
							jiaXiPiaoMsgDialog.show();
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

