package com.fanfanlicai.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fanfanlicai.activity.setting.SettingForCePingActivity;
import com.fanfanlicai.fanfanlicai.R;

import static com.fanfanlicai.fanfanlicai.R.id.btn_cancel;


public class CePingAgainDialog extends Dialog implements
		View.OnClickListener {

	private String overTimes;

	public CePingAgainDialog(Context context) {
		super(context);
	}

	public CePingAgainDialog(Context context, int theme) {
		super(context, theme);
	}

	public CePingAgainDialog(Context context, int theme, String overTimes) {
		super(context, theme);
		this.overTimes = overTimes;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_cepingagain);
		
		initDialog();
	}
	
	
  
	private void initDialog() {
		TextView tv_content = (TextView) findViewById(R.id.tv_content);
		tv_content.setText("您上次的评测结果是保守型，风险承受能力未满足平台要求，暂无法出借。您还有"+overTimes+"次评测机会");
		TextView btn_cancel = (TextView) findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);
		TextView btn_ok = (TextView) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok://重新测评
			 dismiss();
			 Intent intent = new Intent(getContext(), SettingForCePingActivity.class);
			 getContext().startActivity(intent);
			 break;
		case btn_cancel://取消
			 dismiss();
			 break;

		default:
			break;
		}
	}
}

