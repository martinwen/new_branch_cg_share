package com.fanfanlicai.pagers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.MainActivity;
import com.fanfanlicai.activity.home.BannerWebActivity;
import com.fanfanlicai.activity.home.DaFanActivity;
import com.fanfanlicai.activity.home.FanJuActivity;
import com.fanfanlicai.activity.home.GuanFangActivity;
import com.fanfanlicai.activity.home.InviteFriActivity;
import com.fanfanlicai.activity.home.PingTaiShuJuActivity;
import com.fanfanlicai.activity.home.ZongShouYiActivity;
import com.fanfanlicai.activity.home.ZongZiChanActivity;
import com.fanfanlicai.activity.home.ZuoRiShouYiActivity;
import com.fanfanlicai.activity.registerandlogin.HomeLoginActivity;
import com.fanfanlicai.activity.registerandlogin.HomeRegisterActivity;
import com.fanfanlicai.bean.BannerBean;
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
import com.fanfanlicai.view.FloatDragFanJuView;
import com.fanfanlicai.view.banner.CBViewHolderCreator;
import com.fanfanlicai.view.banner.ConvenientBanner;
import com.fanfanlicai.view.banner.NetworkImageHolderView;
import com.fanfanlicai.view.banner.OnItemClickListener;
import com.fanfanlicai.view.dialog.CustomProgressDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MoneyPager extends BasePager implements OnClickListener, OnItemClickListener {

	private CustomProgressDialog progressdialog;
	private ConvenientBanner<String> convenientBanner;//顶部广告栏控件
	private String[] images;
	private List<String> networkImages;
	private ArrayList<BannerBean> list;
	private ImageView iv_invitefriend;//邀请好友
	private ImageView iv_pingtaishuju;//平台数据
	private ImageView iv_fanfanxiaoxi;//饭饭消息
	private ImageView iv_dafan;//打饭签到
	private RelativeLayout rl_fanhe;//饭盒
	private RelativeLayout rl_fanwan;//饭碗
	private RelativeLayout rl_fantong;//饭桶
	private Button bt_register;//注册
	private Button bt_login;//登录
	private LinearLayout ll_registerandlogin;//注册和登录整体显隐的LinearLayout控件
	private LinearLayout ll_shouiyi;//收益整体显隐的LinearLayout控件
	private RelativeLayout rl_zongshouyi;//总收益
	private RelativeLayout rl_jiangjin;//奖金余额
	private RelativeLayout rl_zuori;//昨日收益
	private RelativeLayout rl_zongzichan;//总资产
	private TextView tv_zongshouyi;
	private TextView tv_jiangjin;
	private TextView tv_zuori;
	private TextView tv_zongzichan;
	private TextView tv_fhYearRate;//饭盒年化收益率
	private TextView tv_fwYearRate;//饭碗年化收益率
	private TextView tv_news;//新消息提示
	private ImageView iv_fanheshouqing;//饭盒售罄
	private ImageView iv_fantongshouqing;//饭碗售罄
	private ImageView iv_fanwanshouqing;//饭桶售罄
	private RelativeLayout rl;
	
	public MoneyPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected View initView() {
		View view = View.inflate(mContext, R.layout.pager_money, null);
		progressdialog = new CustomProgressDialog(mContext, "正在加载数据...");
		rl = (RelativeLayout) view.findViewById(R.id.rl);
		convenientBanner = (ConvenientBanner) view.findViewById(R.id.convenientBanner);//顶部广告栏控件
		iv_invitefriend = (ImageView) view.findViewById(R.id.iv_invitefriend);//邀请好友
		iv_pingtaishuju = (ImageView) view.findViewById(R.id.iv_pingtaishuju);//平台数据
		iv_fanfanxiaoxi = (ImageView) view.findViewById(R.id.iv_fanfanxiaoxi);//饭饭消息
		iv_dafan = (ImageView) view.findViewById(R.id.iv_dafan);//打饭签到
		rl_fanhe = (RelativeLayout) view.findViewById(R.id.rl_fanhe);//饭盒
		tv_fhYearRate = (TextView) view.findViewById(R.id.tv_fhYearRate);//饭盒年化收益率
		tv_fwYearRate = (TextView) view.findViewById(R.id.tv_fwYearRate);//饭碗年化收益率
		rl_fanwan = (RelativeLayout) view.findViewById(R.id.rl_fanwan);//饭碗
		rl_fantong = (RelativeLayout) view.findViewById(R.id.rl_fantong);//饭碗
		bt_register = (Button) view.findViewById(R.id.bt_register);//注册
		bt_login = (Button) view.findViewById(R.id.bt_login);//登录
		ll_registerandlogin = (LinearLayout) view.findViewById(R.id.ll_registerandlogin);//注册和登录整体显隐的LinearLayout控件
		ll_shouiyi = (LinearLayout) view.findViewById(R.id.ll_shouiyi);//收益整体显隐的LinearLayout控件
		rl_zongshouyi = (RelativeLayout) view.findViewById(R.id.rl_zongshouyi);//累计收益
		rl_jiangjin = (RelativeLayout) view.findViewById(R.id.rl_jiangjin);//奖金余额
		rl_zuori = (RelativeLayout) view.findViewById(R.id.rl_zuori);//昨日收益
		rl_zongzichan = (RelativeLayout) view.findViewById(R.id.rl_zongzichan);//总资产
		tv_zongshouyi = (TextView) view.findViewById(R.id.tv_zongshouyi);
		tv_jiangjin = (TextView) view.findViewById(R.id.tv_jiangjin);
		tv_zuori = (TextView) view.findViewById(R.id.tv_zuori);
		tv_zongzichan = (TextView) view.findViewById(R.id.tv_zongzichan);
		tv_news = (TextView) view.findViewById(R.id.tv_news);//新消息提示
		iv_fanheshouqing = (ImageView) view.findViewById(R.id.iv_fanheshouqing);//饭盒售罄
		iv_fanwanshouqing = (ImageView) view.findViewById(R.id.iv_fanwanshouqing);//饭碗售罄
		iv_fantongshouqing = (ImageView) view.findViewById(R.id.iv_fantongshouqing);//饭桶售罄
		iv_invitefriend.setOnClickListener(this);
		iv_pingtaishuju.setOnClickListener(this);
		iv_fanfanxiaoxi.setOnClickListener(this);
		iv_dafan.setOnClickListener(this);
		rl_fanhe.setOnClickListener(this);
		rl_fanwan.setOnClickListener(this);
		rl_fantong.setOnClickListener(this);
		bt_register.setOnClickListener(this);
		bt_login.setOnClickListener(this);
		rl_zongshouyi.setOnClickListener(this);
		rl_jiangjin.setOnClickListener(this);
		rl_zuori.setOnClickListener(this);
		rl_zongzichan.setOnClickListener(this);

		FloatDragFanJuView.addFloatDragView((Activity) mContext, rl, new OnClickListener() {
			@Override
			public void onClick(View v) {
				String token = CacheUtils.getString(mContext, "token", null);
				if (TextUtils.isEmpty(token)) {
					mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
				}else {
					mContext.startActivity(new Intent(mContext, FanJuActivity.class));
				}
			}
		});
		return view;
	}
	
	@Override
	public void initData() {
		//加载首页轮播图
		getBannerImages();
		
		String token = CacheUtils.getString(mContext, "token", null);
		if (TextUtils.isEmpty(token)) {// 没有登录过或者登录后退出了
			tv_fhYearRate.setText("8.88%~11.18%");
			tv_fwYearRate.setText("9.00%~13.58%");
			ll_registerandlogin.setVisibility(View.VISIBLE);
			ll_shouiyi.setVisibility(View.GONE);
			iv_fanheshouqing.setVisibility(View.INVISIBLE);
			iv_fanwanshouqing.setVisibility(View.INVISIBLE);
			// 访问网络，初始化默认值
			getDataFromServerWithoutToken();
		} else {// 登录过并且没有退出
			ll_registerandlogin.setVisibility(View.GONE);
			ll_shouiyi.setVisibility(View.VISIBLE);
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
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.INDEX_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						
//						LogUtils.i("首页未登录==="+string);
						
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
									
									// 饭盒年化收益率
									String fhYearRate = data.getString("fhYearRate");
									tv_fhYearRate.setText(fhYearRate);
									
									// 饭盒项目余额
									String fhBorrowSalableBal = data.getString("fhBorrowSalableBal");
									if ("0.00".equals(fhBorrowSalableBal)||"0".equals(fhBorrowSalableBal)) {
										iv_fanheshouqing.setVisibility(View.VISIBLE);
									}else {
										iv_fanheshouqing.setVisibility(View.INVISIBLE);
									}
									
									// 饭碗年化收益率
									String fwYearRate = data.getString("fwYearRate");
									tv_fwYearRate.setText(fwYearRate);
									
									// 饭碗项目余额
									String fwBorrowSalableBal = data.getString("fwBorrowSalableBal");
									String ztSalableSwitch = data.getString("ztSalableSwitch");
									if (("0.00".equals(fwBorrowSalableBal) || "0".equals(fwBorrowSalableBal)) && !"true".equals(ztSalableSwitch)) {
										iv_fanwanshouqing.setVisibility(View.VISIBLE);
									}else {
										iv_fanwanshouqing.setVisibility(View.INVISIBLE);
									}

									// 饭桶项目余额
									String ftBorrowSalableBal = data.getString("ftBorrowSalableBal");
									if ("0.00".equals(ftBorrowSalableBal)||"0".equals(ftBorrowSalableBal)) {
										iv_fantongshouqing.setVisibility(View.VISIBLE);
									}else {
										iv_fantongshouqing.setVisibility(View.INVISIBLE);
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
									
									// 饭盒年化收益率
									String fhYearRate = data.getString("fhYearRate");
									tv_fhYearRate.setText(fhYearRate);
									
									// 饭盒项目余额
									String fhBorrowSalableBal = data.getString("fhBorrowSalableBal");
									if ("0.00".equals(fhBorrowSalableBal)||"0".equals(fhBorrowSalableBal)) {
										iv_fanheshouqing.setVisibility(View.VISIBLE);
									}else {
										iv_fanheshouqing.setVisibility(View.INVISIBLE);
									}
									
									// 饭碗年化收益率
									String fwYearRate = data.getString("fwYearRate");
									tv_fwYearRate.setText(fwYearRate);
									
									// 饭碗项目余额
									String fwBorrowSalableBal = data.getString("fwBorrowSalableBal");
									String ztSalableSwitch = data.getString("ztSalableSwitch");
									if (("0.00".equals(fwBorrowSalableBal) || "0".equals(fwBorrowSalableBal)) && !"true".equals(ztSalableSwitch)) {
										iv_fanwanshouqing.setVisibility(View.VISIBLE);
									}else {
										iv_fanwanshouqing.setVisibility(View.INVISIBLE);
									}

									// 饭桶项目余额
									String ftBorrowSalableBal = data.getString("ftBorrowSalableBal");
									if ("0.00".equals(ftBorrowSalableBal)||"0".equals(ftBorrowSalableBal)) {
										iv_fantongshouqing.setVisibility(View.VISIBLE);
									}else {
										iv_fantongshouqing.setVisibility(View.INVISIBLE);
									}
									
									// 总收益
									String totalIncome = data.getString("totalIncome");
									tv_zongshouyi.setText(totalIncome);
									CacheUtils.putString(mContext, "totalIncome",totalIncome);

									// 奖金账户余额
									String rewardAcctBal = data.getString("rewardAcctBal");
									tv_jiangjin.setText(rewardAcctBal);
									CacheUtils.putString(mContext, "rewardAcctBal",rewardAcctBal);
									
									// 昨日收益
									String yesterdayIncome = data.getString("yesterdayIncome");
									tv_zuori.setText(yesterdayIncome);
									
									// 总资产
									String totalAssets = data.getString("totalAssets");
									if("-9999".equals(totalAssets)){//银行不能正常返回账户余额
										totalAssets = "正在获取中";
										tv_zongzichan.setText(totalAssets);
										rl_zongzichan.setClickable(false);
									}else{
										tv_zongzichan.setText(totalAssets);
										rl_zongzichan.setClickable(true);
									}
									
									// 饭盒累计收益
									String fhTotalIncome = data.getString("fhTotalIncome");
									CacheUtils.putString(mContext, "fhTotalIncome",fhTotalIncome);
									
									// 饭碗累计收益
									String fwTotalIncome = data.getString("fwTotalIncome");
									CacheUtils.putString(mContext, "fwTotalIncome",fwTotalIncome);
									
									// 饭桶累计收益
									String ftTotalIncome = data.getString("ftTotalIncome");
									CacheUtils.putString(mContext, "ftTotalIncome",ftTotalIncome);
									
									// 饭碗待收收益
									String fwWaitRepayAmount = data.getString("fwWaitRepayAmount");
									CacheUtils.putString(mContext, "fwWaitRepayAmount",fwWaitRepayAmount);
									
									// 轮播图要用到邀请码
									String inviteCode = data.getString("inviteCode");
									CacheUtils.putString(mContext, "inviteCode",inviteCode);
									
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
	
	private void getBannerImages() {
		// TODO Auto-generated method stub
		if (!progressdialog.isShowing()) {
			progressdialog.showis();
		}
		Map<String, String> map = SortRequestData.getmap();
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.GETADVERTISELIST_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						
//						LogUtils.i("首页轮播图==="+string);
						
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
									//list
									JSONArray getList=JSON.parseArray(datastr);
									list = (ArrayList<BannerBean>) JSONArray.parseArray(getList.toJSONString(), BannerBean.class);
									//网络加载例子
									images = new String[list.size()];
									for (int i = 0; i < list.size(); i++) {
										BannerBean bannerBean = list.get(i);
										images[i] = bannerBean.getImgSrc();
									}
									networkImages=Arrays.asList(images);
								      convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
								          @Override
								          public NetworkImageHolderView createHolder() {
								              return new NetworkImageHolderView();
								          }
								      },networkImages)
								      //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
								      .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
								      //设置点击监听
								      .setOnItemClickListener(MoneyPager.this);
								      //开始自动翻页
								      convenientBanner.startTurning(3000);
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
						// TODO Auto-generated method stub

						progressdialog.dismiss();
					}
			
		});
	}

	//点击轮播图跳转
	@Override
	public void onItemClick(int position) {
		String token = CacheUtils.getString(mContext, "token", null);

		BannerBean bannerBean = list.get(position);
		String url = bannerBean.getLinkUrl();
		String title = bannerBean.getTitle();
		String isNeedLogin = bannerBean.getIsNeedLogin();
		if ("1".equals(isNeedLogin)) {
			if (TextUtils.isEmpty(token)) {
				mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
			}else {
				//跳转到url对应的网址
				if (url != null&&!"".equals(url)) {
					Intent intent=new Intent(mContext,BannerWebActivity.class);
					intent.putExtra("url", url);
					intent.putExtra("title", title);
					mContext.startActivity(intent);
				}
			}
		}else{
			if (url != null&&!"".equals(url)) {
				Intent intent = new Intent(mContext,BannerWebActivity.class);
				intent.putExtra("url", url);
				intent.putExtra("title", title);
				mContext.startActivity(intent);
			}
		}
	}

	@Override
	public void onClick(View v) {
		String token = CacheUtils.getString(mContext, "token", null);
		switch (v.getId()) {
			case R.id.iv_invitefriend://邀请好友
				if (TextUtils.isEmpty(token)) {
					mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
				}else {
					mContext.startActivity(new Intent(mContext, InviteFriActivity.class));
				}
				break;
			case R.id.iv_pingtaishuju://信息披露
				mContext.startActivity(new Intent(mContext, PingTaiShuJuActivity.class));
				break;
			case R.id.iv_fanfanxiaoxi://饭饭消息
				mContext.startActivity(new Intent(mContext, GuanFangActivity.class));
				break;
			case R.id.iv_dafan://打饭签到
				if (TextUtils.isEmpty(token)) {
					mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
				}else {
					mContext.startActivity(new Intent(mContext, DaFanActivity.class));
				}

				break;
			case R.id.rl_fanhe://饭盒
				ConstantUtils.touziflag = 1;
				Intent intent = new Intent(mContext, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				mContext.startActivity(intent);
				break;
			case R.id.rl_fanwan://饭碗
				ConstantUtils.touziflag = 1;
				ConstantUtils.fanheorfanwanorfantongflag = 1;
				Intent intent2 = new Intent(mContext, MainActivity.class);
				intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				mContext.startActivity(intent2);
				break;
			case R.id.rl_fantong://饭桶
				ConstantUtils.touziflag = 1;
				ConstantUtils.fanheorfanwanorfantongflag = 2;
				Intent intent3 = new Intent(mContext, MainActivity.class);
				intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				mContext.startActivity(intent3);
				break;
			case R.id.bt_register://注册
				mContext.startActivity(new Intent(mContext,HomeRegisterActivity.class));
				break;
			case R.id.bt_login://登录
				mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
				break;
			case R.id.rl_zuori://昨日收益
				mContext.startActivity(new Intent(mContext, ZuoRiShouYiActivity.class));
				break;
			case R.id.rl_jiangjin://奖金余额
				mContext.startActivity(new Intent(mContext, InviteFriActivity.class));
				break;
			case R.id.rl_zongshouyi://总收益
				mContext.startActivity(new Intent(mContext,ZongShouYiActivity.class));
				break;
			case R.id.rl_zongzichan://总资产
				//防止用户在首页登录后，点击总资产里面数据全部为0
				requestData();

				break;

			default:
				break;
		}
		
	}

	private void requestData() {
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
						
//						LogUtils.i("我的页面==="+string);
						
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
									
									
									// 总资产
									String totalAssets = data.getString("totalAssets");
									tv_zongzichan.setText(totalAssets);
									CacheUtils.putString(mContext, "totalAssets",totalAssets);
									
									// 账户余额
									String baseAcctBal = data.getString("baseAcctBal");
									CacheUtils.putString(mContext, "baseAcctBal",baseAcctBal);
									
									// 奖金账户余额
									String rewardAcctBal = data.getString("rewardAcctBal");
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

									mContext.startActivity(new Intent(mContext, ZongZiChanActivity.class));
									
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
}
