package com.fanfanlicai.pagers.jiaxipiao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.pagers.BasePager;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.view.HorizontalViewPager;

public class JiaXiPiaoFhPager extends BasePager implements OnClickListener{

	private List<BasePager> pagers;
	private HorizontalViewPager viewpager;// 页签详情
	private RelativeLayout rl_usable,rl_using,rl_unused;
	private int pageIndex = 0;
	private String code;
	
	public JiaXiPiaoFhPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}


	
	@Override
	protected View initView() {
		View view = View.inflate(mContext, R.layout.pager_jiaxipiaofh, null);
		rl_usable = (RelativeLayout) view.findViewById(R.id.rl_usable);
		rl_usable.setOnClickListener(this);
		rl_using = (RelativeLayout) view.findViewById(R.id.rl_using);
		rl_using.setOnClickListener(this);
		rl_unused = (RelativeLayout) view.findViewById(R.id.rl_unused);
		rl_unused.setOnClickListener(this);
		viewpager = (HorizontalViewPager) view.findViewById(R.id.pager);
		return view;
	}

	@Override
	public void initData() {
		// 准备数据
		pagers = new ArrayList<BasePager>();
		
		FHuseablePager fHuseablePager = new FHuseablePager(mContext);
		fHuseablePager.setCode(code);
		pagers.add(fHuseablePager);
		
		FHusingPager fHusingPager = new FHusingPager(mContext);
		fHusingPager.setCode(code);
		pagers.add(fHusingPager);
		
		FHunusedPager fHunusedPager = new FHunusedPager(mContext);
		fHunusedPager.setCode(code);
		pagers.add(fHunusedPager);
		
		// 给ViewPager设置数据
		viewpager.setAdapter(new MyAdapter());
		viewpager.setOnPageChangeListener(new MyOnPageChangeListener());
		

		// 初始化首页界面的数据
		if (pageIndex==1) {
			viewpager.setCurrentItem(1,false);
			rl_using.setBackgroundResource(R.drawable.fh_jxp_bg_check);
			rl_usable.setBackgroundResource(R.drawable.fh_jxp_bg_uncheck);
			rl_unused.setBackgroundResource(R.drawable.fh_jxp_bg_uncheck);
		}else if (pageIndex==2) {
			viewpager.setCurrentItem(2,false);
			rl_unused.setBackgroundResource(R.drawable.fh_jxp_bg_check);
			rl_usable.setBackgroundResource(R.drawable.fh_jxp_bg_uncheck);
			rl_using.setBackgroundResource(R.drawable.fh_jxp_bg_uncheck);
		}else {
			pagers.get(0).initData();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_usable:
			pageIndex = 0;
			viewpager.setCurrentItem(0,false);
			rl_usable.setBackgroundResource(R.drawable.fh_jxp_bg_check);
			rl_using.setBackgroundResource(R.drawable.fh_jxp_bg_uncheck);
			rl_unused.setBackgroundResource(R.drawable.fh_jxp_bg_uncheck);
			break;
		case R.id.rl_using:
			pageIndex = 1;
			viewpager.setCurrentItem(1,false);
			rl_using.setBackgroundResource(R.drawable.fh_jxp_bg_check);
			rl_usable.setBackgroundResource(R.drawable.fh_jxp_bg_uncheck);
			rl_unused.setBackgroundResource(R.drawable.fh_jxp_bg_uncheck);
			break;
		case R.id.rl_unused:
			pageIndex = 2;
			viewpager.setCurrentItem(2,false);
			rl_unused.setBackgroundResource(R.drawable.fh_jxp_bg_check);
			rl_usable.setBackgroundResource(R.drawable.fh_jxp_bg_uncheck);
			rl_using.setBackgroundResource(R.drawable.fh_jxp_bg_uncheck);
			break;

		default:
			break;
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
		public int getCount() {
			return pagers.size();
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


	public void setCode(String code) {
		this.code = code;
	}

}
