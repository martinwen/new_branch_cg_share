
package com.fanfanlicai.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;

import com.baidu.mobstat.StatService;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends AppCompatActivity {

	@Override
	public Resources getResources() {
	Resources res = super.getResources();
	Configuration config=new Configuration();
	config.setToDefaults();
	res.updateConfiguration(config,res.getDisplayMetrics() );
	return res;

	}
	
	//方便有盟统计，不用在每个Activity中都从写这两个方法
	public void onResume() {
		super.onResume();
		//方便有盟统计
		MobclickAgent.onResume(this);
		//方便百度统计
		StatService.onResume(this);
	}
	
	public void onPause() {
		super.onPause();
		//方便有盟统计
		MobclickAgent.onPause(this);
		//方便百度统计
		StatService.onPause(this);
	}
}
