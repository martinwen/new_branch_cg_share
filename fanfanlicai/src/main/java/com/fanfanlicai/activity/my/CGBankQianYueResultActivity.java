package com.fanfanlicai.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.activity.MainActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.utils.ConstantUtils;

public class CGBankQianYueResultActivity extends BaseActivity implements OnClickListener {

	private Button bt_btn;
	private String result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cgbankqianyueresult);

		initView();
	}
	
	private void initView() {
		TextView get_back = (TextView) findViewById(R.id.get_back);
		get_back.setOnClickListener(this);

		//下一步
		bt_btn = (Button) findViewById(R.id.bt_btn);
		bt_btn.setOnClickListener(this);

		ImageView iv_state = (ImageView) findViewById(R.id.iv_state);
		//图片显示成功或失败
		result = getIntent().getStringExtra("result");
		if("0".equals(result)){
			bt_btn.setText("重新签约");
			iv_state.setImageResource(R.drawable.qianyue_fail);
		}else{
			bt_btn.setText("确定");
			iv_state.setImageResource(R.drawable.qianyue_success);
		}
	}

	@Override
	public void onClick(View v) {
		 switch (v.getId()) {
		 	 case R.id.get_back:
				finish();
				break;
			 case R.id.bt_btn:
				 if("0".equals(result)){//失败
					 Intent intent = new Intent(this, CGBankNotQianYueActivity.class);
					 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					 startActivity(intent);
				 }else{//成功
					 ConstantUtils.touziflag = 2;
					 Intent intent = new Intent(this, MainActivity.class);
					 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					 startActivity(intent);
				 }
				 break;

		 default:
		 break;
		 }
	}

}
