package com.fanfanlicai.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.activity.MainActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.view.dialog.CustomProgressDialog;


public class CGBankChangeSuccessActivity extends BaseActivity implements OnClickListener {

	private CustomProgressDialog progressdialog;
	private Button bt_btn;
	private TextView tv_old;
	private TextView tv_new;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cgbankchangesuccess);

		progressdialog = new CustomProgressDialog(this, "开通中...");
		initView();
		initData();
	}

	private void initView() {
		TextView get_back = (TextView) findViewById(R.id.get_back);
		get_back.setOnClickListener(this);
		bt_btn = (Button) findViewById(R.id.bt_btn);
		bt_btn.setOnClickListener(this);
		tv_old = (TextView) findViewById(R.id.tv_old);
		tv_new = (TextView) findViewById(R.id.tv_new);
	}

	private void initData() {
		String oldBankName = getIntent().getStringExtra("oldBankName");
		String oldCardNum = getIntent().getStringExtra("oldCardNum");
		tv_old.setText("原卡："+oldBankName+"（"+oldCardNum+"）");
		String newBankName = getIntent().getStringExtra("newBankName");
		String newCardNum = getIntent().getStringExtra("newCardNum");
		tv_new.setText("更换为："+newBankName+"（"+newCardNum+"）");
	}


	@Override
	public void onClick(View v) {
		 switch (v.getId()) {
		 	 case R.id.get_back:
				 finish();
				 break;
			 case R.id.bt_btn:
				 ConstantUtils.touziflag = 2;
				 Intent intent = new Intent(this, MainActivity.class);
				 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 startActivity(intent);
				 break;
		 default:
		 break;
		 }
	}


}
