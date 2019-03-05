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
import com.fanfanlicai.activity.my.InvestActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;
import com.fanfanlicai.view.dialog.NormalDialog.OnNormalDialogDismissListener;

import java.util.Map;

public class FanTongDialog extends Dialog implements
		android.view.View.OnClickListener {

	private CustomProgressDialog progressdialog;
	private String day;
	private String money;
	private String rate;
	private String fee;
	private String recAvgTimeStr;
	private String id;

	public FanTongDialog(Context context) {
		super(context);
	}
	
	public FanTongDialog(Context context, int theme, String day, String money, String rate, String fee, String recAvgTimeStr, String id) {
		super(context, theme);
		this.day = day;
		this.money = money;
		this.rate = rate;
		this.fee = fee;
		this.recAvgTimeStr = recAvgTimeStr;
		this.id = id;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_fantong);
		
		progressdialog = new CustomProgressDialog(getContext(), "正在处理中...");
		initDialog();
	}
	
	
  
	private void initDialog() {
		TextView tv_day = (TextView) findViewById(R.id.tv_day);
		tv_day.setText("已投天数："+day+"天");
		TextView tv_money = (TextView) findViewById(R.id.tv_money);
		tv_money.setText("赎回本息："+money+"元");
		TextView tv_rate = (TextView) findViewById(R.id.tv_rate);
		tv_rate.setText("赎回手续费率："+rate+"%");
		TextView tv_fee = (TextView) findViewById(R.id.tv_fee);
		tv_fee.setText("赎回手续费："+fee+"元");
		TextView tv_time = (TextView) findViewById(R.id.tv_time);
		tv_time.setText("（平台平均赎回时间为"+recAvgTimeStr+"）");
		TextView btn_cancel = (TextView) findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);
		TextView btn_yue = (TextView) findViewById(R.id.btn_yue);
		btn_yue.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
			dismiss();

			 break;
		case R.id.btn_yue:
			 dismiss();
			 requestBack();
			 
			 break;

		default:
			break;
		}
	}

	
	private void requestBack() {

		if (!progressdialog.isShowing()) {
			progressdialog.show();
		}
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(getContext(), "token", null);
		map.put("token", token);
		map.put("ftId", id);
		map.put("recToCard", "false");
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.FTRECSUBMIT_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						LogUtils.i("饭桶赎回==="+string);
						// TODO Auto-generated method stub
						progressdialog.dismiss();
						JSONObject json = JSON.parseObject(string);
						String code = json.getString("code");
						
						if ("0".equals(code)) {
							
							NormalDialog normalDialog = new NormalDialog(getContext(), R.style.YzmDialog,
									"赎回申请已提交成功\n您可在我的-总资产-赎回中查看赎回进度");
							normalDialog.show();
							normalDialog.setOnNormalDialogDismissListener(new OnNormalDialogDismissListener() {
								
								@Override
								public void OnNormalDialogDismiss() {
									// TODO Auto-generated method stub
									ConstantUtils.yitouxiangmuflag = 2;
									Intent intent = new Intent(getContext(), InvestActivity.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									getContext().startActivity(intent);
								}
							});
						} else if("666666".equals(code)){

						}else {
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

