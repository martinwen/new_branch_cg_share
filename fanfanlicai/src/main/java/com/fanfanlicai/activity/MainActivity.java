package com.fanfanlicai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Window;

import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.fragment.ContentFragment;
import com.fanfanlicai.global.FanFanApplication;
import com.fanfanlicai.service.RefreshTokenReceiver;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.RefTokenUtils;
import com.fanfanlicai.utils.StringUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.view.dialog.JpushDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends FragmentActivity {
	private String MAIN_TAG = "main";
	private FragmentManager fm;

	/**
	 * 刷新token
	 */
	public static String REFRESH_RECEIVER = "com.fanfanlicai.service.RefreshService";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
	
		initFragment();
		
		/**
		 * 开启AlertManager 并启动service
		 */
		String token = CacheUtils.getString(FanFanApplication.context, "token", "");
		if(StringUtils.isNotBlank(token)){
			RefTokenUtils.startRefrsh(this, 1, RefreshTokenReceiver.class,
					REFRESH_RECEIVER);
		}

		//激光推送消息弹窗显示
		showPushMessage();
	}

	private void showPushMessage() {
		Intent intent = getIntent();
		String message = intent.getStringExtra("message");
		showJpushMessageDialog(message);
	}

	private void showJpushMessageDialog(String message) {
		if (StringUtils.isNotBlank(message)) {
			JpushDialog jpushDialog = new JpushDialog(this, R.style.YzmDialog, message);
			jpushDialog.show();
		}
	}

	private void initFragment() {
		fm = getSupportFragmentManager();
		// 开启事务
		FragmentTransaction transaction = fm.beginTransaction();
		// 替换内容
		transaction.replace(R.id.ll_main, new ContentFragment(), MAIN_TAG);
		// 提交事务
		transaction.commit();
	}

	//方便有盟统计，不用在每个Activity中都从写这两个方法
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
		
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		/**
		 * 关闭AlertManager 并关闭service ，省电
		 */
		RefTokenUtils.stopRefresh(this, RefreshTokenReceiver.class, REFRESH_RECEIVER);
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event != null && keyCode == event.KEYCODE_BACK) {
            exitApp();
        }
        return false;
    }

	/**
     * 双击退出应用
     */
    private boolean isExit = false;
    private void exitApp(){
        if (isExit == false) {
            isExit = true;
            ToastUtils.toastshort("再按一下退出");
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        }else{
        	//友盟用来保存统计数据
        	MobclickAgent.onKillProcess(this);
            finish();
            System.gc();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        
    }
}
