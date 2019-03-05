package com.fanfanlicai.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fanfanlicai.activity.my.CGBindBankActivity;
import com.fanfanlicai.fanfanlicai.R;

public class CGBindCardDialog extends Dialog implements
		View.OnClickListener {

	public CGBindCardDialog(Context context) {
		super(context);
	}

	public CGBindCardDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_cgbindcard);
		
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
		case R.id.btn_ok://去绑卡
			 dismiss();
			 Intent intent = new Intent(getContext(), CGBindBankActivity.class);
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

