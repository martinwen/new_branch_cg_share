
package com.fanfanlicai.activity.invest;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.activity.MainActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.StrToNumber;
import com.fanfanlicai.utils.StringUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;
import com.fanfanlicai.view.dialog.CGBindCardDialog;
import com.fanfanlicai.view.dialog.CGSignCardDialog;
import com.fanfanlicai.view.dialog.CustomProgressDialog;
import com.fanfanlicai.view.dialog.FanHeShuHuiDialog;

import java.util.Map;

public class FanHeCashActivity extends BaseActivity implements OnClickListener {

	private CustomProgressDialog progressdialog;//加载动画
	private TextView get_back;// 返回
	private TextView tv_fanhezichan;// 饭盒资产
	private EditText et_get_money;// 赎回金额
	private Button bt_get;// 确定按钮
	private TextView tv_item1;//小贴士第一条
	private TextView tv_item4;//小贴士第三条

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fanhecash);

		progressdialog = new CustomProgressDialog(this, "正在加载数据...");
		initView();
		initData();
	}

	private void initView() {
		get_back = (TextView) findViewById(R.id.get_back);// 返回
		get_back.setOnClickListener(this);
		tv_fanhezichan = (TextView) findViewById(R.id.tv_fanhezichan);// 饭盒资产
		et_get_money = (EditText) findViewById(R.id.et_get_money);// 赎回金额
		bt_get = (Button) findViewById(R.id.bt_get);// 确定按钮
		bt_get.setOnClickListener(this);
		tv_item1 = (TextView) findViewById(R.id.tv_item1);//小贴士第一条
		tv_item4 = (TextView) findViewById(R.id.tv_item4);//小贴士第一条
	}

	private void initData() {
		//进入该界面即将保存在本地的饭盒资产数据展示
		showDataFromServer();
		
		// 给提现金额设置监听，为了使输入金额不超过两位有效数字
		et_get_money.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				if (s.toString().contains(".")) {
					if (s.length() - 1 - s.toString().indexOf(".") > 2) {
						s = s.toString().subSequence(0,
								s.toString().indexOf(".") + 3);
						et_get_money.setText(s);
						et_get_money.setSelection(s.length());
					}
				}
				if (s.toString().trim().substring(0).equals(".")) {
					s = "0" + s;
					et_get_money.setText(s);
					et_get_money.setSelection(2);
				}

				if (s.toString().startsWith("0")
						&& s.toString().trim().length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						et_get_money.setText(s.subSequence(0, 1));
						et_get_money.setSelection(1);
						return;
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void showDataFromServer() {
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(this, "token", null);
		// 如果没有登录，直接return，不访问网络了
		if (TextUtils.isEmpty(token)) {
			return;
		}
		progressdialog.show();
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.FHVERIFYINFO_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {

						LogUtils.i("饭盒赎回校验==="+string);

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

									// 单日赎回限额
									String fhTotalAssets = data.getString("fhTotalAssets");
									tv_fanhezichan.setText(fhTotalAssets+"元");

									// 单日赎回限额
									String recUserDailyLimit = data.getString("recUserDailyLimit");
									tv_item1.setText("1.每人每日赎回上限为"+recUserDailyLimit+"元；");

									// 平台平均赎回时间限额
									String recAvgTimeStr = data.getString("recAvgTimeStr");
									tv_item4.setText("4.发起赎回申请后，一般当天转入账户余额，平均赎回时间为"+recAvgTimeStr+"，请以实际到账时间为准。");
								} else {
									ToastUtils.toastshort("加载数据异常！");
								}
							}
						} else if("666666".equals(code)){

						}else {
							String msg = json.getString("msg");
							ToastUtils.toastshort(msg);
						}
					}

					@Override
					public void onError(VolleyError error) {
						progressdialog.dismiss();
						ToastUtils.toastshort("网络连接失败，请检查网络");
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_back:
			finish();
			Intent intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case R.id.bt_get:
			String get_money = et_get_money.getText().toString().trim();
			if (TextUtils.isEmpty(get_money)) {
				ToastUtils.toastshort("赎回金额不能为空哦");
				return;
			}
			getDataFromServer(get_money);
			break;
		default:
			break;
		}
	}

	private void getDataFromServer(final String get_money) {
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(this, "token", null);
		// 如果没有登录，直接return，不访问网络了
		if (TextUtils.isEmpty(token)) {
			return;
		}
		progressdialog.show();
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.FHVERIFYINFO_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						
						LogUtils.i("饭盒赎回校验==="+string);
						
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
									
									// 单日赎回限额
									String recUserDailyLimit = data.getString("recUserDailyLimit");
									tv_item1.setText("1.每人每日赎回上限为"+recUserDailyLimit+"万元；");

									// 平台平均赎回时间限额
									String recAvgTimeStr = data.getString("recAvgTimeStr");
									tv_item4.setText("4.发起赎回申请后，一般当天转入账户余额，平均赎回时间为"+recAvgTimeStr+"，请以实际到账时间为准。");

									// 防重uuid
									String uuid = data.getString("uuid");
									
									// 饭盒当日可赎回金额
									String fhRecBal = data.getString("fhRecBal");

									// 是否开通存管户
									String hasFyAccount = data.getString("hasFyAccount");

									// 是否签约
									String isSignCard = data.getString("isSignCard");

									// 是否开通存管户（绑卡）0-未开通；
									if ("0".equals(hasFyAccount)) {// 如果还没有绑卡，去绑卡

										CGBindCardDialog cgBindCardDialog = new CGBindCardDialog(FanHeCashActivity.this, R.style.YzmDialog);
										cgBindCardDialog.show();
									}else{//1-已开通
										if ("0".equals(isSignCard)) {// 如果还没有签约，去签约
											CGSignCardDialog cgSignCardDialog = new CGSignCardDialog(FanHeCashActivity.this, R.style.YzmDialog);
											cgSignCardDialog.show();
										}else{//1-已签约

//											if (StrToNumber.strTodouble(get_money) > StrToNumber.strTodouble(fhRecBal)) {
//												ToastUtils.toastshort("您输入的赎回金额值超出了当日上限哦");
//												return;
//											}

											requestServer(get_money,uuid);

										}

									}

								} else {
									ToastUtils.toastshort("加载数据异常！");
								}
							}
						} else if("666666".equals(code)){

						}else {
							String msg = json.getString("msg");
							ToastUtils.toastshort(msg);
						}
					}

					@Override
					public void onError(VolleyError error) {
						progressdialog.dismiss();
						ToastUtils.toastshort("网络连接失败，请检查网络");
					}
				});
	}


	protected void requestServer(String get_money, String uuid) {
			progressdialog.show();
			Map<String, String> map = SortRequestData.getmap();
			String token = CacheUtils.getString(this, "token", "");
			map.put("token", token);
			map.put("amount", get_money);
			map.put("uuid", uuid);
			map.put("clientType", "android");
			String requestData = SortRequestData.sortString(map);
			String signData = SignUtil.sign(requestData);
			map.put("sign", signData);
			VolleyUtil.sendJsonRequestByPost(ConstantUtils.FHRECSUBMIT_URL, null, map,
					new HttpBackBaseListener() {

						@Override
						public void onSuccess(String string) {
							
							LogUtils.i("饭盒赎回==="+string);
							progressdialog.dismiss();
							JSONObject json = JSON.parseObject(string);
							String code = json.getString("code");

							if ("0".equals(code)) {
								//赎回成功跳出弹窗
								FanHeShuHuiDialog fanHeShuHuiDialog = new FanHeShuHuiDialog(FanHeCashActivity.this,R.style.YzmDialog);
								fanHeShuHuiDialog.setCanceledOnTouchOutside(false);
								fanHeShuHuiDialog.setCancelable(false);
								fanHeShuHuiDialog.show();
							}else if("666666".equals(code)){

							}else {
								String msg = json.getString("msg");
								ToastUtils.toastshort(msg);
							}
						}

						@Override
						public void onError(VolleyError error) {
							progressdialog.dismiss();
							ToastUtils.toastshort("网络连接失败，请检查网络");
						}
					});
		
	}
	
}
