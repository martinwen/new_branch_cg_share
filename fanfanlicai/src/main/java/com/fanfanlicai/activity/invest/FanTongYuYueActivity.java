
package com.fanfanlicai.activity.invest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.utils.StrToNumber;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.view.dialog.FanTongYuYueDialog;

import java.util.Timer;
import java.util.TimerTask;

public class FanTongYuYueActivity extends BaseActivity implements OnClickListener {

	private TextView get_back;// 返回
	private TextView tv_limit_money;// 预约范围
	private TextView tv_mininvest;// 起投金额
	private EditText et_input;// 账户余额
	private Button bt_get;//  预约按钮
	private String minBookAmount;
	private String maxBookAmount;
	private String minInvestAmount;
	private TextView tv_rule;//手续费收取规则
	private double baseRate;
	private double addRate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fantongyuyue);

		initView();
		initData();
	}

	
	private void initView() {
		get_back = (TextView) findViewById(R.id.get_back);// 返回
		get_back.setOnClickListener(this);
		tv_limit_money = (TextView) findViewById(R.id.tv_limit_money);// 预约范围
		tv_mininvest = (TextView) findViewById(R.id.tv_mininvest);// 起投金额
		et_input = (EditText) findViewById(R.id.et_input);// 账户余额
		bt_get = (Button) findViewById(R.id.bt_get);// 预约按钮
		bt_get.setOnClickListener(this);
		tv_rule = (TextView) findViewById(R.id.tv_rule);//手续费收取规则
		tv_rule.setOnClickListener(this);
		String str="6.发起赎回申请后，一般当天转入账户余额，平均赎回时间为12小时，请以实际到账时间为准。" +
				"<font color='#139fe7'>手续费收取规则。</font>";

		tv_rule.setText(Html.fromHtml(str));


		//解决进入预约界面没有弹出软键盘的问题
		et_input.setFocusable(true);
		et_input.setFocusableInTouchMode(true);
		et_input.requestFocus();

		Timer timer = new Timer();
		timer.schedule(new TimerTask() { //让软键盘延时弹出，以更好的加载Activity

			public void run() {
				InputMethodManager inputManager = (InputMethodManager)et_input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(et_input, 0);
			}

		}, 500);

	}

	private void initData() {
		Intent intent = getIntent();
		baseRate = intent.getDoubleExtra("baseRate", 0);
		addRate = intent.getDoubleExtra("addRate", 0);
		minBookAmount = intent.getStringExtra("minBookAmount");
		maxBookAmount = intent.getStringExtra("maxBookAmount");
		minInvestAmount = intent.getStringExtra("minInvestAmount");
		tv_limit_money.setText("请输入"+minBookAmount+"元-"+maxBookAmount+"元预约金额");
		tv_mininvest.setText("1.单笔最低出借金额"+minInvestAmount+"元；");
		
		
		// 给提现金额设置监听，为了使输入金额不超过两位有效数字
		et_input.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				if (s.toString().contains(".")) {
					if (s.length() - 1 - s.toString().indexOf(".") > 2) {
						s = s.toString().subSequence(0,
								s.toString().indexOf(".") + 3);
						et_input.setText(s);
						et_input.setSelection(s.length());
					}
				}
				if (s.toString().trim().substring(0).equals(".")) {
					s = "0" + s;
					et_input.setText(s);
					et_input.setSelection(2);
				}

				if (s.toString().startsWith("0")
						&& s.toString().trim().length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						et_input.setText(s.subSequence(0, 1));
						et_input.setSelection(1);
						return;
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_back:
			finish();
			break;
		case R.id.bt_get:// 预约
			String money = et_input.getText().toString().trim();
			if (money.isEmpty()) {
				ToastUtils.toastshort("预约金额不能为空哦");
			}else {
				if (StrToNumber.strTodouble(money)<StrToNumber.strTodouble(minBookAmount)) {
					ToastUtils.toastshort("您输入的金额小于预约金额最小值，请重新输入。");
				}else if (StrToNumber.strTodouble(money)>StrToNumber.strTodouble(maxBookAmount)) {
					ToastUtils.toastshort("您输入的金额大于预约金额最大值，请重新输入。");
				}else {
					FanTongYuYueDialog fanTongYuYueDialog = new FanTongYuYueDialog(FanTongYuYueActivity.this, R.style.YzmDialog,money);
					fanTongYuYueDialog.show();
				}
			}
			break;
		case R.id.tv_rule:
			Intent intent = new Intent(this, FanTongRateActivity.class);
			intent.putExtra("baseRate", baseRate);
			intent.putExtra("addRate", addRate);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
