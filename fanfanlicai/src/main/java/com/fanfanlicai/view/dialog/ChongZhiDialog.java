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
import com.fanfanlicai.activity.my.ChongZhiSuccessActivity;
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

public class ChongZhiDialog extends Dialog implements
		View.OnClickListener {

	private GridPasswordView gpv_length;
	private ImageView iv_cancel;
	private TextView tv_mimatip;
	private TextView tv_mimaforget;
	private int count = 0;
	private String buy_money;
	private TextView money;
	private Context context;
	private CustomProgressDialog progressdialog;
	private String seqNo;
	private String uuid;
	private String from;

	public ChongZhiDialog(Context context) {
		super(context);
	}

	public ChongZhiDialog(Context context, int theme) {
		super(context, theme);
	}

	public ChongZhiDialog(Context context, int theme, String buy_money, String seqNo, String uuid, String from) {
		super(context, theme);
		this.context = context;
		this.buy_money = buy_money;
		this.seqNo = seqNo;
		this.uuid = uuid;
		this.from = from;
		progressdialog = new CustomProgressDialog(context, "正在充值...");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_get1);

		//与提现共用的dialog_get1布局，需要修改名字，这里叫充值金额，提现中是默认的提现金额
		TextView name = (TextView) findViewById(R.id.name);
		name.setText("充值金额");
		initDialog();
	}

	private void initDialog() {
		iv_cancel = (ImageView) findViewById(R.id.iv_cancel);
		iv_cancel.setOnClickListener(this);
		tv_mimatip = (TextView) findViewById(R.id.tv_mimatip);
		money = (TextView) findViewById(R.id.money);
		money.setText(buy_money + "元");
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
						map.put("amount", buy_money);
						map.put("uuid", uuid);
						map.put("seqNo", seqNo);
						map.put("transPassword",Base64.encode(SignUtil.encrypt(passWord)));
						String requestData = SortRequestData.sortString(map);
						String signData = SignUtil.sign(requestData);
						map.put("sign", signData);
						VolleyUtil.sendJsonRequestByPost(
						ConstantUtils.RECHARGESTEPFINAL_URL,null,map,new HttpBackBaseListener() {

							@Override
							public void onSuccess(String string) {
								LogUtils.i("充值输入交易密码==="+string);
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
											
											// 提现金额
											String status = data.getString("status");

											Intent intent = new Intent(getContext(), ChongZhiSuccessActivity.class);
											intent.putExtra("zong_money", buy_money);
											intent.putExtra("from", from);
											getContext().startActivity(intent);
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
								ToastUtils.toastshort("充值失败！");
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
			intent.putExtra("buy_money", buy_money);
			getContext().startActivity(intent);
			break;

		default:
			break;
		}
	}
}
