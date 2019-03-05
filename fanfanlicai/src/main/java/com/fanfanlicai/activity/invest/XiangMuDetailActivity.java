package com.fanfanlicai.activity.invest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.activity.my.HeTongActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.utils.LogUtils;

public class XiangMuDetailActivity extends BaseActivity implements OnClickListener {

	private TextView tv_name;
	private TextView tv_item1;
	private TextView tv_item2;
	private TextView tv_item3;
	private TextView tv_item4;
	private TextView tv_item5;
	private TextView tv_item6;
	private String id;
	private String proCode;
	private RelativeLayout rl_id;
	private ImageView iv_id;
	private LinearLayout ll_danbao;
	private RelativeLayout rl_xiangmu;
	private RelativeLayout rl_fengkong;
	private RelativeLayout rl_tupian;
	private ImageView iv_xiangmu;
	private ImageView iv_fengkong;
	private ImageView iv_tupian;
	private String loanItemInfo;
	private String riskControl;
	private String picuUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_xiangmudetail);

		initView();
		initData();
	}

	private void initView() {
		TextView xiangmudetail_back = (TextView) findViewById(R.id.xiangmudetail_back);//返回
		xiangmudetail_back.setOnClickListener(this);
		//担保公司
		ll_danbao = (LinearLayout) findViewById(R.id.ll_danbao);

		//项目介绍
		rl_xiangmu = (RelativeLayout) findViewById(R.id.rl_xiangmu);
		rl_xiangmu.setOnClickListener(this);
		iv_xiangmu = (ImageView) findViewById(R.id.iv_xiangmu);

		//风控措施
		rl_fengkong = (RelativeLayout) findViewById(R.id.rl_fengkong);
		rl_fengkong.setOnClickListener(this);
		iv_fengkong = (ImageView) findViewById(R.id.iv_fengkong);

		//图片资料
		rl_tupian = (RelativeLayout) findViewById(R.id.rl_tupian);
		rl_tupian.setOnClickListener(this);
		iv_tupian = (ImageView) findViewById(R.id.iv_tupian);

		RelativeLayout rl_hetong = (RelativeLayout) findViewById(R.id.rl_hetong);//债权合同
		rl_hetong.setOnClickListener(this);
		//借款人身份证
		rl_id = (RelativeLayout) findViewById(R.id.rl_id);
		//借款人身份证
		iv_id = (ImageView) findViewById(R.id.iv_id);

		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_item1 = (TextView) findViewById(R.id.tv_item1);
		tv_item2 = (TextView) findViewById(R.id.tv_item2);
		tv_item3 = (TextView) findViewById(R.id.tv_item3);
		tv_item4 = (TextView) findViewById(R.id.tv_item4);
		tv_item5 = (TextView) findViewById(R.id.tv_item5);
		tv_item6 = (TextView) findViewById(R.id.tv_item6);
	}

	private void initData() {
		Intent intent = getIntent();
		String borrowCode = intent.getStringExtra("borrowCode");
		String borrowName = intent.getStringExtra("borrowName");
		String trulyUserName = intent.getStringExtra("trulyUserName");
		String trulyIdcard = intent.getStringExtra("trulyIdcard");
		String borrowMoney = intent.getStringExtra("borrowMoney");
		String trulyLoanType = intent.getStringExtra("trulyLoanType");
		String trulyLoanCompany = intent.getStringExtra("trulyLoanCompany");
		String isQianyi = intent.getStringExtra("isQianyi");
		String companyName = intent.getStringExtra("companyName");
		loanItemInfo = intent.getStringExtra("loanItemInfo");
		riskControl = intent.getStringExtra("riskControl");
		picuUrl = intent.getStringExtra("picuUrl");
		LogUtils.i("isQianyi=="+isQianyi+" companyName=="+companyName+" loanItemInfo=="+loanItemInfo+" riskControl=="+riskControl+" picuUrl=="+picuUrl);
		id = intent.getStringExtra("id");
		
		
		int flags = intent.getFlags();
		if (flags==0) {
			proCode = "fh";
		}else if(flags==1){
			proCode = "fw";
		}else if(flags==2){
			proCode = "ft";
		}
		tv_item1.setText(borrowCode);
		tv_item2.setText(borrowName);
		tv_item5.setText(borrowMoney+"元");
		if("1".equals(trulyLoanType)){
			tv_name.setText("借款人姓名");
			rl_id.setVisibility(View.VISIBLE);
			iv_id.setVisibility(View.VISIBLE);
			tv_item3.setText(trulyUserName);
			tv_item4.setText(trulyIdcard);
		}else if("2".equals(trulyLoanType)){
			tv_name.setText("借款公司名称");
			rl_id.setVisibility(View.GONE);
			iv_id.setVisibility(View.GONE);
			tv_item3.setText(trulyLoanCompany);
		}

		if("1".equals(isQianyi)){//未迁移
			ll_danbao.setVisibility(View.GONE);
			rl_xiangmu.setVisibility(View.GONE);
			iv_xiangmu.setVisibility(View.GONE);
			rl_fengkong.setVisibility(View.GONE);
			iv_fengkong.setVisibility(View.GONE);
			rl_tupian.setVisibility(View.GONE);
			iv_tupian.setVisibility(View.GONE);
		}else if("0".equals(isQianyi)){//已迁移
			tv_item6.setText(companyName);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.xiangmudetail_back:
			finish();
			break;
		case R.id.rl_xiangmu:
			Intent intent1 = new Intent(this,FengKongActivity.class);
			intent1.putExtra("title", "项目介绍");
			intent1.putExtra("content", loanItemInfo);
			startActivity(intent1);
			break;
		case R.id.rl_fengkong:
			Intent intent2 = new Intent(this,FengKongActivity.class);
			intent2.putExtra("title", "风控措施");
			intent2.putExtra("content", riskControl);
			startActivity(intent2);
			break;
		case R.id.rl_tupian:
			Intent intent3 = new Intent(this,TuPianActivity.class);
			intent3.putExtra("picuUrl", picuUrl);
			startActivity(intent3);
			break;
		case R.id.rl_hetong:
			Intent intent = new Intent(this,HeTongActivity.class);
			intent.putExtra("id", id);
			intent.putExtra("proCode", proCode);
			startActivity(intent);
			break;

		default:
			break;
		}
		
	}

}
