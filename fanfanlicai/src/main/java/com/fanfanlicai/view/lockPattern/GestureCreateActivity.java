package com.fanfanlicai.view.lockPattern;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.activity.MainActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.global.FanFanApplication;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class GestureCreateActivity extends BaseActivity implements OnClickListener {
	private LockPatternIndicator lockPatternIndicator;
	private LockPatternView lockPatternView;
	private TextView reset;
	private TextView messageTv;
//	private ACache aCache;
	private List<LockPatternView.Cell> mChosenPattern = null;
	private static final long DELAYTIME = 600L;
	private static final String TAG = "GestureCreateActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_gesturecreate);

		initView();
	}

	private void initView() {
		lockPatternIndicator = (LockPatternIndicator) findViewById(R.id.lockPatterIndicator);
		lockPatternView = (LockPatternView) findViewById(R.id.lockPatternView);
		//去掉重置按钮
		reset = (TextView) findViewById(R.id.reset);
		reset.setOnClickListener(this);
		messageTv = (TextView) findViewById(R.id.messageTv);
		
		
//		aCache = ACache.get(this);
		lockPatternView.setOnPatternListener(patternListener);
		//震动效果
//		lockPatternView.setTactileFeedbackEnabled(true);
	}

	//重写返回键，目的是强制用户注册或登录后跳到手势密码界面设置手势密码
	@Override
	public void onBackPressed() {
		
	}
	
	/**
	 * 手势监听
	 */
	private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {

		@Override
		public void onPatternStart() {
			lockPatternView.removePostClearPatternRunnable();
			//updateStatus(Status.DEFAULT, null);
			lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
		}

		@Override
		public void onPatternComplete(List<LockPatternView.Cell> pattern) {
			//Log.e(TAG, "--onPatternDetected--");
			if(mChosenPattern == null && pattern.size() >= 4) {
				mChosenPattern = new ArrayList<LockPatternView.Cell>(pattern);
				updateStatus(Status.CORRECT, pattern);
			} else if (mChosenPattern == null && pattern.size() < 4) {
				updateStatus(Status.LESSERROR, pattern);
			} else if (mChosenPattern != null) {
				if (mChosenPattern.equals(pattern)) {
					updateStatus(Status.CONFIRMCORRECT, pattern);
				} else {
					updateStatus(Status.CONFIRMERROR, pattern);
				}
			}
		}
	};

	/**
	 * 更新状态
	 * @param status
	 * @param pattern
     */
	private void updateStatus(Status status, List<LockPatternView.Cell> pattern) {
		messageTv.setTextColor(getResources().getColor(status.colorId));
		messageTv.setText(status.strId);
		switch (status) {
			case DEFAULT:
				lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
				break;
			case CORRECT:
				updateLockPatternIndicator();
				lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
				break;
			case LESSERROR:
				lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
				break;
			case CONFIRMERROR:
				lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
				lockPatternView.postClearPatternRunnable(DELAYTIME);
				break;
			case CONFIRMCORRECT:
				saveChosenPattern(pattern);
				lockPatternView.setPattern(LockPatternView.DisplayMode.OK);
				setLockPatternSuccess();
				break;
		}
	}
	
	/**
	 * 更新 Indicator
	 */
	private void updateLockPatternIndicator() {
		if (mChosenPattern == null)
			return;
		lockPatternIndicator.setIndicator(mChosenPattern);
	}

	/**
	 * 重新设置手势
	 */

	@Override
	public void onClick(View v) {
		mChosenPattern = null;
		lockPatternIndicator.setDefaultIndicator();
		updateStatus(Status.DEFAULT, null);
		lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
		
	}

	/**
	 * 成功设置了手势密码(1秒后跳到首页)
     */
	private void setLockPatternSuccess() {
		FanFanApplication.mainHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if (ConstantUtils.loginflag==1) {
					ConstantUtils.loginflag=0;
					LogUtils.i("没有刷新首页");
					finish();
				}else {
					Intent intent=new Intent(GestureCreateActivity.this,MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置 
					startActivity(intent);
					LogUtils.i("刷新了首页");
					finish();
				}
			}
		}, 1000);
	}

	/**
	 * 保存手势密码
	 */
	private void saveChosenPattern(List<LockPatternView.Cell> cells) {
		byte[] bytes = LockPatternUtil.patternToHash(cells);
		String gesturePassword_string = new String(bytes);
		CacheUtils.putString(GestureCreateActivity.this,CacheUtils.BYTE,gesturePassword_string);
//		aCache.put(Constant.GESTURE_PASSWORD, bytes);
	}

	private enum Status {
		//默认的状态，刚开始的时候（初始化状态）
		DEFAULT(R.string.create_gesture_default, R.color.text_gray),
		//第一次记录成功
		CORRECT(R.string.create_gesture_correct, R.color.text_gray),
		//连接的点数小于4（二次确认的时候就不再提示连接的点数小于4，而是提示确认错误）
		LESSERROR(R.string.create_gesture_less_error, R.color.global_bigred),
		//二次确认错误
		CONFIRMERROR(R.string.create_gesture_confirm_error, R.color.global_bigred),
		//二次确认正确
		CONFIRMCORRECT(R.string.create_gesture_confirm_correct, R.color.text_gray);

		private Status(int strId, int colorId) {
			this.strId = strId;
			this.colorId = colorId;
		}
		private int strId;
		private int colorId;
	}

}
