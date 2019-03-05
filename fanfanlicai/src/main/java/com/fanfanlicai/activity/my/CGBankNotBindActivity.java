package com.fanfanlicai.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.view.dialog.CustomProgressDialog;

public class CGBankNotBindActivity extends BaseActivity implements OnClickListener {

	private CustomProgressDialog progressdialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cgbanknotbind);

		progressdialog = new CustomProgressDialog(CGBankNotBindActivity.this, "正在加载数据...");
		initView();
		initData();
	}

	private void initView() {
		TextView bank_back = (TextView) findViewById(R.id.bank_back);
		bank_back.setOnClickListener(this);
		RelativeLayout rl_bank = (RelativeLayout) findViewById(R.id.rl_bank);
		rl_bank.setOnClickListener(this);
	}

	private void initData() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bank_back:
			finish();
			break;
		case R.id.rl_bank:
			Intent intent = new Intent(this, CGBindBankActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	
}
