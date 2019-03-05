package com.fanfanlicai.activity.setting;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.activity.registerandlogin.HomeLoginActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.global.FanFanApplication;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.view.dialog.GestureCountDialog;
import com.fanfanlicai.view.dialog.GestureCountDialog.OnGestureCountDialogDismissListener;
import com.fanfanlicai.view.dialog.GestureDialog;
import com.fanfanlicai.view.dialog.GestureDialog.OnGestureDialogDismissListener;
import com.fanfanlicai.view.lockPattern.ACache;
import com.fanfanlicai.view.lockPattern.Constant;
import com.fanfanlicai.view.lockPattern.GestureCreateActivity;
import com.fanfanlicai.view.lockPattern.GestureLoginActivity;
import com.fanfanlicai.view.lockPattern.LockPatternUtil;
import com.fanfanlicai.view.lockPattern.LockPatternView;

public class GestureActivity extends BaseActivity implements OnClickListener {

//    private static final String TAG = "LoginGestureActivity";

    private LockPatternView lockPatternView;
    private TextView messageTv;
    private TextView forgetGesture;

//    private ACache aCache;
    private static final long DELAYTIME = 600l;
    private byte[] gesturePassword;
    private String gesturePassword_string;
    private int count = 5;
    private boolean gestureDisable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gesture);
        
        initView();
    }

    private void initView() {
    	lockPatternView = (LockPatternView) findViewById(R.id.lockPatternView);
    	messageTv = (TextView) findViewById(R.id.messageTv);
    	forgetGesture = (TextView) findViewById(R.id.forgetGesture);
    	forgetGesture.setOnClickListener(this);
    	
    	
    	
//        aCache = ACache.get(GestureActivity.this);
        //得到当前用户的手势密码
//        gesturePassword = aCache.getAsBinary(Constant.GESTURE_PASSWORD);
    	gesturePassword_string = CacheUtils.getString(GestureActivity.this,CacheUtils.BYTE,"");
    	gesturePassword = gesturePassword_string.getBytes();
        lockPatternView.setOnPatternListener(patternListener);
        updateStatus(Status.DEFAULT);
    }

    private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {

        @Override
        public void onPatternStart() {
            lockPatternView.removePostClearPatternRunnable();
        }

        @Override
        public void onPatternComplete(List<LockPatternView.Cell> pattern) {
            if(pattern != null){
                if(LockPatternUtil.checkPattern(pattern, gesturePassword)) {
                    updateStatus(Status.CORRECT);
                } else {
                	count--;
                    if (count<=0) {
//						aCache.clear();
                    	CacheUtils.putString(GestureActivity.this, "token", null);
                    	CacheUtils.putString(GestureActivity.this, CacheUtils.BYTE, "");
                    	gestureDisable = true;
                    	CacheUtils.putBoolean(GestureActivity.this, "gestureDisable", gestureDisable);
                    	
						GestureCountDialog gestureCountDialog = new GestureCountDialog(GestureActivity.this, R.style.YzmDialog);
						gestureCountDialog.setCanceledOnTouchOutside(false);
						gestureCountDialog.setCancelable(false);
						gestureCountDialog.setOnGestureCountDialogDismissListener(new OnGestureCountDialogDismissListener() {
							
							@Override
							public void OnGestureCountDialogDismiss() {
								Intent intent = new Intent(GestureActivity.this,HomeLoginActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置 
								startActivity(intent);
								finish();
							}
						});
						gestureCountDialog.show();
					}
                    updateStatus(Status.ERROR);
                }
            }
        }
    };

    /**
     * 更新状态
     * @param status
     */
    private void updateStatus(Status status) {
        messageTv.setText(status.strId);
        messageTv.setTextColor(getResources().getColor(status.colorId));
        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case ERROR:
            	if (count<=0) {
            		messageTv.setText("已超过错误次数限制");
				}else {
					messageTv.setText("密码错误，你还有"+count+"次解锁机会");
				}
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                break;
            case CORRECT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.OK);
                loginGestureSuccess();
                break;
        }
    }

    /**
     * 手势登录成功（去首页）
     */
    private void loginGestureSuccess() {
//        Toast.makeText(GestureActivity.this, "success", Toast.LENGTH_SHORT).show();
    	FanFanApplication.mainHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				finish();
				startActivity(new Intent(GestureActivity.this,GestureCreateActivity.class));
			}
		}, 1000);
    	//dialog消失跳转到手势密码设置
    }

    /**
     * 忘记手势密码（去账号登录界面）
     */
    @Override
	public void onClick(View v) {
      GestureDialog gestureDialog = new GestureDialog(this, R.style.YzmDialog);
      gestureDialog.setOnGestureDialogDismissListener(new OnGestureDialogDismissListener() {
		
		@Override
		public void OnGestureDialogDismiss() {
			finish();
	        Intent intent = new Intent(GestureActivity.this, GestureCreateActivity.class);
	        startActivity(intent);
		}
	});
      gestureDialog.show();
	}

    private enum Status {
        //默认的状态
        DEFAULT(R.string.gesture_fix_default, R.color.global_yellow),
        //密码输入错误
        ERROR(R.string.gesture_error, R.color.global_bigred),
        //密码输入正确
        CORRECT(R.string.gesture_fix_default, R.color.global_yellow);

        private Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }
        private int strId;
        private int colorId;
    }
}
