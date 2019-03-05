package com.fanfanlicai.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.fanfanlicai.view.cunguan.ProvinceActivity;
import com.fanfanlicai.view.dialog.CustomProgressDialog;

import java.util.Map;

public class CGBindBankActivity extends BaseActivity implements OnClickListener {

	private EditText et_name;
	private EditText et_number;
	private RelativeLayout rl_bankname;
	private TextView tv_bankname;
	private RelativeLayout rl_bankaddress;
	private TextView tv_bankaddress;
	private EditText et_card;
	private EditText et_phone;
	private EditText et_password;
	private EditText et_confirm;
	private ImageView iv_eye;
	private ImageView iv_eye2;
	private Button bt_btn;
	private boolean isOpen = false;
	private boolean isOpen2 = false;

	public static String  ADDRESS;
	public static String  ID;
	private CustomProgressDialog progressdialog;

	private String bankNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cgbindbank);

		ADDRESS = "";
		ID = "";
		progressdialog=new CustomProgressDialog(this, "开通中...");
		initView();
		initData();
	}

	private void initView() {
		TextView get_back = (TextView) findViewById(R.id.get_back);
		get_back.setOnClickListener(this);
		//姓名
		et_name = (EditText) findViewById(R.id.et_name);
		//身份证
		et_number = (EditText) findViewById(R.id.et_number);
		//开户银行
		rl_bankname = (RelativeLayout) findViewById(R.id.rl_bankname);
		rl_bankname.setOnClickListener(this);
		tv_bankname = (TextView) findViewById(R.id.tv_bankname);
		//开户地区
		rl_bankaddress = (RelativeLayout) findViewById(R.id.rl_bankaddress);
		rl_bankaddress.setOnClickListener(this);
		tv_bankaddress = (TextView) findViewById(R.id.tv_bankaddress);
		//银行卡号
		et_card = (EditText) findViewById(R.id.et_card);
		//银行预留手机号
		et_phone = (EditText) findViewById(R.id.et_phone);
		//银行密码
		et_password = (EditText) findViewById(R.id.et_password);
		et_password.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		//确认密码
		et_confirm = (EditText) findViewById(R.id.et_confirm);
		et_confirm.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		iv_eye = (ImageView) findViewById(R.id.iv_eye);//密码输入框后的眼睛图片
		iv_eye.setOnClickListener(this);
		iv_eye2 = (ImageView) findViewById(R.id.iv_eye2);//密码输入框后的眼睛图片
		iv_eye2.setOnClickListener(this);
		//下一步
		bt_btn = (Button) findViewById(R.id.bt_btn);
		bt_btn.setOnClickListener(this);
	}

	private void initData() {
		if (!progressdialog.isShowing()) {
			progressdialog.showis();
		}
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(this,"token", "");
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.GETUSERACCOUNTINFO_URL, null, map,
				new HttpBackBaseListener() {
					@Override
					public void onSuccess(String string) {
						LogUtils.i("信息回显==="+string);
						// TODO Auto-generated method stub
						progressdialog.dismiss();
						JSONObject json = JSON.parseObject(string);
						String code = json.getString("code");

						if ("0".equals(code)) {
							String datastr = json.getString("data");
							if (StringUtils.isBlank(datastr)) {
								// datastr为空不验签
							} else {
								String sign = json.getString("sign");
								boolean isSuccess = SignUtil.verify(sign,
										datastr);
								if (isSuccess) {// 验签成功
									JSONObject data = JSON.parseObject(datastr);


									// 真实姓名
									String realName = data.getString("realName");
									if(!TextUtils.isEmpty(realName)){
										et_name.setText(realName);
										et_name.setEnabled(false);
									}

									// 身份证号
									String idNo = data.getString("idNo");
									if(!TextUtils.isEmpty(idNo)){
										et_number.setText(idNo);
										et_number.setEnabled(false);
									}

									// 开户行名称
									String bankName = data.getString("bankName");
									if(!TextUtils.isEmpty(bankName)){
										tv_bankname.setText(bankName);
									}

									// 开户地区
									ADDRESS = data.getString("cardArea");
									if(!TextUtils.isEmpty(ADDRESS)){
										tv_bankaddress.setText(ADDRESS);
									}

									// 银行卡号
									String cardNum = data.getString("cardNum");
									if(!TextUtils.isEmpty(cardNum)){
										et_card.setText(cardNum);
									}

									// 银行预留手机号
									String cardPhone = data.getString("cardPhone");
									if(!TextUtils.isEmpty(cardPhone)){
										et_phone.setText(cardPhone);
									}

									// 银行编号
									bankNo = data.getString("bankNo");

									// 城市ID
									ID = data.getString("cityId");


								} else {
									ToastUtils.toastshort("加载数据异常！");
								}
							}
						}else {
							String msg = json.getString("msg");
							ToastUtils.toastshort(msg);
						}
					}

					@Override
					public void onError(VolleyError error) {
						progressdialog.dismiss();
					}
				});
	}

	@Override
	public void onResume() {
		super.onResume();
		tv_bankaddress.setText(ADDRESS);
	}

	@Override
	public void onClick(View v) {
		 switch (v.getId()) {
		 	 case R.id.get_back:
				finish();
				break;
			 case R.id.rl_bankname:
				 Intent intent1 = new Intent(this,CGBankListActivity.class);
				 startActivityForResult(intent1,1);
				 break;
			 case R.id.rl_bankaddress:
				 Intent intent2 = new Intent(this,ProvinceActivity.class);
				 intent2.setFlags(1);
				 startActivity(intent2);
				 break;
			 case R.id.iv_eye:
				 isOpen = !isOpen;//根据点击改变眼睛的闭合状态
				 if (isOpen) {
					 //眼睛打开，输入框里的密码显示
					 iv_eye.setImageResource(R.drawable.eye_open2);
					 et_password.setTransformationMethod(HideReturnsTransformationMethod
							 .getInstance());
				 } else {
					 //眼睛关闭，输入框里的密码隐藏
					 iv_eye.setImageResource(R.drawable.eye_close2);
					 et_password.setTransformationMethod(PasswordTransformationMethod
							 .getInstance());
				 }
				 // 切换后将EditText光标置于末尾
				 CharSequence charSequence = et_password.getText();
				 if (charSequence instanceof Spannable) {
					 Spannable spanText = (Spannable) charSequence;
					 Selection.setSelection(spanText, charSequence.length());
				 }
				 break;
			 case R.id.iv_eye2:
				 isOpen2 = !isOpen2;//根据点击改变眼睛的闭合状态
				 if (isOpen2) {
					 //眼睛打开，输入框里的密码显示
					 iv_eye2.setImageResource(R.drawable.eye_open2);
					 et_confirm.setTransformationMethod(HideReturnsTransformationMethod
							 .getInstance());
				 } else {
					 //眼睛关闭，输入框里的密码隐藏
					 iv_eye2.setImageResource(R.drawable.eye_close2);
					 et_confirm.setTransformationMethod(PasswordTransformationMethod
							 .getInstance());
				 }
				 // 切换后将EditText光标置于末尾
				 CharSequence charSequence2 = et_confirm.getText();
				 if (charSequence2 instanceof Spannable) {
					 Spannable spanText = (Spannable) charSequence2;
					 Selection.setSelection(spanText, charSequence2.length());
				 }
				 break;
			 case R.id.bt_btn:
				 bind();
				 break;

		 default:
		 break;
		 }
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1 && resultCode == 2){
			String bankName = data.getExtras().getString("bankName");
			tv_bankname.setText(bankName);
			bankNo = data.getExtras().getString("bankNo");
		}
	}

	private void bind() {
		// 绑定银行卡界面输入的参数
		final String realName = et_name.getText().toString().trim();
		final String idNo = et_number.getText().toString().trim();
		final String cardNum = et_card.getText().toString().trim();
		final String cardPhone = et_phone.getText().toString().trim();
		final String cashPassword = et_password.getText().toString().trim();
		final String cashPassword2 = et_confirm.getText().toString().trim();

		if (TextUtils.isEmpty(realName)) {
			ToastUtils.toastshort("姓名不能为空");
			return;
		}
		if (TextUtils.isEmpty(idNo)) {
			ToastUtils.toastshort("身份证号不能为空");
			return;
		}
		if (TextUtils.isEmpty(bankNo)) {
			ToastUtils.toastshort("开户银行不能为空");
			return;
		}
		if (TextUtils.isEmpty(ID)) {
			ToastUtils.toastshort("开户地区不能为空");
			return;
		}
		if (TextUtils.isEmpty(cardNum)) {
			ToastUtils.toastshort("银行卡号不能为空");
			return;
		}
		if (TextUtils.isEmpty(cardPhone)) {
			ToastUtils.toastshort("银行预留手机号不能为空");
			return;
		}
		if (TextUtils.isEmpty(cashPassword)) {
			ToastUtils.toastshort("交易密码不能为空");
			return;
		}
		if (TextUtils.isEmpty(cashPassword2)) {
			ToastUtils.toastshort("确认密码不能为空");
			return;
		}

		progressdialog.show();
		// 绑卡之前 获取UUID
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(this, "token","");
		map.put("token", token);
		map.put("realName", realName);
		map.put("idNo", idNo);
		map.put("bankNo", bankNo);
		map.put("cityId", ID);
		map.put("cardNum", cardNum);
		map.put("cardPhone", cardPhone);
		map.put("cashPassword", cashPassword);
		map.put("cashPassword2", cashPassword2);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		progressdialog.show();
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.BINDCARDANDREG_URL, null,
				map, new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						progressdialog.dismiss();

						JSONObject json = JSON.parseObject(string);
						String code = json.getString("code");

						if ("0".equals(code)) {
							String datastr = json.getString("data");
							if (StringUtils.isBlank(datastr)) {
								// datastr为空不验签
							} else {
								String sign = json.getString("sign");
								boolean isSuccess = SignUtil.verify(sign,datastr);
								if (isSuccess) {// 验签成功
									ToastUtils.toastshort("恭喜您，开户成功了");
									JSONObject data = JSON.parseObject(datastr);
									Intent intent = new Intent(CGBindBankActivity.this,CGBankNotQianYueActivity.class);
									startActivity(intent);
								} else {
									ToastUtils.toastshort("添加失败！");
								}
							}
						} else {
							String msg = json.getString("msg");
							ToastUtils.toastshort(msg);
						}
					}

					@Override
					public void onError(VolleyError error) {
						progressdialog.dismiss();
						ToastUtils.toastshort("添加失败！");
					}
				});
	}
}
