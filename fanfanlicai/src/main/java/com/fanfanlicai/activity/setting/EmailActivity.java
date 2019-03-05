package com.fanfanlicai.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.fanfanlicai.R;
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
import com.fanfanlicai.view.dialog.YzmDialog.OnYzmDialogDismissListener;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailActivity extends BaseActivity implements OnClickListener,TextWatcher {

	private EditText et_emai_address;
	private EditText et_yanzhengma;
	private Button bt_password;

	private CustomProgressDialog progressdialog;
	private ImageView iv_yanzhengma;
	private TextView tv_count;
	private MyCount myCount;
	private RoundProgressBar pw_spinner;
	private String email;
	private String randomId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_email);
		
		progressdialog = new CustomProgressDialog(this, "正在加载数据...");
		initView();
		initData();
		
	}

	private void initView() {
		TextView login_back = (TextView) findViewById(R.id.login_back);
		login_back.setOnClickListener(this);

		et_emai_address = (EditText) findViewById(R.id.et_emai_address);//邮箱
		et_yanzhengma = (EditText) findViewById(R.id.et_yanzhengma);//验证码
		bt_password = (Button) findViewById(R.id.bt_password);
		bt_password.setOnClickListener(this);
		iv_yanzhengma = (ImageView) findViewById(R.id.iv_yanzhengma);// 获取验证码图片
		iv_yanzhengma.setOnClickListener(this);
		tv_count = (TextView) findViewById(R.id.tv_count);// 倒计时
		pw_spinner = (RoundProgressBar) findViewById(R.id.pw_spinner);// 倒计时圆弧进度条

		et_emai_address.addTextChangedListener(this);
		et_yanzhengma.addTextChangedListener(this);  
	}

	private void initData() {
		// 默认按钮不可点击
		bt_password.setBackgroundResource(R.drawable.anniu_chang_bukedianji);  
		bt_password.setClickable(false); 

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
		
		if (StringUtils.isNotBlank(et_emai_address.getText().toString())
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
		switch (v.getId()) {
		case R.id.login_back:
			finish();
			break;
			
		case R.id.bt_password:
			email = et_emai_address.getText().toString().trim();
			String code = et_yanzhengma.getText().toString().trim();
			// 邮箱不能为空
			if (TextUtils.isEmpty(email)) {
				ToastUtils.toastshort("请输入邮箱");
				// 让EditText获取焦点
				et_emai_address.requestFocus();
				return;
			}
			// 验证码不能为空
			if (TextUtils.isEmpty(code)) {
				ToastUtils.toastshort("请点击获取验证码");
				// 让EditText获取焦点
				et_yanzhengma.requestFocus();
				return;
			}
			requestServer(code);
			break;

		case R.id.iv_yanzhengma:// 点击获取验证码，弹出dialog输入提示
			email = et_emai_address.getText().toString().trim();
			// 邮箱不能为空
			if (TextUtils.isEmpty(email)) {
				ToastUtils.toastshort("请输入邮箱");
				// 让EditText获取焦点
				et_emai_address.requestFocus();
				return;
			}
			// 邮箱格式
			if (!checkEmailReg(email)) {
				ToastUtils.toastshort("邮箱格式不正确，请输入正确的邮箱");
				// 让EditText获取焦点
				et_emai_address.requestFocus();
				return;
			}

			checkEmail(email);

			break;
		default:
			break;
		}
	}

	/**
	 * 验证邮箱
	 * @param email
	 * @return
	 */
	public boolean checkEmailReg(String email){
		boolean flag = false;
		try{
			String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		}catch(Exception e){
			flag = false;
		}
		return flag;
	}

	//点击验证码后判断邮箱是否存在
	protected void checkEmail(final String email) {
		Map<String, String> map = SortRequestData.getmap();
		map.put("email", email);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);

		VolleyUtil.sendJsonRequestByPost(ConstantUtils.ISEMAILEXIST_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						JSONObject json = JSON.parseObject(string);
						String code = json.getString("code");
						if("0".equals(code)){
							ToastUtils.toastshort("该邮箱已绑定");
						}else{
							getcode(email);
						}
					}

					@Override
					public void onError(VolleyError error) {
						ToastUtils.toastshort("网络错误");
						return;
					}
				});
	}

	private void getcode(final String email) {
		YzmDialog yzmDialog_sms = new YzmDialog(this, R.style.YzmDialog, email);
		yzmDialog_sms
				.setOnYzmDialogDismissListener(new OnYzmDialogDismissListener() {

					@Override
					public void OnYzmDialogDismiss(String answer) {
						// TODO Auto-generated method stub
						progressdialog.show();
						String token = CacheUtils.getString(EmailActivity.this, "token", null);
						Map<String, String> map = SortRequestData.getmap();
						map.put("email", email);
						map.put("sendType", 2+"");
						map.put("codeType", 5+"");
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
	}

	private void requestServer(String code) {
		progressdialog.show();
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(EmailActivity.this, "token", null);
		map.put("email", email);
		map.put("code", code);
		map.put("token",token);
		map.put("randomId",randomId);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.UPDATEUSEREMAIL_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						progressdialog.dismiss();
						LogUtils.i("绑定邮箱====="+string);
						JSONObject json = JSON.parseObject(string);
						String  code= json.getString("code");
						if("0".equals(code)){
							CacheUtils.putString(getApplicationContext(), CacheUtils.EMAIL,email);
							ToastUtils.toastshort("绑定成功");
							Intent intent = new Intent(EmailActivity.this,SettingActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
							startActivity(intent);
						}else{
							String msg = json.getString("msg");
							ToastUtils.toastshort(msg);
						}
					}

					@Override
					public void onError(VolleyError error) {
						progressdialog.dismiss();
						ToastUtils.toastshort("绑定邮箱失败");
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
			tv_count.setTextColor(getResources().getColor(R.color.text_black));
			tv_count.setTextSize(10);
			pw_spinner.setProgress((int)(millisUntilFinished / 1000));//圆弧进度条
			tv_count.setClickable(false);
		}

	}
}
