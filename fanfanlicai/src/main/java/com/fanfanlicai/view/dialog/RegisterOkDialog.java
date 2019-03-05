package com.fanfanlicai.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.global.FanFanApplication;
import com.fanfanlicai.view.lockPattern.ACache;
import com.fanfanlicai.view.lockPattern.GestureCreateActivity;

public class RegisterOkDialog extends Dialog {

	private OnRegisterDialogDismissListener listener;
	
	public RegisterOkDialog(Context context) {
		super(context);
	}

	public RegisterOkDialog(Context context, int theme) {
		super(context, theme);
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_registerok);
		
		//一秒后注册成功dialog自动消失,跳到手势密码设置界面
		FanFanApplication.mainHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				dismiss();
				if (listener != null) {
					listener.OnRegisterDialogDismiss();
				}
				Intent intent=new Intent(getContext(),GestureCreateActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置 
				getContext().startActivity(intent);
			}
		}, 1000);
		
	}
	
	public interface OnRegisterDialogDismissListener{
    	void OnRegisterDialogDismiss();
    }
	
	public void setOnRegisterDialogDismissListener(OnRegisterDialogDismissListener listener) {
		this.listener = listener;
	}
}

