package com.fanfanlicai.global;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.StringUtils;
import com.fanfanlicai.view.UILImageLoader;
import com.fanfanlicai.view.lockPattern.GestureLoginActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.qiyukf.unicorn.api.SavePowerConfig;
import com.qiyukf.unicorn.api.StatusBarNotificationConfig;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.YSFOptions;

import cn.jpush.android.api.JPushInterface;

public class FanFanApplication extends Application {

	public static Context context;//全局的上下文
	public static Handler mainHandler;//全局的主线程的Handler
	public int count = 0; 
	public static double jiaxi_rate = 0.00;
	public static String jiaxi_ID = null;
	public static boolean isGestureOk = false;

	@Override
	public void onCreate() {
		super.onCreate();

		//初始化JPush
		JPushInterface.setDebugMode(false);
		JPushInterface.init(this);

		//初始化Context
		context = this;

		//初始化Handler
		mainHandler = new Handler();

		//初始化ImageLoader,其实就是初始化缓存目录，内存缓存大小，磁盘缓存大小等
		ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));

		// 初始化七鱼客服，appKey 可以在七鱼管理系统-\>设置-\>APP接入 页面找到
		Unicorn.init(this, "078aaf8c5c6e0cf95332ef1b20bf8c0c", options(), new UILImageLoader());

		//程序在后台运行三分钟后重新进入时需要手势密码登录
		registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
			
			private long startTime;

			@Override
			public void onActivityStopped(Activity activity) {
				count--;
				startTime = System.currentTimeMillis();

			}
			
			@Override
			public void onActivityStarted(Activity activity) {
				//拿到jpush传过来的message
				Intent intent1 = activity.getIntent();
				String message = intent1.getStringExtra("message");

				String token = CacheUtils.getString(context, "token", null);
				boolean gestureDisable = CacheUtils.getBoolean(context, "gestureDisable", false);
				long endTime = System.currentTimeMillis();
                if (count == 0) {
                	if (startTime!=0) {
                		//时间大于3分钟并且手势密码没有失效
                		if (endTime - startTime > 180000&& StringUtils.isNotBlank(token)&&!gestureDisable) {
							LogUtils.i("token!=null");
							Intent intent = new Intent(context,GestureLoginActivity.class);
							isGestureOk = false;
							intent.putExtra("message",message);
							intent.setFlags(10000);
							activity.startActivity(intent);
                		}
					}
                }
                count++;
				
			}
			
			@Override
			public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onActivityResumed(Activity activity) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onActivityPaused(Activity activity) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onActivityDestroyed(Activity activity) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private YSFOptions options() {
		YSFOptions options = new YSFOptions();
		options.statusBarNotificationConfig = new StatusBarNotificationConfig();
		options.savePowerConfig = new SavePowerConfig();
		return options;
	}
	
}
