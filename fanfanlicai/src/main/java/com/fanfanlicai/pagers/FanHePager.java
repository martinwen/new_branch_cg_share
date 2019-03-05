package com.fanfanlicai.pagers;

import android.app.Activity;
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
import com.fanfanlicai.activity.invest.FanHeBuyActivity;
import com.fanfanlicai.activity.invest.FanHeCashActivity;
import com.fanfanlicai.activity.invest.FanHeRateActivity;
import com.fanfanlicai.activity.invest.YouXianGouActivity;
import com.fanfanlicai.activity.my.InvestActivity;
import com.fanfanlicai.activity.registerandlogin.HomeLoginActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.StrToNumber;
import com.fanfanlicai.utils.StringUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;
import com.fanfanlicai.view.FloatDragView;
import com.fanfanlicai.view.dialog.CGBindCardDialog;
import com.fanfanlicai.view.dialog.CGSignCardDialog;
import com.fanfanlicai.view.dialog.CePingAgainDialog;
import com.fanfanlicai.view.dialog.CePingChanceOverDialog;
import com.fanfanlicai.view.dialog.CePingNotDialog;
import com.fanfanlicai.view.dialog.CustomProgressDialog;
import com.fanfanlicai.view.dialog.NormalDialog;

import java.util.Map;



public class FanHePager extends BasePager implements OnClickListener{

	private CustomProgressDialog progressdialog;
	private TextView tv_base;//基本利率
	private TextView tv_jiaxi;//加息
	private TextView tv_shouyixiangqing;//收益详情
	private Button bt_cash;//赎回
	private Button bt_buy;//买入
	private RelativeLayout rl_leiji;//累计收益
	private RelativeLayout rl_jiangjin;//奖金余额
	private RelativeLayout rl_zuori;//昨日收益
	private RelativeLayout rl_zongzichan;//饭盒资产
	private TextView tv_zuori;
	private TextView tv_jiangjin;
	private TextView tv_leiji;
	private TextView tv_fanhe;
	private TextView tv_xiangmuyue;//项目余额
	private TextView tv_min_invest;//起投金额值
	private String fhAcctBal;//饭盒资产
	private RelativeLayout rl;
	private String forbidRedeemTime;
	private Boolean canRedeemFlag;

	public FanHePager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected View initView() {
		LogUtils.i("饭盒投资initView");
		//切换四个主界面按钮时，默认没有添加优先购悬浮窗
		FloatDragView.isAdd = false;
		View view = View.inflate(mContext, R.layout.pager_invest_fanhe, null);
		progressdialog = new CustomProgressDialog(mContext, "正在加载数据...");
		rl = (RelativeLayout) view.findViewById(R.id.rl);
		tv_base = (TextView) view.findViewById(R.id.tv_base);//基本利率
		tv_jiaxi = (TextView) view.findViewById(R.id.tv_jiaxi);//加息
		tv_shouyixiangqing = (TextView) view.findViewById(R.id.tv_shouyixiangqing);//收益详情
		tv_shouyixiangqing.setOnClickListener(this);
		bt_cash = (Button) view.findViewById(R.id.bt_cash);//赎回
		bt_cash.setOnClickListener(this);
		bt_buy = (Button) view.findViewById(R.id.bt_buy);//买入
		bt_buy.setOnClickListener(this);
		rl_leiji = (RelativeLayout) view.findViewById(R.id.rl_leiji);//累计收益
		rl_jiangjin = (RelativeLayout) view.findViewById(R.id.rl_jiangjin);//奖金余额
		rl_zuori = (RelativeLayout) view.findViewById(R.id.rl_zuori);//昨日收益
		rl_zongzichan = (RelativeLayout) view.findViewById(R.id.rl_zongzichan);//饭盒资产
		rl_leiji.setOnClickListener(this);
		rl_jiangjin.setOnClickListener(this);
		rl_zuori.setOnClickListener(this);
		rl_zongzichan.setOnClickListener(this);
		tv_zuori = (TextView) view.findViewById(R.id.tv_zuori);
		tv_jiangjin = (TextView) view.findViewById(R.id.tv_jiangjin);
		tv_leiji = (TextView) view.findViewById(R.id.tv_leiji);
		tv_fanhe = (TextView) view.findViewById(R.id.tv_fanhe);
		tv_xiangmuyue = (TextView) view.findViewById(R.id.tv_xiangmuyue);//项目余额
		tv_min_invest = (TextView) view.findViewById(R.id.tv_min_invest);//起投金额值

		return view;
	}
	
	@Override
	public void initData() {

		LogUtils.i("饭盒投资initData");
		String token = CacheUtils.getString(mContext, "token", null);
		if (TextUtils.isEmpty(token)) {// 没有登录过或者登录后退出了
			tv_zuori.setText("0.00");
			tv_jiangjin.setText("0.00");
			tv_leiji.setText("0.00");
			tv_fanhe.setText("0.00");
			tv_base.setText("8.88%");
			tv_jiaxi.setText("3.24%");
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
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.FHINVESTINFO_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						
//						LogUtils.i("饭盒投资未登录页面==="+string);
						
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
									String baseRate = data.getString("baseRate");
									tv_base.setText(baseRate+"%");
									
									// 加息年化收益率
									String addRate = data.getString("addRate");
									tv_jiaxi.setText(addRate+"%");
									
									// 起投金额
									String minInvestAmount = data.getString("minInvestAmount");
									tv_min_invest.setText(minInvestAmount+"元");
									
									// 饭盒项目余额
									String fhBorrowSalableBal = data.getString("fhBorrowSalableBal");
									tv_xiangmuyue.setText(fhBorrowSalableBal+"万元");
									// 买入按钮状态根据项目余额是否为0来决定
									if ("0.00".equals(fhBorrowSalableBal)||"0".equals(fhBorrowSalableBal)) {
										bt_buy.setBackgroundResource(R.drawable.anniu_duan_yishouqing);  
										bt_buy.setClickable(false);  
										bt_buy.setText("已售罄");
									}else {
										bt_buy.setBackgroundResource(R.drawable.button_short_selector);
										bt_buy.setClickable(true);  
										bt_buy.setText("买入");
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
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.FHINVESTINFO_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						
						LogUtils.i("饭盒投资页面==="+string);
						
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
									String baseRate = data.getString("baseRate");
									tv_base.setText(baseRate+"%");
									
									// 加息年化收益率
									String addRate = data.getString("addRate");
									tv_jiaxi.setText(addRate+"%");
									
									// 起投金额
									String minInvestAmount = data.getString("minInvestAmount");
									tv_min_invest.setText(minInvestAmount+"元");
									
									// 饭盒项目余额
									String fhBorrowSalableBal = data.getString("fhBorrowSalableBal");
									tv_xiangmuyue.setText(fhBorrowSalableBal+"万元");
									
									// 饭盒昨日收益
									String yesterdayIncome = data.getString("yesterdayIncome");
									tv_zuori.setText(yesterdayIncome);
									
									// 奖金余额
									String rewardAcctBal = data.getString("rewardAcctBal");
									tv_jiangjin.setText(rewardAcctBal);
									CacheUtils.putString(mContext, "rewardAcctBal",rewardAcctBal);
									
									// 饭盒累计收益
									String totalIncome = data.getString("totalIncome");
									tv_leiji.setText(totalIncome);
									
									// 饭盒资产
									fhAcctBal = data.getString("fhAcctBal");
									CacheUtils.putString(mContext, "fhAcctBal",fhAcctBal);
									tv_fanhe.setText(fhAcctBal);

									// 是否新手
									boolean isNewUser = data.getBoolean("isNewUser");
									if (isNewUser) {//新手
										bt_cash.setVisibility(View.GONE);
										// 买入按钮状态根据项目余额是否为0来决定
										if ("0.00".equals(fhBorrowSalableBal)||"0".equals(fhBorrowSalableBal)) {
											bt_buy.setBackgroundResource(R.drawable.anniu_chang_yishouqing);  
											bt_buy.setClickable(false);  
											bt_buy.setText("已售罄");
										}else {
											bt_buy.setBackgroundResource(R.drawable.button_long_selector);
											bt_buy.setClickable(true);  
											bt_buy.setText("买入");
										}
									}else {//老司机
										bt_cash.setVisibility(View.VISIBLE);
										// 买入按钮状态根据项目余额是否为0来决定
										if ("0.00".equals(fhBorrowSalableBal)||"0".equals(fhBorrowSalableBal)) {
											bt_buy.setBackgroundResource(R.drawable.anniu_duan_yishouqing);  
											bt_buy.setClickable(false);  
											bt_buy.setText("已售罄");
										}else {
											bt_buy.setBackgroundResource(R.drawable.button_short_selector);
											bt_buy.setClickable(true);  
											bt_buy.setText("买入");
										}
									}

									// 优先购悬浮窗显示开关
									boolean fhYxgSalableSwitch = data.getBoolean("fhYxgSalableSwitch");
									if(fhYxgSalableSwitch&&!FloatDragView.isAdd){
										FloatDragView.addFloatDragView((Activity) mContext, rl, new OnClickListener() {
											@Override
											public void onClick(View v) {
												mContext.startActivity(new Intent(mContext, YouXianGouActivity.class));
											}
										});
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
						ToastUtils.toastshort("网络连接失败，请检查网络");
					}
				});
	}

	@Override
	public void onClick(View v) {
		
		String token = CacheUtils.getString(mContext, "token", "");
		switch (v.getId()) {
		case R.id.tv_shouyixiangqing:
			if (TextUtils.isEmpty(token)) {//未登录时，点击进入登录页面
				mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
			}else {//登录
				mContext.startActivity(new Intent(mContext, FanHeRateActivity.class));
			}
			break;
		case R.id.bt_cash://赎回
			if (TextUtils.isEmpty(token)) {//未登录时，点击赎回进入登录页面；
				mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
			}else {//登录
				// 赎回按钮置灰后弹窗内容
				if(canRedeemFlag){
					checkFromServer();
				}else {
					NormalDialog normalDialog = new NormalDialog(mContext, R.style.YzmDialog, forbidRedeemTime);
					normalDialog.show();
				}
			}
			break;
		case R.id.bt_buy://买入
			if (TextUtils.isEmpty(token)) {//未登录时，点击买入进入登录页面；
				mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
			}else {//登录
				//因为买入成功后需要返回到饭盒投资界面，并刷新数据，就定义了此flag，
				ConstantUtils.touziflag = 1;
				requestPut();
				
			}
			break;
		case R.id.rl_zuori://昨日收益
			if (TextUtils.isEmpty(token)) {//未登录
				mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
			}else {//登录
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
				mContext.startActivity(new Intent(mContext,LeiJiShouYiActivity.class));
			}
			break;
		case R.id.rl_zongzichan://饭盒资产
			if (TextUtils.isEmpty(token)) {//未登录
				mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
			}else {//登录
				mContext.startActivity(new Intent(mContext, InvestActivity.class));
			}
			break;
		default:
			break;
		}
	}

	private void checkFromServer() {
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(mContext, "token", null);
		// 如果没有登录，直接return，不访问网络了
		if (TextUtils.isEmpty(token)) {
			return;
		}
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.FHVERIFYINFO_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {

						LogUtils.i("饭盒赎回校验==="+string);

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
											//因为赎回成功后需要返回到饭盒投资界面，并刷新数据，就定义了此flag，
											ConstantUtils.touziflag = 1;
											Intent intent = new Intent(mContext, FanHeCashActivity.class);
											mContext.startActivity(intent);
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
						ToastUtils.toastshort("网络连接失败，请检查网络");
					}
				});
	}

	private void requestPut() {
		if (!progressdialog.isShowing()) {
			progressdialog.showis();
		}
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(mContext, "token", null);
		map.put("token", token);
		map.put("type", "fh");
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.GOTOINVEST_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						LogUtils.i("饭盒买入==="+string);
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

									// 奖金余额
									String rewardAcctBal = data.getString("rewardAcctBal");
									CacheUtils.putString(mContext, "rewardAcctBal",rewardAcctBal);

									// 交易流水号
									String seqNo = data.getString("seqNo");

									// 单笔最大投资金额
									String maxInvestAmount = data.getString("maxInvestAmount");
									CacheUtils.putString(mContext, "maxInvestAmount",maxInvestAmount);

									// 单笔投资最小值
									String singleMinInvestAmount = data.getString("singleMinInvestAmount");

									// 单笔投资上限
									String singleMaxInvestAmount = data.getString("singleMaxInvestAmount");

									// 是否新手标
									Boolean isNewUser = data.getBoolean("isNewUser");

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
													if("-9999".equals(baseBal)) {//银行不能正常返回账户余额
														ToastUtils.toastshort("正在获取账户余额...");
													}else{
														Intent intent = new Intent(mContext, FanHeBuyActivity.class);
														intent.putExtra("baseBal", baseBal);
														intent.putExtra("seqNo", seqNo);
														intent.putExtra("isNewUser", isNewUser);
														intent.putExtra("maxInvestAmount", maxInvestAmount);
														intent.putExtra("singleMinInvestAmount", singleMinInvestAmount);
														intent.putExtra("singleMaxInvestAmount", singleMaxInvestAmount);
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
}
