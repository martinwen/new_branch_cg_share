package com.fanfanlicai.pagers;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.fanfanlicai.fanfanlicai.R;



public class BasePager {
	
	protected Context mContext;// 子类使用的Context，用来创建布局
	public View rootView;
	protected FrameLayout fl_basepager_content;

	public BasePager(Context context){
		this.mContext = context;
		rootView = initView();
	}
	/**
	 * 子类创建布局，必须实现
	 * @return
	 */
	protected  View initView(){
		View view = View.inflate(mContext, R.layout.pager_base, null);		
		fl_basepager_content = (FrameLayout) view.findViewById(R.id.fl_basepager_content);
		return view;
	}
	
	/**
	 * 子类更新界面，不必须实现
	 */
	public void initData(){
		
	}
}
