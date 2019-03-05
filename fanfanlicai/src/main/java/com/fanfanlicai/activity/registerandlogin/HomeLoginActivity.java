package com.fanfanlicai.activity.registerandlogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.activity.MainActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.service.RefreshTokenReceiver;
import com.fanfanlicai.sign.Base64;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.RefTokenUtils;
import com.fanfanlicai.utils.StringUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;
import com.fanfanlicai.view.dialog.CustomProgressDialog;
import com.fanfanlicai.view.lockPattern.GestureCreateActivity;
import com.qiyukf.unicorn.api.Unicorn;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author WenGuangJun
 *
 */
public class HomeLoginActivity extends BaseActivity implements OnClickListener,TextWatcher {
	private TextView homelogin_back;
	private TextView tv_getyanzheng;
	private ImageView iv_eye;
	private EditText et_mima;
	private TextView tv_to_register;
	private EditText et_phone;

	private String phone;
	private String mima;
	private Button bt_homelogin;
	private CustomProgressDialog progressdialog;
	private boolean isMima = true;
	private boolean isOpen = false;
	private ImageView iv_cancel;// 手机号码后面的取消按钮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_homelogin);
		progressdialog = new CustomProgressDialog(this, "正在登录......");
		initView();
		initData();
	}

	private void initView() {
		homelogin_back = (TextView) findViewById(R.id.homelogin_back);// 返回
		et_phone = (EditText) findViewById(R.id.et_phone);// 手机号输入框
		et_mima = (EditText) findViewById(R.id.et_mima);// 设置密码
		et_mima.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		tv_getyanzheng = (TextView) findViewById(R.id.tv_getyanzheng);// 忘记密码
		iv_eye = (ImageView) findViewById(R.id.iv_eye);// 密码登录里的眼睛打开图片
		tv_to_register = (TextView) findViewById(R.id.tv_to_register);// 没有账号？去注册
		bt_homelogin = (Button) findViewById(R.id.bt_homelogin);
		iv_cancel = (ImageView) findViewById(R.id.iv_cancel);// 手机号码后面的取消按钮
		
		homelogin_back.setOnClickListener(this);
		tv_to_register.setOnClickListener(this);
		tv_getyanzheng.setOnClickListener(this);
		iv_eye.setOnClickListener(this);
		bt_homelogin.setOnClickListener(this);
		iv_cancel.setOnClickListener(this);
		
		et_phone.addTextChangedListener(this);  
		et_mima.addTextChangedListener(this);  

		//解决进入登录界面没有弹出软键盘的问题
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
		//默认按钮不可点击
		bt_homelogin.setBackgroundResource(R.drawable.anniu_chang_bukedianji);  
		bt_homelogin.setClickable(false);  
	
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
						&&StringUtils.isNotBlank(et_mima.getText().toString()))
				{
					//当输入框都不为空时，按钮可点击
					bt_homelogin.setBackgroundResource(R.drawable.button_long_selector);
					bt_homelogin.setPressed(false);  
					bt_homelogin.setClickable(true);  
				}else {
					bt_homelogin.setBackgroundResource(R.drawable.anniu_chang_bukedianji);
					bt_homelogin.setClickable(false);  
				}
			}
	
	public static boolean isMobile(String phone) {
		return phone.length() == 11;
	}

	public static boolean isMima(String mima) {
		return mima.length() >= 6 && mima.length() <= 15;
	}

	//输错5次手势密码后点返回时进入主页面，
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		int flags = getIntent().getFlags();
		if (flags==10) {
			//七鱼客服退出时清除聊天记录
			Unicorn.setUserInfo(null);
			// 访问网络，告知服务端用户已退出登录
			getDataFromServer();
			// 清除掉手势密码
			CacheUtils.putString(this, CacheUtils.BYTE, "");
			// 退出登录，数据清空
			CacheUtils.putString(this, CacheUtils.REALNAME, "");
			CacheUtils.putString(this, CacheUtils.IDNO, "");
			CacheUtils.putString(this, CacheUtils.LOGINPHONE, "");
			CacheUtils.putString(this, CacheUtils.LOGINPASSWORD, "");
			CacheUtils.putString(this, CacheUtils.INVITECODE, "");
			CacheUtils.putString(this, CacheUtils.TOTALASSETS, "");
			CacheUtils.putString(this, CacheUtils.BASEACCTBAL, "");
			CacheUtils.putString(this, CacheUtils.FHACCTBAL, "");
			CacheUtils.putString(this, CacheUtils.FWACCTBAL, "");
			CacheUtils.putString(this, CacheUtils.REWARDACCTBAL, "");
			CacheUtils.putString(this, CacheUtils.FWWAITPAYINCOME, "");
			CacheUtils.putString(this, CacheUtils.REDEEMAMOUNT, "");
			CacheUtils.putString(this, CacheUtils.REWARDUSEDAMOUNT, "");
			CacheUtils.putString(this, CacheUtils.CARDNUM, "");
			CacheUtils.putString(this, CacheUtils.CARDPHONE, "");
			CacheUtils.putString(this, CacheUtils.ADDRESS, "");
			CacheUtils.putString(this, CacheUtils.ZIPCODE, "");
			CacheUtils.putString(this, CacheUtils.TOTALINCOME, "");
			CacheUtils.putString(this, CacheUtils.FHTOTALINCOME, "");
			CacheUtils.putString(this, CacheUtils.FWTOTALINCOME, "");
			CacheUtils.putString(this, CacheUtils.FWWAITREPAYAMOUNT, "");
			CacheUtils.putString(this, "token", "");
			RefTokenUtils.stopRefresh(this, RefreshTokenReceiver.class, MainActivity.REFRESH_RECEIVER);
			//退出登录跳转到首页
			Intent intent = new Intent(this,MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
			startActivity(intent);
		}
	}

	protected void getDataFromServer() {

		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(this,"token",null);
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.LOGOUT_URL, null,
				map, new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						// TODO Auto-generated method stub
						LogUtils.i("调了退出登录的接口");

					}

					@Override
					public void onError(VolleyError error) {

					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.homelogin_back:
			int flags = getIntent().getFlags();
			if (flags==10) {//输错5次手势密码后点返回时进入主页面，
				//七鱼客服退出时清除聊天记录
				Unicorn.setUserInfo(null);
				// 访问网络，告知服务端用户已退出登录
				getDataFromServer();
				// 清除掉手势密码
				CacheUtils.putString(this, CacheUtils.BYTE, "");
				// 退出登录，数据清空
				CacheUtils.putString(this, CacheUtils.REALNAME, "");
				CacheUtils.putString(this, CacheUtils.IDNO, "");
				CacheUtils.putString(this, CacheUtils.LOGINPHONE, "");
				CacheUtils.putString(this, CacheUtils.LOGINPASSWORD, "");
				CacheUtils.putString(this, CacheUtils.INVITECODE, "");
				CacheUtils.putString(this, CacheUtils.TOTALASSETS, "");
				CacheUtils.putString(this, CacheUtils.BASEACCTBAL, "");
				CacheUtils.putString(this, CacheUtils.FHACCTBAL, "");
				CacheUtils.putString(this, CacheUtils.FWACCTBAL, "");
				CacheUtils.putString(this, CacheUtils.REWARDACCTBAL, "");
				CacheUtils.putString(this, CacheUtils.FWWAITPAYINCOME, "");
				CacheUtils.putString(this, CacheUtils.REDEEMAMOUNT, "");
				CacheUtils.putString(this, CacheUtils.REWARDUSEDAMOUNT, "");
				CacheUtils.putString(this, CacheUtils.CARDNUM, "");
				CacheUtils.putString(this, CacheUtils.CARDPHONE, "");
				CacheUtils.putString(this, CacheUtils.ADDRESS, "");
				CacheUtils.putString(this, CacheUtils.ZIPCODE, "");
				CacheUtils.putString(this, CacheUtils.TOTALINCOME, "");
				CacheUtils.putString(this, CacheUtils.FHTOTALINCOME, "");
				CacheUtils.putString(this, CacheUtils.FWTOTALINCOME, "");
				CacheUtils.putString(this, CacheUtils.FWWAITREPAYAMOUNT, "");
				CacheUtils.putString(this, "token", "");
				RefTokenUtils.stopRefresh(this, RefreshTokenReceiver.class, MainActivity.REFRESH_RECEIVER);
				//退出登录跳转到首页
				Intent intent = new Intent(this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
				startActivity(intent);
			}else{
				finish();
			}
			break;
		case R.id.iv_cancel:// 手机号码后面的取消按钮
			et_phone.getText().clear();
			break;
		case R.id.iv_eye:// 密码的显示与隐藏
			isOpen = !isOpen;
			if (isOpen) {// 打开眼睛图片，显示密码
				iv_eye.setImageResource(R.drawable.eye_open2);
				et_mima.setTransformationMethod(HideReturnsTransformationMethod
						.getInstance());
			} else {// 关闭眼睛图片，隐藏密码
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
		case R.id.tv_getyanzheng:// 点击进入找回密码界面
			Intent intent = new Intent(HomeLoginActivity.this,
					ForgetPasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_to_register:// 点击进入注册页面
			Intent intent2 = new Intent(HomeLoginActivity.this,
					HomeRegisterActivity.class);
			startActivity(intent2);
			break;
		case R.id.bt_homelogin:// 快速登录
			login();
			break;
		default:
			break;
		}
	}

	private void login() {
		
		//密码登录
		mima = et_mima.getText().toString().trim();
		phone = et_phone.getText().toString().trim();
		
		if (TextUtils.isEmpty(phone)) {// 手机号码为空
			ToastUtils.toastshort("手机号不能为空");
			return;
		}
		if (TextUtils.isEmpty(mima)) {// 密码为空
			ToastUtils.toastshort("密码不能为空");
			return;
		}
			postToLogin(phone, mima);
	}

	 private void postToLogin(final String phone, final String mima) {
		progressdialog.show();
		String url="";
		Map<String, String> map =  SortRequestData.getmap();
		map.put("phone", phone);
		if(isMima){
			//密码登录
			url=ConstantUtils.MIMA_LOGIN_URL;
			map.put("password", Base64.encode(SignUtil.encrypt(mima)));
		}else{
			//验证码登录
			url=ConstantUtils.YANZHENGMA_LOGIN_URL;
			map.put("code", mima);
		}
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
      
		VolleyUtil.sendJsonRequestByPost(url, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						progressdialog.dismiss();

						JSONObject json = JSON.parseObject(string);
						String  code= json.getString("code");
						if("0".equals(code)){
							//登录成功后对之前手势密码连输错误5次情况的处理
	                    	CacheUtils.putBoolean(getApplicationContext(), "gestureDisable", false);
	                    	//登录成功保存token和登录手机号及密码，并且跳到绘制手势密码的界面
							String token = json.getString("token");
							CacheUtils.putString(getApplicationContext(), "token",token);
							CacheUtils.putString(getApplicationContext(), CacheUtils.LOGINPHONE,phone);
							CacheUtils.putString(getApplicationContext(), CacheUtils.LOGINPASSWORD,Base64.encode(SignUtil.encrypt(mima)));
							//登录成功后获取用户信息
							getDataFromServer(token);
							
						}else{
							String msg = json.getString("msg");
							ToastUtils.toastshort(msg);
						}
					}

					@Override
					public void onError(VolleyError error) {
						progressdialog.dismiss();
						ToastUtils.toastshort("网络请求失败");
					}
				});
	}
	 
	 
	 private void getDataFromServer(String token) {
		Map<String, String> map = SortRequestData.getmap();
		// 如果没有登录，直接return，不访问网络了
		if (TextUtils.isEmpty(token)) {
			return;
		}
		progressdialog.show();
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.INDEX_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						
						LogUtils.i("首页==="+string);
						
						progressdialog.dismiss();
						JSONObject json = JSON.parseObject(string);
						String code = json.getString("code");

						if ("0".equals(code)) {
							String datastr = json.getString("data");
							if (StringUtils.isBlank(datastr)) {
								// datastr为空不验签
							} else {
								String sign = json.getString("sign");
								boolean isSuccess = SignUtil.verify(sign,
										datastr);
								if (isSuccess) {// 验签成功
									JSONObject data = JSON.parseObject(datastr);
									
									// 轮播图要用到邀请码
									String inviteCode = data.getString("inviteCode");
									CacheUtils.putString(HomeLoginActivity.this, "inviteCode",inviteCode);
									
									//登录成功后设置手势密码
									Intent intent=new Intent(HomeLoginActivity.this,GestureCreateActivity.class);
									startActivity(intent);
									finish();
											
								} else {
									ToastUtils.toastshort("加载数据异常！");
								}
							}
						} else {
							String msg = json.getString("msg");
							ToastUtils.toastshort(msg);
						}
					}

					@Override
					public void onError(VolleyError error) {
						progressdialog.dismiss();
						ToastUtils.toastshort("网络连接失败，请检查网络");
					}
				});
		}

}
