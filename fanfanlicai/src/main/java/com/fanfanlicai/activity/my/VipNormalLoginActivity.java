package com.fanfanlicai.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.activity.MainActivity;
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
import com.fanfanlicai.view.dialog.CustomProgressDialog;

import java.util.Map;

public class VipNormalLoginActivity extends BaseActivity implements OnClickListener {

	private CustomProgressDialog progressdialog;
	private TextView tv_back;//返回
	private TextView vip_number;//号码
	private TextView tv_vip_fanhe;//饭盒资产
	private TextView tv_vip_fanwan;//饭碗资产
	private TextView tv_vip_fantong;//饭桶资产

	private TextView tv_fanhe;//饭盒收益率
	private TextView tv_shengri;//生日福利
	private TextView tv_tixian;//提现票
	private TextView tv_yaoqing;//邀请好友

	private TextView tv_upgradecore;//升级到核心
	private TextView tv_toufanwan;//投资饭碗升级
	private TextView tv_upgradehighcore;//升级到高级核心
	private TextView tv_toufantong;//投资饭桶升级

	private RelativeLayout rl_user_normal;//普通用户
	private ImageView iv_user_normal;
	private LinearLayout ll_user_normal;
	private TextView tv_tiaojian_normal;
	private TextView tv_fanhe_normal;
	private TextView tv_shengri_normal;
	private TextView tv_tixian_normal;
	private TextView tv_yaoqing_normal;

	private RelativeLayout rl_user_core;//核心用户
	private ImageView iv_user_core;
	private LinearLayout ll_user_core;
	private TextView tv_tiaojian_core;
	private TextView tv_fanhe_core;
	private TextView tv_shengri_core;
	private TextView tv_tixian_core;
	private TextView tv_yaoqing_core;
	private TextView tv_choujiang_core;
	private TextView tv_huiyuanri_core;

	private RelativeLayout rl_user_highcore;//高级核心用户
	private ImageView iv_user_highcore;
	private LinearLayout ll_user_highcore;
	private TextView tv_tiaojian_highcore;
	private TextView tv_fanhe_highcore;
	private TextView tv_shengri_highcore;
	private TextView tv_tixian_highcore;
	private TextView tv_youxiangou_highcore;
	private TextView tv_yaoqing_highcore;
	private TextView tv_choujiang_highcore;
	private TextView tv_huiyuanri_highcore;
	private TextView tv_kefu_highcore;

	private boolean isUserNormalOpen = false;
	private boolean isUserCoreOpen = false;
	private boolean isUserHighCoreOpen = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_vipnormallogin);

		progressdialog = new CustomProgressDialog(this, "正在加载数据...");
		initView();
		initData();
		
	}

	private void initView() {
		tv_back = (TextView) findViewById(R.id.tv_back);//返回
		tv_back.setOnClickListener(this);
		vip_number = (TextView) findViewById(R.id.vip_number);//号码
		tv_vip_fanhe = (TextView) findViewById(R.id.tv_vip_fanhe);//饭盒资产
		tv_vip_fanwan = (TextView) findViewById(R.id.tv_vip_fanwan);//饭碗资产
		tv_vip_fantong = (TextView) findViewById(R.id.tv_vip_fantong);//饭桶资产

		tv_fanhe = (TextView) findViewById(R.id.tv_fanhe);//饭盒收益率
		tv_shengri = (TextView) findViewById(R.id.tv_shengri);//生日福利
		tv_tixian = (TextView) findViewById(R.id.tv_tixian);//提现票
		tv_yaoqing = (TextView) findViewById(R.id.tv_yaoqing);//邀请好友

		tv_upgradecore = (TextView) findViewById(R.id.tv_upgradecore);//升级到核心
		tv_toufanwan = (TextView) findViewById(R.id.tv_toufanwan);
		tv_upgradecore.setOnClickListener(this);
		tv_upgradehighcore = (TextView) findViewById(R.id.tv_upgradehighcore);//升级到高级核心
		tv_toufantong = (TextView) findViewById(R.id.tv_toufantong);
		tv_upgradehighcore.setOnClickListener(this);

		rl_user_normal = (RelativeLayout) findViewById(R.id.rl_user_normal);
		iv_user_normal = (ImageView) findViewById(R.id.iv_user_normal);
		ll_user_normal = (LinearLayout) findViewById(R.id.ll_user_normal);
		rl_user_normal.setOnClickListener(this);
		tv_tiaojian_normal = (TextView) findViewById(R.id.tv_tiaojian_normal);
		tv_fanhe_normal = (TextView) findViewById(R.id.tv_fanhe_normal);
		tv_shengri_normal = (TextView) findViewById(R.id.tv_shengri_normal);
		tv_tixian_normal = (TextView) findViewById(R.id.tv_tixian_normal);
		tv_yaoqing_normal = (TextView) findViewById(R.id.tv_yaoqing_normal);

		rl_user_core = (RelativeLayout) findViewById(R.id.rl_user_core);
		iv_user_core = (ImageView) findViewById(R.id.iv_user_core);
		ll_user_core = (LinearLayout) findViewById(R.id.ll_user_core);
		rl_user_core.setOnClickListener(this);
		tv_tiaojian_core = (TextView) findViewById(R.id.tv_tiaojian_core);
		tv_fanhe_core = (TextView) findViewById(R.id.tv_fanhe_core);
		tv_shengri_core = (TextView) findViewById(R.id.tv_shengri_core);
		tv_tixian_core = (TextView) findViewById(R.id.tv_tixian_core);
		tv_yaoqing_core = (TextView) findViewById(R.id.tv_yaoqing_core);
		tv_choujiang_core = (TextView) findViewById(R.id.tv_choujiang_core);
		tv_huiyuanri_core = (TextView) findViewById(R.id.tv_huiyuanri_core);

		rl_user_highcore = (RelativeLayout) findViewById(R.id.rl_user_highcore);
		iv_user_highcore = (ImageView) findViewById(R.id.iv_user_highcore);
		ll_user_highcore = (LinearLayout) findViewById(R.id.ll_user_highcore);
		rl_user_highcore.setOnClickListener(this);
		tv_tiaojian_highcore = (TextView) findViewById(R.id.tv_tiaojian_highcore);
		tv_fanhe_highcore = (TextView) findViewById(R.id.tv_fanhe_highcore);
		tv_shengri_highcore = (TextView) findViewById(R.id.tv_shengri_highcore);
		tv_tixian_highcore = (TextView) findViewById(R.id.tv_tixian_highcore);
		tv_youxiangou_highcore = (TextView) findViewById(R.id.tv_youxiangou_highcore);
		tv_yaoqing_highcore = (TextView) findViewById(R.id.tv_yaoqing_highcore);
		tv_choujiang_highcore = (TextView) findViewById(R.id.tv_choujiang_highcore);
		tv_huiyuanri_highcore = (TextView) findViewById(R.id.tv_huiyuanri_highcore);
		tv_kefu_highcore = (TextView) findViewById(R.id.tv_kefu_highcore);

	}

	
	private void initData() {
		String phone = CacheUtils.getString(this, "phone", null);
		if(phone.length()!=0){
			vip_number.setText(fixNum(phone));
		}
		//显示当前权益
		showMemberEquity();
		//会员等级说明
		getDataFromServer();
	}

	//手机号码星号处理
	private String fixNum(String phone) {
		return phone = phone.substring(0,3) + "****" + phone.substring(7);
	}

	private void showMemberEquity() {
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(this, "token", null);
		if (!progressdialog.isShowing()) {
			progressdialog.show();
		}
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.GETMEMBEREQUITY_URL,
				null, map, new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						// TODO Auto-generated method stub
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
									LogUtils.i("当前权益返回数据==" + datastr);
									JSONObject data = JSON.parseObject(datastr);

									// 饭盒资产
									String fhAcctBal = data.getString("fhAcctBal");
									tv_vip_fanhe.setText(fhAcctBal);
									// 饭碗资产
									String fwAcctBal = data.getString("fwAcctBal");
									tv_vip_fanwan.setText(fwAcctBal);
									// 饭桶资产
									String ftAcctBal = data.getString("ftAcctBal");
									tv_vip_fantong.setText(ftAcctBal);

									// 当前权益
									// 饭盒收益率
									String fhRateAndQuotaDesc = data.getString("fhRateAndQuotaDesc");
									tv_fanhe.setText(fhRateAndQuotaDesc);
									// 生日福利
									boolean hasBirthdayGift = data.getBoolean("hasBirthdayGift");
									// 提现票
									String cashTicketNumDesc = data.getString("cashTicketNumDesc");
									tv_tixian.setText(cashTicketNumDesc);
									// 邀请好友投资奖励
									String inviteRewardDesc = data.getString("inviteRewardDesc");
									if (inviteRewardDesc.contains("|")) {//   | 表示换行
										String replaceDesc = inviteRewardDesc.replaceAll("\\|", "\n");
										tv_yaoqing.setText(replaceDesc);
									}else {
										tv_yaoqing.setText(inviteRewardDesc);
									}

									// 用户升级
									// 升级核心会员还差多少
									String upgradeCoreDesc = data.getString("upgradeCoreDesc");
									if (upgradeCoreDesc.contains("|")) {//   | 表示换行
										String replaceDesc = upgradeCoreDesc.replaceAll("\\|", "\n");
										tv_toufanwan.setText(replaceDesc);
									}else {
										tv_toufanwan.setText(upgradeCoreDesc);
									}
									// 升级高级核心会员还差多少
									String upgradeHighCoreDesc = data.getString("upgradeHighCoreDesc");
									if (upgradeHighCoreDesc.contains("|")) {//   | 表示换行
										String replaceDesc = upgradeHighCoreDesc.replaceAll("\\|", "\n");
										tv_toufantong.setText(replaceDesc);
									}else {
										tv_toufantong.setText(upgradeHighCoreDesc);
									}


								} else {
									ToastUtils.toastshort("加载数据异常！");
								}
							}
						} else {
							ToastUtils.toastshort("加载数据失败！");
						}

					}

					@Override
					public void onError(VolleyError error) {
						// TODO Auto-generated method stub

					}

				});
	}
	private void getDataFromServer() {
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(this, "token", null);
		if (!progressdialog.isShowing()) {
			progressdialog.show();
		}
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.GETALLMEMBEREQUITY_URL,
				null, map, new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						// TODO Auto-generated method stub
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
									LogUtils.i("会员体系返回数据==" + datastr);
									JSONObject data = JSON.parseObject(datastr);

									// 普通用户tv_tiaojian_normal,tv_fanhe_normal,tv_shengri_normal,tv_tixian_normal,tv_yaoqing_normal
									JSONObject normalMemberWelfare = data.getJSONObject("normalMemberWelfare");
									// 满足条件
									String upgradeCondtionDesc_normal = normalMemberWelfare.getString("upgradeCondtionDesc");
									tv_tiaojian_normal.setText(upgradeCondtionDesc_normal);
									// 饭盒收益率
									String fhRateAndQuotaDesc_normal = normalMemberWelfare.getString("fhRateAndQuotaDesc");
									tv_fanhe_normal.setText(fhRateAndQuotaDesc_normal);
									// 生日福利
									boolean hasBirthdayGift_normal = normalMemberWelfare.getBoolean("hasBirthdayGift");
									// 提现票
									String cashTicketNumDesc_normal = normalMemberWelfare.getString("cashTicketNumDesc");
									tv_tixian_normal.setText(cashTicketNumDesc_normal);
									// 邀请好友投资奖励
									String inviteRewardDesc_normal = normalMemberWelfare.getString("inviteRewardDesc");
									if (inviteRewardDesc_normal.contains("|")) {//   | 表示换行
										String replaceDesc = inviteRewardDesc_normal.replaceAll("\\|", "\n");
										tv_yaoqing_normal.setText(replaceDesc);
									}else {
										tv_yaoqing_normal.setText(inviteRewardDesc_normal);
									}
									// 打饭
									boolean signable_normal = normalMemberWelfare.getBoolean("signable");
									// 打饭描述
									String signableDesc_normal = normalMemberWelfare.getString("signableDesc");
									// 核心会员日
									String coreUserDayDesc_normal = normalMemberWelfare.getString("coreUserDayDesc");
									// 专属客服
									boolean hasPrivateService_normal = normalMemberWelfare.getBoolean("hasPrivateService");


									// 核心用户
									JSONObject coreMemberWelfare = data.getJSONObject("coreMemberWelfare");
									// 满足条件
									String upgradeCondtionDesc_core = coreMemberWelfare.getString("upgradeCondtionDesc");
									if (upgradeCondtionDesc_core.contains("|")) {//   | 表示换行
										String replaceDesc = upgradeCondtionDesc_core.replaceAll("\\|", "\n");
										tv_tiaojian_core.setText(replaceDesc);
									}else {
										tv_tiaojian_core.setText(upgradeCondtionDesc_core);
									}
									// 饭盒收益率
									String fhRateAndQuotaDesc_core = coreMemberWelfare.getString("fhRateAndQuotaDesc");
									tv_fanhe_core.setText(fhRateAndQuotaDesc_core);
									// 生日福利
									boolean hasBirthdayGift_core = coreMemberWelfare.getBoolean("hasBirthdayGift");
									// 提现票
									String cashTicketNumDesc_core = coreMemberWelfare.getString("cashTicketNumDesc");
									tv_tixian_core.setText(cashTicketNumDesc_core);
									// 邀请好友投资奖励
									String inviteRewardDesc_core = coreMemberWelfare.getString("inviteRewardDesc");
									if (inviteRewardDesc_core.contains("|")) {//   | 表示换行
										String replaceDesc = inviteRewardDesc_core.replaceAll("\\|", "\n");
										tv_yaoqing_core.setText(replaceDesc);
									}else {
										tv_yaoqing_core.setText(inviteRewardDesc_core);
									}
									// 打饭
									boolean signable_core = coreMemberWelfare.getBoolean("signable");
									// 打饭描述
									String signableDesc_core = coreMemberWelfare.getString("signableDesc");
									tv_choujiang_core.setText(signableDesc_core);
									// 核心会员日
									String coreUserDayDesc_core = coreMemberWelfare.getString("coreUserDayDesc");
									if (coreUserDayDesc_core.contains("|")) {//   | 表示换行
										String replaceDesc = coreUserDayDesc_core.replaceAll("\\|", "\n");
										tv_huiyuanri_core.setText(replaceDesc);
									}else {
										tv_huiyuanri_core.setText(coreUserDayDesc_core);
									}
									// 专属客服
									boolean hasPrivateService_core = coreMemberWelfare.getBoolean("hasPrivateService");


									// 高级核心用户
									JSONObject highCoreMemberWelfare = data.getJSONObject("highCoreMemberWelfare");
									// 满足条件
									String upgradeCondtionDesc_highCore = highCoreMemberWelfare.getString("upgradeCondtionDesc");
									if (upgradeCondtionDesc_highCore.contains("|")) {//   | 表示换行
										String replaceDesc = upgradeCondtionDesc_highCore.replaceAll("\\|", "\n");
										tv_tiaojian_highcore.setText(replaceDesc);
									}else {
										tv_tiaojian_highcore.setText(upgradeCondtionDesc_highCore);
									}
									// 饭盒收益率
									String fhRateAndQuotaDesc_highCore = highCoreMemberWelfare.getString("fhRateAndQuotaDesc");
									tv_fanhe_highcore.setText(fhRateAndQuotaDesc_highCore);
									// 生日福利
									boolean hasBirthdayGift_highCore = highCoreMemberWelfare.getBoolean("hasBirthdayGift");
									// 提现票
									String cashTicketNumDesc_highCore = highCoreMemberWelfare.getString("cashTicketNumDesc");
									tv_tixian_highcore.setText(cashTicketNumDesc_highCore);
									// 优先购
									String preferTicketNumDesc = highCoreMemberWelfare.getString("preferTicketNumDesc");
									tv_youxiangou_highcore.setText(preferTicketNumDesc);
									// 邀请好友投资奖励
									String inviteRewardDesc_highCore = highCoreMemberWelfare.getString("inviteRewardDesc");
									if (inviteRewardDesc_highCore.contains("|")) {//   | 表示换行
										String replaceDesc = inviteRewardDesc_highCore.replaceAll("\\|", "\n");
										tv_yaoqing_highcore.setText(replaceDesc);
									}else {
										tv_yaoqing_highcore.setText(inviteRewardDesc_highCore);
									}
									// 打饭
									boolean signable_highCore = highCoreMemberWelfare.getBoolean("signable");
									// 打饭描述
									String signableDesc_highCore = highCoreMemberWelfare.getString("signableDesc");
									tv_choujiang_highcore.setText(signableDesc_highCore);
									// 核心会员日
									String coreUserDayDesc_highCore = highCoreMemberWelfare.getString("coreUserDayDesc");
									if (coreUserDayDesc_highCore.contains("|")) {//   | 表示换行
										String replaceDesc = coreUserDayDesc_highCore.replaceAll("\\|", "\n");
										tv_huiyuanri_highcore.setText(replaceDesc);
									}else {
										tv_huiyuanri_highcore.setText(coreUserDayDesc_highCore);
									}
									// 专属客服
									boolean hasPrivateService_highCore = highCoreMemberWelfare.getBoolean("hasPrivateService");




								} else {
									ToastUtils.toastshort("加载数据异常！");
								}
							}
						} else {
							ToastUtils.toastshort("加载数据失败！");
						}

					}

					@Override
					public void onError(VolleyError error) {
						// TODO Auto-generated method stub

					}

				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			finish();//返回上个界面
			break;
		case R.id.tv_upgradecore:
			ConstantUtils.touziflag = 1;
			ConstantUtils.fanheorfanwanorfantongflag = 1;
			Intent intent = new Intent(this,MainActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.tv_upgradehighcore:
			ConstantUtils.touziflag = 1;
			ConstantUtils.fanheorfanwanorfantongflag = 1;
			Intent intent2 = new Intent(this,MainActivity.class);
			startActivity(intent2);
			finish();
			break;
		case R.id.rl_user_normal:
			isUserNormalOpen = !isUserNormalOpen;
			if(isUserNormalOpen){
				ll_user_normal.setVisibility(View.VISIBLE);
				iv_user_normal.setImageResource(R.drawable.arrow_up);
				ll_user_core.setVisibility(View.GONE);
				iv_user_core.setImageResource(R.drawable.arrow_down);
				ll_user_highcore.setVisibility(View.GONE);
				iv_user_highcore.setImageResource(R.drawable.arrow_down);
				isUserCoreOpen = false;
				isUserHighCoreOpen = false;
			}else{
				ll_user_normal.setVisibility(View.GONE);
				iv_user_normal.setImageResource(R.drawable.arrow_down);
			}
			break;
		case R.id.rl_user_core:
			isUserCoreOpen = !isUserCoreOpen;
			if(isUserCoreOpen){
				ll_user_normal.setVisibility(View.GONE);
				iv_user_normal.setImageResource(R.drawable.arrow_down);
				ll_user_core.setVisibility(View.VISIBLE);
				iv_user_core.setImageResource(R.drawable.arrow_up);
				ll_user_highcore.setVisibility(View.GONE);
				iv_user_highcore.setImageResource(R.drawable.arrow_down);
				isUserNormalOpen = false;
				isUserHighCoreOpen = false;
			}else{
				ll_user_core.setVisibility(View.GONE);
				iv_user_core.setImageResource(R.drawable.arrow_down);
			}
			break;
		case R.id.rl_user_highcore:
			isUserHighCoreOpen = !isUserHighCoreOpen;
			if(isUserHighCoreOpen){
				ll_user_normal.setVisibility(View.GONE);
				iv_user_normal.setImageResource(R.drawable.arrow_down);
				ll_user_core.setVisibility(View.GONE);
				iv_user_core.setImageResource(R.drawable.arrow_down);
				ll_user_highcore.setVisibility(View.VISIBLE);
				iv_user_highcore.setImageResource(R.drawable.arrow_up);
				isUserNormalOpen = false;
				isUserCoreOpen = false;
			}else{
				ll_user_highcore.setVisibility(View.GONE);
				iv_user_highcore.setImageResource(R.drawable.arrow_down);
			}
			break;
		default:
			break;
		}
	}
}
