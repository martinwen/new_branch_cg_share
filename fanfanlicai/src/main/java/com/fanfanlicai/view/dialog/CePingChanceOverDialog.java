package com.fanfanlicai.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fanfanlicai.fanfanlicai.R;


public class CePingChanceOverDialog extends Dialog implements
		View.OnClickListener {

	public CePingChanceOverDialog(Context context) {
		super(context);
	}

	public CePingChanceOverDialog(Context context, int theme) {
		super(context, theme);
	}

	public CePingChanceOverDialog(Context context, int theme, String msg) {
		super(context, theme);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_cepingchanceover);
		
		initDialog();
	}
	
	
  
	private void initDialog() {
		TextView btn_ok = (TextView) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			 dismiss();
			 break;

		default:
			break;
		}
	}
	
}

