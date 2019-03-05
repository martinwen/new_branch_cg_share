
package com.fanfanlicai.activity.invest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.activity.MainActivity;
import com.fanfanlicai.activity.my.ChongZhiActivity;
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

import java.util.Map;


public class FanHeBuyYxgActivity extends BaseActivity implements OnClickListener {

	private CustomProgressDialog progressdialog;
	private TextView get_back;// 返回
	private TextView tv_jiangjin;// 奖金余额
	private TextView tv_zong;// 买入总计
	private Button bt_buy;// 确认提现按钮
	private TextView tv_buy_money;//买入金额
	private ImageView iv_usereward;// 奖金使用状态图标
	private String jiangjin;// 奖金
	private boolean isGou = true;// 当可点击时，奖金已经勾上
	private String baseBal;//账户余额
	private String seqNo;


	private TextView tv_zhanghuyue;//账户余额
	private TextView tv_chongzhi;//充值
	private String buyMoney;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fanhebuyyxg);

		progressdialog = new CustomProgressDialog(this, "买入确认中...");
		initView();
		initData();
	}

	//FanHeBuyYxgActivity启动模式是singleTask，充值成功返回后会回调此方法
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		//更新账户余额
		baseBal = intent.getStringExtra("baseBal");
		tv_zhanghuyue.setText(baseBal+"元");
		//根据输入的金额改变按钮状态
		if (StrToNumber.strTodouble(buyMoney) > StrToNumber.strTodouble(baseBal)) {
			bt_buy.setText("余额不足，立即去充值");
		}else{
			bt_buy.setText("立即买入");
		}
	}

	private void initView() {
		get_back = (TextView) findViewById(R.id.get_back);// 返回
		tv_jiangjin = (TextView) findViewById(R.id.tv_jiangjin);// 奖金余额
		tv_zong = (TextView) findViewById(R.id.tv_zong);// 买入总计
		tv_buy_money = (TextView) findViewById(R.id.tv_buy_money);//买入金额
		get_back.setOnClickListener(this);
		iv_usereward = (ImageView) findViewById(R.id.iv_usereward);// 奖金使用状态图标
		iv_usereward.setOnClickListener(this);
		bt_buy = (Button) findViewById(R.id.bt_buy);// 确认提现按钮
		bt_buy.setOnClickListener(this);


		tv_zhanghuyue = (TextView) findViewById(R.id.tv_zhanghuyue);//账户余额
		tv_chongzhi = (TextView) findViewById(R.id.tv_chongzhi);//充值
		tv_chongzhi.setOnClickListener(this);

	}

	private void initData() {
		//从买入第一步中拿到交易流水号
		Intent intent = getIntent();
		seqNo = intent.getStringExtra("seqNo");
		//账户余额
		baseBal = intent.getStringExtra("baseBal");
		tv_zhanghuyue.setText(baseBal+"元");

		//优先购金额
		buyMoney = intent.getStringExtra("amount");
		tv_buy_money.setText(buyMoney+"（优先购）");

		//获取到奖金
		jiangjin = CacheUtils.getString(this, CacheUtils.REWARDACCTBAL, "");
		tv_jiangjin.setText(MathUtil.subString(jiangjin, 2)+"元");
		

		if (!TextUtils.isEmpty(buyMoney)) {
			//根据输入的金额改变奖金和买入总计
			if (StrToNumber.strTodouble(buyMoney) >=
					StrToNumber.strTodouble(CacheUtils.getString(FanHeBuyYxgActivity.this, CacheUtils.REWARDACCTBAL, ""))) {
				iv_usereward.setImageResource(R.drawable.usereward_yes);
				// 买入总计
				tv_zong.setText(MathUtil.subDouble((StrToNumber.strTodouble(buyMoney)
						+ StrToNumber.strTodouble(CacheUtils.getString(FanHeBuyYxgActivity.this, CacheUtils.REWARDACCTBAL, ""))), 2) + "元");
				//勾选，获取奖金值
				jiangjin = CacheUtils.getString(FanHeBuyYxgActivity.this, CacheUtils.REWARDACCTBAL, "");
			} else {//不勾选奖金
				iv_usereward.setImageResource(R.drawable.usereward);
				// 买入总计
				tv_zong.setText(MathUtil.subDouble(StrToNumber.strTodouble(buyMoney), 2)+"元");
				//不勾选，把奖金改为0
				jiangjin = "0.00";
			}

			//根据输入的金额改变按钮状态
			if (StrToNumber.strTodouble(buyMoney) > StrToNumber.strTodouble(baseBal)) {
				bt_buy.setText("余额不足，立即去充值");
			}else{
				bt_buy.setText("立即买入");
			}

		} else {
			// 买入总计
			tv_zong.setText("0.00元");
		}
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
			if (StrToNumber.strTodouble(buyMoney) >=
					StrToNumber.strTodouble(CacheUtils.getString(FanHeBuyYxgActivity.this, CacheUtils.REWARDACCTBAL, ""))) {
				isGou = !isGou;
				CacheUtils.putBoolean(this, "isGou", isGou);
				if (isGou) {//勾选奖金
					iv_usereward.setImageResource(R.drawable.usereward_yes);
					//买入总计
					tv_zong.setText(MathUtil.subDouble((StrToNumber.strTodouble(buyMoney)+
							StrToNumber.strTodouble(CacheUtils.getString(FanHeBuyYxgActivity.this, CacheUtils.REWARDACCTBAL, ""))),2)+"元");
					//勾选，获取奖金值
					jiangjin = CacheUtils.getString(FanHeBuyYxgActivity.this, CacheUtils.REWARDACCTBAL, "");
				} else {//不勾选奖金
					iv_usereward.setImageResource(R.drawable.usereward);
					//买入总计
					tv_zong.setText(MathUtil.subDouble(StrToNumber.strTodouble(buyMoney), 2)+"元");
					//不勾选，把奖金改为0
					jiangjin = "0.00";
				}
			}
			break;
		case R.id.tv_chongzhi:
			Intent intent2 = new Intent(FanHeBuyYxgActivity.this, ChongZhiActivity.class);
			intent2.putExtra("from","fhyxg");
			startActivity(intent2);
			break;
		case R.id.bt_buy:

			//根据输入的金额，点击按钮进入不同页面
			if (StrToNumber.strTodouble(buyMoney) > StrToNumber.strTodouble(baseBal)) {
				Intent intent3 = new Intent(FanHeBuyYxgActivity.this, ChongZhiActivity.class);
				intent3.putExtra("from","fhyxg");
				startActivity(intent3);
			}else{
				getDataFromServer(buyMoney, jiangjin, seqNo);
			}

			break;
		default:
			break;
		}
	}

	private void getDataFromServer(String buyMoney, String jiangjin, String seqNo) {
		final long startTime = System.currentTimeMillis();
		if(!progressdialog.isShowing()){
			progressdialog.show();
		}
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(FanHeBuyYxgActivity.this, "token", null);
		map.put("token", token);
		map.put("amount", buyMoney);
		map.put("reward", jiangjin);
		map.put("seqNo", seqNo);
		LogUtils.i("amount=="+buyMoney+"reward=="+jiangjin+"seqNo=="+seqNo);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.SUBMITINVEST_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						LogUtils.i("饭盒立即买入==="+string);
						long endTime = System.currentTimeMillis();
						if(endTime-startTime>12000){
							progressdialog.dismiss();
							Intent intent = new Intent(FanHeBuyYxgActivity.this,BuyShenQingActivity.class);
							startActivity(intent);
						}else{
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

										Intent intent = new Intent(FanHeBuyYxgActivity.this,FanHeBuySuccessActivity.class);
										intent.putExtra("zong_money", investTotalAmount);
										startActivity(intent);

									} else {
										ToastUtils.toastshort("加载数据异常！");
									}
								}
							}else if ("4003".equals(code)) {

								Intent intent = new Intent(FanHeBuyYxgActivity.this,BuyShenQingActivity.class);
								startActivity(intent);

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
