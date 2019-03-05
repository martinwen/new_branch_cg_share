
package com.fanfanlicai.activity.my;

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
import com.fanfanlicai.view.dialog.ChongZhiDialog;
import com.fanfanlicai.view.dialog.CustomProgressDialog;

import java.util.Map;

public class ChongZhiActivity extends BaseActivity implements OnClickListener {

	private TextView get_back;// 返回
	private TextView tv_name;// 姓名
	private TextView tv_number;// 身份证
	private TextView tv_bankname;// 银行
	private TextView tv_limit;// 银行限额
	private EditText et_buy_money;// 充值金额
	private CustomProgressDialog progressdialog;
	private Button bt_buy;
	private String maxRechargeAmount;
	private String minRechargeAmount;
	private String seqNo;
	private String uuid;
	private String from;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chongzhi);

		progressdialog=new CustomProgressDialog(this, "正在充值...");
		initView();
		initData();
	}

	private void initView() {
		get_back = (TextView) findViewById(R.id.get_back);// 返回
		get_back.setOnClickListener(this);
		tv_name = (TextView) findViewById(R.id.tv_name);// 姓名
		tv_number = (TextView) findViewById(R.id.tv_number);// 身份证
		tv_bankname = (TextView) findViewById(R.id.tv_bankname);// 银行
		tv_limit = (TextView) findViewById(R.id.tv_limit);// 银行限额
		et_buy_money = (EditText) findViewById(R.id.et_buy_money);// 充值金额
		bt_buy = (Button) findViewById(R.id.bt_buy);
		bt_buy.setOnClickListener(this);

	}

	private void initData() {

		//来自饭盒还是饭碗还是充值页面
		from = getIntent().getStringExtra("from");

		getDataFromServer();

		
		// 给买入金额设置监听，为了使输入金额不超过两位有效数字
		et_buy_money.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				if (s.toString().contains(".")) {
					if (s.length() - 1 - s.toString().indexOf(".") > 2) {
						s = s.toString().subSequence(0,
								s.toString().indexOf(".") + 3);
						et_buy_money.setText(s);
						et_buy_money.setSelection(s.length());
					}
				}
				if (s.toString().trim().substring(0).equals(".")) {
					s = "0" + s;
					et_buy_money.setText(s);
					et_buy_money.setSelection(2);
				}

				if (s.toString().startsWith("0")
						&& s.toString().trim().length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						et_buy_money.setText(s.subSequence(0, 1));
						et_buy_money.setSelection(1);
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

	//身份证星号处理
	private String fixNum(String idNo) {
		return idNo.replace(idNo.substring(1,16), "****************");
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_back:
			finish();
			break;
		case R.id.bt_buy:
			String buy_money = et_buy_money.getText().toString().trim();
			if (!TextUtils.isEmpty(buy_money)) {
				if (StrToNumber.strTodouble(buy_money) > StrToNumber.strTodouble(maxRechargeAmount)) {
					ToastUtils.toastshort("您的单笔充值金额已超限");
					return;
				}
				if (StrToNumber.strTodouble(buy_money) < StrToNumber.strTodouble(minRechargeAmount)) {
					ToastUtils.toastshort("您的单笔充值金额不能小于"+minRechargeAmount);
					return;
				}

				// 把充值金额通过构造方法传递到Dialog
				ChongZhiDialog chongZhiDialog= new ChongZhiDialog(this, R.style.YzmDialog, buy_money,seqNo,uuid,from);
				chongZhiDialog.show();
				
			} else {
				ToastUtils.toastshort("请输入充值金额");
			}
			break;
		default:
			break;
		}
	}

	private void getDataFromServer() {
		if (!progressdialog.isShowing()) {
			progressdialog.showis();
		}
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(this, "token", "");
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.RECHARGESTEPONE_URL, null, map,
				new HttpBackBaseListener() {
					@Override
					public void onSuccess(String string) {
						LogUtils.i("充值界面的数据==="+string);
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


									//卡号
									String cardNum = data.getString("cardNum");

									//身份证号
									String idNo = data.getString("idNo");
									tv_number.setText(fixNum(idNo));

									//银行名称和后4位尾数
									String openBank = data.getString("openBank");
									tv_bankname.setText(openBank + "(" + cardNum.substring(cardNum.length()-4, cardNum.length()) + ")");

									//真实姓名
									String realName = data.getString("realName");
									tv_name.setText(realName);

									//单笔限额
									String singleLimit = data.getString("singleLimit");
									//单日限额
									String dailyLimit = data.getString("dailyLimit");

									if(StrToNumber.strTodouble(dailyLimit)>0){
										tv_limit.setText("限额：单笔"+singleLimit+"元，单日"+dailyLimit+"元");
									}else {
										tv_limit.setText("限额：单笔"+singleLimit+"元，单日不限");

									}

									//交易流水号
									seqNo = data.getString("seqNo");

									//uuid
									uuid = data.getString("transUuid");

									//最小充值金额
									minRechargeAmount = data.getString("minRechargeAmount");

									//最大充值金额
									maxRechargeAmount = data.getString("maxRechargeAmount");


								} else {
									ToastUtils.toastshort("加载数据异常！");
								}
							}
						}  else if("666666".equals(code)){

						}else {
							String msg = json.getString("msg");
							ToastUtils.toastshort(msg);
						}
					}

					@Override
					public void onError(VolleyError error) {
						progressdialog.dismiss();
					}
				});
	}

}
