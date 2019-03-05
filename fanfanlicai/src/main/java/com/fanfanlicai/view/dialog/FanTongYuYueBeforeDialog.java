package com.fanfanlicai.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fanfanlicai.activity.invest.FanTongQuestionActivity;
import com.fanfanlicai.fanfanlicai.R;


public class FanTongYuYueBeforeDialog extends Dialog implements
		View.OnClickListener {

	private String minBookAmount;
	private String maxBookAmount;
	private String minInvestAmount;
	private double baseRate;
	private double addRate;

	public FanTongYuYueBeforeDialog(Context context) {
		super(context);
	}

	public FanTongYuYueBeforeDialog(Context context, int theme) {
		super(context, theme);
	}

	public FanTongYuYueBeforeDialog(Context context, int theme, String minBookAmount, String maxBookAmount, String minInvestAmount, double baseRate, double addRate) {
		super(context, theme);
		this.minBookAmount = minBookAmount;
		this.maxBookAmount = maxBookAmount;
		this.minInvestAmount = minInvestAmount;
		this.baseRate = baseRate;
		this.addRate = addRate;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_fantongyuyuebefore);
		
		initDialog();
	}
	
	
  
	private void initDialog() {
		TextView btnOk = (TextView) findViewById(R.id.btn_ok);
		btnOk.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			 dismiss();
			Intent intent = new Intent(getContext(), FanTongQuestionActivity.class);
			intent.putExtra("minBookAmount", minBookAmount);
			intent.putExtra("maxBookAmount", maxBookAmount);
			intent.putExtra("minInvestAmount", minInvestAmount);
			intent.putExtra("baseRate", baseRate);
			intent.putExtra("addRate", addRate);
			getContext().startActivity(intent);
			 break;

		default:
			break;
		}
	}
	
}

