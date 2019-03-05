package com.fanfanlicai.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fanfanlicai.fanfanlicai.R;

public class SettingOutDialog extends Dialog implements android.view.View.OnClickListener {

	private OnDialogDismissListener listener;
	
	public SettingOutDialog(Context context) {
		super(context);
	}

	public SettingOutDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_settingout);
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
			if (listener!=null) {
				listener.OnDialogDismiss();
			}
			break;
		case R.id.btn_cancel:
			dismiss();
			break;

		default:
			break;
		}
	}
	
	public interface OnDialogDismissListener{
    	void OnDialogDismiss();
    }
	
	public void setOnDialogDismissListener(OnDialogDismissListener onDialogDismissListener) {
		this.listener = onDialogDismissListener;
	}
}

