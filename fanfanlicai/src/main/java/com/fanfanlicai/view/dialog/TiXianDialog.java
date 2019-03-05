package com.fanfanlicai.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.my.CashDetailActivity;
import com.fanfanlicai.activity.setting.CashFixActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.sign.Base64;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.StringUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;
import com.fanfanlicai.view.password.GridPasswordView;
import com.fanfanlicai.view.password.GridPasswordView.OnPasswordChangedListener;
import com.fanfanlicai.view.password.PasswordType;

import java.util.Map;


public class TiXianDialog extends Dialog implements
		android.view.View.OnClickListener {

	private GridPasswordView gpv_length;
	private ImageView iv_cancel;
	private TextView tv_mimatip;
	private TextView tv_mimaforget;
	private String get_money;
	private TextView money;
	private Context context;
	private CustomProgressDialog progressdialog;

	public TiXianDialog(Context context) {
		super(context);
	}

	public TiXianDialog(Context context, int theme) {
		super(context, theme);
	}

	public TiXianDialog(Context context, int theme, String get_money) {
		super(context, theme);
		this.context = context;
		this.get_money = get_money;
		progressdialog = new CustomProgressDialog(context, "正在提现...");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_get1);
		initDialog();
	}

	private void initDialog() {
		iv_cancel = (ImageView) findViewById(R.id.iv_cancel);
		iv_cancel.setOnClickListener(this);
		tv_mimatip = (TextView) findViewById(R.id.tv_mimatip);
		money = (TextView) findViewById(R.id.money);
		money.setText(get_money + "元");
		tv_mimaforget = (TextView) findViewById(R.id.tv_mimaforget);
		tv_mimaforget.setOnClickListener(this);
		gpv_length = (GridPasswordView) findViewById(R.id.gpv_length);
		gpv_length.setPasswordType(PasswordType.NUMBER);
		gpv_length.setOnPasswordChangedListener(new OnPasswordChangedListener() {

					@Override
					public void onTextChanged(String psw) {

					}

					@Override
					public void onInputFinish(String psw) {
						final String passWord = gpv_length.getPassWord();
						// 调用提现接口
						progressdialog.show();
						Map<String, String> map = SortRequestData.getmap();
						String token = CacheUtils.getString(context,"token",null);
						map.put("token", token);
						map.put("amount", get_money);
						map.put("password",Base64.encode(SignUtil.encrypt(passWord)));
						String requestData = SortRequestData.sortString(map);
						String signData = SignUtil.sign(requestData);
						map.put("sign", signData);
						VolleyUtil.sendJsonRequestByPost(
						ConstantUtils.SUBMITCASH_URL,null,map,new HttpBackBaseListener() {

							@Override
							public void onSuccess(String string) {
								LogUtils.i("提现第三步string=="+string);
								progressdialog.dismiss();
								JSONObject json = JSON.parseObject(string);
								String code = json.getString("code");
								if ("0".equals(code)) {

									String datastr = json.getString("data");
									if (StringUtils.isBlank(datastr)) {
										// datastr为空不验签
									} else {
										String sign = json.getString("sign");
										boolean isSuccess = SignUtil.verify(sign,datastr);
										if (isSuccess) {// 验签成功
											
											JSONObject data = JSON.parseObject(datastr);
											
											// 银行卡号
											String cardNum = data.getString("cardNum");
											
											Intent intent = new Intent(context,CashDetailActivity.class);
											intent.putExtra("cardNum",cardNum);
											intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											context.startActivity(intent);
											dismiss();
											
										} else {
											ToastUtils.toastshort("加载数据异常！");
										}
									}
								
								}else if("666666".equals(code)){

								} else {
									String msg = json.getString("msg");
									gpv_length.clearPassword();
									tv_mimaforget.setVisibility(View.VISIBLE);
									tv_mimatip.setVisibility(View.VISIBLE);
									tv_mimatip.setText(msg);
								}
							}

							@Override
							public void onError(VolleyError error) {
								progressdialog.dismiss();
								ToastUtils.toastshort("提现失败！");
							}
						});
					}
		});
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_cancel:
			dismiss();
			break;
		case R.id.tv_mimaforget:
			ConstantUtils.updateflag=1;
			Intent intent = new Intent(getContext(), CashFixActivity.class);
			intent.putExtra("get_money", get_money);
			getContext().startActivity(intent);
			break;

		default:
			break;
		}
	}
}
