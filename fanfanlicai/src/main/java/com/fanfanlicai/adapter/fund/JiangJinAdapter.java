package com.fanfanlicai.adapter.fund;

import java.util.ArrayList;

import com.fanfanlicai.bean.NoticeBean;
import com.fanfanlicai.bean.ShouYiBean;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.utils.MathUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class JiangJinAdapter extends BaseAdapter {

	private ArrayList<ShouYiBean> list;
	private Context mContext;
	public JiangJinAdapter(Context context,ArrayList<ShouYiBean> list) {
		super();
		this.mContext = context;
		this.list = list;
	}
	public JiangJinAdapter(Context context) {
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
//		401：邀请奖金，451：使用奖金
		//左侧小图片
		ImageLoader.getInstance().displayImage(shouYiBean.getIconUrl(), holder.iv_allmoney);
		//标题
		holder.tv_title.setText(shouYiBean.getTransName());
		//时间
		holder.tv_time.setText(shouYiBean.getTransTime());
		if (shouYiBean.getAmount()>=0) {
			holder.tv_money.setText("+" + MathUtil.subDouble(shouYiBean.getAmount(), 2) + "元");
			holder.tv_money.setTextColor(Color.parseColor("#F86402"));
		}else {
			holder.tv_money.setText(MathUtil.subDouble(shouYiBean.getAmount(), 2) + "元");
			holder.tv_money.setTextColor(Color.parseColor("#139fe7"));
		}
		
		return convertView;
	}

	class NoticeHolder{
		ImageView iv_allmoney;
		TextView tv_title;
		TextView tv_time;
		TextView tv_money;
		
	}
}
