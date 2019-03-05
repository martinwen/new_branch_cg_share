package com.fanfanlicai.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.view.dialog.CustomProgressDialog;
import com.fanfanlicai.view.password.GridPasswordView;
import com.fanfanlicai.view.password.GridPasswordView.OnPasswordChangedListener;
import com.fanfanlicai.view.password.PasswordType;

public class SetPassWordActivity extends BaseActivity implements OnClickListener {
	private GridPasswordView gpv_length;
	private TextView tv_mima;
	private CustomProgressDialog progressdialog;
	private String get_money;
	private String ticketId;
	private String useDefaultTicket;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pay);

		progressdialog = new CustomProgressDialog(this, "密码设置中，请稍后......");
		initView();
		initData();
	}

	private void initView() {
		get_money=getIntent().getStringExtra("get_money");
		ticketId=getIntent().getStringExtra("ticketId");
		useDefaultTicket=getIntent().getStringExtra("useDefaultTicket");

		tv_mima = (TextView) findViewById(R.id.tv_mima);
		TextView pay_back = (TextView) findViewById(R.id.pay_back);
		pay_back.setOnClickListener(this);
		gpv_length = (GridPasswordView) findViewById(R.id.gpv_length);
		gpv_length.setPasswordType(PasswordType.NUMBER);

	}

	private void initData() {
		tv_mima.setText("请输入当前6位数字交易密码");
		gpv_length.setOnPasswordChangedListener(new OnPasswordChangedListener() {
			
			@Override
			public void onTextChanged(String psw) {
				
			}
			
			@Override
			public void onInputFinish(String psw) {
				String newPassWord = gpv_length.getPassWord();
				Intent intent = new Intent(SetPassWordActivity.this,SetPassWordActivity2.class);
				intent.putExtra("newPassWord", newPassWord);
				intent.putExtra("get_money", get_money);
				intent.putExtra("ticketId", ticketId);
				intent.putExtra("useDefaultTicket", useDefaultTicket);
				startActivity(intent);
				finish();
			}
		});						
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pay_back:
			finish();
			break;

		default:
			break;
		}
	}
}
