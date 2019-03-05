package com.fanfanlicai.adapter;

import java.util.ArrayList;

import com.fanfanlicai.bean.NoticeBean;
import com.fanfanlicai.bean.ShouYiBean;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.utils.MathUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AllMoneyAdapter extends BaseAdapter {

	private ArrayList<ShouYiBean> list;
	private Context mContext;
	public AllMoneyAdapter(Context context,ArrayList<ShouYiBean> list) {
		super();
		this.mContext = context;
		this.list = list;
	}
	public AllMoneyAdapter(Context context) {
		super();
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NoticeHolder holder = null;
		if (convertView == null) {
			holder = new NoticeHolder();
			convertView = View.inflate(mContext, R.layout.items_allmoney, null);
			holder.iv_allmoney = (ImageView) convertView.findViewById(R.id.iv_allmoney);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title); 
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time); 
			holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money); 
			convertView.setTag(holder);
		}else {
			holder = (NoticeHolder) convertView.getTag();
		}
		ShouYiBean shouYiBean = list.get(position);
//		202：饭盒基本收益，203：饭盒活动加息收益
		//左侧小图片
		ImageLoader.getInstance().displayImage(shouYiBean.getIconUrl(), holder.iv_allmoney);
		//标题	
		holder.tv_title.setText(shouYiBean.getTransName());
		//时间
		holder.tv_time.setText(shouYiBean.getTransTime());
		
		holder.tv_money.setText("+"+MathUtil.subDouble(shouYiBean.getAmount(), 2)+"元");
		
		return convertView;
	}

	class NoticeHolder{
		ImageView iv_allmoney;
		TextView tv_title;
		TextView tv_time;
		TextView tv_money;
		
	}
}
