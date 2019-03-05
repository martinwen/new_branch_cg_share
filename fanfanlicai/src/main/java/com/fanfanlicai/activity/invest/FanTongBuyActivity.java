
package com.fanfanlicai.activity.invest;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.fanfanlicai.utils.MathUtil;
import com.fanfanlicai.utils.StrToNumber;
import com.fanfanlicai.utils.StringUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;
import com.fanfanlicai.view.dialog.CustomProgressDialog;
import com.fanfanlicai.view.dialog.NormalDialog;

import java.util.Map;

public class FanTongBuyActivity extends BaseActivity implements OnClickListener {

	private CustomProgressDialog progressdialog;
	private TextView get_back;// 返回
	private TextView tv_yuyuemoney;// 预约金额
	private EditText ed_zhanghuyue;// 账户余额输入框
	private TextView tv_zhanghuyue;// 账户余额
	private EditText ed_fanhe;// 饭盒资产输入框
	private TextView tv_fanhe;// 饭盒资产
	private TextView tv_jiangjin;// 奖金余额
	private TextView tv_zong;// 买入总计
	private Button bt_buy;// 确认提现按钮
	private ImageView iv_usereward;// 奖金使用状态图标
	private TextView tv_item1;//小贴士第1条
//	private TextView tv_rule;//手续费收取规则
	private String jiangjin;// 奖金
	private boolean isGou = true;// 当可点击时，奖金已经勾上
	private String seqNo;
	private String baseAcctBal;//账户余额
	private String fhAcctBal;//饭盒资产
	private String minInvestAmount;//起投金额
	private String bookAmount;//预约金额
	private double baseRate;
	private double addRate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fantongbuy);

		progressdialog = new CustomProgressDialog(this, "买入确认中...");
		initView();
		initData();
	}

	private void initView() {
		get_back = (TextView) findViewById(R.id.get_back);// 返回
		tv_yuyuemoney = (TextView) findViewById(R.id.tv_yuyuemoney);// 预约金额
		ed_zhanghuyue = (EditText) findViewById(R.id.ed_zhanghuyue);//账户余额输入框
		tv_zhanghuyue = (TextView) findViewById(R.id.tv_zhanghuyue);// 账户余额
		ed_fanhe = (EditText) findViewById(R.id.ed_fanhe);// 饭盒资产输入框
		tv_fanhe = (TextView) findViewById(R.id.tv_fanhe);// 饭盒资产
		tv_jiangjin = (TextView) findViewById(R.id.tv_jiangjin);// 奖金余额
		tv_zong = (TextView) findViewById(R.id.tv_zong);// 买入总计
		get_back.setOnClickListener(this);
		iv_usereward = (ImageView) findViewById(R.id.iv_usereward);// 奖金使用状态图标
		iv_usereward.setOnClickListener(this);
		bt_buy = (Button) findViewById(R.id.bt_buy);// 确认提现按钮
		bt_buy.setOnClickListener(this);
		tv_item1 = (TextView) findViewById(R.id.tv_item1);//小贴士第1条
//		tv_rule = (TextView) findViewById(R.id.tv_rule);//手续费收取规则
//		tv_rule.setOnClickListener(this);
	}

	private void initData() {
		//从买入第一步中拿到交易流水号
		Intent intent = getIntent();
		baseRate = intent.getDoubleExtra("baseRate", 0);
		addRate = intent.getDoubleExtra("addRate", 0);
		// 交易流水号
		seqNo = intent.getStringExtra("seqNo");
		//账户余额
		baseAcctBal = intent.getStringExtra("baseAcctBal");
		tv_zhanghuyue.setText("账户余额："+baseAcctBal+"元");
		//饭盒资产
		fhAcctBal = intent.getStringExtra("fhAcctBal");
		tv_fanhe.setText("饭盒资产："+fhAcctBal+"元");
		//起投金额
		minInvestAmount = intent.getStringExtra("minInvestAmount");
		// 小贴士第1条
		tv_item1.setText("1.单笔最低出借金额"+minInvestAmount+"元；");
		//预约金额
		bookAmount = intent.getStringExtra("bookAmount");
		tv_yuyuemoney.setText("预约金额："+bookAmount+"元");
		
		//获取到奖金
		jiangjin = CacheUtils.getString(this, CacheUtils.REWARDACCTBAL, "");
		tv_jiangjin.setText(MathUtil.subString(jiangjin, 2)+"元");
		
		
		
		// 给账户余额输入框设置监听，为了使奖金使用状态图标根据不同的输入值变化而变化
		ed_zhanghuyue.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				if (s.toString().contains(".")) {
					if (s.length() - 1 - s.toString().indexOf(".") > 2) {
						s = s.toString().subSequence(0,
								s.toString().indexOf(".") + 3);
						ed_zhanghuyue.setText(s);
						ed_zhanghuyue.setSelection(s.length());
					}
				}
				if (s.toString().trim().substring(0).equals(".")) {
					s = "0" + s;
					ed_zhanghuyue.setText(s);
					ed_zhanghuyue.setSelection(2);
				}

				if (s.toString().startsWith("0")
						&& s.toString().trim().length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						ed_zhanghuyue.setText(s.subSequence(0, 1));
						ed_zhanghuyue.setSelection(1);
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

				String zhanghuMoney = ed_zhanghuyue.getText().toString().trim();
				String fanheMoney = ed_fanhe.getText().toString().trim();
				
				if (StrToNumber.strTodouble(zhanghuMoney)+ StrToNumber.strTodouble(fanheMoney) >= 
						StrToNumber.strTodouble(CacheUtils.getString(FanTongBuyActivity.this, CacheUtils.REWARDACCTBAL, ""))) {
					iv_usereward.setImageResource(R.drawable.usereward_yes);
					// 买入总计
					tv_zong.setText(MathUtil.subDouble((StrToNumber.strTodouble(zhanghuMoney)+ StrToNumber.strTodouble(fanheMoney)
							+ StrToNumber.strTodouble(CacheUtils.getString(FanTongBuyActivity.this, CacheUtils.REWARDACCTBAL, ""))), 2) + "元");
					//勾选，获取奖金值
					jiangjin = CacheUtils.getString(FanTongBuyActivity.this, CacheUtils.REWARDACCTBAL, "");
				} else {//不勾选奖金
					iv_usereward.setImageResource(R.drawable.usereward);
					// 买入总计
					tv_zong.setText(MathUtil.subDouble(StrToNumber.strTodouble(zhanghuMoney)+ StrToNumber.strTodouble(fanheMoney), 2)+"元");
					//不勾选，把奖金改为0
					jiangjin = "0.00";		
				}
				
			}
		});
		// 给饭盒资产输入框设置监听，为了使奖金使用状态图标根据不同的输入值变化而变化
		ed_fanhe.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				if (s.toString().contains(".")) {
					if (s.length() - 1 - s.toString().indexOf(".") > 2) {
						s = s.toString().subSequence(0,
								s.toString().indexOf(".") + 3);
						ed_fanhe.setText(s);
						ed_fanhe.setSelection(s.length());
					}
				}
				if (s.toString().trim().substring(0).equals(".")) {
					s = "0" + s;
					ed_fanhe.setText(s);
					ed_fanhe.setSelection(2);
				}

				if (s.toString().startsWith("0")
						&& s.toString().trim().length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						ed_fanhe.setText(s.subSequence(0, 1));
						ed_fanhe.setSelection(1);
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

				String fanheMoney = ed_fanhe.getText().toString().trim();
				String zhanghuMoney = ed_zhanghuyue.getText().toString().trim();
				
				if (StrToNumber.strTodouble(zhanghuMoney)+ StrToNumber.strTodouble(fanheMoney) >= 
						StrToNumber.strTodouble(CacheUtils.getString(FanTongBuyActivity.this, CacheUtils.REWARDACCTBAL, ""))) {
					iv_usereward.setImageResource(R.drawable.usereward_yes);
					// 买入总计
					tv_zong.setText(MathUtil.subDouble((StrToNumber.strTodouble(zhanghuMoney)+ StrToNumber.strTodouble(fanheMoney)
							+ StrToNumber.strTodouble(CacheUtils.getString(FanTongBuyActivity.this, CacheUtils.REWARDACCTBAL, ""))), 2) + "元");
					//勾选，获取奖金值
					jiangjin = CacheUtils.getString(FanTongBuyActivity.this, CacheUtils.REWARDACCTBAL, "");
				} else {//不勾选奖金
					iv_usereward.setImageResource(R.drawable.usereward);
					// 买入总计
					tv_zong.setText(MathUtil.subDouble(StrToNumber.strTodouble(zhanghuMoney)+ StrToNumber.strTodouble(fanheMoney), 2)+"元");
					//不勾选，把奖金改为0
					jiangjin = "0.00";		
				}
				
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
		case R.id.iv_usereward:
			String fanheMoney = ed_fanhe.getText().toString().trim();
			String zhanghuMoney = ed_zhanghuyue.getText().toString().trim();
			if (StrToNumber.strTodouble(fanheMoney)+ StrToNumber.strTodouble(zhanghuMoney) >= 
					StrToNumber.strTodouble(CacheUtils.getString(FanTongBuyActivity.this, CacheUtils.REWARDACCTBAL, ""))) {
				isGou = !isGou;
				if (isGou) {//勾选奖金
					iv_usereward.setImageResource(R.drawable.usereward_yes);
					//买入总计
					tv_zong.setText(MathUtil.subDouble((StrToNumber.strTodouble(fanheMoney)+ StrToNumber.strTodouble(zhanghuMoney)+
							StrToNumber.strTodouble(CacheUtils.getString(FanTongBuyActivity.this, CacheUtils.REWARDACCTBAL, ""))),2)+"");
					//勾选，获取奖金值
					jiangjin = CacheUtils.getString(FanTongBuyActivity.this, CacheUtils.REWARDACCTBAL, "");
				} else {//不勾选奖金
					iv_usereward.setImageResource(R.drawable.usereward);
					//买入总计
					tv_zong.setText(MathUtil.subDouble(StrToNumber.strTodouble(fanheMoney)+ StrToNumber.strTodouble(zhanghuMoney), 2)+"");
					//不勾选，把奖金改为0
					jiangjin = "0.00";
				}
			}
			break;
		case R.id.bt_buy:
			String fanhe_Money = ed_fanhe.getText().toString().trim();
			String zhanghu_Money = ed_zhanghuyue.getText().toString().trim();
			String buy_money = (StrToNumber.strTodouble(fanhe_Money)+StrToNumber.strTodouble(zhanghu_Money))+"";
			String zong_money = (StrToNumber.strTodouble(fanhe_Money)+StrToNumber.strTodouble(zhanghu_Money)+StrToNumber.strTodouble(jiangjin))+"";
			
			if (StrToNumber.strTodouble(buy_money)!=0) {
				if (StrToNumber.strTodouble(zhanghu_Money)>StrToNumber.strTodouble(baseAcctBal)) {
					NormalDialog normalDialog = new NormalDialog(this, R.style.YzmDialog, "您输入的金额大于账户余额，请重新输入。");
					normalDialog.show();
					return;
				}
				if (StrToNumber.strTodouble(fanhe_Money)>StrToNumber.strTodouble(fhAcctBal)) {
					NormalDialog normalDialog = new NormalDialog(this, R.style.YzmDialog, "您输入的金额大于饭盒资产，请重新输入。");
					normalDialog.show();
					return;
				}
				if (StrToNumber.strTodouble(buy_money)<StrToNumber.strTodouble(minInvestAmount)) {
					NormalDialog normalDialog = new NormalDialog(this, R.style.YzmDialog, "买入金额必须大于最低出借金额，请重新输入。");
					normalDialog.show();
					return;
				}
				if (StrToNumber.strTodouble(buy_money) > StrToNumber.strTodouble(bookAmount)) {
					NormalDialog normalDialog = new NormalDialog(this, R.style.YzmDialog, "“使用账户余额+使用饭盒资产”需小于等于预约金额");
					normalDialog.show();
					return;
				}
				
				getDataFromServer(zong_money, zhanghu_Money, fanhe_Money, jiangjin, seqNo);
				
			} else {
				ToastUtils.toastshort("预约金额不能为空哦");
			}
			
			break;
//		case R.id.tv_rule:
//			Intent intent2 = new Intent(this, FanTongRateActivity.class);
//			intent2.putExtra("baseRate", baseRate);
//			intent2.putExtra("addRate", addRate);
//			startActivity(intent2);
//			break;
		default:
			break;
		}
	}

	private void getDataFromServer(final String zong_money, String zhanghu_Money, String fanhe_Money, String jiangjin, String seqNo) {
		if(!progressdialog.isShowing()){
			progressdialog.showis();
		}
		
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(FanTongBuyActivity.this,"token",null);
		map.put("token", token);
		map.put("balAmount", zhanghu_Money);
		map.put("fhAmount", fanhe_Money);
		map.put("rewardAmount", jiangjin);
		map.put("seqNo", seqNo);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(
				ConstantUtils.FTINVESTSTEP2_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						progressdialog.dismiss();
						JSONObject json = JSON.parseObject(string);
						String code = json.getString("code");

						if ("0".equals(code)) {
							String datastr = json.getString("data");
							if (StringUtils.isBlank(datastr)) {
								// datastr为空不验签
							} else {
								String sign = json.getString("sign");
								boolean isSuccess = SignUtil.verify(sign, datastr);
								if (isSuccess) {
									Intent intent2 = new Intent(FanTongBuyActivity.this, FanHeBuySuccessActivity.class);
									intent2.putExtra("zong_money", zong_money);
									startActivity(intent2);
								} else {
									ToastUtils.toastshort("买入失败！");
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
}
