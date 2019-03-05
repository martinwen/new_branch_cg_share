package com.fanfanlicai.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fanfanlicai.activity.setting.SettingForCePingActivity;
import com.fanfanlicai.fanfanlicai.R;

public class CePingNotDialog extends Dialog implements
		View.OnClickListener {

	public CePingNotDialog(Context context) {
		super(context);
	}

	public CePingNotDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_ceping);
		
		initDialog();
	}
	
	
  
	private void initDialog() {
		TextView btn_cancel = (TextView) findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);
		TextView btnOk = (TextView) findViewById(R.id.btn_ok);
		btnOk.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok://去评测
			 dismiss();
			Intent intent = new Intent(getContext(), SettingForCePingActivity.class);
			getContext().startActivity(intent);
			 break;
		case R.id.btn_cancel://取消
			 dismiss();
			 break;

		default:
			break;
		}
	}
}

