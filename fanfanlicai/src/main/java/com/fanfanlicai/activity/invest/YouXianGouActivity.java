package com.fanfanlicai.activity.invest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.fragment.totalmoney.TotalmoneyPagerAdapter;
import com.fanfanlicai.fragment.youxiangou.YouXianGouUnuseableFragment;
import com.fanfanlicai.fragment.youxiangou.YouXianGouUseableFragment;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.StringUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;
import com.fanfanlicai.view.HorizontalViewPager;
import com.nineoldandroids.view.ViewHelper;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YouXianGouActivity extends FragmentActivity implements OnClickListener {

	private HorizontalViewPager vp_total_container;
	private List<Fragment> fragments;
	private View indicate_line;

	private TextView invest_back;//返回
	private TextView tv_usable, tv_unused;//可使用，已失效

	private TotalmoneyPagerAdapter adapter;
	private EditText et_code;
	private ImageView iv_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_youxiangou);

		initView();
		initData();
	}

	//方便有盟统计，不用在每个Activity中都从写这两个方法
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
		
	private void initView() {
		indicate_line = findViewById(R.id.total_indicate_line);
		invest_back = (TextView) findViewById(R.id.invest_back);//返回
		invest_back.setOnClickListener(this);
		tv_usable = (TextView) findViewById(R.id.tv_usable);//可使用，
		tv_usable.setOnClickListener(this);
		tv_unused = (TextView) findViewById(R.id.tv_unused);//已失效
		tv_unused.setOnClickListener(this);
		vp_total_container = (HorizontalViewPager) findViewById(R.id.vp_total_container);

		et_code = (EditText) findViewById(R.id.et_code);
		iv_btn = (ImageView) findViewById(R.id.iv_btn);
		iv_btn.setOnClickListener(this);

	}

	private void initData() {
		// 准备数据
		fragments = new ArrayList<Fragment>();
		YouXianGouUseableFragment youXianGouUseableFragment = new YouXianGouUseableFragment();
		fragments.add(youXianGouUseableFragment);

		YouXianGouUnuseableFragment youXianGouUnuseableFragment = new YouXianGouUnuseableFragment();
		fragments.add(youXianGouUnuseableFragment);

		adapter = new TotalmoneyPagerAdapter(getSupportFragmentManager(), fragments);
		vp_total_container.setAdapter(adapter);
		// 设置默认文字颜色
		tv_usable.setTextColor(getResources().getColor(R.color.global_yellowcolor));
		// 监听ViewPager
		vp_total_container.setOnPageChangeListener(new MyOnPageChangeListener());
		// 初始化首页界面的数据
		fragments.get(0);
		// 得到屏幕宽度
		int screenW = getWindowManager().getDefaultDisplay().getWidth();
		// 根据屏幕宽度除以fragment的个数初始化指示器宽度
		indicate_line.getLayoutParams().width = screenW / fragments.size();
		indicate_line.requestLayout();
		indicate_line.setVisibility(View.VISIBLE);
	}

	class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			//这四句代码主要是让指示器跟随pager页的跳动而显示
			int offsetX = (int) (positionOffset * indicate_line.getWidth());
			int startX = position * indicate_line.getWidth();
			int translationX = startX + offsetX;
			ViewHelper.setTranslationX(indicate_line, translationX);
		}

		@Override
		public void onPageSelected(int position) {

		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}

	}

	@Override
	public void onClick(View v) {
		//初始文字颜色为灰色
		tv_usable.setTextColor(getResources().getColor(R.color.text_gray));
		tv_unused.setTextColor(getResources().getColor(R.color.text_gray));
		switch (v.getId()) {
		case R.id.invest_back:
			finish();
			break;
		case R.id.tv_usable:
			//根据选中的标题让页面切换到对应的内容
			vp_total_container.setCurrentItem(0, false);// 参数2，是否需要滑动的效果
			//根据选中的标题改变标题的文字颜色
			tv_usable.setTextColor(getResources().getColor(R.color.global_yellowcolor));
			break;
		case R.id.tv_unused:
			vp_total_container.setCurrentItem(1, false);
			tv_unused.setTextColor(getResources().getColor(R.color.global_yellowcolor));
			break;
		case R.id.iv_btn:

			String code = et_code.getText().toString().trim();
			getTicket(code);

			break;
		default:
			break;
		}
	}

	private void getTicket(String code) {
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(this, "token", null);
		// 如果没有登录，直接return，不访问网络了
		if (TextUtils.isEmpty(token)) {
			return;
		}
		map.put("code", code);
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.PREFERTICKET_EXCHANGE,
				null, map, new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						// TODO Auto-generated method stub
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

									ToastUtils.toastshort("兑换成功！");
									//刷新界面
									initData();
								} else {
									ToastUtils.toastshort("加载数据异常！");
								}
							}
						} else {
							String msg = json.getString("msg");
							ToastUtils.toastshort(msg);
						}

					}

					@Override
					public void onError(VolleyError error) {
						// TODO Auto-generated method stub
						ToastUtils.toastshort("加载数据失败！");
					}

				});

	}

}
