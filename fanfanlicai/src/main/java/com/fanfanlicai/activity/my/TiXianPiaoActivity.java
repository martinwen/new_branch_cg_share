package com.fanfanlicai.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.fragment.tixianpiao.TiXianUnuseableFragment;
import com.fanfanlicai.fragment.tixianpiao.TiXianUseableFragment;
import com.fanfanlicai.fragment.totalmoney.TotalmoneyPagerAdapter;
import com.fanfanlicai.view.HorizontalViewPager;
import com.nineoldandroids.view.ViewHelper;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class TiXianPiaoActivity extends FragmentActivity implements OnClickListener {

	private HorizontalViewPager vp_total_container;
	private List<Fragment> fragments;
	private View indicate_line;

	private TextView invest_back;//返回
	private TextView tv_usable, tv_unused;//可使用，已失效

	private TotalmoneyPagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tixianpiao);

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
		tv_usable = (TextView) findViewById(R.id.tv_usable);//饭盒
		tv_unused = (TextView) findViewById(R.id.tv_unused);//饭碗
		invest_back.setOnClickListener(this);
		tv_usable.setOnClickListener(this);
		tv_unused.setOnClickListener(this);
		vp_total_container = (HorizontalViewPager) findViewById(R.id.vp_total_container);

	}

	private void initData() {
		//从活动页面跳转过来
		Intent intent = getIntent();
		String code = intent.getStringExtra("code");

		// 准备数据
		fragments = new ArrayList<Fragment>();
		TiXianUseableFragment tiXianUseableFragment = new TiXianUseableFragment();
		tiXianUseableFragment.setCode(code);
		fragments.add(tiXianUseableFragment);

		TiXianUnuseableFragment tiXianUnuseableFragment = new TiXianUnuseableFragment();
		tiXianUnuseableFragment.setCode(code);
		fragments.add(tiXianUnuseableFragment);

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

		default:
			break;
		}
	}
}