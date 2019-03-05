package com.fanfanlicai.utils;

import android.widget.Toast;

import com.fanfanlicai.global.FanFanApplication;

public class ToastUtils {

    public static void toastshort(String str){
    	Toast.makeText(FanFanApplication.context,str, Toast.LENGTH_SHORT).show();
    }
    public static void toastlong(String str){
    	Toast.makeText(FanFanApplication.context,str, Toast.LENGTH_LONG).show();
    }
}
