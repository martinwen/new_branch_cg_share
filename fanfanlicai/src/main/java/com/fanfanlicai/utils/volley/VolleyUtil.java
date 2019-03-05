package com.fanfanlicai.utils.volley;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fanfanlicai.activity.JieSuanActivity;
import com.fanfanlicai.activity.UDSystemActivity;
import com.fanfanlicai.global.FanFanApplication;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * 类名：VolleyUtil
 * 
 * 描述：volley请求封装工具类
 * 
 */
public class VolleyUtil {
	private static RequestQueue requestQueue;

	private final static int TIME_OUT = 15000;

	

	/**
	 * Construct 构造方法
	 */
	private VolleyUtil() {
	}

	/**
	 * 使用单例模式取得RequestQueue 对象
	 * 
	 * @return
	 */
	public static RequestQueue getInstance() {
		if (requestQueue == null) {
			synchronized (VolleyUtil.class) {
				if (requestQueue == null) {
					requestQueue = Volley
							.newRequestQueue(FanFanApplication.context);
					requestQueue.start();
				}
			}
		}
		return requestQueue;
	}

	public static <T> void addRequest(RequestQueue requestQueue,
			Request<T> request, Object tag) {
		if (tag != null) {
			request.setTag(tag);
		}
		request.setShouldCache(false);
		request.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		requestQueue.add(request);
	}

	public static void cancelAllByTag(Object tag) {
		if (null != requestQueue) {
			if (tag != null) {
				requestQueue.cancelAll(tag);
			}
		}
	}

	public static void cancelAll(Context context) {
		if (null != requestQueue) {
			requestQueue.cancelAll(context);
		}
	}

	/**
	 * 向服务器发送一个post json 请求 返回是个list对象 传输的参数是个对象 分页
	 * 
	 * @param url
	 *            地址
	 * @param tag
	 *            标签
	 * @param object
	 *            传输的对象
	 * @param clazz
	 *            返回的list中实体类
	 * @param listener
	 */
//	public static <T> void PostForJson(final String url,
//			Object tag, final Object object,
//			final HttpBackBaseListener listener) {
//		JsonObjectRequest jsonrequest = new JsonObjectRequest(Method.POST, url,
//				null, new Response.Listener<org.json.JSONObject>() {
//
//					@Override
//					public void onResponse(org.json.JSONObject paramT) {
//						listener.onSuccess(paramT.toString());
//					}
//				}, new Response.ErrorListener() {
//
//					@Override
//					public void onErrorResponse(VolleyError paramVolleyError) {
//						if (paramVolleyError != null) {
//							listener.onError(paramVolleyError);
//						}
//					}
//				}) {
//
//			@Override
//			public Map<String, String> getHeaders() throws AuthFailureError {
//				Map<String, String> headers = new HashMap<String, String>();
//				headers.put("Accept", "application/json");
//				headers.put("Content-Type", "application/json; charset=UTF-8");
//				return headers;
//			}
//
//			@Override
//			public byte[] getBody() {
//				String objectstr = JSONObject.toJSONString(object);
//				try {
//					return objectstr.getBytes("UTF-8");
//				} catch (UnsupportedEncodingException e) {
//					e.printStackTrace();
//				}
//				return super.getBody();
//			}
//		};
//		addRequest(getInstance(), jsonrequest, tag);
//	}
	
	/**
	 * json post请求方式 发送请求 返回的是结果 成功的话是ok
	 * 
	 * @param url
	 * @param tag
	 * @param params
	 * @param listener
	 * @param
	 * @param
	 */
	public static void sendJsonRequestByPost(final String url, Object tag,
			final Map<String, String> params,
			final HttpBackBaseListener listener) {
		org.json.JSONObject jsonparams = new org.json.JSONObject(params);


		JsonObjectRequest jsonrequest =new JsonObjectRequest(Method.POST, url, jsonparams, new Listener<org.json.JSONObject>() {

			@Override
			public void onResponse(org.json.JSONObject response) {
//				LogUtils.i("response"+response);
				try {
					String code = (String)response.getString("code");
					if ("3003".equals(code)) {//token失效，登录超时
						//当token失效，自动登录
						login();
						listener.onSuccess(response.toString());
					}else if ("1003".equals(code)) {
						String datastr = response.getString("data");
						JSONObject data = JSON.parseObject(datastr);
						String upgInfo = data.getString("upgInfo");
						boolean upgSwitch = data.getBoolean("upgSwitch");

						if (upgSwitch) {
							Intent intent = new Intent(FanFanApplication.context, UDSystemActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.putExtra("upgInfo", upgInfo);
							FanFanApplication.context.startActivity(intent);
						}

					}else if ("666666".equals(code)) {
						listener.onSuccess(response.toString());

						String msg = response.getString("msg");
						Intent intent = new Intent(FanFanApplication.context, JieSuanActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("msg", msg);
						FanFanApplication.context.startActivity(intent);
					}else{

						listener.onSuccess(response.toString());

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError error) {
				if (error != null) {
					listener.onError(error);
				}
			}
			
		}){

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headers = new HashMap<String, String>();
				headers.put("Accept", "application/json");
				headers.put("Content-Type", "application/json; charset=UTF-8");
				return headers;
			}
			
		};
		addRequest(getInstance(), jsonrequest, tag);
	}
	
	public static void sendJsonRequestByPost1(final String url, Object tag,
			final String re,
			final HttpBackBaseListener listener) {
		StringRequest jsonrequest =new StringRequest(Method.POST, url,new Listener<String>() {

			@Override
			public void onResponse(String response) {
				listener.onSuccess(response.toString());
			}
		}, new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError error) {
				if (error != null) {
					listener.onError(error);
				}
			}

		}){


			@Override
			public byte[] getBody() throws AuthFailureError {
				// TODO Auto-generated method stub

				return re.getBytes();
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headers = new HashMap<String, String>();
				headers.put("Accept", "application/json");
				headers.put("Content-Type", "text/html;charset=UTF-8");
				return headers;
			}

		};
		addRequest(getInstance(), jsonrequest, tag);
	}
	
	
	/**
     * 手势登录成功（去首页）
     */
    private static void login() {
    	
    	//获取到注册时的手机号和登录密码
    	String phone = CacheUtils.getString(FanFanApplication.context, CacheUtils.LOGINPHONE,"");
    	String password = CacheUtils.getString(FanFanApplication.context, CacheUtils.LOGINPASSWORD,"");
    	if (phone.equals("")||password.equals("")) {
			return;
		}
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
							CacheUtils.putString(FanFanApplication.context, "token",token);
						}
					}

					@Override
					public void onError(VolleyError error) {
						
					}
				});
    }
	/**
	 * json get请求方式 发送请求 返回的是结果 成功的话是ok
	 * 
	 * @param url
	 * @param tag
	 * @param params
	 * @param listener
	 */
//	public static void sendJsonRequestByGet(final String url, Object tag,
//			final Map<String, String> params,
//			final HttpBackBaseListener listener) {
//		org.json.JSONObject jsonparams = new org.json.JSONObject(params);
//		JsonObjectRequest jsonrequest =new JsonObjectRequest(Method.GET, url, jsonparams, new Listener<org.json.JSONObject>() {
//			
//			@Override
//			public void onResponse(org.json.JSONObject response) {
//				listener.onSuccess(response.toString());
//			}
//		}, new ErrorListener(){
//			
//			@Override
//			public void onErrorResponse(VolleyError error) {
//				if (error != null) {
//					listener.onError(error);
//				}
//			}
//			
//		}){
//			
//			@Override
//			public Map<String, String> getHeaders() throws AuthFailureError {
//				Map<String, String> headers = new HashMap<String, String>();
//				headers.put("Accept", "application/json");
//				headers.put("Content-Type", "application/json; charset=UTF-8");
//				return headers;
//			}
//			
//		};
//		addRequest(getInstance(), jsonrequest, tag);
//	}
	/**
	 * get 方式请求 返回的是 最原始的String 其他的一些外部接口使用的 直接返回最原始的String 自己根据外部的接口的规则自己去解析
	 * 
	 * @param url
	 * @param tag
	 * @param params
	 * @param listener
	 */
//	public static void sendGetRequestToString(final String url, Object tag,
//			final Map<String, String> params,
//			final HttpBackBaseListener listener) {
//		StringRequest stringRequest = new StringRequest(Method.GET, url,
//				new Response.Listener<String>() {
//					@Override
//					public void onResponse(String s) {
//						listener.onSuccess(s);
//					}
//				}, new Response.ErrorListener() {
//
//					@Override
//					public void onErrorResponse(VolleyError volleyError) {
//						if (volleyError != null) {
//							listener.onError(volleyError);
//						}
//					}
//				}) {
//			@Override
//			public Map<String, String> getHeaders() throws AuthFailureError {
//				Map<String, String> headers = new HashMap<String, String>();
//				headers.put("Accept", "application/json");
//				headers.put("Content-Type", "application/json; charset=UTF-8");
//				return headers;
//			}
//
//			@Override
//			public String getUrl() {
//				String sParams = BaseUtil.mapToStringParams(params);
//				if (sParams.equals("")) {
//					return super.getUrl();
//				} else {
//					return url + "?" + sParams;
//				}
//			}
//		};
//		addRequest(getInstance(), stringRequest, tag);
//	}
}
