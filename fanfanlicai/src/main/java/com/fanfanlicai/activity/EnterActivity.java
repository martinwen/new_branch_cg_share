package com.fanfanlicai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.baidu.mobstat.StatService;
import com.fanfanlicai.activity.registerandlogin.HomeLoginActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.global.FanFanApplication;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.UpdateDialogUtil;
import com.fanfanlicai.utils.UpdateDialogUtil.NoUpdateListener;
import com.fanfanlicai.view.lockPattern.GestureLoginActivity;

public class EnterActivity extends BaseActivity {

	public static String IS_APP_FIRST_OPEN  = "is_app_first_open";// 是否应用第一次打开
	private UpdateDialogUtil upta;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		//百度统计
		StatService.start(this);
		//让启动页不变形显示加的代码，有的手机有虚拟键盘导致启动页图片变形
		if(android.os.Build.VERSION.SDK_INT>=14){  
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);  
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);  
              
		}else if(android.os.Build.VERSION.SDK_INT>=16){  
	         getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);  
	         getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);  
	        } 
     

		upta = new UpdateDialogUtil(this);
		upta.goupdate();
        upta.setNoUpdateListener(new NoUpdateListener() {
			
			@Override
			public void noupdate() {
				
				// 根据保存的应用是否是第一次打开boolean值，判断进入什么界面
				boolean isAppFirstOpen = CacheUtils.getBoolean(EnterActivity.this, IS_APP_FIRST_OPEN, true);
				if(isAppFirstOpen){// 应用第一次打开，进入引导界面
					startActivity(new Intent(EnterActivity.this,GuideActivity.class));
					finish();
				}else{// 应用不是第一次打开，进入主界面
					FanFanApplication.mainHandler.postDelayed(new  Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							goMain();
							
						}
					}, 1000);
				}
			}
		});
	}
	
	
	/**
	 *  欢迎页完成之后的判断
	 */
	private void goMain() {
		//得到当前用户的手势密码
		String gesturePassword_string = CacheUtils.getString(this, CacheUtils.BYTE, "");
        if(null==gesturePassword_string||"".equals(gesturePassword_string)){
			//没设置过手势密码（注册后没有输手势密码，登录后退出登录，手势密码输错超过5次）
        	String token = CacheUtils.getString(this, "token", null);
    		// 从未登录过或者登录过，但是登录后退出了，直接跳首页
    		if (TextUtils.isEmpty(token)) {
    			startActivity(new Intent(this,MainActivity.class));
    		}else{// 登录过，注册后没有输手势密码或者手势密码输错超过5次
    			boolean gestureDisable = CacheUtils.getBoolean(this, "gestureDisable", false);
    			if (gestureDisable) {//当手势密码输错超过5次，手势密码失效的情况
					Intent intent = new Intent(this, HomeLoginActivity.class);
					intent.setFlags(10);
					startActivity(intent);
				}else {//这种情况是注册或登录后没有设置手势密码就退出，当再次进入app时跳设置手势密码界面
					startActivity(new Intent(this,HomeLoginActivity.class));
				}
    		}
			
			finish();
        
        }else{
        	//设置过手势密码并且手势密码没有清除掉（没有退出登录，输错次数不超过5次）
			startActivity(new Intent(this,GestureLoginActivity.class));
			finish();
        }
	}
}
