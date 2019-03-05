package com.fanfanlicai.sign;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.fanfanlicai.utils.DeviceUtil;
import com.fanfanlicai.utils.MD5;

public class SortRequestData {

	public static String sortString(Map<String, String> map){
		
		
		
		JSONObject json = new JSONObject();
		json.putAll(map);
		Set<String> keySet = json.keySet();
		keySet.remove("token");
		String[] keys = new String[keySet.size()];
		keySet.toArray(keys);
		Arrays.sort(keys);
		
		StringBuffer sb = new StringBuffer();
		for(String key : keys){
			sb.append(key +"="+ json.getString(key));
			if(!key.equals(keys[keys.length-1])){
				sb.append("&");
			}
		}
		return MD5.getMd5(sb.toString());

	}
	
	
	public static Map<String, String> getmap(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("device_id", DeviceUtil.DEVICE_ID);
		map.put("system_type", "android");
		map.put("system_version", DeviceUtil.OS_VERSION);
		map.put("app_version", DeviceUtil.APP_VERSIONNAME);
		return map;
	}
}
