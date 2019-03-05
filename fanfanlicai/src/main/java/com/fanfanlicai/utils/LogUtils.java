package com.fanfanlicai.utils;

import android.util.Log;

public class LogUtils {
	public static String TAG="lixiaonan";


    public static void i(String tag, String msg) {
        Log.i(tag, msg);
    }
    public static void i(String msg) {
    	Log.i(TAG,msg);
    }

    public static void longStr(String tag , String string){
        if (string.length() > 4000) {
            for (int i = 0; i < string.length(); i += 4000) {
                //当前截取的长度<总长度则继续截取最大的长度来打印
                if (i + 4000 < string.length()) {
                    LogUtils.i(tag + i, string.substring(i, i + 4000));
                } else {
                    //当前截取的长度已经超过了总长度，则打印出剩下的全部信息
                    LogUtils.i(tag + i, string.substring(i, string.length()));
                }
            }
        } else {
            //直接打印
            LogUtils.i(tag, string);
        }
    }
}
