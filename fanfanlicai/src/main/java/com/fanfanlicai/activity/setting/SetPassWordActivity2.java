package com.fanfanlicai.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.activity.MainActivity;
import com.fanfanlicai.activity.my.CashDetailActivity;
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
import com.fanfanlicai.view.dialog.CustomProgressDialog;
import com.fanfanlicai.view.password.GridPasswordView;
import com.fanfanlicai.view.password.GridPasswordView.OnPasswordChangedListener;
import com.fanfanlicai.view.password.PasswordType;

import java.util.Map;

public class SetPassWordActivity2 extends BaseActivity implements OnClickListener {
	protected static final String NEWPASSWORD = null;
	private GridPasswordView gpv_length;
	private TextView tv_mima;
	private CustomProgressDialog progressdialog;
	private String get_money;
	private String ticketId;
	private String useDefaultTicket;
	private String newPassWord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pay);
		progressdialog = new CustomProgressDialog(this, "正在发送，请稍后......");
		initView();
		initData();
	}

	private void initView() {
		get_money=getIntent().getStringExtra("get_money");
		ticketId=getIntent().getStringExtra("ticketId");
		useDefaultTicket=getIntent().getStringExtra("useDefaultTicket");
		newPassWord=getIntent().getStringExtra("newPassWord");
		
		tv_mima = (TextView) findViewById(R.id.tv_mima);
		TextView pay_back = (TextView) findViewById(R.id.pay_back);
		pay_back.setOnClickListener(this);
		gpv_length = (GridPasswordView) findViewById(R.id.gpv_length);
		gpv_length.setPasswordType(PasswordType.NUMBER);

	}

	private void initData() {
		tv_mima.setText("再次确认6位数字交易密码");
		gpv_length.setOnPasswordChangedListener(new OnPasswordChangedListener() {
			
			@Override
			public void onTextChanged(String psw) {
				
			}
			
			@Override
			public void onInputFinish(String psw) {
				String confirmPassWord = gpv_length.getPassWord();
				if (confirmPassWord.equals(newPassWord)) {
					// 访问网络
					getDataFromServer(newPassWord);
					
				}else {
					ToastUtils.toastshort("您输入的密码不正确");
					gpv_length.clearPassword();
				}
			}
		});						
	}
	
	protected void getDataFromServer(final String newPassWord) {
		progressdialog.show();
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(this, "token",null);
		map.put("token", token);
		map.put("password", Base64.encode(SignUtil.encrypt(newPassWord)));
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.SETCASHPWD_URL, null,
				map, new HttpBackBaseListener() {

			@Override
			public void onSuccess(String string) {
				// TODO Auto-generated method stub
				progressdialog.dismiss();
				JSONObject json = JSON.parseObject(string);
				String code = json.getString("code");

				if ("0".equals(code)) {
						//如果成功则调用提现接口
					if(ConstantUtils.updateflag==1){
						goget(newPassWord);
					}else if(ConstantUtils.updateflag==2){
						ToastUtils.toastshort( "设置密码成功");
						finish();
					}else if(ConstantUtils.updateflag==3){
						goget(newPassWord);
					}
				} else {
					ToastUtils.toastshort("密码设置失败！");
				}
			}

			@Override
			public void onError(VolleyError error) {
				progressdialog.dismiss();
				ToastUtils.toastshort("密码设置失败！");
			}
		});
		
	}
	
	
    void goget(final String  password){
   
	// 获取UUID 调用提现接口
	Map<String, String> map = SortRequestData.getmap();
	String token = CacheUtils.getString(SetPassWordActivity2.this, "token",
			null);
	map.put("token", token);
	String requestData = SortRequestData.sortString(map);
	String signData = SignUtil.sign(requestData);
	map.put("sign", signData);
	progressdialog.show();
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
								// 调用提现接口
								Map<String, String> map = SortRequestData.getmap();
								String token = CacheUtils.getString(SetPassWordActivity2.this,"token",null);
								map.put("token", token);
								map.put("amount", get_money);
								map.put("uuid", datastr);
								map.put("ticketId", ticketId);
								map.put("useDefaultTicket", useDefaultTicket);
								map.put("password",Base64.encode(SignUtil.encrypt(password)));
								String requestData = SortRequestData.sortString(map);
								String signData = SignUtil.sign(requestData);
								map.put("sign", signData);
								LogUtils.i("amount===="+get_money);
								LogUtils.i("提现参数===="+JSON.toJSONString(map));
								VolleyUtil.sendJsonRequestByPost(
												ConstantUtils.SUBMITCASH_URL,null,map,new HttpBackBaseListener() {

													@Override
													public void onSuccess(String string) {
														LogUtils.i("提现==string="+string);
														progressdialog.dismiss();
														JSONObject json = JSON.parseObject(string);
														String code = json.getString("code");
														if ("0".equals(code)) {
															Intent intent = new Intent(SetPassWordActivity2.this,CashDetailActivity.class);
															startActivity(intent);
															finish();
														}else if("666666".equals(code)){

														} else {
															Intent intent = new Intent(SetPassWordActivity2.this,MainActivity.class);
															intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
															startActivity(intent);
															String msg = json.getString("msg");
															ToastUtils.toastshort(msg);
														}
													}

													@Override
													public void onError(
															VolleyError error) {
														LogUtils.i("提现===");
														progressdialog.dismiss();
														ToastUtils.toastshort("提现失败！");
													}
												});
							} else {
								LogUtils.i("获取uuid=验签失败===");
								progressdialog.dismiss();
								ToastUtils.toastshort("提现失败！");
							}
						}
					} else {
						LogUtils.i("获取uuid=code部位0===");
						progressdialog.dismiss();
						ToastUtils.toastshort("提现失败！");
					}
				}

				@Override
				public void onError(VolleyError error) {
					LogUtils.i("获取uuid===="+error);
					progressdialog.dismiss();
					ToastUtils.toastshort("提现失败！");
				}
			});
 	
    }
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pay_back:
			finish();
			break;

		default:
			break;
		}
	}
}
