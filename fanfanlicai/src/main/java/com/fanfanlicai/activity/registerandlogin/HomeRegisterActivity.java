package com.fanfanlicai.activity.registerandlogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.fanfanlicai.R;
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
import com.fanfanlicai.view.dialog.RegisterOkDialog;
import com.fanfanlicai.view.dialog.RegisterOkDialog.OnRegisterDialogDismissListener;
import com.fanfanlicai.view.dialog.YzmDialog;
import com.fanfanlicai.view.dialog.YzmDialog.OnYzmDialogDismissListener;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeRegisterActivity extends BaseActivity implements OnClickListener,TextWatcher {

	private boolean isGou = true;// 协议前面的对勾，默认勾上
	private boolean isOpen = false;// 密码后面的眼睛，默认密码可见
	private TextView register_back;// 返回
	private TextView tv_isregistered;// 是否已注册，去登录
	private TextView tv_count;// 倒计时
	private TextView tv_phone;// 注册手机
	private ImageView iv_cancel;// 注册手机号码后面的取消按钮
	private ImageView iv_gou;// 协议前面的对勾图片
	private ImageView iv_eye;// 密码后面的眼睛图片
	private ImageView iv_yanzhengma;// 获取验证码图片
	private ImageView iv_saoyisao;// 扫一扫图片
	private EditText et_phone;// 手机号输入框
	private EditText et_mima;// 设置密码框
	private Button bt_register;// 注册按钮
	private MyCount myCount;// 自定义倒计时类
	private RoundProgressBar pw_spinner;// 倒计时圆弧进度条
	private EditText et_yaoqing;// 邀请输入框
	private EditText et_yanzhengma;// 短信验证码输入框
	private TextView tv_yuyin;// 语音验证码
	private TextView tv_xieyi;// 饭饭金服协议
	private RelativeLayout rl_shownumber;// 验证码已发送至xxx号码
	private String phone;
	private String mima;
	private String yanzhengma;
	private String yaoqingma;
	private CustomProgressDialog progressdialog;
	private String randomId;
	
	// 接收并拦截短信的广播接收者
//	private BroadcastReceiver mSmsRecevier = new BroadcastReceiver() {
//		public void onReceive(Context context, Intent intent) {
//			// 拦截短信
//			Object[] objs = (Object[]) intent.getExtras().get("pdus");
//			for (Object obj : objs) {
//				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
//				// 获取短信内容
//				String messageBody = smsMessage.getMessageBody();
//				// 截取六位验证码，让其自动回显在验证码输入框
//				if (!messageBody.isEmpty()) {
//					int i = messageBody.lastIndexOf("验证码是");
//					if (i != -1) {
//						String substring = messageBody.substring(i + 4, i + 10);
//						et_yanzhengma.setText(substring);
//
//						// 让隐藏的验证码已发送至xxx号码显示出来，通知用户
//						tv_phone.setText(et_phone.getText().toString().trim());
//						rl_shownumber.setVisibility(View.VISIBLE);
//					}
//				}
//			}
//		}
//	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_homeregister);

		initView();
		initData();

		// 注册验证码短信广播
//		IntentFilter filter = new IntentFilter();
//		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
//		registerReceiver(mSmsRecevier, filter);
		
		progressdialog=new CustomProgressDialog(HomeRegisterActivity.this, "正在获取短信验证码...");
	}

//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		// 解除注册
//		unregisterReceiver(mSmsRecevier);
//	}

	private void initView() {
		register_back = (TextView) findViewById(R.id.register_back);// 返回
		tv_isregistered = (TextView) findViewById(R.id.tv_isregistered);// 是否已注册，去登录
		iv_eye = (ImageView) findViewById(R.id.iv_eye);// 眼睛图片
		et_phone = (EditText) findViewById(R.id.et_phone);// 手机号输入框
		et_mima = (EditText) findViewById(R.id.et_mima);// 设置密码
		et_mima.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		iv_yanzhengma = (ImageView) findViewById(R.id.iv_yanzhengma);// 获取验证码图片
		tv_count = (TextView) findViewById(R.id.tv_count);// 倒计时
		tv_phone = (TextView) findViewById(R.id.tv_phone);// 注册手机号
		pw_spinner = (RoundProgressBar) findViewById(R.id.pw_spinner);// 倒计时圆弧进度条
		bt_register = (Button) findViewById(R.id.bt_register);// 完成注册按钮
		iv_cancel = (ImageView) findViewById(R.id.iv_cancel);// 注册手机号码后面的取消按钮
		iv_gou = (ImageView) findViewById(R.id.iv_gou);// 服务协议前面的对勾图片
		iv_saoyisao = (ImageView) findViewById(R.id.iv_saoyisao);// 扫一扫
		et_yaoqing = (EditText) findViewById(R.id.et_yaoqing);// 邀请
		et_yanzhengma = (EditText) findViewById(R.id.et_yanzhengma);// 短信验证码输入框
		tv_yuyin = (TextView) findViewById(R.id.tv_yuyin);// 语音验证码
		tv_xieyi = (TextView) findViewById(R.id.tv_xieyi);// 饭饭金服协议
		rl_shownumber = (RelativeLayout) findViewById(R.id.rl_shownumber);// 验证码已发送至xxx号码
		
		register_back.setOnClickListener(this);
		tv_isregistered.setOnClickListener(this);
		iv_cancel.setOnClickListener(this);
		iv_gou.setOnClickListener(this);
		iv_eye.setOnClickListener(this);
		iv_yanzhengma.setOnClickListener(this);
		bt_register.setOnClickListener(this);
		tv_count.setOnClickListener(this);
		iv_saoyisao.setOnClickListener(this);
		tv_yuyin.setOnClickListener(this);
		tv_xieyi.setOnClickListener(this);
		
		et_phone.addTextChangedListener(this);  
		et_mima.addTextChangedListener(this);  
		et_yanzhengma.addTextChangedListener(this);  
		
		
		//解决进入设置提现密码界面没有弹出软键盘的问题
		et_phone.setFocusable(true);
		et_phone.setFocusableInTouchMode(true);
		et_phone.requestFocus();

		Timer timer = new Timer(); 
		timer.schedule(new TimerTask() { //让软键盘延时弹出，以更好的加载Activity

		public void run() { 
		InputMethodManager inputManager = (InputMethodManager)et_phone.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(et_phone, 0); 
		} 

		}, 500);
	}

	private void initData() {
		// 验证码已发送至xxx号码,使其隐藏
		rl_shownumber.setVisibility(View.INVISIBLE);
		//默认按钮不可点击
		bt_register.setBackgroundResource(R.drawable.anniu_chang_bukedianji);  
		bt_register.setClickable(false);  
		// 手机号输入框失去焦点监听，当输入格式不符合时，toast提示
		et_phone.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				phone = et_phone.getText().toString().trim();
				if (hasFocus) {
					
				} else {
					if (!isMobile(phone)) {
						ToastUtils.toastshort("手机号格式输入不正确");
					}else {
						//检查手机号是否存在
						checkPhone(phone);
					}
				}
			}
		});

		// 密码输入框失去焦点监听，当输入格式不符合时，toast提示
		et_mima.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				mima = et_mima.getText().toString().trim();
				if (hasFocus) {
				} else {
//					if (!isMima(mima)) {
//						ToastUtils.toastshort("密码长度需在6—15位之间");
//					}
//					if (isContainChinese(mima)) {
//						ToastUtils.toastshort("密码不能包含中文");
//					}
				}
			}
		});
		
	}

	//为了使所有edittext不为空时按钮才可点击，实现了TextWatcher接口，下面三个方法是必须实现的
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			
			if (StringUtils.isNotBlank(et_phone.getText().toString())
					&&StringUtils.isNotBlank(et_mima.getText().toString())
					&&StringUtils.isNotBlank(et_yanzhengma.getText().toString()))
			{
				//当输入框都不为空时，按钮可点击
				bt_register.setBackgroundResource(R.drawable.button_long_selector);
				bt_register.setPressed(false);  
				bt_register.setClickable(true);  
			}else {
				bt_register.setBackgroundResource(R.drawable.anniu_chang_bukedianji);  
				bt_register.setClickable(false);  
			}
		}
	
	//输入手机号框失去焦点后判断手机号是否存在
	protected void checkPhone(String phone) {
		Map<String, String> map = SortRequestData.getmap();
		map.put("phone", phone);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);

		VolleyUtil.sendJsonRequestByPost(ConstantUtils.VERIFYPHONE_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						JSONObject json = JSON.parseObject(string);
						String code = json.getString("code");
						if("0".equals(code)){
							//手机号没注册过
						}else{
							//手机号注册了或输入格式不正确等等情况
							String msg = json.getString("msg");
							ToastUtils.toastshort(msg);
						}
					}

					@Override
					public void onError(VolleyError error) {
						
					}
				});
	}

	 public static boolean isContainChinese(String str) {

	        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
	        Matcher m = p.matcher(str);
	        if (m.find()) {
	            return true;
	        }
	        return false;
	    }
	 
	public static boolean isMobile(String phone) {
		return phone.length() == 11;
	}
	public static boolean isMobileNO(String phone) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");  
		Matcher m = p.matcher(phone);  
		return m.matches();
	}

	public static boolean isMima(String mima) {
		return mima.length() >= 6 && mima.length() <= 15;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_back://返回
			finish();
			break;
		case R.id.iv_cancel:// 注册手机号码后面的取消按钮
			et_phone.getText().clear();
			break;
		case R.id.tv_xieyi://饭饭金服注册协议
			startActivity(new Intent(this,XieYiActivity.class));
			break;
		case R.id.iv_gou:
			isGou = !isGou;
			if (isGou) {
				iv_gou.setImageResource(R.drawable.gou_hou);
			} else {
				iv_gou.setImageResource(R.drawable.gou_qian);
			}
			break;
		case R.id.iv_eye:
			isOpen = !isOpen;
			if (isOpen) {
				iv_eye.setImageResource(R.drawable.eye_open2);
				et_mima.setTransformationMethod(HideReturnsTransformationMethod
						.getInstance());
			} else {
				iv_eye.setImageResource(R.drawable.eye_close2);
				et_mima.setTransformationMethod(PasswordTransformationMethod
						.getInstance());
			}
			// 切换后将EditText光标置于末尾
			CharSequence charSequence = et_mima.getText();
			if (charSequence instanceof Spannable) {
				Spannable spanText = (Spannable) charSequence;
				Selection.setSelection(spanText, charSequence.length());
			}
			break;
		case R.id.iv_yanzhengma:// 点击获取验证码，弹出dialog输入提示
			mima = et_mima.getText().toString().trim();
			phone = et_phone.getText().toString().trim();
			
			if (!isMobile(phone)) {
				ToastUtils.toastshort("您输入的手机号不符合规则");
				return;
			}else if (!isMima(mima)) {
				ToastUtils.toastshort("密码长度需在6—15位之间");
			}else {
				Map<String, String> map = SortRequestData.getmap();
				map.put("phone", phone);
				String requestData = SortRequestData.sortString(map);
				String signData = SignUtil.sign(requestData);
				map.put("sign", signData);

				VolleyUtil.sendJsonRequestByPost(ConstantUtils.VERIFYPHONE_URL, null, map,
						new HttpBackBaseListener() {

							@Override
							public void onSuccess(String string) {
								JSONObject json = JSON.parseObject(string);
								String code = json.getString("code");
								if("0".equals(code)){
									//手机号没注册过
									YzmDialog yzmDialog_sms = new YzmDialog(HomeRegisterActivity.this, R.style.YzmDialog, phone);
									yzmDialog_sms
											.setOnYzmDialogDismissListener(new OnYzmDialogDismissListener() {

												@Override
												public void OnYzmDialogDismiss(String answer) {
													//调用接口调用短信验证码
													progressdialog.show();
													Map<String, String> map = SortRequestData.getmap();
													map.put("phone", phone);
													map.put("sendType", 0+"");
													map.put("codeType", 0+"");
													map.put("captchaResult", answer);
													map.put("token", "");
													String requestData = SortRequestData.sortString(map);
													String signData = SignUtil.sign(requestData);
													map.put("sign", signData);
													VolleyUtil.sendJsonRequestByPost(ConstantUtils.SMS_URL, null, map, new HttpBackBaseListener() {
														
														@Override
														public void onSuccess(String string) {
															progressdialog.dismiss();
															JSONObject json = JSON.parseObject(string);
															String code = json.getString("code");
															if("0".equals(code)){
																String datastr = json.getString("data");
																JSONObject data = JSON.parseObject(datastr);
																randomId = data.getString("randomId");

																iv_yanzhengma.setVisibility(View.GONE);//获取验证码图片消失，倒计时显示
																myCount = new MyCount(60000, 1000);// 设置倒计时时间为60秒，间隔为1秒
																myCount.start();
																
																// 让隐藏的验证码已发送至xxx号码显示出来，通知用户
																tv_phone.setText(phone);
																rl_shownumber.setVisibility(View.VISIBLE);
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
								}else{
									//手机号注册了或输入格式不正确等等情况
									String msg = json.getString("msg");
									ToastUtils.toastshort(msg);
								}
							}

							@Override
							public void onError(VolleyError error) {
								
							}
						});
			}
			break;
		
		case R.id.tv_yuyin:// 获取语音验证码
			mima = et_mima.getText().toString().trim();
			phone = et_phone.getText().toString().trim();
			if (!isMobile(phone)) {
				ToastUtils.toastshort("您输入的手机号不符合规则");
				return;
			}else if (!isMima(mima)) {
				ToastUtils.toastshort("密码长度需在6—15位之间");
			}else {
				Map<String, String> map = SortRequestData.getmap();
				map.put("phone", phone);
				String requestData = SortRequestData.sortString(map);
				String signData = SignUtil.sign(requestData);
				map.put("sign", signData);

				VolleyUtil.sendJsonRequestByPost(ConstantUtils.VERIFYPHONE_URL, null, map,
						new HttpBackBaseListener() {

							@Override
							public void onSuccess(String string) {
								JSONObject json = JSON.parseObject(string);
								String code = json.getString("code");
								if("0".equals(code)){
									//手机号没注册过
									YzmDialog yzmDialog_yuyin = new YzmDialog(HomeRegisterActivity.this, R.style.YzmDialog, phone);
									yzmDialog_yuyin
											.setOnYzmDialogDismissListener(new OnYzmDialogDismissListener() {

												@Override
												public void OnYzmDialogDismiss(String answer) {
													//调用接口调用语音验证码
													progressdialog.show();
													Map<String, String> map = SortRequestData.getmap();
													map.put("phone", phone);
													map.put("sendType", 1+"");
													map.put("codeType", 0+"");
													map.put("captchaResult", answer);
													map.put("token", "");
													String requestData = SortRequestData.sortString(map);
													String signData = SignUtil.sign(requestData);
													map.put("sign", signData);
													VolleyUtil.sendJsonRequestByPost(ConstantUtils.SMS_URL, null, map, new HttpBackBaseListener() {
														
														@Override
														public void onSuccess(String string) {
															progressdialog.dismiss();
															JSONObject json = JSON.parseObject(string);
															String code = json.getString("code");
															if("0".equals(code)){
																String datastr = json.getString("data");
																JSONObject data = JSON.parseObject(datastr);
																randomId = data.getString("randomId");

																iv_yanzhengma.setVisibility(View.GONE);//获取验证码图片消失，倒计时显示
																myCount = new MyCount(60000, 1000);// 设置倒计时时间为60秒，间隔为1秒
																myCount.start();
															}else{
																ToastUtils.toastshort("获取语音验证码失败");
															}
														}
														
														@Override
														public void onError(VolleyError error) {
															progressdialog.dismiss();
															ToastUtils.toastshort("获取语音验证码失败");
														}
													});
													
												}
											});
									yzmDialog_yuyin.show();
								}else{
									//手机号注册了或输入格式不正确等等情况
									String msg = json.getString("msg");
									ToastUtils.toastshort(msg);
								}
							}

							@Override
							public void onError(VolleyError error) {
								
							}
						});
			}
			break;
		case R.id.iv_saoyisao:// 扫一扫
			Intent intent = new Intent(this, ScanerActivity.class);
			startActivityForResult(intent, 0x01);
			break;
		case R.id.tv_isregistered:// 已注册，去登录
			startActivity(new Intent(this, HomeLoginActivity.class));
			break;
		case R.id.bt_register:// 完成注册
			
			register();

			break;
		default:
			break;
		}
	}

	/**
	 * 扫描结果处理
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0x01 && resultCode == 0x02 && data != null) {
			if (data.getExtras().containsKey("result")) {
				// 让扫描出来的邀请码显示在这里
				String url = data.getExtras().getString("result");
				//截取字符串,从InviteFriendActivity中拿到url中的inviteCode
				int i = url.indexOf("ukey=");
				LogUtils.i("index="+i);
				String inviteCode = url.substring(i+5, i+11);
				// 通过url来获取相应的邀请码
				et_yaoqing.setText(inviteCode);
			}
		}
	}

	// 注册
	private void register() {
		phone = et_phone.getText().toString().trim();
		mima = et_mima.getText().toString().trim();
		yanzhengma = et_yanzhengma.getText().toString().trim();
		yaoqingma = et_yaoqing.getText().toString().trim();
		
		if (TextUtils.isEmpty(phone)) {// 手机号码为空
			ToastUtils.toastshort("手机号不能为空");
			return;
		}
		if (TextUtils.isEmpty(mima)) {// 密码为空
			ToastUtils.toastshort("密码不能为空");
			return;
		}
		if (TextUtils.isEmpty(yanzhengma)) {// 验证码为空
			ToastUtils.toastshort("验证码不能为空");
			return;
		}
		if (!isGou) {// 服务协议没有勾选
			ToastUtils.toastshort("请您勾选注册协议");
			return;
		} 
		
		postInfo(phone, mima, yanzhengma ,yaoqingma);

	}

	private void postInfo(final String phone,final String mima, final String yanzhengma, final String yaoqingma) {
		progressdialog.show();
		progressdialog.setContent("正在注册....");
		 //获取UUID
		Map<String, String> map = SortRequestData.getmap();
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(
				ConstantUtils.GETUUID_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {

						JSONObject json = JSON.parseObject(string);
						String code = json.getString("code");

						if ("0".equals(code)) {
							String datastr = json.getString("data");
							if (StringUtils.isBlank(datastr)) {
								// datastr为空不验签
							} else {
								String sign = json.getString("sign");
								boolean isSuccess = SignUtil.verify(sign, datastr);
								if (isSuccess) {// 验签成功
									progressdialog.show();
									progressdialog.setContent("正在注册....");
									Map<String, String> map = SortRequestData.getmap();
									map.put("phone", phone);
									map.put("code", yanzhengma);
									map.put("randomId", randomId);
									map.put("password", Base64.encode(SignUtil.encrypt(mima)));
									map.put("inviteCode", yaoqingma);
									map.put("uuid", datastr);
									String requestData = SortRequestData.sortString(map);
									String signData = SignUtil.sign(requestData);
									map.put("sign", signData);
							
									VolleyUtil.sendJsonRequestByPost(ConstantUtils.REGISTER_URL, null, map,
											new HttpBackBaseListener() {
							
												@Override
												public void onSuccess(String string) {
													progressdialog.dismiss();
													JSONObject json = JSON.parseObject(string);
													String code = json.getString("code");
													if("0".equals(code)){
														String token = json.getString("token");
														CacheUtils.putString(getApplicationContext(), "token",token);
														CacheUtils.putString(getApplicationContext(), CacheUtils.LOGINPHONE,phone);
														CacheUtils.putString(getApplicationContext(), CacheUtils.LOGINPASSWORD,Base64.encode(SignUtil.encrypt(mima)));
														// 跳转到注册成功dialog
														showRegisterSuccessDialog();
													}else{
														String msg = json.getString("msg");
														ToastUtils.toastshort(msg);
													}
												}
							
												@Override
												public void onError(VolleyError error) {
													progressdialog.dismiss();
							                        ToastUtils.toastshort( "网络请求失败");
												}
											});
								} else {
									progressdialog.dismiss();
									ToastUtils.toastshort("注册失败！");
								}
							}
						} else {
							progressdialog.dismiss();
							ToastUtils.toastshort("注册失败！");
						}
					}

					@Override
					public void onError(VolleyError error) {
						progressdialog.dismiss();
						ToastUtils.toastshort("注册失败！");
					}
				});
	}

	// 弹出注册成功dialog
	private void showRegisterSuccessDialog() {
		RegisterOkDialog registerOkDialog = new RegisterOkDialog(this,
				R.style.YzmDialog);
		registerOkDialog
				.setOnRegisterDialogDismissListener(new OnRegisterDialogDismissListener() {

					@Override
					public void OnRegisterDialogDismiss() {
						finish();
					}
				});
		registerOkDialog.show();
	}

	// 自定义倒计时类
	class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			// 倒计时完要做的事情
			tv_count.setClickable(true);
			iv_yanzhengma.setImageResource(R.drawable.yanzhengmaagain);// 改为重新获取图片
			iv_yanzhengma.setVisibility(View.VISIBLE);// 让重新获取图片显示
			// 倒计时完让语音验证码可点击
			tv_yuyin.setClickable(true);
			//获取验证码过程使手机号输入框重新获得焦点
			et_phone.setEnabled(true);
			et_phone.setCursorVisible(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			tv_count.setText(millisUntilFinished / 1000 + "");// 60秒倒计时
			tv_count.setTextColor(getResources().getColor(R.color.text_black));
			tv_count.setTextSize(10);
			pw_spinner.setProgress((int) (millisUntilFinished / 1000));// 圆弧进度条
			tv_count.setClickable(false);
			//获取验证码过程使语音验证码点击失效
			tv_yuyin.setClickable(false);
			//获取验证码过程使手机号输入框失去焦点
			et_phone.setEnabled(false);
			et_phone.setCursorVisible(false);
		}
	}
}
