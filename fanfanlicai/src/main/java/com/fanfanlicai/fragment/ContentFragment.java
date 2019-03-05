package com.fanfanlicai.fragment;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.global.FanFanApplication;
import com.fanfanlicai.pagers.BasePager;
import com.fanfanlicai.pagers.FindPager;
import com.fanfanlicai.pagers.InvestPager;
import com.fanfanlicai.pagers.MoneyPager;
import com.fanfanlicai.pagers.MyPager;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.view.HorizontalViewPager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ContentFragment extends BaseFragment {
	
	@ViewInject(R.id.vp_content_container)
	private HorizontalViewPager vp_content_container;
	
	@ViewInject(R.id.rg_content_bottom)
	private RadioGroup rg_content_bottom;
	
	private List<BasePager> pagers;
	@Override
	protected View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_content, null);
		// 注入xutils
		ViewUtils.inject(this,view);
		// 1、找当前类的@ViewInject成员变量
		// 2、findviewbyid 找到控件，给成员变量赋值
		return view;
	}
	
	@Override
	public void initData() {
		// 更新界面
		// 准备数据
		pagers = new ArrayList<BasePager>();
		pagers.add(new MoneyPager(mActivity));
		pagers.add(new InvestPager(mActivity));
		pagers.add(new FindPager(mActivity));
		pagers.add(new MyPager(mActivity));
	
		// 设置数据适配器
		vp_content_container.setAdapter(new MyAdapter());
		// 监听底部单选按钮
		rg_content_bottom.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
		// 设置默认选中首页按钮
		rg_content_bottom.check(R.id.rb_bottom_money);
		//  监听ViewPager
		vp_content_container.setOnPageChangeListener(new MyOnPageChangeListener());
		
		if (ConstantUtils.touziflag == 1) {
			rg_content_bottom.check(R.id.rb_bottom_safe);
			vp_content_container.setCurrentItem(1,false);
			ConstantUtils.touziflag = 0;
		}else if (ConstantUtils.touziflag == 2) {
			rg_content_bottom.check(R.id.rb_bottom_my);
			vp_content_container.setCurrentItem(3,false);
			ConstantUtils.touziflag = 0;
		}else {
			// 初始化首页界面的数据
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
	class MyOnCheckedChangeListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.rb_bottom_money:
				vp_content_container.setCurrentItem(0,false);// 参数2，是否需要滑动的效果
				//加这个判断是从我的界面进去点击选择加息票后跳到饭碗界面，没有使用，切换到其他界面，再切回到饭碗界面时，加息票的加息失效
				if(ConstantUtils.touziflag==0){
					FanFanApplication.jiaxi_rate = 0;
					FanFanApplication.jiaxi_ID = null;
				}
				break;
			case R.id.rb_bottom_safe:
				vp_content_container.setCurrentItem(1,false);
				break;
			case R.id.rb_bottom_find:
				vp_content_container.setCurrentItem(2,false);
				if(ConstantUtils.touziflag==0){
					FanFanApplication.jiaxi_rate = 0;
					FanFanApplication.jiaxi_ID = null;
				}
				break;
			case R.id.rb_bottom_my:
				vp_content_container.setCurrentItem(3,false);
				if(ConstantUtils.touziflag==0){
					FanFanApplication.jiaxi_rate = 0;
					FanFanApplication.jiaxi_ID = null;
				}
				break;

			default:
				break;
			}
		}
		
	}
	
	
	class MyAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return pagers.size();

		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			
			BasePager pager = pagers.get(position);
			container.addView(pager.rootView);
//			pager.initData(); 加载数据方法放在这，会导致提前调用，浪费流量
			return pager.rootView;

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		
	}

}
