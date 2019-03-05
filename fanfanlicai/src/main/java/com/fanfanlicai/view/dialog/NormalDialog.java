package com.fanfanlicai.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fanfanlicai.fanfanlicai.R;

public class NormalDialog extends Dialog implements
		android.view.View.OnClickListener {

	private String msg; 
	private OnNormalDialogDismissListener listener;
	
	public NormalDialog(Context context) {
		super(context);
	}
	
	public NormalDialog(Context context, int theme, String msg) {
		super(context, theme);
		this.msg = msg;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_normal);
		
		initDialog();
	}
	
	
  
	private void initDialog() {
		TextView btnOk = (TextView) findViewById(R.id.btn_ok);
		btnOk.setText("我知道了");
		btnOk.setOnClickListener(this);
		TextView tv_content = (TextView) findViewById(R.id.tv_content);
		tv_content.setText(msg);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			 dismiss();
			 if (listener!=null) {
				 listener.OnNormalDialogDismiss();
			}
			 break;

		default:
			break;
		}
	}
	
	public interface OnNormalDialogDismissListener{
    	void OnNormalDialogDismiss();
    }
	
	public void setOnNormalDialogDismissListener(OnNormalDialogDismissListener listener) {
		this.listener = listener;
	}
}

