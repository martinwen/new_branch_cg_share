package com.fanfanlicai.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fanfanlicai.fanfanlicai.R;

public class PiPeiDialog extends Dialog implements
		View.OnClickListener {

	private String fhReMatchAmout;
	private OnNormalDialogDismissListener listener;

	public PiPeiDialog(Context context) {
		super(context);
	}

	public PiPeiDialog(Context context, int theme) {
		super(context);
	}

	public PiPeiDialog(Context context, int theme, String fhReMatchAmout) {
		super(context, theme);
		this.fhReMatchAmout = fhReMatchAmout;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_pipei);
		
		initDialog();
	}
	
	
  
	private void initDialog() {
		TextView btnOk = (TextView) findViewById(R.id.btn_ok);
		btnOk.setOnClickListener(this);
		TextView tv_content = (TextView) findViewById(R.id.tv_content);
		tv_content.setText("匹配中金额是累计未满"+fhReMatchAmout+"元的收益，收益累计满"+fhReMatchAmout+"元后，系统将自动为您复投");
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

