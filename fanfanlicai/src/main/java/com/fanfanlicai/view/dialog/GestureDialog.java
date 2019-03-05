package com.fanfanlicai.view.dialog;

import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.registerandlogin.HomeLoginActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.sign.Base64;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;
import com.fanfanlicai.view.lockPattern.GestureCreateActivity;

public class GestureDialog extends Dialog implements
		android.view.View.OnClickListener {

	private EditText et_mima;
	private TextView tv_mimatip;
	private TextView tv_mimaforget;
	private ImageView iv_eye2;
	private boolean isOpen2 = false;
	private CustomProgressDialog progressdialog;
	
    private OnGestureDialogDismissListener listener;
    
	public GestureDialog(Context context) {
		super(context);
	}

	public GestureDialog(Context context, int theme) {
		super(context, theme);
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_gesture);
		progressdialog = new CustomProgressDialog(getContext(), "正在验证......");
		initDialog();
	}

	private void initDialog() {
		tv_mimatip = (TextView) findViewById(R.id.tv_mimatip);
		tv_mimaforget = (TextView) findViewById(R.id.tv_mimaforget);
		iv_eye2 = (ImageView) findViewById(R.id.iv_eye2);
		et_mima = (EditText) findViewById(R.id.et_mima);
		et_mima.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		
		TextView btnOk = (TextView) findViewById(R.id.btn_ok);
		TextView btnCancel = (TextView) findViewById(R.id.btn_cancel);
		
		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		tv_mimaforget.setOnClickListener(this);
		iv_eye2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			String phone = CacheUtils.getString(getContext(), CacheUtils.LOGINPHONE,"");
			String password = et_mima.getText().toString().trim();
			if (TextUtils.isEmpty(password)) {
				ToastUtils.toastshort("密码不能为空");
				return;
			}
				
			requestServer(phone,password);
				
			break;
		case R.id.btn_cancel:
			dismiss();
			break;
		case R.id.tv_mimaforget:
			getContext().startActivity(new Intent(getContext(),HomeLoginActivity.class));
			break;
		case R.id.iv_eye2://密码的显示与隐藏
			isOpen2 = !isOpen2;
			if (isOpen2) {//打开眼睛图片，显示密码
				iv_eye2.setImageResource(R.drawable.eye_open2);
				et_mima.setTransformationMethod(HideReturnsTransformationMethod
						.getInstance());
			} else {//关闭眼睛图片，隐藏密码
				iv_eye2.setImageResource(R.drawable.eye_close2);
				et_mima.setTransformationMethod(PasswordTransformationMethod
						.getInstance());
			}
			// 切换后将EditText光标置于末尾
			CharSequence charSequence2 = et_mima.getText();
			if (charSequence2 instanceof Spannable) {
				Spannable spanText = (Spannable) charSequence2;
				Selection.setSelection(spanText, charSequence2.length());
			}
			break;
		default:
			break;
		}
	}
	private void requestServer(String phone, String password) {
		
		progressdialog.show();
		Map<String, String> map =  SortRequestData.getmap();
		map.put("phone", phone);
		map.put("password", Base64.encode(SignUtil.encrypt(password)));
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
      
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.MIMA_LOGIN_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						progressdialog.dismiss();

						JSONObject json = JSON.parseObject(string);
						String  code= json.getString("code");
						if("0".equals(code)){
							dismiss();
							if (listener != null) {
								listener.OnGestureDialogDismiss();
							}
						}else{
							tv_mimatip.setVisibility(View.VISIBLE);
							tv_mimaforget.setVisibility(View.VISIBLE);
//							String msg = json.getString("msg");
							ToastUtils.toastshort("您输入的密码有误，请重新输入");
						}
					}

					@Override
					public void onError(VolleyError error) {
						progressdialog.dismiss();
						ToastUtils.toastshort("网络错误，验证失败");
					}
				});
	}
	public interface OnGestureDialogDismissListener{
    	void OnGestureDialogDismiss();
    }
	
	public void setOnGestureDialogDismissListener(OnGestureDialogDismissListener listener) {
		this.listener = listener;
	}
}

