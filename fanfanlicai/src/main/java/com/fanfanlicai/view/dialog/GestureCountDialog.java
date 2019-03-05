package com.fanfanlicai.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fanfanlicai.fanfanlicai.R;

public class GestureCountDialog extends Dialog implements android.view.View.OnClickListener {

	private OnGestureCountDialogDismissListener listener;
	
	public GestureCountDialog(Context context) {
		super(context);
	}

	public GestureCountDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_gesturecount);
		initDialog();
	}



	private void initDialog() {
		TextView tv_loginagain = (TextView) findViewById(R.id.tv_loginagain);
		tv_loginagain.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_loginagain:
			dismiss();
			if (listener!=null) {
				listener.OnGestureCountDialogDismiss();
			}
			break;

		default:
			break;
		}
	}
	
	public interface OnGestureCountDialogDismissListener{
    	void OnGestureCountDialogDismiss();
    }
	
	public void setOnGestureCountDialogDismissListener(OnGestureCountDialogDismissListener onGestureCountDialogDismissListener) {
		this.listener = onGestureCountDialogDismissListener;
	}
}

