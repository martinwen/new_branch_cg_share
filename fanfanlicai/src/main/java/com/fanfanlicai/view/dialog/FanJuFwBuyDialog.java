package com.fanfanlicai.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fanfanlicai.fanfanlicai.R;


public class FanJuFwBuyDialog extends Dialog implements View.OnClickListener {

	private OnFanJuFwBuyDialogDismissListener listener;

	public FanJuFwBuyDialog(Context context) {
		super(context);
	}

	public FanJuFwBuyDialog(Context context, int theme) {
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
			if (listener!=null) {
				listener.OnFanJuFwBuyDialogDismiss();
			}
			break;
		case R.id.btn_cancel:
			dismiss();
			break;

		default:
			break;
		}
	}

	public interface OnFanJuFwBuyDialogDismissListener{
		void OnFanJuFwBuyDialogDismiss();
	}

	public void setOnFanJuFwBuyDialogDismissListener(OnFanJuFwBuyDialogDismissListener listener) {
		this.listener = listener;
	}
}

