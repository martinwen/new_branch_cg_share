
package com.fanfanlicai.activity.my;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.MathUtil;
import com.fanfanlicai.utils.StrToNumber;
import com.fanfanlicai.utils.StringUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;
import com.fanfanlicai.view.dialog.CustomProgressDialog;
import com.fanfanlicai.view.dialog.TiXianDialog;

import java.util.Map;

public class CashActivity extends BaseActivity implements OnClickListener {

	private CustomProgressDialog progressdialog;
	private TiXianDialog tiXianDialog;
	private TextView get_back;// 返回
	private TextView tv_zhanghuyue;// 账户余额
	private ImageView iv_xuxian;// 虚线
	private TextView tv_noinvest_money;// 未投资过的金额
	private TextView tv_bankname;// 银行卡
	private Button bt_get;// 确认提现按钮
	private EditText et_get_money;// 提现金额
	private TextView tv_cashfee;// 提现手续费
	private TextView tv_fanpiao_money;// 提现票抵扣金额
	private String baseBal;//账户余额
	private String cashFee;// 提现手续费
	private LinearLayout ll_tixianpiao;
	private boolean hasRecharge;//是否包含充值未投资金额

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cash);
		progressdialog = new CustomProgressDialog(this, "正在加载数据...");
		initView();
		initData();
	}


	private void initView() {
		get_back = (TextView) findViewById(R.id.get_back);// 返回
		get_back.setOnClickListener(this);
		tv_zhanghuyue = (TextView) findViewById(R.id.tv_zhanghuyue);// 账户余额
		iv_xuxian = (ImageView)findViewById(R.id.iv_xuxian);
		tv_noinvest_money = (TextView) findViewById(R.id.tv_noinvest_money);// 未投资过的金额
		tv_bankname = (TextView) findViewById(R.id.tv_bankname);// 银行卡
		et_get_money = (EditText) findViewById(R.id.et_get_money);// 提现金额
		tv_cashfee = (TextView) findViewById(R.id.tv_cashfee);// 提现手续费
		ll_tixianpiao = (LinearLayout) findViewById(R.id.ll_tixianpiao);
		tv_fanpiao_money = (TextView) findViewById(R.id.tv_fanpiao_money);// 提现票金额
		bt_get = (Button) findViewById(R.id.bt_get);// 确认提现按钮
		bt_get.setOnClickListener(this);

	}

	private void initData() {
		
		Intent intent = getIntent();
		String rechBal = intent.getStringExtra("rechBal");

		//当未投资过的金额大于0时才让其显示
		if (StrToNumber.strTodouble(rechBal) > 0) {
			iv_xuxian.setVisibility(View.VISIBLE);
			tv_noinvest_money.setVisibility(View.VISIBLE);
			tv_noinvest_money.setText("未出借过的金额:"+rechBal+"元");
		}

		
		//提现第一步获取用户信息
		getUserInfo();
		
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
				String get_money = et_get_money.getText().toString().trim();
				LogUtils.i("get_money=="+get_money);
				if (!"".equals(get_money)&&get_money != null) {
					
					getCashFee(get_money);
				}else {
					tv_cashfee.setText("0元");
					ll_tixianpiao.setVisibility(View.INVISIBLE);
				}
			}
		});
	}

	private void getCashFee(final String get_money) {
		if (!progressdialog.isShowing()) {
			progressdialog.showis();
		}
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(this, "token", null);
		map.put("token", token);
		map.put("amount", get_money);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.QUERYCASHFEE_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						LogUtils.i("提现第二步获取提现手续费==="+string);
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
									JSONObject data = JSON.parseObject(datastr);
									
									//是否包含充值未投资金额
									hasRecharge = data.getBoolean("hasRecharge");

									// 提现票抵扣金额
									String ticketAmount = data.getString("ticketAmount");

									// 提现票ID
									String ticketId = data.getString("ticketId");

									if (hasRecharge||ticketId==null) {
										ll_tixianpiao.setVisibility(View.INVISIBLE);

									}else {
			 							ll_tixianpiao.setVisibility(View.VISIBLE);

										if (ticketAmount!=""&&ticketAmount!=null) {
											tv_fanpiao_money.setText(ticketAmount+"元");
										}
									}

									// 提现手续费
									cashFee = data.getString("cashFee");
									if (StrToNumber.strTodouble(ticketAmount) == 0 && StrToNumber.strTodouble(cashFee)== 0){//首次提现

										tv_cashfee.setText(MathUtil.subString(cashFee, 2) + "元");

									}else{//非首次提现

										if(StrToNumber.strTodouble(get_money) < StrToNumber.strTodouble(cashFee)) {
											ToastUtils.toastshort("提现金额不能小于手续费哦");
											tv_cashfee.setText(MathUtil.subString(cashFee, 2) + "元");
										}else {
											tv_cashfee.setText(MathUtil.subString(cashFee, 2) + "元");
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
						ToastUtils.toastshort("网络请求失败");
					}
				});
		
	}
	
	
	private void getUserInfo() {

		if (!progressdialog.isShowing()) {
			progressdialog.show();
		}
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(this, "token", null);
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.CASHVERIFYINFO_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						LogUtils.i("提现第一步获取用户信息==="+string);
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
									JSONObject data = JSON.parseObject(datastr);
									

									// 账户余额
									baseBal = data.getString("baseBal");
									if("-9999".equals(baseBal)) {//银行不能正常返回账户余额
										baseBal = "正在获取中";
										tv_zhanghuyue.setText(baseBal);
									}else{
										tv_zhanghuyue.setText(baseBal + "元");
									}

									
									// 银行名称
									String bankName = data.getString("bankName");
									
									// 银行卡号
									String cardNum = data.getString("cardNum");
									
									//银行名
									tv_bankname.setText(bankName + "(" + cardNum+ ")");

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
						ToastUtils.toastshort("网络请求失败");
					}
				});
	
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_back:
			finish();
			break;
		case R.id.bt_get:// 确认提现
			String get_money = et_get_money.getText().toString().trim();
				if (!TextUtils.isEmpty(get_money)) {
					if (StrToNumber.strTodouble(get_money) > StrToNumber.strTodouble(baseBal)) {
						ToastUtils.toastshort("您的提现金额已超出账户余额");
						return;
					} else if (StrToNumber.strTodouble(get_money) < StrToNumber.strTodouble(cashFee)) {
						ToastUtils.toastshort("提现金额不能小于手续费哦");
						return;
					} else {
						tiXianDialog = new TiXianDialog(this, R.style.YzmDialog, get_money);
						tiXianDialog.show();
					}

				} else {
					ToastUtils.toastshort("请输入提现金额");
				}
			break;
		default:
			break;
		}
	}
}
