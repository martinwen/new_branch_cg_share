package com.fanfanlicai.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;


/**
 * 使用AlarmManager 轮训请求token
 */
public class RefTokenUtils {

/*	*//**
	 * 开启服务
	 * @param context
	 * @param seconds
	 * @param cls
	 * @param action
	 *//*
	public static void startRefrsh(Context context,int seconds,Class<?> cls,String action){
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context,cls);
		intent.setAction(action);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		long triggerAtime = SystemClock.elapsedRealtime();
		alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtime, seconds*1000, pendingIntent);
	}
	*//**
	 * 关闭服务
	 * @param context
	 * @param cls
	 * @param action
	 *//*
	public static void stopRefresh(Context context,Class<?> cls,String action){
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context,cls);
		intent.setAction(action);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.cancel(pendingIntent);
	}*/
	
	/**
	 * 开启服务
	 * @param context
	 * @param seconds
	 * @param cls
	 * @param action
	 */
	public static void startRefrsh(Context context,int seconds,Class<?> cls,String action){
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context,cls);
		intent.setAction(action);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		long triggerAtime = SystemClock.elapsedRealtime();
		alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtime, seconds*60000, pendingIntent);
	}
	/**
	 * 关闭服务
	 * @param context
	 * @param cls
	 * @param action
	 */
	public static void stopRefresh(Context context,Class<?> cls,String action){
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context,cls);
		intent.setAction(action);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.cancel(pendingIntent);
	}
}
