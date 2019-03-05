package com.fanfanlicai.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.utils.CacheUtils;

public class EmailHasBindActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_emailhasbind);
		
		initView();

	}

	private void initView() {
		TextView login_back = (TextView) findViewById(R.id.login_back);
		login_back.setOnClickListener(this);

		String email = CacheUtils.getString(this, CacheUtils.EMAIL,"");
		TextView tv_email = (TextView) findViewById(R.id.tv_email);//mail地址
		tv_email.setText(email);

		Button bt_password = (Button) findViewById(R.id.bt_password);
		bt_password.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_back:
			finish();
			break;
		case R.id.bt_password:
			finish();
			startActivity(new Intent(this,EmailActivity.class));
			break;
		}
	}
}
