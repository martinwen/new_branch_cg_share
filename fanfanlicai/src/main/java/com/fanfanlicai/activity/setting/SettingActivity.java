package com.fanfanlicai.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.activity.MainActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.global.FanFanApplication;
import com.fanfanlicai.service.RefreshTokenReceiver;
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
import com.fanfanlicai.view.dialog.SettingOutDialog;
import com.fanfanlicai.view.dialog.SettingOutDialog.OnDialogDismissListener;
import com.qiyukf.unicorn.api.Unicorn;

import java.util.Map;

public class SettingActivity extends BaseActivity implements OnClickListener{

	private RelativeLayout rl_phone;
	private RelativeLayout rl_mail;
	private RelativeLayout rl_pay;
	private RelativeLayout rl_login;
	private RelativeLayout rl_address;
	private RelativeLayout rl_ceping;
	private RelativeLayout rl_gesture;
	private TextView setting_back;
	private TextView setting_out;
	private TextView tv_isBind;
	private String email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);
		initView();

	}
	

	private void initView() {
		rl_phone = (RelativeLayout) findViewById(R.id.rl_phone);
		rl_mail = (RelativeLayout) findViewById(R.id.rl_mail);
		rl_pay = (RelativeLayout) findViewById(R.id.rl_pay);
		rl_login = (RelativeLayout) findViewById(R.id.rl_login);
		rl_address = (RelativeLayout) findViewById(R.id.rl_address);
		rl_ceping = (RelativeLayout) findViewById(R.id.rl_ceping);
		rl_gesture = (RelativeLayout) findViewById(R.id.rl_gesture);
		setting_back=(TextView) findViewById(R.id.setting_back);
		setting_out = (TextView) findViewById(R.id.setting_out);
		tv_isBind = (TextView) findViewById(R.id.tv_isBind);

		rl_phone.setOnClickListener(this);
		rl_mail.setOnClickListener(this);
		rl_pay.setOnClickListener(this);
		rl_login.setOnClickListener(this);
		rl_address.setOnClickListener(this);
		rl_ceping.setOnClickListener(this);
		rl_gesture.setOnClickListener(this);
		setting_back.setOnClickListener(this);
		setting_out.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		email = CacheUtils.getString(SettingActivity.this, CacheUtils.EMAIL,"");
		LogUtils.i("email=="+email);
		if("".equals(email)){
			tv_isBind.setText("未绑定");
		}else {
			tv_isBind.setText("已绑定");
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_back:
			finish();
			break;
		case R.id.rl_phone:
			startActivity(new Intent(this,PhoneActivity.class));
			break;
		case R.id.rl_mail:
			if("".equals(email)){
				startActivity(new Intent(this,EmailActivity.class));
			}else {
				startActivity(new Intent(this,EmailHasBindActivity.class));
			}
			break;
		case R.id.rl_login:
			startActivity(new Intent(this,LoginFixActivity.class));
			break;
		case R.id.rl_pay:
			checkState();
			break;
		case R.id.rl_address:
			startActivity(new Intent(this,AddressActivity.class));
			break;
		case R.id.rl_ceping:
			startActivity(new Intent(this, SettingForCePingActivity.class));
			break;
		case R.id.rl_gesture:
			startActivity(new Intent(this,GestureActivity.class));
			break;
		case R.id.setting_out:
			SettingOutDialog settingOutDialog = new SettingOutDialog(this, R.style.YzmDialog);
			settingOutDialog.show();
			settingOutDialog.setOnDialogDismissListener(new OnDialogDismissListener() {
				
				@Override
				public void OnDialogDismiss() {
					FanFanApplication.jiaxi_rate = 0;
					FanFanApplication.jiaxi_ID = null;
					//七鱼客服退出时清除聊天记录
					Unicorn.setUserInfo(null);
					// 访问网络，告知服务端用户已退出登录
					getDataFromServer();
					// 清除掉手势密码
					CacheUtils.putString(SettingActivity.this, CacheUtils.BYTE, "");
					// 退出登录，数据清空
					CacheUtils.putString(SettingActivity.this, CacheUtils.REALNAME, "");
					CacheUtils.putString(SettingActivity.this, CacheUtils.IDNO, "");
					CacheUtils.putString(SettingActivity.this, CacheUtils.LOGINPHONE, "");
					CacheUtils.putString(SettingActivity.this, CacheUtils.LOGINPASSWORD, "");
					CacheUtils.putString(SettingActivity.this, CacheUtils.INVITECODE, "");
					CacheUtils.putString(SettingActivity.this, CacheUtils.TOTALASSETS, "");
					CacheUtils.putString(SettingActivity.this, CacheUtils.BASEACCTBAL, "");
					CacheUtils.putString(SettingActivity.this, CacheUtils.FHACCTBAL, "");
					CacheUtils.putString(SettingActivity.this, CacheUtils.FWACCTBAL, "");
					CacheUtils.putString(SettingActivity.this, CacheUtils.REWARDACCTBAL, "");
					CacheUtils.putString(SettingActivity.this, CacheUtils.FWWAITPAYINCOME, "");
					CacheUtils.putString(SettingActivity.this, CacheUtils.REDEEMAMOUNT, "");
					CacheUtils.putString(SettingActivity.this, CacheUtils.CASHINGMONEY, "");
					CacheUtils.putString(SettingActivity.this, CacheUtils.REWARDUSEDAMOUNT, "");
					CacheUtils.putString(SettingActivity.this, CacheUtils.CARDNUM, "");
					CacheUtils.putString(SettingActivity.this, CacheUtils.CARDPHONE, "");
					CacheUtils.putString(SettingActivity.this, CacheUtils.ADDRESS, "");
					CacheUtils.putString(SettingActivity.this, CacheUtils.ZIPCODE, "");
					CacheUtils.putString(SettingActivity.this, CacheUtils.TOTALINCOME, "");
					CacheUtils.putString(SettingActivity.this, CacheUtils.FHTOTALINCOME, "");
					CacheUtils.putString(SettingActivity.this, CacheUtils.FWTOTALINCOME, "");
					CacheUtils.putString(SettingActivity.this, CacheUtils.FWWAITREPAYAMOUNT, "");
					CacheUtils.putString(SettingActivity.this, "token", "");
					RefTokenUtils.stopRefresh(SettingActivity.this, RefreshTokenReceiver.class, MainActivity.REFRESH_RECEIVER);
					//退出登录跳转到首页
					Intent intent = new Intent(SettingActivity.this,MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置 
					startActivity(intent);
					finish();
				}
			});
			break;

		default:
			break;
		}
	}

	private void checkState() {
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(this, "token", "");
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.QUERYBINDCARANDSIGNSTATUS_URL, null, map,
				new HttpBackBaseListener() {
					@Override
					public void onSuccess(String string) {
						LogUtils.i("点击设置交易密码==="+string);
						// TODO Auto-generated method stub
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


									// 是否开通存管户
									String hasFyAccount = data.getString("hasFyAccount");

									// 是否开通存管户（绑卡）0-未开通；
									if ("0".equals(hasFyAccount)) {// 如果还没有绑卡，去绑卡
										ToastUtils.toastshort("您尚未开通银行存管账户，请先进行开户");
									}else{//1-已开通
										ConstantUtils.updateflag=2;
										startActivity(new Intent(SettingActivity.this,CashFixActivity.class));
									}

								} else {
									ToastUtils.toastshort("加载数据异常！");
								}
							}
						}  else if("666666".equals(code)){

						}else {
							String msg = json.getString("msg");
							ToastUtils.toastshort(msg);
						}
					}

					@Override
					public void onError(VolleyError error) {
					}
				});
	}

	protected void getDataFromServer() {

		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(SettingActivity.this, "token",null);
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
}
