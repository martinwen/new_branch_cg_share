package com.fanfanlicai.view.lockPattern;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.activity.MainActivity;
import com.fanfanlicai.activity.registerandlogin.HomeLoginActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.global.FanFanApplication;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.StringUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;
import com.fanfanlicai.view.dialog.GestureCountDialog;
import com.fanfanlicai.view.dialog.GestureCountDialog.OnGestureCountDialogDismissListener;
import com.fanfanlicai.view.dialog.JpushDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.List;
import java.util.Map;


/**
 * Created by Sym on 2015/12/24.
 */
public class GestureLoginActivity extends BaseActivity {

    private LockPatternView lockPatternView;
    private TextView messageTv;
    private TextView tv_user;
    private TextView forgetGesture;
    private TextView other;

    private static final long DELAYTIME = 600l;
    private byte[] gesturePassword;
    private String gesturePassword_string;
    private int count = 5;
    private String phone;
    private String password;
    private boolean gestureDisable = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesturelogin);
        initView();
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


    private void initView() {
    	//获取到注册时的手机号和登录密码
    	phone = CacheUtils.getString(getApplicationContext(), CacheUtils.LOGINPHONE,"");
    	password = CacheUtils.getString(getApplicationContext(), CacheUtils.LOGINPASSWORD,"");
    	
    	lockPatternView = (LockPatternView) findViewById(R.id.lockPatternView);
    	messageTv = (TextView) findViewById(R.id.messageTv);
    	
    	//忘记手势密码
    	forgetGesture = (TextView) findViewById(R.id.forgetGesture);
    	forgetGesture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 Intent intent = new Intent(GestureLoginActivity.this, HomeLoginActivity.class);
			      startActivity(intent);
			}
		});
    	
    	//其他账号登录
    	other = (TextView) findViewById(R.id.other);
    	other.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 Intent intent = new Intent(GestureLoginActivity.this, HomeLoginActivity.class);
			      startActivity(intent);
			}
		});
    	
    	tv_user = (TextView) findViewById(R.id.tv_user);//手机号
    	tv_user.setText(fixNum(phone));
    	
        //得到当前用户的手势密码
    	gesturePassword_string = CacheUtils.getString(GestureLoginActivity.this,CacheUtils.BYTE,"");
    	gesturePassword = gesturePassword_string.getBytes();
        lockPatternView.setOnPatternListener(patternListener);
        //震动效果
//      lockPatternView.setTactileFeedbackEnabled(true);
        updateStatus(Status.DEFAULT);
    }

	//手机号码星号处理
	private String fixNum(String phone) {
		return phone = phone.substring(0,3) + "****" + phone.substring(7);
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
                    	CacheUtils.putString(GestureLoginActivity.this, CacheUtils.BYTE, "");
                    	gestureDisable = true;
                    	CacheUtils.putBoolean(GestureLoginActivity.this, "gestureDisable", gestureDisable);
                    	
						GestureCountDialog gestureCountDialog = new GestureCountDialog(GestureLoginActivity.this, R.style.YzmDialog);
						gestureCountDialog.setCanceledOnTouchOutside(false);
						gestureCountDialog.setCancelable(false);
						gestureCountDialog.setOnGestureCountDialogDismissListener(new OnGestureCountDialogDismissListener() {
							
							@Override
							public void OnGestureCountDialogDismiss() {
								Intent intent = new Intent(GestureLoginActivity.this, HomeLoginActivity.class);
								intent.setFlags(10);
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


  //重写返回键，因为程序在后台超过三分钟后再次回到前台时会出现手势密码登录界面，为了防止用户按返回键直接进入
  	@Override
  	public void onBackPressed() {
  		Intent intent = getIntent();
  		int flags = intent.getFlags();
  		if (flags==10000) {
			LogUtils.i("flags==10000");
			return;
		}else{
			LogUtils.i("tuichu===");
			//友盟用来保存统计数据
			MobclickAgent.onKillProcess(this);
//			finish();
//			System.gc();
//			android.os.Process.killProcess(android.os.Process.myPid());
			ActivityManager manager = (ActivityManager)getSystemService(ACTIVITY_SERVICE); //获取应用程序管理器
			manager.killBackgroundProcesses(getPackageName()); //强制结束当前应用程序
		}
  		//super.onBackPressed();
  	}
  	
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
            		messageTv.setText("输入错误超过5次咯");
				}else {
					messageTv.setText("密码错误，你还有"+count+"次解锁机会");
				}
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                break;
            case CORRECT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.OK);
				FanFanApplication.isGestureOk = true;
                loginGestureSuccess();
                break;
        }
    }

    /**
     * 手势登录成功（去首页）
     */
    private void loginGestureSuccess() {
    	
		Map<String, String> map =  SortRequestData.getmap();
		map.put("phone", phone);
		map.put("password", password);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
      
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.MIMA_LOGIN_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {

						JSONObject json = JSON.parseObject(string);
						String  code= json.getString("code");
						if("0".equals(code)){
							String token = json.getString("token");
							CacheUtils.putString(getApplicationContext(), "token",token);
						    Intent intent=new Intent(GestureLoginActivity.this,MainActivity.class);
						    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置 
						    startActivity(intent);
						    finish();
						}else{
							String msg = json.getString("msg");
							ToastUtils.toastshort("手势密码："+msg);
							Intent intent=new Intent(GestureLoginActivity.this,MainActivity.class);
						    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置 
						    startActivity(intent);
						    finish();
						}
					}

					@Override
					public void onError(VolleyError error) {
						Intent intent=new Intent(GestureLoginActivity.this,MainActivity.class);
					    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置 
					    startActivity(intent);
					    finish();
					}
				});
    }


    private enum Status {
        //默认的状态
        DEFAULT(R.string.gesture_default, R.color.global_yellowcolor),
        //密码输入错误
        ERROR(R.string.gesture_error, R.color.global_bigred),
        //密码输入正确
        CORRECT(R.string.gesture_correct, R.color.global_yellowcolor);

        private Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }
        private int strId;
        private int colorId;
    }
}
