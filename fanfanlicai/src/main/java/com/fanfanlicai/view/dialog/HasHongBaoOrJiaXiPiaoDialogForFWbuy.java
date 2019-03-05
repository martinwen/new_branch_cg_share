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
import com.fanfanlicai.activity.invest.BuyShenQingActivity;
import com.fanfanlicai.activity.invest.FanHeBuySuccessActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.global.FanFanApplication;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.StringUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;

import java.util.Map;

public class HasHongBaoOrJiaXiPiaoDialogForFWbuy extends Dialog implements
		View.OnClickListener {

	private CustomProgressDialog progressdialog;
	private String buyMoney;
	private String jiangjin;
	private String ticketId;
	private String seqNo;
	private String jiaxi;
	private String reward;

	public HasHongBaoOrJiaXiPiaoDialogForFWbuy(Context context) {
		super(context);
	}

	public HasHongBaoOrJiaXiPiaoDialogForFWbuy(Context context, int theme, String buyMoney,
		String jiangjin, String ticketId, String seqNo, String jiaxi,String reward) {
		super(context, theme);
		this.buyMoney = buyMoney;
		this.jiangjin = jiangjin;
		this.ticketId = ticketId;
		this.seqNo = seqNo;
		this.jiaxi = jiaxi;
		this.reward = reward;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_hashongbaoorjiaxipiao);
		
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
			getDataFromServer();
			 break;

		default:
			break;
		}
	}

	private void getDataFromServer() {
		final long startTime = System.currentTimeMillis();
		//获取uuid
		if(!progressdialog.isShowing()){
			progressdialog.show();
		}
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(getContext(), "token", null);
		map.put("token", token);
		map.put("amount", buyMoney);
		map.put("reward", jiangjin);
		map.put("fwAddRateId", FanFanApplication.jiaxi_ID);
		map.put("redbagTicketId", ticketId);
		map.put("redbagAmount", reward);
		map.put("seqNo", seqNo);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.SUBMITINVEST_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						LogUtils.i("饭碗立即买入==="+string);
						long endTime = System.currentTimeMillis();
						if(endTime-startTime>12000){
							progressdialog.dismiss();
							Intent intent = new Intent(getContext(),BuyShenQingActivity.class);
							getContext().startActivity(intent);
						}else {
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
										JSONObject data = JSON.parseObject(datastr);


										// 买入总计
										String investTotalAmount = data.getString("investTotalAmount");

										Intent intent = new Intent(getContext(),FanHeBuySuccessActivity.class);
										intent.putExtra("zong_money", investTotalAmount);
										getContext().startActivity(intent);

									} else {
										ToastUtils.toastshort("加载数据异常！");
									}
								}
							}else if ("4003".equals(code)) {

								Intent intent = new Intent(getContext(),BuyShenQingActivity.class);
								getContext().startActivity(intent);

							}else if("666666".equals(code)){

							}else {
								String msg = json.getString("msg");
								ToastUtils.toastshort(msg);
							}
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

