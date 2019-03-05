package com.fanfanlicai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.utils.CacheUtils;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity implements OnClickListener {
	private ViewPager vp_guide_bg;
	private Button bt_guide_start;
	private List<ImageView> imgs = new ArrayList<ImageView>();//保存引导页图片的集合

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);

		initView();
		initData();
	}

	private void initView() {
		vp_guide_bg = (ViewPager) findViewById(R.id.vp_guide_bg);
		bt_guide_start = (Button) findViewById(R.id.bt_guide_start);
		bt_guide_start.setOnClickListener(this);
	}

	class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {

		}

		@Override
		public void onPageSelected(int position) {
			// 当选中最后一页时，把button显示出来
			if (position == imgs.size() - 1) {
				bt_guide_start.setVisibility(View.VISIBLE);
			} else {
				bt_guide_start.setVisibility(View.GONE);
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}

	}

	private void initData() {
		int[] imgId = new int[] { R.drawable.guide_1, R.drawable.guide_2,
				R.drawable.guide_3, R.drawable.guide_4 };
		
		for (int i = 0; i < imgId.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(imgId[i]);
			imgs.add(imageView);
		}
		// 设置Adapter
		vp_guide_bg.setAdapter(new MyAdapter());
		// 监听ViewPager
		vp_guide_bg.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imgs.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = imgs.get(position);
			container.addView(imageView);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	@Override
	public void onClick(View v) {
		// 跳到主界面
		startActivity(new Intent(this, MainActivity.class));
		// 把已经打开过应用的boolean值存起来
		CacheUtils.putBoolean(this, EnterActivity.IS_APP_FIRST_OPEN, false);
		finish();
	}
}
