
package com.fanfanlicai.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;

public class ZongShouYiActivity extends BaseActivity implements OnClickListener {

	private TextView get_back;// 返回
	private RelativeLayout rl_fanheleiji;
	private RelativeLayout rl_fanwanleiji;
	private RelativeLayout rl_fantongleiji;
	private TextView tv_zong;
	private TextView tv_fanhe;
	private TextView tv_fanwan;
	private TextView tv_fantong;
	private TextView tv_daishou;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_zongshouyi);

		initView();
		initData();
	}

	private void initView() {
		get_back = (TextView) findViewById(R.id.get_back);// 返回
		get_back.setOnClickListener(this);
		rl_fanheleiji = (RelativeLayout) findViewById(R.id.rl_fanheleiji);
		rl_fanheleiji.setOnClickListener(this);
		rl_fanwanleiji = (RelativeLayout) findViewById(R.id.rl_fanwanleiji);
		rl_fanwanleiji.setOnClickListener(this);
		rl_fantongleiji = (RelativeLayout) findViewById(R.id.rl_fantongleiji);
		rl_fantongleiji.setOnClickListener(this);
		tv_zong = (TextView) findViewById(R.id.tv_zong);
		tv_fanhe = (TextView) findViewById(R.id.tv_fanhe);
		tv_fanwan = (TextView) findViewById(R.id.tv_fanwan);
		tv_fantong = (TextView) findViewById(R.id.tv_fantong);
		tv_daishou = (TextView) findViewById(R.id.tv_daishou);
	}

	private void initData() {
		//总收益
		String totalIncome = CacheUtils.getString(this, CacheUtils.TOTALINCOME, "0.00");
		tv_zong.setText(totalIncome+"元");
		//饭盒累计收益
		String fhTotalIncome = CacheUtils.getString(this, CacheUtils.FHTOTALINCOME, "0.00");
		tv_fanhe.setText(fhTotalIncome+"元");
		//饭碗累计收益
		String fwTotalIncome = CacheUtils.getString(this, CacheUtils.FWTOTALINCOME, "0.00");
		tv_fanwan.setText(fwTotalIncome+"元");
		//饭桶累计收益
		String ftTotalIncome = CacheUtils.getString(this, CacheUtils.FTTOTALINCOME, "0.00");
		tv_fantong.setText(ftTotalIncome+"元");
		//饭碗待收收益
		String fwWaitRepayAmount = CacheUtils.getString(this, CacheUtils.FWWAITREPAYAMOUNT, "0.00");
		tv_daishou.setText(fwWaitRepayAmount+"元");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_back:
			finish();
			break;
		case R.id.rl_fanheleiji:
			startActivity(new Intent(this, LeiJiShouYiActivity.class));
			break;
		case R.id.rl_fanwanleiji:
			ConstantUtils.shouyiflag = 1;
			startActivity(new Intent(this, LeiJiShouYiActivity.class));
			break;
		case R.id.rl_fantongleiji:
			ConstantUtils.shouyiflag = 2;
			startActivity(new Intent(this, LeiJiShouYiActivity.class));
			break;
		default:
			break;
		}
	}
}
