
package com.fanfanlicai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.fanfanlicai.fanfanlicai.R;

public class JieSuanActivity extends BaseActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_jiesuan);


		Intent intent = getIntent();
		String msg = intent.getStringExtra("msg");

		TextView tv_content = (TextView) findViewById(R.id.tv_content);
		tv_content.setText(msg);
		TextView btn_ok = (TextView) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

}
