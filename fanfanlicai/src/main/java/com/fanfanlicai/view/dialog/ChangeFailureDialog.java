package com.fanfanlicai.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fanfanlicai.activity.MainActivity;
import com.fanfanlicai.activity.my.CGBankChangeActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.utils.ConstantUtils;


public class ChangeFailureDialog extends Dialog implements View.OnClickListener {

	private String msg;
	private String realName;
	private String idNo;
	public ChangeFailureDialog(Context context) {
		super(context);
	}

	public ChangeFailureDialog(Context context, int theme, String msg, String realName, String idNo) {
		super(context, theme);
		this.msg = msg;
		this.realName = realName;
		this.idNo = idNo;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_changefailure);
		initDialog();
	}



	private void initDialog() {
		TextView btn_ok = (TextView) findViewById(R.id.btn_ok);
		TextView btn_cancel = (TextView) findViewById(R.id.btn_cancel);
		btn_ok.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		TextView tv_content = (TextView) findViewById(R.id.tv_content);
		tv_content.setText(msg);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			dismiss();
			Intent intent = new Intent(getContext(), CGBankChangeActivity.class);
			intent.putExtra("realName",realName);
			intent.putExtra("idNo",idNo);
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

