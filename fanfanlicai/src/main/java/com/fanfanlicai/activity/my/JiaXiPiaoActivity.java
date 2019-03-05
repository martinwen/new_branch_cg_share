package com.fanfanlicai.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.pagers.BasePager;
import com.fanfanlicai.pagers.jiaxipiao.JiaXiPiaoFhPager;
import com.fanfanlicai.pagers.jiaxipiao.JiaXiPiaoFwPager;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.view.HorizontalViewPager;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

public class JiaXiPiaoActivity extends FragmentActivity implements OnClickListener {

	private List<BasePager> pagers;
	private String[] titleTab = {"饭盒","饭碗"};
	
	private TabPageIndicator indicator;// 指针控件
	
	private HorizontalViewPager viewpager;// 页签详情
	private TextView invest_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_jiaxipiao);

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
		invest_back = (TextView) findViewById(R.id.invest_back);
		invest_back.setOnClickListener(this);
		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		viewpager = (HorizontalViewPager) findViewById(R.id.pager);

	}

	private void initData() {
		//从活动页面跳转过来
		Intent intent = getIntent();
		String code = intent.getStringExtra("code");
		// 准备数据
		pagers = new ArrayList<BasePager>();
		
		JiaXiPiaoFhPager jiaXiPiaoFhPager = new JiaXiPiaoFhPager(this);
		jiaXiPiaoFhPager.setCode(code);
		pagers.add(jiaXiPiaoFhPager);
		
		JiaXiPiaoFwPager jiaXiPiaoFwPager = new JiaXiPiaoFwPager(this);
		jiaXiPiaoFwPager.setCode(code);
		pagers.add(jiaXiPiaoFwPager);
		
		// 给ViewPager设置数据
		viewpager.setAdapter(new MyAdapter());
		
		// 关联ViewPagerIndicator和ViewPager
		indicator.setVisibility(View.VISIBLE);	
		indicator.setViewPager(viewpager);
		indicator.notifyDataSetChanged();
		
		// 监听ViewPager
		indicator.setOnPageChangeListener(new MyOnPageChangeListener());

		// 初始化首页界面的数据
		pagers.get(0).initData();
	}

	class MyOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			
		}

		@Override
		public void onPageSelected(int position) {
			// 当选中某一页时，才加载当前界面的数据
			pagers.get(position).initData();

		}

		@Override
		public void onPageScrollStateChanged(int state) {
			
		}
		
	}

	
	class MyAdapter extends PagerAdapter{
		
		@Override
		public CharSequence getPageTitle(int position) {
			// 返回标题的方法
			return titleTab[position];
		}
		
		@Override
		public int getCount() {
			return titleTab.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			LogUtils.i("position="+position);
			BasePager pager = pagers.get(position);
			container.addView(pager.rootView);
//			pager.initData();
			return pager.rootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.invest_back:
			finish();
			break;

		default:
			break;
		}
	}

}
