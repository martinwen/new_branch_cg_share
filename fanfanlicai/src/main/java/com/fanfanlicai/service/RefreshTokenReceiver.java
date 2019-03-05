package com.fanfanlicai.service;

import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.global.FanFanApplication;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.StringUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;

/**
 * 刷新token的广播接收器
 */
public class RefreshTokenReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String url = ConstantUtils.REFRESHTOKEN_URL;
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(FanFanApplication.context, "token", null);
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(url, null, map, new HttpBackBaseListener() {
			
			@Override
			public void onSuccess(String string) {
				JSONObject json = JSON.parseObject(string);
				String code = json.getString("code");
				if ("0".equals(code)) {
					
					String token = json.getString("token");
					CacheUtils.putString(FanFanApplication.context, "token", token);
				}	
			}
			
			@Override
			public void onError(VolleyError error) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
