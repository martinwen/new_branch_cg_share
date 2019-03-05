
package com.fanfanlicai.activity.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.bean.SignBean;
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
import com.fanfanlicai.view.calendar.DateSignData;
import com.fanfanlicai.view.calendar.MonthSignData;
import com.fanfanlicai.view.calendar.SignCalendar;
import com.fanfanlicai.view.dialog.CustomProgressDialog;
import com.fanfanlicai.view.dialog.NormalDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;


public class DaFanActivity extends BaseActivity implements OnClickListener {

	private CustomProgressDialog progressdialog;
	private TextView get_back;// 返回
	private TextView tv_days;// 连续打饭
	private TextView tv_playtime;// 抽奖次数
	private SignCalendar signCalendar;
	private Button bt_sign;
	private String currentTime;
	private ArrayList<SignBean> list = new ArrayList<SignBean>();
	private int currentYear;
	private int currentMonth;
	private int currentDay;
	private int showyear;
	private int showmonth;
	private String uuid;

	//当前选中图片的id
	private int currentId = 0;
	//图片转动的圈数
	private int circleNum = 0;
	//创建定时器
	private AnimThread animThread = null;
	private MyHandler mHandler = new MyHandler();
	private ImageView iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8;
	private ImageView[] imgArr = new ImageView[8];

	//默认后台返回的resultId是-1
	private int resultId = -1;
	private ImageView bt_try;

	//抽中奖后返回的中奖信息
	private String prizeMsg = null;
	//抽奖后返回的抽奖次数
	private String overLotTimes = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dafan);

		progressdialog = new CustomProgressDialog(this, "正在加载数据...");
		initView();
		initData();
	}

	private void initView() {
		get_back = (TextView) findViewById(R.id.get_back);// 返回
		get_back.setOnClickListener(this);
		signCalendar = (SignCalendar) findViewById(R.id.my_sign_calendar);
		bt_sign = (Button) findViewById(R.id.bt_sign);
		bt_sign.setOnClickListener(this);
		tv_days = (TextView) findViewById(R.id.tv_days);// 连续打饭

		//老虎机相关
		tv_playtime = (TextView) findViewById(R.id.tv_playtime);// 抽奖次数
		bt_try = (ImageView) findViewById(R.id.bt_try);
		bt_try.setOnClickListener(this);

		iv1 = (ImageView) findViewById(R.id.iv1);
		iv2 = (ImageView) findViewById(R.id.iv2);
		iv3 = (ImageView) findViewById(R.id.iv3);
		iv4 = (ImageView) findViewById(R.id.iv4);
		iv5 = (ImageView) findViewById(R.id.iv5);
		iv6 = (ImageView) findViewById(R.id.iv6);
		iv7 = (ImageView) findViewById(R.id.iv7);
		iv8 = (ImageView) findViewById(R.id.iv8);

	}

	private void initData() {
		//老虎机相关
		imgArr[0] = iv1;
		imgArr[1] = iv2;
		imgArr[2] = iv3;
		imgArr[3] = iv5;
		imgArr[4] = iv8;
		imgArr[5] = iv7;
		imgArr[6] = iv6;
		imgArr[7] = iv4;

		//获取当前年月日
		Calendar calendar = Calendar.getInstance();
		currentYear= calendar.get(Calendar.YEAR);
		currentMonth= calendar.get(Calendar.MONTH);
		currentDay=calendar.get(Calendar.DAY_OF_MONTH);

		showyear= calendar.get(Calendar.YEAR);
		showmonth= calendar.get(Calendar.MONTH);

		currentTime = currentYear+"-"+(currentMonth+1);
		LogUtils.i("当前年月日=="+currentYear+"=="+currentMonth+"=="+currentDay);

		ArrayList<MonthSignData> monthDatas = new ArrayList<MonthSignData>();
		MonthSignData monthData = new MonthSignData();
        monthData.setYear(currentYear);
        monthData.setMonth(currentMonth);
        ArrayList<DateSignData> signDates = new ArrayList<DateSignData>();
        monthData.setSignDates(signDates);
        monthDatas.add(monthData);
//		Date today = new Date(currentYear -1900,currentMonth, currentDay);
//        signCalendar.setToday(today);
        signCalendar.setSignDatas(monthDatas);

        //左侧箭头显示上一个月数据
        signCalendar.setLeftArrowClick(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Calendar calendar = Calendar.getInstance();
			        calendar.set(showyear,showmonth,19);
			        calendar.add(Calendar.MONTH, -1);    //得到前一个月

			        showyear= calendar.get(Calendar.YEAR);
			        showmonth= calendar.get(Calendar.MONTH);
			        LogUtils.i("当前年月日=="+showyear+"=="+showmonth+"=="+currentDay);
				getDataFromServer(showyear+"-"+(showmonth+1),showyear,showmonth,currentDay);
			}
		});

        //右侧箭头显示下一个月数据
        signCalendar.setRightArrowClick(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Calendar calendar = Calendar.getInstance();
		        calendar.set(showyear,showmonth,19);
		        calendar.add(Calendar.MONTH, +1);    //得到后一个月

		        showyear= calendar.get(Calendar.YEAR);
		        showmonth= calendar.get(Calendar.MONTH);
		        LogUtils.i("当前年月日=="+showyear+"=="+showmonth+"=="+currentDay);
			getDataFromServer(showyear+"-"+(showmonth+1),showyear,showmonth,currentDay);
			}
		});

		//查询对应月份的数据
		getDataFromServer(currentTime,currentYear,currentMonth,currentDay);

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_back:
			finish();
			break;
		case R.id.bt_try:// 试试手气
			currentId = 0;
			circleNum = 0;
			resultId = -1;
			//抽奖按钮改变图标（置灰）
			bt_try.setImageResource(R.drawable.try02);
			bt_try.setClickable(false);

			//背景开始转动
			animThread = new AnimThread();
			animThread.start();
			//从后台返回奖项
			tigerPlay();

			break;
		case R.id.bt_sign:// 打饭
			requestServer(currentYear,currentMonth,currentDay);
			break;
		default:
			break;
		}
	}


	private void tigerPlay() {

		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(this, "token", null);
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.SIGNDRAWOPER_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						LogUtils.i("模拟后台返回抽中的id===" + string);
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
									JSONObject drawTimeInfo = data.getJSONObject("drawTimeInfo");
									JSONObject winPrizeInfo = data.getJSONObject("winPrizeInfo");

									//剩余”抽奖次数”
									overLotTimes = drawTimeInfo.getString("overLotTimes");
									tv_playtime.setText(overLotTimes+"次");

									//"奖励值"
									String prizeValue = winPrizeInfo.getString("prizeValue");

									//奖品级别（中的是哪个奖）
									int prizeLevel = winPrizeInfo.getInteger("prizeLevel");
									LogUtils.i("剩余抽奖次数overLotTimes="+overLotTimes+"  奖励值prizeValue="+prizeValue+"  中的是第几个prizeLevel="+prizeLevel);
									resultId = prizeLevel;//服务器返回的数据id

								} else {
									ToastUtils.toastshort("加载数据异常！");
								}
							}
						} else if("1000027".equals(code)){
							//没有抽奖的情况却能点击抽奖（微信和ios端app同时操作导致此问题）
							animThread.stopAnim();
							tv_playtime.setText("0次");
							String msg = json.getString("msg");
							NormalDialog normalDialog = new NormalDialog(DaFanActivity.this, R.style.YzmDialog, msg);
							normalDialog.setCancelable(false);
							normalDialog.setCanceledOnTouchOutside(false);
							normalDialog.show();
							normalDialog.setOnNormalDialogDismissListener(new NormalDialog.OnNormalDialogDismissListener() {
								@Override
								public void OnNormalDialogDismiss() {
									//清空所有图片背景
									for (int i = 0; i < imgArr.length; i++) {
										ImageView iv = imgArr[i];
										iv.setImageResource(0);
									}
								}
							});
						}else{
							//其他情况
							animThread.stopAnim();
							String msg = json.getString("msg");
							ToastUtils.toastshort(msg);
						}
					}

					@Override
					public void onError(VolleyError error) {
						ToastUtils.toastshort("网络请求失败");
						//网络请求失败时，AnimThread停止计算序号
						animThread.stopAnim();
						//抽奖按钮改变图标
						bt_try.setImageResource(R.drawable.try01);
						bt_try.setClickable(true);
						//清空所有图片背景
						for (int i = 0; i < imgArr.length; i++) {
							ImageView iv = imgArr[i];
							iv.setImageResource(0);
						}
					}
				});
	}

	/**
	 * 处理转盘转动过程中的界面变化
	 */
	private class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.what == 1) {
				//清空所有图片背景
				for (int i = 0; i < imgArr.length; i++) {
					ImageView iv = imgArr[i];
					iv.setImageResource(0);
				}
				//将选中背景图片加个蓝色边框
				int index = msg.arg2;
				ImageView selectIv = imgArr[index];
				selectIv.setImageResource(R.drawable.tigerplaychoose);
			} else if (msg.what == 2) {//此时已经转完了2圈了，并且拿到后台返回的数据，此时currentId=resultId
				//清空所有图片背景
				for (int i = 0; i < imgArr.length; i++) {
					ImageView iv = imgArr[i];
					iv.setImageResource(0);
				}
				//将选中背景图片加个蓝色边框
				int index = msg.arg2;
				ImageView selectIv = imgArr[index];
				selectIv.setImageResource(R.drawable.tigerplaychoose);

				//显示弹窗
				showPrizeDialog(index);

			}
		}
	}

	private void showPrizeDialog(int index) {
		switch (index){
			case 0:
				prizeMsg =  "恭喜您获得饭盒加息0.2%";
				break;
			case 1:
				prizeMsg =  "恭喜您获得饭盒昨日收益翻倍";
				break;
			case 2:
				prizeMsg =  "恭喜您获得提现票1张";
				break;
			case 3:
				prizeMsg =  "恭喜您获得饭碗红包100元";
				break;
			case 4:
				prizeMsg =  "恭喜您获得饭盒加息0.5%";
				break;
			case 5:
				prizeMsg =  "恭喜您获得饭碗红包50元";
				break;
			case 6:
				prizeMsg =  "恭喜您获得饭盒加息0.3%";
				break;
			case 7:
				prizeMsg =  "恭喜您获得饭碗红包60元";
				break;
			default:
				break;
		}

		//弹出弹窗
		NormalDialog normalDialog = new NormalDialog(DaFanActivity.this, R.style.YzmDialog, prizeMsg);
		normalDialog.setCancelable(false);
		normalDialog.setCanceledOnTouchOutside(false);
		normalDialog.show();
		normalDialog.setOnNormalDialogDismissListener(new NormalDialog.OnNormalDialogDismissListener() {
			@Override
			public void OnNormalDialogDismiss() {
				//清空所有图片背景
				for (int i = 0; i < imgArr.length; i++) {
					ImageView iv = imgArr[i];
					iv.setImageResource(0);
				}

				if ("0".equals(overLotTimes)) {
					//没有抽奖
					bt_try.setImageResource(R.drawable.try02);
					bt_try.setClickable(false);
				} else {
					//有抽奖
					bt_try.setImageResource(R.drawable.try01);
					bt_try.setClickable(true);
				}
			}
		});
	}

	/*
	* 在转动期间一直计算序号
     */
	private class AnimThread extends Thread {

		private boolean isStopped = false;

		//停止计算序号
		public void stopAnim() {
			isStopped = true;
			animThread.interrupt();
		}

		@Override
		public void run() {
			while (!isStopped) {
				//计算当前选中图片的序号
				currentId++;
				if (currentId > 7) {
					currentId = 0;
					circleNum++;
				}
				try {
					Thread.sleep(150);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (circleNum >= 2 && resultId == currentId) {//转了2圈了并且后台已经返回数据
					LogUtils.i("转了2圈了并且后台已经返回数据");

					//发送结果给MyHandler进行处理
					Message msg = Message.obtain();
					msg.arg2 = currentId;
					msg.what = 2;
					mHandler.sendMessage(msg);

					stopAnim();

				} else {//其他：1.还没转到2圈  2.转了2圈了但是后台数据还没返回
					//发送结果给MyHandler进行处理
					Message msg = Message.obtain();
					msg.arg2 = currentId;
					msg.what = 1;
					mHandler.sendMessage(msg);
				}

			}
		}
	}

	private void requestServer(final int year, final int month, final int day) {

		if (!progressdialog.isShowing()) {
			progressdialog.show();
		}
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(this, "token", null);
		map.put("uuid", uuid);
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.SIGNINSUBMIT_URL,
				null, map, new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						LogUtils.i("签到===" + string);
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
									 //是否有奖励
									boolean prize = data.getBoolean("prize");

									//签到返回消息
									String signInMsg = data.getString("signInMsg");

									 //今天是否签到
									boolean isSign = data.getBoolean("isSign");

									// 连续签到天数
									String signTotalDays = data.getString("signTotalDays");
									tv_days.setText(signTotalDays);

									if (isSign) {
										bt_sign.setBackgroundResource(R.drawable.anniu_chang_yishouqing);
										bt_sign.setClickable(false);
										bt_sign.setText("已打饭");
									}else {
										bt_sign.setBackgroundResource(R.drawable.button_long_selector);
										bt_sign.setClickable(true);
										bt_sign.setText("打饭");
									}

									//签完到后重新查询，刷新界面
									getDataFromServer(currentTime,currentYear,currentMonth,currentDay);

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
						ToastUtils.toastshort("加载数据失败！");
					}

				});


	}

	private void getDataFromServer(String date, final int year, final int month, final int currentDay) {
		progressdialog.show();
		LogUtils.i("当前签到时间===" + date);
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(this, "token", null);
		map.put("date", date);
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.SIGNINLIST_URL,
				null, map, new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						LogUtils.i("当前签到记录===" + string);
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
									 //是否可签到
									boolean signInAble = data.getBoolean("signInAble");

									//不可签到的显示信息
									String signInMsg = data.getString("signInMsg");

									//防重码
									uuid = data.getString("uuid");

									 //今天是否签到
									boolean isSign = data.getBoolean("isSign");

									// 连续签到天数
									String signTotalDays = data.getString("signTotalDays");
									tv_days.setText(signTotalDays);

									// 是否有抽奖机会
									overLotTimes = data.getString("overLotTimes");
									tv_playtime.setText(overLotTimes+"次");
									if ("0".equals(overLotTimes)) {
										//没有抽奖
										bt_try.setImageResource(R.drawable.try02);
										bt_try.setClickable(false);
									} else {
										//有抽奖
										bt_try.setImageResource(R.drawable.try01);
										bt_try.setClickable(true);
									}

									//list
									JSONArray getList=data.getJSONArray("signInList");
									list = (ArrayList<SignBean>) JSONArray.parseArray(getList.toJSONString(), SignBean.class);
									LogUtils.i("list长度=="+list.size());
									ArrayList<MonthSignData> monthDatas = new ArrayList<MonthSignData>();
									MonthSignData monthData = new MonthSignData();
							        monthData.setYear(year);
							        monthData.setMonth(month);
							        ArrayList<DateSignData> signDates = new ArrayList<DateSignData>();
							        if (list.size()!=0) {
							        	for (int i = 0; i < list.size(); i++) {
//							        		Date date= new Date(year-1900, month, list.get(i).getSignTime());
//							        		signDates.add(date);
							        		DateSignData dateSignData = new DateSignData(year, month, list.get(i).getSignTime());
							        		dateSignData.setPrize(list.get(i).isPrize());
							        		signDates.add(dateSignData);
							        	}
									}
							        monthData.setSignDates(signDates);
							        monthDatas.add(monthData);

							        Date today = new Date(currentYear -1900,currentMonth, currentDay);
							        signCalendar.setToday(today);
							        signCalendar.setSignDatas(monthDatas);

									if (signInAble) {
										if (isSign) {
											bt_sign.setBackgroundResource(R.drawable.anniu_chang_yishouqing);
											bt_sign.setClickable(false);
											bt_sign.setText("已打饭");
										}else {
											bt_sign.setBackgroundResource(R.drawable.button_long_selector);
											bt_sign.setClickable(true);
											bt_sign.setText("打饭");
										}

									}else {
										bt_sign.setText(signInMsg);
										bt_sign.setBackgroundResource(R.drawable.anniu_chang_yishouqing);
										bt_sign.setClickable(false);
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
						// TODO Auto-generated method stub
						progressdialog.dismiss();
						ToastUtils.toastshort("加载数据失败！");
					}

				});

	}
}
