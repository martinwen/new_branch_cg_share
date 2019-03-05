package com.fanfanlicai.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fanfanlicai.activity.MainActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.utils.ConstantUtils;



public class FanJuDialog extends Dialog implements View.OnClickListener {


	public FanJuDialog(Context context) {
		super(context);
	}

	public FanJuDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_fanju);
		initDialog();
	}



	private void initDialog() {
		TextView btn_ok = (TextView) findViewById(R.id.btn_ok);
		TextView btn_cancel = (TextView) findViewById(R.id.btn_cancel);
		btn_ok.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			dismiss();
			//点击使用
			ConstantUtils.touziflag = 1;
			ConstantUtils.fanheorfanwanorfantongflag = 1;
			Intent intent = new Intent(getContext(), MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			getContext().startActivity(intent);
			break;
		case R.id.btn_cancel:
			dismiss();
			break;

		default:
			break;
		}
	}
	
}

