package com.fanfanlicai.pagers;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

public class InvestPager extends BasePager{
	
	private List<BasePager> pagers;
	private String[] titleTab = {"饭盒","饭碗","饭桶"};
	
	@ViewInject(R.id.indicator)
	private TabPageIndicator indicator;// 指针控件
	
	@ViewInject(R.id.pager)
	private ViewPager viewpager;// 页签详情

	public InvestPager(Context context) {
		super(context);
	}
	
	@Override
	protected View initView() {
		View view = View.inflate(mContext, R.layout.pager_invest, null);
		ViewUtils.inject(this,view);
		return view;
	}
	@Override
	public void initData() {
		
		// 准备数据
		pagers = new ArrayList<BasePager>();
		pagers.add(new FanHePager(mContext));
		pagers.add(new FanWanPager(mContext));
		pagers.add(new FanTongPager(mContext));
		// 给ViewPager设置数据
		viewpager.setAdapter(new MyAdapter());
		
		// 关联ViewPagerIndicator和ViewPager
		indicator.setVisibility(View.VISIBLE);	
		indicator.setViewPager(viewpager);
		indicator.notifyDataSetChanged();
		
		// 监听ViewPager
		indicator.setOnPageChangeListener(new MyOnPageChangeListener());
		// 初始化首页界面的数据
		if (ConstantUtils.fanheorfanwanorfantongflag == 1) {//进入饭碗投资界面
			viewpager.setCurrentItem(1, false);
			ConstantUtils.fanheorfanwanorfantongflag = 0;
		}else if (ConstantUtils.fanheorfanwanorfantongflag == 2) {//进入饭桶投资界面
			viewpager.setCurrentItem(2, false);
			ConstantUtils.fanheorfanwanorfantongflag = 0;
		}else {//进入饭盒投资界面
			pagers.get(0).initData();
		}
		
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
}
