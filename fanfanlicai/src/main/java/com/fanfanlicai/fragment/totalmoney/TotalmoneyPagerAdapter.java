package com.fanfanlicai.fragment.totalmoney;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TotalmoneyPagerAdapter extends FragmentPagerAdapter {

	public List<Fragment> fragments;
	public TotalmoneyPagerAdapter(FragmentManager fm,List<Fragment> list) {
		super(fm);
		this.fragments = list;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragments.size();
	}

}
