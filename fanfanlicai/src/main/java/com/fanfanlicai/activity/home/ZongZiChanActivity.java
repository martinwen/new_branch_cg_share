package com.fanfanlicai.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.activity.my.DirectInvestLiushuiActivity;
import com.fanfanlicai.activity.my.ShuHuiActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.MathUtil;

import static com.fanfanlicai.fanfanlicai.R.id.tv_tixian;

public class ZongZiChanActivity extends BaseActivity implements OnClickListener {

	private TextView total_back;//返回
	private TextView tv_zongzichan;//总资产
	private RelativeLayout rl_zhanghu;
	private RelativeLayout rl_fanhe;
	private RelativeLayout rl_fanwan;
	private RelativeLayout rl_fantong;
	private RelativeLayout rl_jiangjin;
	private RelativeLayout rl_shuhui;
	private TextView tv_zhanghuyue;//账户余额
	private TextView tv_fanhezichan;//饭盒资产
	private TextView tv_fanwanzichan;//饭碗资产
	private TextView tv_fantongzichan;//饭碗资产
	private TextView tv_jiangjinyue;//奖金余额
	private TextView tv_shuhui;//赎回
	private TextView tv_tixian;//提现中
	private TextView tv_daishou;//待收收益

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_zongzichan);

		initView();
		initData();
	}

	private void initView() {
		total_back = (TextView) findViewById(R.id.total_back);//返回
		tv_zongzichan = (TextView) findViewById(R.id.tv_zongzichan);//总资产
		rl_zhanghu = (RelativeLayout) findViewById(R.id.rl_zhanghu);//账户余额
		rl_fanhe = (RelativeLayout) findViewById(R.id.rl_fanhe);//饭盒资产
		rl_fanwan = (RelativeLayout) findViewById(R.id.rl_fanwan);//饭碗资产
		rl_fantong = (RelativeLayout) findViewById(R.id.rl_fantong);//饭桶资产
		rl_jiangjin = (RelativeLayout) findViewById(R.id.rl_jiangjin);//奖金余额
		rl_shuhui = (RelativeLayout) findViewById(R.id.rl_shuhui);//赎回中
		tv_zhanghuyue = (TextView) findViewById(R.id.tv_zhanghuyue);//账户余额
		tv_fanhezichan = (TextView) findViewById(R.id.tv_fanhezichan);//饭盒资产
		tv_fanwanzichan = (TextView) findViewById(R.id.tv_fanwanzichan);//饭碗资产
		tv_fantongzichan = (TextView) findViewById(R.id.tv_fantongzichan);//饭碗资产
		tv_jiangjinyue = (TextView) findViewById(R.id.tv_jiangjinyue);//奖金余额
		tv_jiangjinyue = (TextView) findViewById(R.id.tv_jiangjinyue);//奖金余额
		tv_daishou = (TextView) findViewById(R.id.tv_daishou);//待收收益
		tv_shuhui = (TextView) findViewById(R.id.tv_shuhui);//赎回中
		tv_tixian = (TextView) findViewById(R.id.tv_tixian);//提现中
		total_back.setOnClickListener(this);
		rl_zhanghu.setOnClickListener(this);
		rl_fanhe.setOnClickListener(this);
		rl_fanwan.setOnClickListener(this);
		rl_fantong.setOnClickListener(this);
		rl_jiangjin.setOnClickListener(this);
		rl_shuhui.setOnClickListener(this);
	}

	private void initData() {
		//总资产
		String totalAssets = CacheUtils.getString(this, CacheUtils.TOTALASSETS, "0.00");
		tv_zongzichan.setText(MathUtil.subString(totalAssets, 2)+"元");
		//账户余额
		String baseAcctBal = CacheUtils.getString(this, CacheUtils.BASEACCTBAL, "0.00");
		tv_zhanghuyue.setText(MathUtil.subString(baseAcctBal, 2)+"元");
		//饭盒资产
		String fhAcctBal = CacheUtils.getString(this, CacheUtils.FHACCTBAL, "0.00");
		tv_fanhezichan.setText(MathUtil.subString(fhAcctBal, 2)+"元");
		//饭碗资产
		String fwAcctBal = CacheUtils.getString(this, CacheUtils.FWACCTBAL, "0.00");
		tv_fanwanzichan.setText(MathUtil.subString(fwAcctBal, 2)+"元");
		//饭桶资产
		String ftAcctBal = CacheUtils.getString(this, CacheUtils.FTACCTBAL, "0.00");
		tv_fantongzichan.setText(MathUtil.subString(ftAcctBal, 2)+"元");
		// 奖金余额
		String rewardAcctBal = CacheUtils.getString(this, CacheUtils.REWARDACCTBAL, "0.00");
		tv_jiangjinyue.setText(MathUtil.subString(rewardAcctBal, 2)+"元");
		//饭碗待收收益
		String fwWaitPayIncome = CacheUtils.getString(this, CacheUtils.FWWAITPAYINCOME, "0.00");
		tv_daishou.setText(MathUtil.subString(fwWaitPayIncome, 2)+"元");
		//赎回中
		String redeemAmount = CacheUtils.getString(this, CacheUtils.REDEEMAMOUNT, "0.00");
		tv_shuhui.setText(MathUtil.subString(redeemAmount, 2)+"元");
		//体现中
		String cashingMoney = CacheUtils.getString(this, CacheUtils.CASHINGMONEY, "0.00");
		tv_tixian.setText(MathUtil.subString(cashingMoney, 2)+"元");
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.total_back:
			finish();
			break;
		case R.id.rl_zhanghu:
			Intent intent1 = new Intent(this,ZhangHuLiuShuiActivity.class);
			startActivity(intent1);
			break;
		case R.id.rl_fanhe:
			Intent intent2 = new Intent(this,FanHeLiuShuiActivity.class);
			startActivity(intent2);
			break;
		case R.id.rl_fanwan:
			Intent intent3 = new Intent(this,DirectInvestLiushuiActivity.class);
			startActivity(intent3);
			break;
		case R.id.rl_fantong:
			Intent intent4 = new Intent(this,FanTongLiuShuiActivity.class);
			startActivity(intent4);
			break;
		case R.id.rl_jiangjin:
			Intent intent5 = new Intent(this,JiangJinLiuShuiActivity.class);
			startActivity(intent5);
			break;
		case R.id.rl_shuhui:
			Intent intent6 = new Intent(this,ShuHuiActivity.class);
			startActivity(intent6);
			break;

		default:
			break;
		}
	}

}
