package com.fanfanlicai.utils.volley;

import com.android.volley.VolleyError;


/**
 * 类名:		HttpBackStringListener
 * 描述:		通信成功后的接口回调  其中成功的方法会直接string 返回回来
 */
public interface HttpBackBaseListener {
  /**
   * 成功的方法
   * @param string
   */
  public void onSuccess(String string);
 
  /**
   * volley框架 请求失败调用的方法
   * @param error
   */
  public void onError(VolleyError error);
}
