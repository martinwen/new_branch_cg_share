package com.fanfanlicai.pagers;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.home.InviteFriActivity;
import com.fanfanlicai.activity.home.LeiJiShouYiActivity;
import com.fanfanlicai.activity.invest.FanTongBuyActivity;
import com.fanfanlicai.activity.invest.FanTongRateActivity;
import com.fanfanlicai.activity.my.InvestActivity;
import com.fanfanlicai.activity.registerandlogin.HomeLoginActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.MathUtil;
import com.fanfanlicai.utils.StrToNumber;
import com.fanfanlicai.utils.StringUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;
import com.fanfanlicai.view.dialog.CGBindCardDialog;
import com.fanfanlicai.view.dialog.CGSignCardDialog;
import com.fanfanlicai.view.dialog.CePingAgainDialog;
import com.fanfanlicai.view.dialog.CePingChanceOverDialog;
import com.fanfanlicai.view.dialog.CePingNotDialog;
import com.fanfanlicai.view.dialog.CustomProgressDialog;
import com.fanfanlicai.view.dialog.FanTongYuYueBeforeDialog;
import com.fanfanlicai.view.dialog.NormalDialog;

import java.util.Map;

public class FanTongPager extends BasePager implements OnClickListener{

	private CustomProgressDialog progressdialog;
	private TextView tv_base;//基本利率
	private TextView tv_shouyixiangqing;//查看详细规则
	private Button bt_cash;//赎回
	private Button bt_buy;//买入
	private RelativeLayout rl_zuori;//昨日收益
	private RelativeLayout rl_jiangjin;//奖金余额
	private RelativeLayout rl_leiji;//累计收益
	private RelativeLayout rl_zongzichan;//饭桶资产
	private TextView tv_zuori;
	private TextView tv_jiangjin;
	private TextView tv_leiji;
	private TextView tv_fantong;
	private TextView tv_xiangmuyue;//项目余额
	private TextView tv_min_invest;//起投金额
	
	private double baseRate;
	private double addRate;
	
	private String bookStatus;
	private boolean isBuyDay;
	private String bookAmount;
	private String forbidRedeemTime;
	private Boolean canRedeemFlag;

	public FanTongPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected View initView() {
		LogUtils.i("饭桶投资initView");
		View view = View.inflate(mContext, R.layout.pager_invest_fantong, null);
		progressdialog = new CustomProgressDialog(mContext, "正在加载数据...");
		tv_base = (TextView) view.findViewById(R.id.tv_base);//基本利率
		tv_shouyixiangqing = (TextView) view.findViewById(R.id.tv_shouyixiangqing);//查看详细规则
		tv_shouyixiangqing.setOnClickListener(this);
		bt_cash = (Button) view.findViewById(R.id.bt_cash);//赎回
		bt_cash.setOnClickListener(this);
		bt_buy = (Button) view.findViewById(R.id.bt_buy);//买入
		bt_buy.setOnClickListener(this);
		rl_zuori = (RelativeLayout) view.findViewById(R.id.rl_zuori);//昨日收益
		rl_jiangjin = (RelativeLayout) view.findViewById(R.id.rl_jiangjin);//奖金余额
		rl_leiji = (RelativeLayout) view.findViewById(R.id.rl_leiji);//累计收益
		rl_zongzichan = (RelativeLayout) view.findViewById(R.id.rl_zongzichan);//饭桶资产
		rl_zuori.setOnClickListener(this);
		rl_jiangjin.setOnClickListener(this);
		rl_leiji.setOnClickListener(this);
		rl_zongzichan.setOnClickListener(this);
		tv_zuori = (TextView) view.findViewById(R.id.tv_zuori);
		tv_jiangjin = (TextView) view.findViewById(R.id.tv_jiangjin);
		tv_leiji = (TextView) view.findViewById(R.id.tv_leiji);
		tv_fantong = (TextView) view.findViewById(R.id.tv_fantong);
		tv_xiangmuyue = (TextView) view.findViewById(R.id.tv_xiangmuyue);//项目余额
		tv_min_invest = (TextView) view.findViewById(R.id.tv_min_invest);//起投金额
		return view;
	}
	
	@Override
	public void initData() {
		LogUtils.i("饭桶投资initData");
		String token = CacheUtils.getString(mContext, "token", null);
		if (TextUtils.isEmpty(token)) {// 没有登录过或者登录后退出了
			tv_zuori.setText("0.00");
			tv_jiangjin.setText("0.00");
			tv_leiji.setText("0.00");
			tv_fantong.setText("0.00");
			tv_base.setText("12.12%");
			// 访问网络，初始化默认值（起投金额，项目余额）
			getDataFromServerWithoutToken();
		} else {// 登录过并且没有退出
			// 访问网络
			getDataFromServer();
		}
	}

	private void getDataFromServerWithoutToken() {
		if (!progressdialog.isShowing()) {
			progressdialog.showis();
		}
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(mContext, "token", null);
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.FTINVESTINFO_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						
//						LogUtils.i("饭桶投资未登录页面==="+string);
						
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
									
									// 基础年化收益率
									baseRate = data.getDouble("baseRate");
									// 加息年化收益率
									addRate = data.getDouble("addRate");
									tv_base.setText(MathUtil.subDouble((baseRate+addRate), 2)+"%");
									
									// 饭桶起投金额
									String ftMinInvestAmount = data.getString("ftMinInvestAmount");
									tv_min_invest.setText(StrToNumber.strTodouble(ftMinInvestAmount)/10000+"万元");
									
									// 饭桶可售金额
									String ftBorrowSalableBal = data.getString("ftBorrowSalableBal");
									tv_xiangmuyue.setText(ftBorrowSalableBal+"万元");
									
									// 0未预约，1待审核，2预约成功
									bookStatus = data.getString("bookStatus");
									
									// 是否到预约买入时间
									isBuyDay = data.getBoolean("isBuyDay");
									
									// 预约金额
									bookAmount = data.getString("bookAmount");
									
									if ("0".equals(bookStatus)) {
										bt_buy.setBackgroundResource(R.drawable.button_short_selector2);
										bt_buy.setClickable(true); 
										bt_buy.setText("预约买入");
									}else if ("1".equals(bookStatus)) {
										bt_buy.setBackgroundResource(R.drawable.anniu_duan_yishouqing);  
										bt_buy.setClickable(false);
										bt_buy.setText("已预约");
									}else if ("2".equals(bookStatus)) {
										if (isBuyDay) {
											if ("0.00".equals(ftBorrowSalableBal)||"0".equals(ftBorrowSalableBal)) {
												bt_buy.setBackgroundResource(R.drawable.anniu_duan_yishouqing);  
												bt_buy.setClickable(false);  
												bt_buy.setText("已售罄");
											}else {
												bt_buy.setBackgroundResource(R.drawable.button_short_selector2);
												bt_buy.setClickable(true); 
												bt_buy.setText("买入");
											}
										}else {
											bt_buy.setBackgroundResource(R.drawable.anniu_duan_yishouqing);  
											bt_buy.setClickable(false);
											bt_buy.setText("已预约");
										}
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
					}
				});
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
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.FTINVESTINFO_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						
						LogUtils.i("饭桶投资页面==="+string);
						
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
									
									// 基础年化收益率
									baseRate = data.getDouble("baseRate");
									// 加息年化收益率
									addRate = data.getDouble("addRate");
									tv_base.setText(MathUtil.subDouble((baseRate+addRate), 2)+"%");
									
									// 饭桶起投金额
									String ftMinInvestAmount = data.getString("ftMinInvestAmount");
									tv_min_invest.setText(StrToNumber.strTodouble(ftMinInvestAmount)/10000+"万元");
									
									// 饭桶昨日收益
									String ftYesterdayIncome = data.getString("ftYesterdayIncome");
									tv_zuori.setText(ftYesterdayIncome);
									
									// 奖金余额
									String rewardAcctBal = data.getString("rewardAcctBal");
									tv_jiangjin.setText(rewardAcctBal);
									CacheUtils.putString(mContext, "rewardAcctBal",rewardAcctBal);
									
									// 饭桶累计收益
									String ftTotalIncome = data.getString("ftTotalIncome");
									tv_leiji.setText(ftTotalIncome);
									
									// 饭桶资产
									String ftAcctBal = data.getString("ftAcctBal");
									tv_fantong.setText(ftAcctBal);
									
									// 账户余额
									String baseAcctBal = data.getString("baseAcctBal");
									
									// 饭盒资产
									String fhAcctBal = data.getString("fhAcctBal");
									
									// 饭桶可售金额
									String ftBorrowSalableBal = data.getString("ftBorrowSalableBal");
									tv_xiangmuyue.setText(ftBorrowSalableBal+"万元");
									
									// 0未预约，1待审核，2预约成功
									bookStatus = data.getString("bookStatus");
									
									// 是否到预约买入时间
									isBuyDay = data.getBoolean("isBuyDay");
									
									// 预约金额
									bookAmount = data.getString("bookAmount");
									
									if ("0".equals(bookStatus)) {
										bt_buy.setBackgroundResource(R.drawable.button_short_selector2);
										bt_buy.setClickable(true); 
										bt_buy.setText("预约买入");
									}else if ("1".equals(bookStatus)) {
										bt_buy.setBackgroundResource(R.drawable.anniu_duan_yishouqing);  
										bt_buy.setClickable(false);
										bt_buy.setText("已预约");
									}else if ("2".equals(bookStatus)) {
										if (isBuyDay) {
											if ("0.00".equals(ftBorrowSalableBal)||"0".equals(ftBorrowSalableBal)) {
												bt_buy.setBackgroundResource(R.drawable.anniu_duan_yishouqing);  
												bt_buy.setClickable(false);  
												bt_buy.setText("已售罄");
											}else {
												bt_buy.setBackgroundResource(R.drawable.button_short_selector2);
												bt_buy.setClickable(true); 
												bt_buy.setText("买入");
											}
										}else {
											bt_buy.setBackgroundResource(R.drawable.anniu_duan_yishouqing);  
											bt_buy.setClickable(false);
											bt_buy.setText("已预约");
										}
									}

									// 赎回按钮是否可点击
									canRedeemFlag = data.getBoolean("canRedeemFlag");
									// 赎回按钮置灰后弹窗内容
									forbidRedeemTime = data.getString("forbidRedeemTime");
									if(!canRedeemFlag){
										bt_cash.setBackgroundResource(R.drawable.anniu_duan_yishouqing);
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
					}
				});
	}
	
	@Override
	public void onClick(View v) {
		
		String token = CacheUtils.getString(mContext, "token", null);
		switch (v.getId()) {
		case R.id.tv_shouyixiangqing:
			if (TextUtils.isEmpty(token)) {//未登录时，点击进入登录页面
				mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
			}else {//登录
				Intent intent = new Intent(mContext, FanTongRateActivity.class);
				intent.putExtra("baseRate", baseRate);
				intent.putExtra("addRate", addRate);
				mContext.startActivity(intent);
			}
			break;
		case R.id.bt_cash://赎回
			if (TextUtils.isEmpty(token)) {//未登录时，点击赎回进入登录页面；
				mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
			}else {//登录
				// 赎回按钮置灰后弹窗内容
				if(canRedeemFlag){
					checkState();
				}else {
					NormalDialog normalDialog = new NormalDialog(mContext, R.style.YzmDialog, forbidRedeemTime);
					normalDialog.show();
				}
			}
			break;
		case R.id.bt_buy://预约买入
			if (TextUtils.isEmpty(token)) {//未登录时，点击买入进入登录页面；
				mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
			}else {//登录
				//因为买入成功后需要返回到饭桶投资界面，并刷新数据，就定义了此flag，
				ConstantUtils.touziflag = 1;
				ConstantUtils.fanheorfanwanorfantongflag = 2;

				// bookStatus 0未预约，1待审核，2预约成功
				if ("0".equals(bookStatus)) {
					//走预约方法
					requestBook();

				}else if ("2".equals(bookStatus)) {
					if (isBuyDay) {
						//走买入方法
						requestBuy();

					}
				}
			}
			break;
		case R.id.rl_zuori://昨日收益
			if (TextUtils.isEmpty(token)) {//未登录
				mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
			}else {//登录
				ConstantUtils.shouyiflag = 2;
				mContext.startActivity(new Intent(mContext, LeiJiShouYiActivity.class));
			}
			break;
		
		case R.id.rl_jiangjin://奖金余额
			if (TextUtils.isEmpty(token)) {//未登录
				mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
			}else {//登录
				mContext.startActivity(new Intent(mContext, InviteFriActivity.class));
			}
			break;
		case R.id.rl_leiji://累计收益
			if (TextUtils.isEmpty(token)) {//未登录
				mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
			}else {//登录
				ConstantUtils.shouyiflag = 2;
				mContext.startActivity(new Intent(mContext,LeiJiShouYiActivity.class));
			}
			break;
		case R.id.rl_zongzichan://饭桶资产
			if (TextUtils.isEmpty(token)) {//未登录
				mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
			}else {//登录
				//因为赎回成功后需要返回到饭桶投资界面，并刷新数据，就定义了此flag，
				ConstantUtils.touziflag = 1;
				ConstantUtils.yitouxiangmuflag = 2;
				mContext.startActivity(new Intent(mContext, InvestActivity.class));
			}
			break;
		default:
			break;
		}
	}

	private void checkState() {
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
											//因为赎回成功后需要返回到饭桶投资界面，并刷新数据，就定义了此flag，
											ConstantUtils.touziflag = 1;
											ConstantUtils.yitouxiangmuflag = 2;
											mContext.startActivity(new Intent(mContext, InvestActivity.class));
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

	private void requestBook() {
		if (!progressdialog.isShowing()) {
			progressdialog.showis();
		}
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(mContext, "token", null);
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.FTBOOKVERIFYINFO_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
//						LogUtils.i("饭桶预约校验==="+string);
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
									
									// 最小预约金额
									String minBookAmount = data.getString("minBookAmount");
									// 最大预约金额
									String maxBookAmount = data.getString("maxBookAmount");
									// 起投金额
									String minInvestAmount = data.getString("minInvestAmount");
									// 是否开通存管户
									String hasFyAccount = data.getString("hasFyAccount");

									// 是否签约
									String isSignCard = data.getString("isSignCard");

									// 是否测评
									Boolean hasEvln = data.getBoolean("hasEvln");
									// 是否可投资
									Boolean canInvest = data.getBoolean("canInvest");
									// 测评剩余次数
									String overTimes = data.getString("overTimes");

									// 是否开通存管户（绑卡）0-未开通；
									if ("0".equals(hasFyAccount)) {// 如果还没有绑卡，去绑卡

										CGBindCardDialog cgBindCardDialog = new CGBindCardDialog(mContext, R.style.YzmDialog);
										cgBindCardDialog.show();
									}else{//1-已开通
										if ("0".equals(isSignCard)) {// 如果还没有签约，去签约
											CGSignCardDialog cgSignCardDialog = new CGSignCardDialog(mContext, R.style.YzmDialog);
											cgSignCardDialog.show();
										}else{//1-已签约
											if(hasEvln){//评测过
												if(canInvest) {//评测过并可投资

													FanTongYuYueBeforeDialog fanTongYuYueBeforeDialog = new FanTongYuYueBeforeDialog(mContext,
															R.style.YzmDialog, minBookAmount,maxBookAmount,minInvestAmount,baseRate,addRate);
													fanTongYuYueBeforeDialog.show();
												}else{//评测过不可投资
													if(StrToNumber.strTodouble(overTimes)>0){//还有评测次数
														CePingAgainDialog cePingAgainDialog = new CePingAgainDialog(mContext, R.style.YzmDialog,overTimes);
														cePingAgainDialog.show();
													}else{//没有评测次数
														CePingChanceOverDialog cePingChanceOverDialog = new CePingChanceOverDialog(mContext, R.style.YzmDialog);
														cePingChanceOverDialog.show();
													}
												}
											}else{// 未评测过
												CePingNotDialog cePingNotDialog = new CePingNotDialog(mContext, R.style.YzmDialog);
												cePingNotDialog.show();
											}
										}
									}
									
								} else {
									ToastUtils.toastshort("加载数据异常！");
								}
							}
						} else {
							String msg = json.getString("msg");
							NormalDialog normalDialog = new NormalDialog(mContext, R.style.YzmDialog, msg);
							normalDialog.show();
						}
					}

					@Override
					public void onError(VolleyError error) {
						progressdialog.dismiss();
					}
				});
	}
	
	private void requestBuy() {
		if (!progressdialog.isShowing()) {
			progressdialog.showis();
		}
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(mContext, "token", null);
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.FTINVESTSTEP1_URL, null, map,
				new HttpBackBaseListener() {
			
			@Override
			public void onSuccess(String string) {
				LogUtils.i("饭桶预约成功后买入==="+string);
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
							String baseAcctBal = data.getString("baseAcctBal");
							
							// 饭盒资产
							String fhAcctBal = data.getString("fhAcctBal");
							
							// 交易流水号
							String seqNo = data.getString("seqNo");
							
							// 预约金额
							bookAmount = data.getString("bookAmount");
							
							// 起投金额
							String minInvestAmount = data.getString("minInvestAmount");

							// 是否开通存管户
							String hasFyAccount = data.getString("hasFyAccount");

							// 是否签约
							String isSignCard = data.getString("isSignCard");

							// 是否测评
							Boolean hasEvln = data.getBoolean("hasEvln");
							// 是否可投资
							Boolean canInvest = data.getBoolean("canInvest");
							// 测评剩余次数
							String overTimes = data.getString("overTimes");

							// 是否开通存管户（绑卡）0-未开通；
							if ("0".equals(hasFyAccount)) {// 如果还没有绑卡，去绑卡

								CGBindCardDialog cgBindCardDialog = new CGBindCardDialog(mContext, R.style.YzmDialog);
								cgBindCardDialog.show();
							}else{//1-已开通
								if ("0".equals(isSignCard)) {// 如果还没有签约，去签约
									CGSignCardDialog cgSignCardDialog = new CGSignCardDialog(mContext, R.style.YzmDialog);
									cgSignCardDialog.show();
								}else{//1-已签约
									if(hasEvln){//评测过
										if(canInvest) {//评测过并可投资

											// 账户余额
											if("-9999".equals(baseAcctBal)) {//银行不能正常返回账户余额
												ToastUtils.toastshort("正在获取账户余额...");
											}else{
												Intent intent = new Intent(mContext, FanTongBuyActivity.class);
												intent.putExtra("seqNo", seqNo);
												intent.putExtra("baseAcctBal", baseAcctBal);
												intent.putExtra("fhAcctBal", fhAcctBal);
												intent.putExtra("bookAmount", bookAmount);
												intent.putExtra("minInvestAmount", minInvestAmount);
												intent.putExtra("baseRate", baseRate);
												intent.putExtra("addRate", addRate);
												mContext.startActivity(intent);
											}
										}else{//评测过不可投资
											if(StrToNumber.strTodouble(overTimes)>0){//还有评测次数
												CePingAgainDialog cePingAgainDialog = new CePingAgainDialog(mContext, R.style.YzmDialog,overTimes);
												cePingAgainDialog.show();
											}else{//没有评测次数
												CePingChanceOverDialog cePingChanceOverDialog = new CePingChanceOverDialog(mContext, R.style.YzmDialog);
												cePingChanceOverDialog.show();
											}
										}
									}else{// 未评测过
										CePingNotDialog cePingNotDialog = new CePingNotDialog(mContext, R.style.YzmDialog);
										cePingNotDialog.show();
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
