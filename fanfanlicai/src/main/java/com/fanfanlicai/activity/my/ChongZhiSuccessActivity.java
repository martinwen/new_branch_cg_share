
package com.fanfanlicai.activity.my;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.activity.MainActivity;
import com.fanfanlicai.activity.invest.FanHeBuyActivity;
import com.fanfanlicai.activity.invest.FanHeBuyYxgActivity;
import com.fanfanlicai.activity.invest.FanWanBuyActivity;
import com.fanfanlicai.activity.invest.FanWanBuyNewActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.global.FanFanApplication;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.NumAnim;
import com.fanfanlicai.utils.StringUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;
import com.fanfanlicai.view.dialog.CustomProgressDialog;

import java.util.Map;

public class ChongZhiSuccessActivity extends BaseActivity implements OnClickListener {

	private TextView get_back;// 返回
	private TextView tv_money;// 买入金额
	private Button bt_success;// 确认提现按钮
	private ImageView iv_success;//动画背景
	private AnimationDrawable mAnimation;
	private LinearLayout ll_money;
	private String from;
	private String zong_money;
	private CustomProgressDialog progressdialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chongzhisuccess);

		progressdialog = new CustomProgressDialog(this, "正在加载数据...");
		initView();
		initData();
	}

	private void initView() {
		get_back = (TextView) findViewById(R.id.get_back);// 返回
		get_back.setOnClickListener(this);
		tv_money = (TextView) findViewById(R.id.tv_money);//买入金额
		iv_success = (ImageView) findViewById(R.id.iv_success);//动画背景
		iv_success.setOnClickListener(this);
		bt_success = (Button) findViewById(R.id.bt_success);// 确认提现按钮
		bt_success.setOnClickListener(this);
		ll_money = (LinearLayout) findViewById(R.id.ll_money);
	}

	private void initData() {
		Intent intent = getIntent();
		from = intent.getStringExtra("from");
		zong_money = intent.getStringExtra("zong_money");
		//背景侦动画
		iv_success.setBackgroundResource(R.anim.buysuccessanim);
		mAnimation = (AnimationDrawable)iv_success.getBackground();
		mAnimation.start();

		int duration = 0;

        for(int i=0;i<mAnimation.getNumberOfFrames();i++){

            duration += mAnimation.getDuration(i);

        }

        FanFanApplication.mainHandler.postDelayed(new Runnable() {

            public void run() {

            	ll_money.setVisibility(View.VISIBLE);
        		//买入金额递增动画
        		NumAnim.startAnim(tv_money, Float.valueOf(zong_money), 1000);

            }

        }, duration);
	}

	@Override
	public void onBackPressed() {
		if("fh".equals(from)||"fhyxg".equals(from)||"fw".equals(from)){
			getDataFromServer();
		}else{
			Intent intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_back:
		case R.id.bt_success:
			if("fh".equals(from)||"fhyxg".equals(from)||"fw".equals(from)){
				getDataFromServer();
			}else{
				Intent intent = new Intent(this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}

	private void requestPut() {
		if (!progressdialog.isShowing()) {
			progressdialog.showis();
		}
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(this, "token", "");
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

									// 账户余额
									if("-9999".equals(baseBal)) {//银行不能正常返回账户余额

									}else{
										if("fh".equals(from)){
											Intent intent = new Intent(ChongZhiSuccessActivity.this, FanHeBuyActivity.class);
											intent.putExtra("baseBal", baseBal);
											startActivity(intent);
										}else if("fhyxg".equals(from)){
											Intent intent = new Intent(ChongZhiSuccessActivity.this, FanHeBuyYxgActivity.class);
											intent.putExtra("baseBal", baseBal);
											startActivity(intent);
										}else if("fw".equals(from)){
											Intent intent = new Intent(ChongZhiSuccessActivity.this, FanWanBuyNewActivity.class);
											intent.putExtra("baseBal", baseBal);
											startActivity(intent);
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

	private void getDataFromServer() {
		if (!progressdialog.isShowing()) {
			progressdialog.showis();
		}
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(ChongZhiSuccessActivity.this, "token", null);
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.MY_ACCOUNT_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {

						LogUtils.i("充值成功-我的资产==="+string);

						progressdialog.dismiss();
						JSONObject json = JSON.parseObject(string);
						String code = json.getString("code");

						if ("0".equals(code)) {
							String datastr = json.getString("data");
							if (!StringUtils.isBlank(datastr)) {
								String sign = json.getString("sign");
								boolean isSuccess = SignUtil.verify(sign,
										datastr);
								if (isSuccess) {// 验签成功
									JSONObject data = JSON.parseObject(datastr);
									// 账户余额
									String baseAcctBal = data.getString("baseAcctBal");
									Intent intent = null;
									if ("fh".equals(from)) {
										intent = new Intent(ChongZhiSuccessActivity.this, FanHeBuyActivity.class);
									} else if ("fhyxg".equals(from)) {
										intent = new Intent(ChongZhiSuccessActivity.this, FanHeBuyYxgActivity.class);
									} else if ("fw".equals(from)) {
										intent = new Intent(ChongZhiSuccessActivity.this, FanWanBuyNewActivity.class);
									}
									intent.putExtra("baseBal", baseAcctBal);
									startActivity(intent);
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
