package com.fanfanlicai.activity.setting;

import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.StringUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;
import com.fanfanlicai.view.dialog.CustomProgressDialog;

public class AddressActivity extends BaseActivity implements OnClickListener,TextWatcher {

	private EditText et_detail;
	private EditText et_code;
	private CustomProgressDialog progressdialog;
	private Button bt_save;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_address);
		
		progressdialog = new CustomProgressDialog(this, "正在保存数据......");
		
		initView();
		initData();
	}

	private void initView() {
		TextView address_back = (TextView) findViewById(R.id.address_back);
		address_back.setOnClickListener(this);
		et_detail = (EditText) findViewById(R.id.et_detail);
		et_code = (EditText) findViewById(R.id.et_code);
		bt_save = (Button) findViewById(R.id.bt_save);
		bt_save.setOnClickListener(this);
		
		et_detail.addTextChangedListener(this);  
		et_code.addTextChangedListener(this);  
	}
	
	private void initData() {
		// 默认按钮不可点击
		bt_save.setBackgroundResource(R.drawable.anniu_chang_bukedianji);  
		bt_save.setClickable(false); 
		//默认显示已经设置过的地址
		String address = CacheUtils.getString(this, CacheUtils.ADDRESS, "");
		et_detail.setText(address);
		String zipCode = CacheUtils.getString(this, CacheUtils.ZIPCODE, "");
		et_code.setText(zipCode);
	}

	//为了使所有edittext不为空时按钮才可点击，实现了TextWatcher接口，下面三个方法是必须实现的
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		
		if (StringUtils.isNotBlank(et_detail.getText().toString())
				&&StringUtils.isNotBlank(et_code.getText().toString())
				)
		{
			//当输入框都不为空时，按钮可点击
			bt_save.setBackgroundResource(R.drawable.button_long_selector);
			bt_save.setPressed(false);  
			bt_save.setClickable(true);  
		}else {
			bt_save.setBackgroundResource(R.drawable.anniu_chang_bukedianji);  
			bt_save.setClickable(false);  
		}
	}
		
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.address_back:
			finish();
			break;
		case R.id.bt_save:
			getDataFromServer();
			break;

		default:
			break;
		}
	}

	private void getDataFromServer() {
		final String address = et_detail.getText().toString().trim();
		final String zipCode = et_code.getText().toString().trim();
		if (TextUtils.isEmpty(address)) {
			ToastUtils.toastshort("请输入详细地址");
			return;
		}
		if (TextUtils.isEmpty(zipCode)) {
			ToastUtils.toastshort("请输入邮政编码");
			return;
		}
		
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(this, "token", null);
		// 如果没有登录，直接return，不访问网络了
		if (TextUtils.isEmpty(token)) {
			return;
		}
		progressdialog.show();
		map.put("token", token);
		map.put("address", address);
		map.put("zipCode", zipCode);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.SETADDRESS_URL, null, map,
				new HttpBackBaseListener() {
					
					@Override
					public void onSuccess(String string) {
						LogUtils.i("地址信息===="+string);
						progressdialog.dismiss();
						JSONObject json = JSON.parseObject(string);
						String code = json.getString("code");
						if("0".equals(code)){
							CacheUtils.putString(AddressActivity.this, "address", address);
							CacheUtils.putString(AddressActivity.this, "zipCode", zipCode);
							ToastUtils.toastshort("保存成功");
							finish();
						}else{
							String msg = json.getString("msg");
							ToastUtils.toastshort(msg);
						}
					}
					
					@Override
					public void onError(VolleyError error) {
						progressdialog.dismiss();
						ToastUtils.toastshort("保存失败");
					}
				});
	}

}
