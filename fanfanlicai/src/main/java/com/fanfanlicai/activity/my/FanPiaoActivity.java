package com.fanfanlicai.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.activity.invest.YouXianGouActivity;
import com.fanfanlicai.fanfanlicai.R;

public class FanPiaoActivity extends BaseActivity implements OnClickListener {

	private TextView invest_back;
	private ImageView iv_jiaxipiao;
	private ImageView iv_tixianpiao;
	private ImageView iv_hongbao;
	private ImageView iv_youxiangou;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fanpiao);

		initView();
		
	}

		
	private void initView() {
		invest_back = (TextView) findViewById(R.id.invest_back);
		invest_back.setOnClickListener(this);
		iv_jiaxipiao = (ImageView) findViewById(R.id.iv_jiaxipiao);
		iv_jiaxipiao.setOnClickListener(this);
		iv_tixianpiao = (ImageView) findViewById(R.id.iv_tixianpiao);
		iv_tixianpiao.setOnClickListener(this);
		iv_hongbao = (ImageView) findViewById(R.id.iv_hongbao);
		iv_hongbao.setOnClickListener(this);
		iv_youxiangou = (ImageView) findViewById(R.id.iv_youxiangou);
		iv_youxiangou.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.invest_back:
			finish();
			break;
		case R.id.iv_jiaxipiao:
			startActivity(new Intent(this, JiaXiPiaoActivity.class));
			break;
		case R.id.iv_tixianpiao:
			startActivity(new Intent(this, TiXianPiaoActivity.class));
			break;
		case R.id.iv_hongbao:
			startActivity(new Intent(this, HongBaoActivity.class));
			break;
		case R.id.iv_youxiangou:
			startActivity(new Intent(this, YouXianGouActivity.class));
			break;

		default:
			break;
		}
	}

}
