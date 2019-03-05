package com.fanfanlicai.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class CacheUtils {
	private static String FILE_NAME = "fanfanlicai";
	private static SharedPreferences sp;
	
	public static String BYTE="BYTE";// 手势密码常量
	public static String REALNAME="realName";//姓名
	public static String IDNO="idNo";//身份证号
	public static String LOGINPHONE="loginphone";//登录手机号
	public static String LOGINPASSWORD="loginpassword";//登录密码
	public static String INVITECODE="inviteCode";//邀请码
	public static String TOTALASSETS="totalAssets";// 总资产（余额+饭盒+饭碗+奖金+饭碗待收+赎回中）
	public static String BASEACCTBAL="baseAcctBal";// 账户余额
	public static String FHACCTBAL="fhAcctBal";// 饭盒资产
	public static String FWACCTBAL="fwAcctBal";// 饭碗资产
	public static String FTACCTBAL="ftAcctBal";// 饭碗资产
	public static String REWARDACCTBAL="rewardAcctBal";// 奖金余额
	public static String FWWAITPAYINCOME="fwWaitPayIncome";// 饭碗待收收益
	public static String REDEEMAMOUNT="redeemAmount";// 赎回中金额
	public static String CASHINGMONEY="cashingMoney";// 体现中金额
	public static String REWARDUSEDAMOUNT="rewardUsedAmount";// 已使用奖金
	public static String CARDNUM="cardNum";// 银行卡号
	public static String CARDPHONE="cardPhone";// 银行预留手机号
	public static String ADDRESS="address";// 详细地址
	public static String ZIPCODE="zipCode";// 邮政编码
	public static String TOTALINCOME="totalIncome";// 总收益（首页）
	public static String FHTOTALINCOME="fhTotalIncome";// 饭盒累计收益（首页）
	public static String FWTOTALINCOME="fwTotalIncome";// 饭碗累计收益（首页）
	public static String FTTOTALINCOME="ftTotalIncome";// 饭桶累计收益（首页）
	public static String FWWAITREPAYAMOUNT="fwWaitRepayAmount";// 饭碗待收收益（首页）
	public static String PHONE="phone";// 电话
	public static String EMAIL="email";// 邮箱

	
	
	// 保存boolean值
	public static void putBoolean(Context context, String key, boolean value) {
		if (sp == null) {
			sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		}
		sp.edit().putBoolean(key, value).commit();
	}

	// 取boolean值
	public static boolean getBoolean(Context context, String key,
			boolean defValue) {
		if (sp == null) {
			sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		}
		return sp.getBoolean(key, defValue);
	}
	
	public static void putString(Context context, String key, String value) {
		if (sp == null) {
			sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		}
		sp.edit().putString(key, value).commit();
	}
	
	public static String getString(Context context, String key, String defValue) {
		if (sp == null) {
			sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		}
		return sp.getString(key, defValue);
	}
	
}
