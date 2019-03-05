package com.fanfanlicai.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.activity.MainActivity;
import com.fanfanlicai.activity.home.ZhangHuLiuShuiActivity;
import com.fanfanlicai.fanfanlicai.R;

public class CashDetailActivity extends BaseActivity implements OnClickListener {
	
	private TextView getdetail_back;//返回
	private TextView tv_card;//卡号
	private TextView tv_check;//资金流水中查看提现进度
	private Button bt_finish;//完成按钮
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cashdetail);

		initView();
		initData();
	}

	private void initView() {
		getdetail_back = (TextView) findViewById(R.id.getdetail_back);//返回
		getdetail_back.setOnClickListener(this);
		tv_card = (TextView) findViewById(R.id.tv_card);//资金流水中查看提现进度
		tv_check = (TextView) findViewById(R.id.tv_check);//资金流水中查看提现进度
		bt_finish = (Button) findViewById(R.id.bt_finish);//完成注册按钮
		bt_finish.setOnClickListener(this);
	}

	private void initData() {
		String cardNum = getIntent().getStringExtra("cardNum");
		tv_card.setText(cardNum);
		//使点击进入资金流水界面
		linktext(tv_check);
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(CashDetailActivity.this,MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置 
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.getdetail_back:
			Intent intent1 = new Intent(CashDetailActivity.this,MainActivity.class);
			intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置 
			startActivity(intent1);
			break;
		case R.id.bt_finish://完成
			Intent intent2 = new Intent(CashDetailActivity.this,MainActivity.class);
			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置 
			startActivity(intent2);
			break;
		default:
			break;
		}
	}
	
	private void linktext(TextView textView) {
		SpannableStringBuilder ss = new SpannableStringBuilder("您也可在账户余额流水中查看提现进度");
		ClickableSpan span = new ClickableSpan() {

			@Override
			public void onClick(View widget) {
				startActivity(new Intent(CashDetailActivity.this,ZhangHuLiuShuiActivity.class));
			}

			@Override
			public void updateDrawState(TextPaint ds) {
				super.updateDrawState(ds);
				// 去掉下划线
				ds.setUnderlineText(false);
			}
		};
		// 参数2：span的开始位置（包含），参数3：span的结束位置 （不包含）
		ss.setSpan(span, 4, 10, 0);

		textView.setText(ss);
		// 让span 可以点击
		textView.setMovementMethod(LinkMovementMethod.getInstance());
	}
}
