package com.fanfanlicai.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
	protected Activity mActivity;// 抽取Activity变量，让子类创建布局时使用
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = getActivity();
		View view = initView();
		return view;
	}
	// Activity创建完成后，回调
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// 更新布局 
		initData();
	}
	/**
	 * 子类更新界面，不必须实现
	 */
	public void initData(){
		
	}
	/**
	 * 子类返回具体的控件、布局，必须实现
	 * @return
	 */
	protected abstract View initView();
}
