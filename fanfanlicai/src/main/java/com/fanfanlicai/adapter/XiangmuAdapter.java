package com.fanfanlicai.adapter;

import java.util.ArrayList;

import com.fanfanlicai.bean.FHInvestingBean;
import com.fanfanlicai.bean.InviteDetailBean;
import com.fanfanlicai.bean.NoticeBean;
import com.fanfanlicai.fanfanlicai.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class XiangmuAdapter extends BaseAdapter {

	private ArrayList<FHInvestingBean> list;
	private Context mContext;
	public XiangmuAdapter(Context context, ArrayList<FHInvestingBean> list) {
		super();
		this.mContext = context;
		this.list = list;
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
			convertView = View.inflate(mContext, R.layout.items_xiangmu, null);
			holder.xiangmu_name = (TextView) convertView.findViewById(R.id.xiangmu_name);
			holder.xiangmu_number = (TextView) convertView.findViewById(R.id.xiangmu_number); 
			holder.xiangmu_time = (TextView) convertView.findViewById(R.id.xiangmu_time); 
			convertView.setTag(holder);
		}else {
			holder = (NoticeHolder) convertView.getTag();
		}
		FHInvestingBean investingBean = list.get(position);
		holder.xiangmu_name.setText(investingBean.getBorrowName());
		holder.xiangmu_number.setText(investingBean.getBorrowMoney()+"元");
		holder.xiangmu_time.setText(investingBean.getMatchTime());
		
		return convertView;
	}

	class NoticeHolder{
		TextView xiangmu_name;
		TextView xiangmu_number;
		TextView xiangmu_time;
		
	}
}
