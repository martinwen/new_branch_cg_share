package com.fanfanlicai.fragment.setting;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.fragment.BaseFragment;
import com.fanfanlicai.sign.Base64;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.StringUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;
import com.fanfanlicai.view.ProgressBar.RoundProgressBar;
import com.fanfanlicai.view.dialog.CustomProgressDialog;
import com.fanfanlicai.view.dialog.YzmDialog;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginEmailFragment extends BaseFragment implements View.OnClickListener,TextWatcher {

	private EditText et_password;
	private EditText et_new_password;
	private EditText et_new_confirm;
	private EditText et_yanzhengma;
	private ImageView iv_eye;
	private ImageView iv_eye1;
	private ImageView iv_eye2;
	private boolean isOpen = false;
	private boolean isOpen1 = false;
	private boolean isOpen2 = false;
	private Button bt_password;

	private CustomProgressDialog progressdialog;
	private ImageView iv_yanzhengma;
	private TextView tv_count;
	private MyCount myCount;
	private RoundProgressBar pw_spinner;
	private String email;
	private String randomId;

	@Override
	protected View initView() {
		email = CacheUtils.getString(mActivity, CacheUtils.EMAIL, "");
		progressdialog = new CustomProgressDialog(mActivity, "正在加载数据...");

		View view = View.inflate(mActivity, R.layout.fragment_loginemail, null);
		et_password = (EditText) view.findViewById(R.id.et_password);//原密码
		et_new_password = (EditText) view.findViewById(R.id.et_new_password);//新密码
		et_new_confirm = (EditText) view.findViewById(R.id.et_new_confirm);//确认新密码
		et_yanzhengma = (EditText) view.findViewById(R.id.et_yanzhengma);//验证码
		iv_eye = (ImageView) view.findViewById(R.id.iv_eye);
		et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

		iv_eye1 = (ImageView) view.findViewById(R.id.iv_eye1);
		et_new_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

		iv_eye2 = (ImageView) view.findViewById(R.id.iv_eye2);
		et_new_confirm.setTransformationMethod(PasswordTransformationMethod.getInstance());

		bt_password = (Button) view.findViewById(R.id.bt_password);
		iv_yanzhengma = (ImageView) view.findViewById(R.id.iv_yanzhengma);// 获取验证码图片
		iv_yanzhengma.setOnClickListener(this);
		tv_count = (TextView) view.findViewById(R.id.tv_count);// 倒计时
		pw_spinner = (RoundProgressBar) view.findViewById(R.id.pw_spinner);// 倒计时圆弧进度条

		iv_eye.setOnClickListener(this);
		iv_eye1.setOnClickListener(this);
		iv_eye2.setOnClickListener(this);
		bt_password.setOnClickListener(this);

		et_password.addTextChangedListener(this);
		et_new_password.addTextChangedListener(this);
		et_new_confirm.addTextChangedListener(this);
		et_yanzhengma.addTextChangedListener(this);
		return view;
	}

	@Override
	public void initData() {
		LogUtils.i("登录密码===邮箱");
		// 默认按钮不可点击
		bt_password.setBackgroundResource(R.drawable.anniu_chang_bukedianji);
		bt_password.setClickable(false);
		// 原密码输入框失去焦点监听，当输入不正确时，toast提示
//		et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				// TODO Auto-generated method stub
//				String mima = et_password.getText().toString().trim();
//				String loginPassWord = CacheUtils.getString(mActivity, CacheUtils.LOGINPASSWORD, "");
//				if (hasFocus) {
//				} else {
//					if (!isMima(mima)) {
//						ToastUtils.toastshort("密码长度需在6—15位之间");
//					}
//				}
//			}
//		});

		// 新密码输入框失去焦点监听，当输入格式不符合时，toast提示
		et_new_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				String mima = et_new_password.getText().toString().trim();
				if (hasFocus) {
				} else {
//					if (!isMima(mima)) {
//						ToastUtils.toastshort("密码长度应在6-15位之间");
//					}
//					if (isContainChinese(mima)) {
//						ToastUtils.toastshort("密码不能包含中文");
//					}
				}
			}
		});

		// 新密码确认框失去焦点监听，当输入格式不符合时，toast提示
		et_new_confirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				String mima = et_new_confirm.getText().toString().trim();
				if (hasFocus) {
				} else {
//					if (!isMima(mima)) {
//						ToastUtils.toastshort("密码长度应在6-15位之间");
//					}
//					if (isContainChinese(mima)) {
//						ToastUtils.toastshort("密码不能包含中文");
//					}
				}
			}
		});
	}

	public static boolean isMima(String mima) {
		return mima.length() >= 6 && mima.length() <= 15;
	}

	public static boolean isContainChinese(String str) {

		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	//为了使所有edittext不为空时按钮才可点击，实现了TextWatcher接口，下面三个方法是必须实现的
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {

		if (StringUtils.isNotBlank(et_password.getText().toString())
				&&StringUtils.isNotBlank(et_new_password.getText().toString())
				&&StringUtils.isNotBlank(et_new_confirm.getText().toString())
				&&StringUtils.isNotBlank(et_yanzhengma.getText().toString())
				)
		{
			//当输入框都不为空时，按钮可点击
			bt_password.setBackgroundResource(R.drawable.button_long_selector);
			bt_password.setPressed(false);
			bt_password.setClickable(true);
		}else {
			bt_password.setBackgroundResource(R.drawable.anniu_chang_bukedianji);
			bt_password.setClickable(false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.bt_password:
				String Pwd = et_password.getText().toString().trim();
				String inputPwd = et_new_password.getText().toString().trim();
				String confirmPwd = et_new_confirm.getText().toString().trim();
				String code = et_yanzhengma.getText().toString().trim();
				// 先判断原密码不能为空
				if (TextUtils.isEmpty(Pwd)) {
					ToastUtils.toastshort("信息填写不完整");
					// 让EditText获取焦点
					et_password.requestFocus();
					return;
				}
				// 新密码不能为空
				if (TextUtils.isEmpty(inputPwd)) {
					ToastUtils.toastshort("信息填写不完整");
					// 让EditText获取焦点
					et_new_password.requestFocus();
					return;
				}
				// 确认密码不能为空
				if (TextUtils.isEmpty(confirmPwd)) {
					ToastUtils.toastshort("信息填写不完整");
					// 让EditText获取焦点
					et_new_confirm.requestFocus();
					return;
				}
				// 验证码不能为空
				if (TextUtils.isEmpty(code)) {
					ToastUtils.toastshort("请点击获取验证码");
					// 让EditText获取焦点
					et_yanzhengma.requestFocus();
					return;
				}
				// 判断原密码和新密码是否相同
				if (TextUtils.equals(Pwd, inputPwd)) {
					ToastUtils.toastshort("新密码与原密码相同,请重新输入");
					et_new_password.requestFocus();
					return;
				}
				// 判断两个新密码是否相同
				if (!TextUtils.equals(inputPwd, confirmPwd)) {
					ToastUtils.toastshort("两次输入的密码不一致");
					et_new_confirm.requestFocus();
					return;
				}
				requestServer(Pwd,confirmPwd,code);
				break;

			case R.id.iv_eye://密码的显示与隐藏
				isOpen = !isOpen;
				if (isOpen) {//打开眼睛图片，显示密码
					iv_eye.setImageResource(R.drawable.eye_open2);
					et_password.setTransformationMethod(HideReturnsTransformationMethod
							.getInstance());
				} else {//关闭眼睛图片，隐藏密码
					iv_eye.setImageResource(R.drawable.eye_close2);
					et_password.setTransformationMethod(PasswordTransformationMethod
							.getInstance());
				}
				// 切换后将EditText光标置于末尾
				CharSequence charSequence = et_new_password.getText();
				if (charSequence instanceof Spannable) {
					Spannable spanText = (Spannable) charSequence;
					Selection.setSelection(spanText, charSequence.length());
				}
				break;
			case R.id.iv_eye1://密码的显示与隐藏
				isOpen1 = !isOpen1;
				if (isOpen1) {//打开眼睛图片，显示密码
					iv_eye1.setImageResource(R.drawable.eye_open2);
					et_new_password.setTransformationMethod(HideReturnsTransformationMethod
							.getInstance());
				} else {//关闭眼睛图片，隐藏密码
					iv_eye1.setImageResource(R.drawable.eye_close2);
					et_new_password.setTransformationMethod(PasswordTransformationMethod
							.getInstance());
				}
				// 切换后将EditText光标置于末尾
				CharSequence charSequence1 = et_new_password.getText();
				if (charSequence1 instanceof Spannable) {
					Spannable spanText = (Spannable) charSequence1;
					Selection.setSelection(spanText, charSequence1.length());
				}
				break;
			case R.id.iv_eye2://密码的显示与隐藏
				isOpen2 = !isOpen2;
				if (isOpen2) {//打开眼睛图片，显示密码
					iv_eye2.setImageResource(R.drawable.eye_open2);
					et_new_confirm.setTransformationMethod(HideReturnsTransformationMethod
							.getInstance());
				} else {//关闭眼睛图片，隐藏密码
					iv_eye2.setImageResource(R.drawable.eye_close2);
					et_new_confirm.setTransformationMethod(PasswordTransformationMethod
							.getInstance());
				}
				// 切换后将EditText光标置于末尾
				CharSequence charSequence2 = et_new_confirm.getText();
				if (charSequence2 instanceof Spannable) {
					Spannable spanText = (Spannable) charSequence2;
					Selection.setSelection(spanText, charSequence2.length());
				}
				break;
			case R.id.iv_yanzhengma:// 点击获取验证码，弹出dialog输入提示
				YzmDialog yzmDialog_sms = new YzmDialog(mActivity, R.style.YzmDialog, email);
				yzmDialog_sms
						.setOnYzmDialogDismissListener(new YzmDialog.OnYzmDialogDismissListener() {

							@Override
							public void OnYzmDialogDismiss(String answer) {
								// TODO Auto-generated method stub
								progressdialog.show();
								String token = CacheUtils.getString(mActivity, "token", null);
								Map<String, String> map = SortRequestData.getmap();
								map.put("email", email);
								map.put("sendType", 2+"");
								map.put("codeType", 3+"");
								map.put("captchaResult", answer);
								map.put("token",token);
								String requestData = SortRequestData.sortString(map);
								String signData = SignUtil.sign(requestData);
								map.put("sign", signData);
								VolleyUtil.sendJsonRequestByPost(ConstantUtils.SMS_URL, null, map, new HttpBackBaseListener() {

									@Override
									public void onSuccess(String string) {
										LogUtils.i("string=="+string);
										progressdialog.dismiss();
										JSONObject json = JSON.parseObject(string);
										String code = json.getString("code");
										if("0".equals(code)){
											String datastr = json.getString("data");
											JSONObject data = JSON.parseObject(datastr);
											randomId = data.getString("randomId");

											iv_yanzhengma.setVisibility(View.GONE);
											myCount = new MyCount(60000, 1000);// 设置倒计时时间为60秒，间隔为1秒
											myCount.start();
										}else{
											String msg = json.getString("msg");
											ToastUtils.toastshort(msg);
										}
									}

									@Override
									public void onError(VolleyError error) {
										progressdialog.dismiss();
										ToastUtils.toastshort("获取验证码失败");
									}
								});
							}
						});
				yzmDialog_sms.show();

				break;
		}
	}


	private void requestServer(String loginPassWord, final String confirmPwd, String code) {
		progressdialog.show();
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(mActivity, "token", null);
		map.put("code", code);
		map.put("sendType", "2");
		map.put("token",token);
		map.put("randomId",randomId);
		map.put("newPass",Base64.encode(SignUtil.encrypt(confirmPwd)));
		map.put("oldPass",Base64.encode(SignUtil.encrypt(loginPassWord)));
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.UPDATEPWD_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						progressdialog.dismiss();
						JSONObject json = JSON.parseObject(string);
						String  code= json.getString("code");
						if("0".equals(code)){
							CacheUtils.putString(mActivity, CacheUtils.LOGINPASSWORD, Base64.encode(SignUtil.encrypt(confirmPwd)));
							ToastUtils.toastshort("修改成功");
							getActivity().finish();

						}else{
							String msg = json.getString("msg");
							ToastUtils.toastshort(msg);
						}
					}

					@Override
					public void onError(VolleyError error) {
						progressdialog.dismiss();
						ToastUtils.toastshort("修改密码失败");
					}
				});

	}

	class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			//倒计时完要做的事情
			tv_count.setEnabled(true);
			tv_count.setClickable(true);
			iv_yanzhengma.setImageResource(R.drawable.yanzhengmaagain);//改为重新获取图片
			iv_yanzhengma.setVisibility(View.VISIBLE);//让重新获取图片显示
		}

		@Override
		public void onTick(long millisUntilFinished) {
			tv_count.setText(millisUntilFinished / 1000 + "");//60秒倒计时
			tv_count.setTextSize(10);
			pw_spinner.setProgress((int)(millisUntilFinished / 1000));//圆弧进度条
			tv_count.setClickable(false);
		}

	}

}
