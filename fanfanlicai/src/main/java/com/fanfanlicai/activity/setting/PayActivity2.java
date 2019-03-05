package com.fanfanlicai.activity.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.view.password.GridPasswordView;
import com.fanfanlicai.view.password.GridPasswordView.OnPasswordChangedListener;
import com.fanfanlicai.view.password.PasswordType;

public class PayActivity2 extends BaseActivity implements OnClickListener {
	protected static final String NEWPASSWORD = null;
	private GridPasswordView gpv_length;
	private TextView tv_mima;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pay);

		initView();
		initData();
	}

	private void initView() {
		tv_mima = (TextView) findViewById(R.id.tv_mima);
		TextView pay_back = (TextView) findViewById(R.id.pay_back);
		pay_back.setOnClickListener(this);
		gpv_length = (GridPasswordView) findViewById(R.id.gpv_length);
		gpv_length.setPasswordType(PasswordType.NUMBER);

	}

	private void initData() {
		tv_mima.setText("设置6位数字提现密码");
		gpv_length.setOnPasswordChangedListener(new OnPasswordChangedListener() {
			
			@Override
			public void onTextChanged(String psw) {
				
			}
			
			@Override
			public void onInputFinish(String psw) {
				String newPassWord = gpv_length.getPassWord();
				CacheUtils.putString(getApplicationContext(), NEWPASSWORD, newPassWord);
				startActivity(new Intent(PayActivity2.this,PayActivity3.class));
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