package com.fanfanlicai.utils;
/**
 * 
 * @author wgj
 * 作用是保留2位小数，一个返回String一个返回float
 *
 */
public class NumUtil {

	public static String NumberFormat(float f,int m){
        return String.format("%."+m+"f",f);
    }

    public static float NumberFormatFloat(float f,int m){
        String strfloat = NumberFormat(f,m);
        return Float.parseFloat(strfloat);
    }
}
