package com.fanfanlicai.pagers;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.home.InviteFriActivity;
import com.fanfanlicai.activity.my.CGBankNotBindActivity;
import com.fanfanlicai.activity.my.CGBankQianYueActivity;
import com.fanfanlicai.activity.my.ChongZhiActivity;
import com.fanfanlicai.activity.my.ContactActivity;
import com.fanfanlicai.activity.my.HelpActivity;
import com.fanfanlicai.activity.my.ZhanNeiXinActivity;
import com.fanfanlicai.activity.my.CashActivity;
import com.fanfanlicai.activity.my.FanPiaoActivity;
import com.fanfanlicai.activity.my.InvestActivity;
import com.fanfanlicai.activity.home.JiangJinLiuShuiActivity;
import com.fanfanlicai.activity.my.VipCoreLoginActivity;
import com.fanfanlicai.activity.my.VipHighCoreLoginActivity;
import com.fanfanlicai.activity.my.VipNormalLoginActivity;
import com.fanfanlicai.activity.my.VipNotLoginActivity;
import com.fanfanlicai.activity.home.ZhangHuLiuShuiActivity;
import com.fanfanlicai.activity.home.ZongZiChanActivity;
import com.fanfanlicai.activity.registerandlogin.HomeLoginActivity;
import com.fanfanlicai.activity.setting.SettingActivity;
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
import com.fanfanlicai.view.dialog.CGBindCardDialog;
import com.fanfanlicai.view.dialog.CGSignCardDialog;
import com.fanfanlicai.view.dialog.CustomProgressDialog;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.Unicorn;

import java.util.Map;

public class MyPager extends BasePager implements OnClickListener {

	private CustomProgressDialog progressdialog;
	private ImageView iv_setting;//设置
	private RelativeLayout rl1_total;//总资产
	private RelativeLayout rl2_zhanghu;//账户余额
	private RelativeLayout rl3_jiangjin;//奖金余额
	private RelativeLayout ll_image1;//已投项目
	private RelativeLayout ll_image2;//饭饭消息
	private RelativeLayout ll_image3;//邀请好友
	private RelativeLayout ll_image4;//帮助反馈
	private RelativeLayout ll_image5;//银行卡
	private RelativeLayout ll_image6;//联系我们
	private RelativeLayout ll_image7;//我的饭票
	private RelativeLayout ll_image8;//饭饭介绍
	private RelativeLayout ll_image9;//饭饭介绍
	
	private ImageView my_portrait;//头像
	private ImageView core;//会员
	private TextView my_number;//号码
	private TextView tv_zongzichan;//总资产
	private TextView tv_zhanghuyue;//账户余额
	private TextView tv_jiangjinyue;//奖金余额
	private Button bt_cash;//提现
	private Button bt_invest;//充值
	
	private TextView tv_news;//新消息提示

	private String memberLevel;//会员等级


	public MyPager(Context context) {
		super(context);
	}

	@Override
	protected View initView() {
		View view = View.inflate(mContext, R.layout.pager_my, null);
		progressdialog = new CustomProgressDialog(mContext, "正在加载数据...");
		my_number = (TextView) view.findViewById(R.id.my_number);//账户号码
		my_portrait = (ImageView) view.findViewById(R.id.my_portrait);//图像
		core = (ImageView) view.findViewById(R.id.core);//会员
		iv_setting = (ImageView) view.findViewById(R.id.iv_setting);//设置
		rl1_total = (RelativeLayout) view.findViewById(R.id.rl1_total);//总资产
		rl2_zhanghu = (RelativeLayout) view.findViewById(R.id.rl2_zhanghu);//账户余额
		rl3_jiangjin = (RelativeLayout) view.findViewById(R.id.rl3_jiangjin);//奖金余额
		
		bt_cash = (Button) view.findViewById(R.id.bt_cash);//提现
		bt_invest = (Button) view.findViewById(R.id.bt_invest);//充值
		
		ll_image1 = (RelativeLayout) view.findViewById(R.id.ll_image1);//已投项目
		ll_image2 = (RelativeLayout) view.findViewById(R.id.ll_image2);//饭饭消息
		ll_image3 = (RelativeLayout) view.findViewById(R.id.ll_image3);//邀请好友
		ll_image4 = (RelativeLayout) view.findViewById(R.id.ll_image4);//帮助中心
		ll_image5 = (RelativeLayout) view.findViewById(R.id.ll_image5);//银行卡
		ll_image6 = (RelativeLayout) view.findViewById(R.id.ll_image6);//联系我们
		ll_image7 = (RelativeLayout) view.findViewById(R.id.ll_image7);//我的饭票(2.1版加的新功能)
		ll_image8 = (RelativeLayout) view.findViewById(R.id.ll_image8);//饭饭介绍(2.1版加的新功能)
		ll_image9 = (RelativeLayout) view.findViewById(R.id.ll_image9);//在线客服(2.3版加的新功能)
		
		tv_zongzichan = (TextView) view.findViewById(R.id.tv_zongzichan);//总资产
		tv_zhanghuyue = (TextView) view.findViewById(R.id.tv_zhanghuyue);//账户余额
		tv_jiangjinyue = (TextView) view.findViewById(R.id.tv_jiangjinyue);//奖金余额

		tv_news = (TextView) view.findViewById(R.id.tv_news);//新消息提示

		my_number.setOnClickListener(this);
		core.setOnClickListener(this);
		iv_setting.setOnClickListener(this);
		rl1_total.setOnClickListener(this);
		rl2_zhanghu.setOnClickListener(this);
		rl3_jiangjin.setOnClickListener(this);
		
		bt_cash.setOnClickListener(this);
		bt_invest.setOnClickListener(this);
		
		ll_image1.setOnClickListener(this);
		ll_image2.setOnClickListener(this);
		ll_image3.setOnClickListener(this);
		ll_image4.setOnClickListener(this);
		ll_image5.setOnClickListener(this);
		ll_image6.setOnClickListener(this);
		ll_image7.setOnClickListener(this);
		ll_image8.setOnClickListener(this);
		ll_image9.setOnClickListener(this);
		

		return view;
	}
	@Override
	public void initData() {
		String token = CacheUtils.getString(mContext, "token", null);
		if (!TextUtils.isEmpty(token)) {//登录状态
			my_portrait.setImageResource(R.drawable.head_in);
			// 访问网络
			getDataFromServer();
		}else {//未登录状态
			my_portrait.setImageResource(R.drawable.head_out);
			core.setVisibility(View.GONE);
			my_number.setText("您好，请登录");
			tv_zongzichan.setText("0.00");
			tv_zhanghuyue.setText("0.00");
			tv_jiangjinyue.setText("0.00");
		}
		
	}

	private void getDataFromServer() {

		if (!progressdialog.isShowing()) {
			progressdialog.showis();
		}
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(mContext, "token", null);
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.MY_ACCOUNT_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						
						LogUtils.i("我的页面==="+string);
						
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
									
									// 用户类型
									memberLevel = data.getString("memberLevel");
									if("0".equals(memberLevel)){
										core.setImageResource(R.drawable.user_normal);
									}else if("1".equals(memberLevel)){
										core.setImageResource(R.drawable.user_core);
									}else if("2".equals(memberLevel)) {
										core.setImageResource(R.drawable.user_highcore);
									}

									// 真实姓名
									String realName = data.getString("realName");
									CacheUtils.putString(mContext, "realName",realName);

									// 身份证
									String idNo = data.getString("idNo");
									CacheUtils.putString(mContext, "idNo",idNo);

									// 银行卡号
									String cardNum = data.getString("cardNum");
									CacheUtils.putString(mContext, "cardNum",cardNum);

									// 手机号
									String phone = data.getString("phone");
									CacheUtils.putString(mContext, "phone",phone);
									if(phone.length()!=0){
										LogUtils.i("phone=="+phone);
										my_number.setText(fixNum(phone));
									}

									// 总资产
									String totalAssets = data.getString("totalAssets");
									CacheUtils.putString(mContext, "totalAssets",totalAssets);
									
									// 账户余额
									String baseAcctBal = data.getString("baseAcctBal");
									if("-9999".equals(baseAcctBal)){//银行不能正常返回账户余额
										baseAcctBal = "正在获取中";
										tv_zongzichan.setText(baseAcctBal);
										tv_zhanghuyue.setText(baseAcctBal);
										rl1_total.setClickable(false);
										rl2_zhanghu.setClickable(false);
									}else{
										tv_zongzichan.setText(totalAssets);
										tv_zhanghuyue.setText(baseAcctBal);
										rl1_total.setClickable(true);
										rl2_zhanghu.setClickable(true);
										CacheUtils.putString(mContext, "baseAcctBal",baseAcctBal);
									}

									// 奖金账户余额
									String rewardAcctBal = data.getString("rewardAcctBal");
									tv_jiangjinyue.setText(rewardAcctBal);
									CacheUtils.putString(mContext, "rewardAcctBal",rewardAcctBal);
									
									// 饭盒资产
									String fhAcctBal = data.getString("fhAcctBal");
									CacheUtils.putString(mContext, "fhAcctBal",fhAcctBal);
									
									// 饭碗资产
									String fwAcctBal = data.getString("fwAcctBal");
									CacheUtils.putString(mContext, "fwAcctBal",fwAcctBal);
									
									// 饭桶资产
									String ftAcctBal = data.getString("ftAcctBal");
									CacheUtils.putString(mContext, "ftAcctBal",ftAcctBal);
									
									// 饭碗待收收益
									String fwWaitPayIncome = data.getString("fwWaitPayIncome");
									CacheUtils.putString(mContext, "fwWaitPayIncome",fwWaitPayIncome);
									
									// 赎回中
									String redeemAmount = data.getString("redeemAmount");
									CacheUtils.putString(mContext, "redeemAmount",redeemAmount);

									// 体现中
									String cashingMoney = data.getString("cashingMoney");
									CacheUtils.putString(mContext, "cashingMoney",cashingMoney);

									// 已使用奖金
									String rewardUsedAmount = data.getString("rewardUsedAmount");
									CacheUtils.putString(mContext, "rewardUsedAmount",rewardUsedAmount);
									
									// 邀请码
									String inviteCode = data.getString("inviteCode");
									CacheUtils.putString(mContext, "inviteCode",inviteCode);
									
									// 地址
									String address = data.getString("address");
									CacheUtils.putString(mContext, "address",address);
											
									// 邮编
									String zipCode = data.getString("zipCode");
									CacheUtils.putString(mContext, "zipCode",zipCode);
									
									// 是否绑卡
									Boolean isBindCard = data.getBoolean("isBindCard");
									CacheUtils.putBoolean(mContext, "isBindCard",isBindCard);

									// email
									String email = data.getString("email");
									LogUtils.i("email==="+email);
									CacheUtils.putString(mContext, "email",email);

									// 是否有新消息
									boolean hasNews = data.getBoolean("hasNews"); 
									if (hasNews) {
										tv_news.setVisibility(View.VISIBLE);
									}else {
										tv_news.setVisibility(View.INVISIBLE);
									}
									
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

	//手机号码星号处理
	private String fixNum(String phone) {
		return phone = phone.substring(0,3) + "****" + phone.substring(7);
	}

	@Override
	public void onClick(View v) {
		
		String token = CacheUtils.getString(mContext, "token", null);
		switch (v.getId()) {
		case R.id.my_number:
			if (TextUtils.isEmpty(token)) {
				mContext.startActivity(new Intent(mContext, HomeLoginActivity.class));
			}
			break;
		case R.id.core:
			if (TextUtils.isEmpty(token)) {

			}else{
				if("0".equals(memberLevel)){
					mContext.startActivity(new Intent(mContext, VipNormalLoginActivity.class));
				}else if("1".equals(memberLevel)){
					mContext.startActivity(new Intent(mContext, VipCoreLoginActivity.class));
				}else if("2".equals(memberLevel)) {
					mContext.startActivity(new Intent(mContext, VipHighCoreLoginActivity.class));
				}
			}
			break;
		case R.id.iv_setting://设置
			if (TextUtils.isEmpty(token)) {
				mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
			}else {
				mContext.startActivity(new Intent(mContext, SettingActivity.class));
			}
			break;
		case R.id.rl1_total://总资产
			if (TextUtils.isEmpty(token)) {
				mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
			}else {
				mContext.startActivity(new Intent(mContext, ZongZiChanActivity.class));
			}
			break;
		case R.id.rl2_zhanghu://账户余额
			if (TextUtils.isEmpty(token)) {
				mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
			}else {
				mContext.startActivity(new Intent(mContext, ZhangHuLiuShuiActivity.class));
			}
			break;
		case R.id.rl3_jiangjin://奖金余额
			if (TextUtils.isEmpty(token)) {
				mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
			}else {
				mContext.startActivity(new Intent(mContext, JiangJinLiuShuiActivity.class));
			}
			break;
		case R.id.bt_cash://提现
			if (TextUtils.isEmpty(token)) {
				mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
			}else {
				//因为提现成功后需要返回到我的界面，并刷新数据，就定义了此flag，
				//其实这个标记放在getdetailactivity里对应的三个返回方法里也行
				ConstantUtils.touziflag = 2;
				//提现第一步获取用户信息，先拿到是否已绑卡
				requestCash();
			}
			break;
		case R.id.bt_invest://充值
			if (TextUtils.isEmpty(token)) {
				mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
			}else {
				//因为充值成功后需要返回到我的界面，并刷新数据，就定义了此flag，
				//其实这个标记放在chongzhisuccessactivity里对应的三个返回方法里也行
				ConstantUtils.touziflag = 2;
				//充值第一步获取用户信息
				checkChongZhiState();
			}
			break;
		
		case R.id.ll_image1://已投项目
			if (TextUtils.isEmpty(token)) {
				mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
			}else {
				ConstantUtils.touziflag = 2;
				mContext.startActivity(new Intent(mContext, InvestActivity.class));
			}
			break;
		case R.id.ll_image2://饭饭消息
			if (TextUtils.isEmpty(token)) {
				mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
			}else {
				mContext.startActivity(new Intent(mContext, ZhanNeiXinActivity.class));
			}
			break;
		case R.id.ll_image3://邀请好友
			if (TextUtils.isEmpty(token)) {
				mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
			}else {
				mContext.startActivity(new Intent(mContext, InviteFriActivity.class));
			}
			break;
			
		case R.id.ll_image4://帮助中心
			mContext.startActivity(new Intent(mContext, HelpActivity.class));
			break;
		case R.id.ll_image5://银行卡
			if (TextUtils.isEmpty(token)) {
				mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
			}else {
				checkBank();
			}
			
			break;
		case R.id.ll_image6://联系我们
			mContext.startActivity(new Intent(mContext, ContactActivity.class));
			break;
		case R.id.ll_image7://我的饭票
			if (TextUtils.isEmpty(token)) {
				mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
			}else {
				mContext.startActivity(new Intent(mContext, FanPiaoActivity.class));
			}
			break;
		case R.id.ll_image8://会员体系
			if (TextUtils.isEmpty(token)) {
				mContext.startActivity(new Intent(mContext, VipNotLoginActivity.class));
			}else{
				if("0".equals(memberLevel)){
					mContext.startActivity(new Intent(mContext, VipNormalLoginActivity.class));
				}else if("1".equals(memberLevel)){
					mContext.startActivity(new Intent(mContext, VipCoreLoginActivity.class));
				}else if("2".equals(memberLevel)) {
					mContext.startActivity(new Intent(mContext, VipHighCoreLoginActivity.class));
				}
			}
			break;
		case R.id.ll_image9://在线客服
			ConsultSource source = new ConsultSource(null, null, null);
			Unicorn.openServiceActivity(mContext, // 上下文
					"饭饭客服", // 聊天窗口的标题
					source // 咨询的发起来源，包括发起咨询的url，title，描述信息等
			);
			break;

		default:
			break;
		}
	}


	private void checkChongZhiState() {
		if (!progressdialog.isShowing()) {
			progressdialog.showis();
		}
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(mContext, "token", null);
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.QUERYBINDCARANDSIGNSTATUS_URL, null, map,
				new HttpBackBaseListener() {
					@Override
					public void onSuccess(String string) {
						LogUtils.i("充值按钮==="+string);
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
									JSONObject data = JSON.parseObject(datastr);


									// 是否开通存管户
									String hasFyAccount = data.getString("hasFyAccount");

									// 是否签约
									String isSignCard = data.getString("isSignCard");

									// 是否开通存管户（绑卡）0-未开通；
									if ("0".equals(hasFyAccount)) {// 如果还没有绑卡，去绑卡

										CGBindCardDialog cgBindCardDialog = new CGBindCardDialog(mContext, R.style.YzmDialog);
										cgBindCardDialog.show();
									}else{//1-已开通
										if ("0".equals(isSignCard)) {// 如果还没有签约，去签约
											CGSignCardDialog cgSignCardDialog = new CGSignCardDialog(mContext, R.style.YzmDialog);
											cgSignCardDialog.show();
										}else{//1-已签约
											Intent intent = new Intent(mContext, ChongZhiActivity.class);
											mContext.startActivity(intent);
										}

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
						progressdialog.dismiss();
					}
				});
	}

	private void checkBank() {
		if (!progressdialog.isShowing()) {
			progressdialog.showis();
		}
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(mContext, "token", null);
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.QUERYBINDCARANDSIGNSTATUS_URL, null, map,
				new HttpBackBaseListener() {
					@Override
					public void onSuccess(String string) {
						LogUtils.i("银行卡==="+string);
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
									JSONObject data = JSON.parseObject(datastr);


									// 是否开通存管户
									String hasFyAccount = data.getString("hasFyAccount");

									// 是否签约
									String isSignCard = data.getString("isSignCard");

									// 是否开通存管户（绑卡）0-未开通；
									if ("0".equals(hasFyAccount)) {// 如果还没有绑卡，去绑卡

										Intent intent = new Intent(mContext, CGBankNotBindActivity.class);
										mContext.startActivity(intent);
									}else{//1-已开通
										Intent intent = new Intent(mContext, CGBankQianYueActivity.class);
										intent.putExtra("isSignCard",isSignCard);
										mContext.startActivity(intent);
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
						progressdialog.dismiss();
					}
				});
	}


	private void requestCash() {
		if (!progressdialog.isShowing()) {
			progressdialog.showis();
		}
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(mContext, "token", null);
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.CASHVERIFYINFO_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						LogUtils.i("提现第一步判断是否开户==="+string);
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
									JSONObject data = JSON.parseObject(datastr);

									// 账户余额
									String baseBal = data.getString("baseBal");

									//充值未投资金额
									String rechBal = data.getString("rechBal");

									// 是否开通存管户
									String hasFyAccount = data.getString("hasFyAccount");

									// 是否签约
									String isSignCard = data.getString("isSignCard");

									// 是否开通存管户（绑卡）0-未开通；
									if ("0".equals(hasFyAccount)) {// 如果还没有绑卡，去绑卡

										CGBindCardDialog cgBindCardDialog = new CGBindCardDialog(mContext, R.style.YzmDialog);
										cgBindCardDialog.show();
									}else{//1-已开通
										if ("0".equals(isSignCard)) {// 如果还没有签约，去签约
											CGSignCardDialog cgSignCardDialog = new CGSignCardDialog(mContext, R.style.YzmDialog);
											cgSignCardDialog.show();
										}else{//1-已签约
											// 账户余额
											if("-9999".equals(baseBal)) {//银行不能正常返回账户余额
												ToastUtils.toastshort("正在获取账户余额...");
											}else {
												Intent intent = new Intent(mContext, CashActivity.class);
												intent.putExtra("rechBal", rechBal);
												mContext.startActivity(intent);
											}
										}

									}

								} else {
									ToastUtils.toastshort("加载数据异常！");
								}
							}
						} else if("666666".equals(code)){

						}else {
							String msg = json.getString("msg");
							ToastUtils.toastshort(msg);
						}
					}

					@Override
					public void onError(VolleyError error) {
						progressdialog.dismiss();
					}
				});
	}

}
