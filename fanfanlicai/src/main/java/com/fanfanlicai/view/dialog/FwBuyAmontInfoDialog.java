package com.fanfanlicai.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fanfanlicai.fanfanlicai.R;

/**
 * @author lijinliu
 * @date 20180205
 * 饭碗买入 剩余金额小于最小买入金额提示全部买了
 */
public class FwBuyAmontInfoDialog extends Dialog implements
		View.OnClickListener {

	public FwBuyAmontInfoDialog(Context context) {
		super(context);
	}

	public FwBuyAmontInfoDialog(Context context, int theme) {
		super(context, theme);
	}

	public FwBuyAmontInfoDialog(Context context, int theme, String msg) {
		super(context, theme);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_fwbuy_info);
		
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

