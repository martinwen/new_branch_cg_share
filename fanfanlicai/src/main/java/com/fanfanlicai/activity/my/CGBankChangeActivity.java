package com.fanfanlicai.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.view.cunguan.ProvinceActivity;
import com.fanfanlicai.view.dialog.CustomProgressDialog;


public class CGBankChangeActivity extends BaseActivity implements OnClickListener {

	private TextView tv_name;
	private TextView tv_number;
	private RelativeLayout rl_bankname;
	private TextView tv_bankname;
	private RelativeLayout rl_bankaddress;
	private TextView tv_bankaddress;
	private EditText et_card;
	private Button bt_btn;
	public static String  ADDRESS;
	public static String  ID;
	private CustomProgressDialog progressdialog;

	private String bankNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cgbankchange);

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
		tv_name = (TextView) findViewById(R.id.tv_name);
		//身份证
		tv_number = (TextView) findViewById(R.id.tv_number);
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
		//下一步
		bt_btn = (Button) findViewById(R.id.bt_btn);
		bt_btn.setOnClickListener(this);
	}

	private void initData() {
		String realName = getIntent().getStringExtra("realName");
		tv_name.setText(realName);
		String idNo = getIntent().getStringExtra("idNo");
		tv_number.setText(idNo);
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
				 intent2.setFlags(2);
				 startActivity(intent2);
				 break;
			 case R.id.bt_btn:
				 String cardNum = CacheUtils.getString(this, CacheUtils.CARDNUM, "");
				 String newCard = et_card.getText().toString().trim();
				 if(TextUtils.isEmpty(bankNo)){
				 	 ToastUtils.toastshort("请选择开户银行");
					 return;
				 }else if(TextUtils.isEmpty(ID)){
					 ToastUtils.toastshort("请选择开户地区");
					 return;
				 }else if(TextUtils.isEmpty(newCard)){
					 ToastUtils.toastshort("请输入新的银行卡号");
					 return;
				 }else if(newCard.equals(cardNum)){
					 ToastUtils.toastshort("新银行卡不能跟当前银行卡相同");
					 return;
				 }

				 Intent intent = new Intent(this,CGBankChangeForPhotoActivity.class);
				 intent.putExtra("cardNum",newCard);
				 intent.putExtra("cityId",ID);
				 intent.putExtra("bankNo",bankNo);
				 startActivity(intent);

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
}
