
package com.fanfanlicai.activity.invest;

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

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.activity.MainActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.global.FanFanApplication;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.NumAnim;

public class FanHeBuySuccessActivity extends BaseActivity implements OnClickListener {

	private TextView get_back;// 返回
	private TextView tv_money;// 买入金额
	private Button bt_success;// 确认提现按钮
	private ImageView iv_success;//动画背景
	private AnimationDrawable mAnimation;
	private LinearLayout ll_money;
	private String zong_money;//买入的钱（包括奖金）

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fanhebuysuccess);

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
		zong_money = intent.getStringExtra("zong_money");
		LogUtils.i("zong_money", zong_money);
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
		FanFanApplication.jiaxi_rate = 0;
		FanFanApplication.jiaxi_ID = null;
		Intent intent = new Intent(this,MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置 
		startActivity(intent);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_back:
			finish();
			FanFanApplication.jiaxi_rate = 0;
			FanFanApplication.jiaxi_ID = null;
			Intent intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case R.id.bt_success:
			finish();
			FanFanApplication.jiaxi_rate = 0;
			FanFanApplication.jiaxi_ID = null;
			Intent intent2 = new Intent(this, MainActivity.class);
			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent2);
			break;
		default:
			break;
		}
	}
}
