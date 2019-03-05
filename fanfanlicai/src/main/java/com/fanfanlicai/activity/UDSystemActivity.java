
package com.fanfanlicai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.fanfanlicai.fanfanlicai.R;

public class UDSystemActivity extends BaseActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_udsystem);
		
		
		Intent intent = getIntent();
		String upgInfo = intent.getStringExtra("upgInfo");

		TextView tv_info = (TextView) findViewById(R.id.tv_info);
		tv_info.setText(upgInfo);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	//	super.onBackPressed();
	}
}
