package com.fanfanlicai.activity.my;

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.utils.DeviceUtil;
import com.fanfanlicai.utils.LogUtils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ContactActivity extends BaseActivity implements OnClickListener {

	private TextView contact_back;
	private RelativeLayout rl_weixin;
	private RelativeLayout rl_location1;
	private RelativeLayout rl_location2;
	private ImageView iv_weixin;
	private ImageView iv_location;
	private LinearLayout ll_weixin;
	private TextView tv_telephone;
	private TextView tv_version;
	private boolean isExpend1 = false;//微信公众号条目初始为关闭状态
	private boolean isExpend2 = false;//定位我们条目初始为关闭状态

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_contact);
		
		initView();
		initData();
		
	}

	private void initView() {
		contact_back = (TextView) findViewById(R.id.contact_back);//返回
		contact_back.setOnClickListener(this);
		
		ll_weixin = (LinearLayout) findViewById(R.id.ll_weixin);//微信公众号条目的内容
		rl_weixin = (RelativeLayout) findViewById(R.id.rl_weixin);
		iv_weixin = (ImageView) findViewById(R.id.iv_weixin);
		rl_weixin.setOnClickListener(this);
		
		rl_location1 = (RelativeLayout) findViewById(R.id.rl_location1);//定位我们条目的内容
		rl_location2 = (RelativeLayout) findViewById(R.id.rl_location2);
		iv_location = (ImageView) findViewById(R.id.iv_location);
		rl_location1.setOnClickListener(this);
		
		tv_telephone = (TextView) findViewById(R.id.tv_telephone);//客服电话条目的内容
		tv_telephone.setOnClickListener(this);
		
		tv_version = (TextView) findViewById(R.id.tv_version);//版本
	}

	
	private void initData() {
		//初始化微信公众号和定位我们
		ll_weixin.setVisibility(View.GONE);
		iv_weixin.setImageResource(R.drawable.arrow_down);
		rl_location2.setVisibility(View.GONE);
		iv_location.setImageResource(R.drawable.arrow_down);
		tv_version.setText(DeviceUtil.getVersionname());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.contact_back:
			finish();//返回上个界面
			break;
		case R.id.rl_weixin://根据点击改变isExpend1的值，控制微信公众号条目的开与关
			isExpend1 = !isExpend1;
			if (isExpend1) {
				ll_weixin.setVisibility(View.VISIBLE);
				iv_weixin.setImageResource(R.drawable.arrow_up);
				//打开的同时关闭位置条目
				if (isExpend2) {
					rl_location2.setVisibility(View.GONE);
					iv_location.setImageResource(R.drawable.arrow_down);
					isExpend2 = !isExpend2;
				}
			}else {
				ll_weixin.setVisibility(View.GONE);
				iv_weixin.setImageResource(R.drawable.arrow_down);
			}
			break;

		case R.id.rl_location1://根据点击改变isExpend1的值，控制定位我们条目的开与关
			isExpend2 = !isExpend2;
			if (isExpend2) {
				rl_location2.setVisibility(View.VISIBLE);
				iv_location.setImageResource(R.drawable.arrow_up);
				//打开的同时关闭微信条目
				if (isExpend1) {
					ll_weixin.setVisibility(View.GONE);
					iv_weixin.setImageResource(R.drawable.arrow_down);
					isExpend1 = !isExpend1;
				}
			}else {
				rl_location2.setVisibility(View.GONE);
				iv_location.setImageResource(R.drawable.arrow_down);
			}
			break;
			
		case R.id.tv_telephone:
			String number = tv_telephone.getText().toString();
			 //用intent启动拨打电话  
			LogUtils.i("打电话啊~~~~~~~~~~~"+number);
            Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+number));  
            startActivity(intent);
            LogUtils.i("走到这了~~~~~~~~~~~");
		default:
			break;
		}
	}
}
