
package com.fanfanlicai.activity.invest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.fanfanlicai.R;

public class FengKongActivity extends BaseActivity implements OnClickListener {

	private TextView get_back;// 返回
	private TextView tv_title;// 标题
	private TextView tv_content;// 内容

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fengkong);

		initView();
		initData();
	}

	private void initView() {
		get_back = (TextView) findViewById(R.id.get_back);// 返回
		get_back.setOnClickListener(this);
		tv_title = (TextView) findViewById(R.id.tv_title);// 标题
		tv_content = (TextView) findViewById(R.id.tv_content);// 内容

	}

	private void initData() {
		Intent intent = getIntent();
		String title = intent.getStringExtra("title");
		tv_title.setText(title);
		String content = intent.getStringExtra("content");
		tv_content.setText(content);
	}


	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_back:
			finish();
			break;
		}
	}
}
