package com.fanfanlicai.service;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.fanfanlicai.activity.MainActivity;
import com.fanfanlicai.global.FanFanApplication;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.StringUtils;
import com.fanfanlicai.view.lockPattern.GestureLoginActivity;

import cn.jpush.android.api.JPushInterface;

import static android.content.Context.ACTIVITY_SERVICE;


/**
 * Created by wangjian on 2017/1/4.
 */

public class JpushReceiver extends BroadcastReceiver {

    private static final String TAG = "JPush";
    private int count = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        //收到的通知内容
        String message = bundle.getString("cn.jpush.android.ALERT");

        if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "接收到推送下来的通知");
//            ActivityManager am = (ActivityManager) FanFanApplication.context.getSystemService(ACTIVITY_SERVICE);
//            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
//            Log.d(TAG, "cls="+cn.getClassName());
//            if("com.fanfanlicai.activity.registerandlogin.HomeLoginActivity".equals(cn.getClassName())||
//                    "com.fanfanlicai.activity.registerandlogin.HomeRegisterActivity".equals(cn.getClassName())||
//                    "com.fanfanlicai.activity.registerandlogin.XieYiActivity".equals(cn.getClassName())){
//                CacheUtils.putString(FanFanApplication.context, "token",null);
//            }


        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "用户点击打开了通知");
            ActivityManager am = (ActivityManager) FanFanApplication.context.getSystemService(ACTIVITY_SERVICE);
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            Log.d(TAG, "cls="+cn.getClassName());
            //如果点击通知栏时app停留在这几个页面，则不弹出消息
            if("com.fanfanlicai.view.lockPattern.GestureLoginActivity".equals(cn.getClassName())||
                    "com.fanfanlicai.activity.registerandlogin.HomeLoginActivity".equals(cn.getClassName())||
                    "com.fanfanlicai.activity.registerandlogin.ForgetPasswordActivity".equals(cn.getClassName())||
                    "com.fanfanlicai.activity.registerandlogin.HomeRegisterActivity".equals(cn.getClassName())||
                    "com.fanfanlicai.activity.registerandlogin.XieYiActivity".equals(cn.getClassName())) {

                //如果点击通知栏时app停留在输入手势密码页面，则弹出消息，并停留在此页面
            }else{

                String token = CacheUtils.getString(context, "token", null);
                LogUtils.i("token="+token);
                if(FanFanApplication.isGestureOk|| StringUtils.isBlank(token)){
                    //设置标记，其他情况无论在那个界面停留时点击通知栏都进入到首页
                    ConstantUtils.touziflag = 0;
                    //手机页面没有停留在app，在MainActivity弹出消息
                    Intent i = new Intent(context, MainActivity.class);
                    i.putExtras(bundle);
                    i.putExtra("message", message);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    context.startActivity(i);
                }else{
                    LogUtils.i("ceshi===");
                    Intent i = new Intent(context, GestureLoginActivity.class);
                    i.putExtras(bundle);
                    i.putExtra("message", message);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    context.startActivity(i);
                }
            }
        }
    }
}

