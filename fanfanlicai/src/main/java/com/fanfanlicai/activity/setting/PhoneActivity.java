package com.fanfanlicai.activity.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.utils.CacheUtils;

public class PhoneActivity extends BaseActivity implements OnClickListener {

	private TextView phone_back;
	private TextView tv_phone;
	private TextView tv_change;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_phone);
		
		initView();
		String phone = CacheUtils.getString(this, CacheUtils.PHONE, "");
		tv_phone.setText(phone);

	}

	private void initView() {
		phone_back = (TextView) findViewById(R.id.phone_back);
		phone_back.setOnClickListener(this);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_change = (TextView) findViewById(R.id.tv_change);
		tv_change.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.phone_back:
			finish();
			break;
		case R.id.tv_change:
            startActivity(new Intent(this,PhoneChangeActivity.class));
			break;
		default:
			break;
		}
	}
}
