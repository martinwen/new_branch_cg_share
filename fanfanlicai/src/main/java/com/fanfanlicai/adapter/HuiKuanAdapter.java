package com.fanfanlicai.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanfanlicai.bean.ShuHuiBean;
import com.fanfanlicai.fanfanlicai.R;

import java.util.ArrayList;

public class HuiKuanAdapter extends BaseAdapter {

	private ArrayList<ShuHuiBean> list;
	private Context mContext;
	public HuiKuanAdapter(Context context, ArrayList<ShuHuiBean> list) {
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
			convertView = View.inflate(mContext, R.layout.items_huikuan, null);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
			holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
			holder.iv_isover = (ImageView) convertView.findViewById(R.id.iv_isover);
			convertView.setTag(holder);
		}else {
			holder = (NoticeHolder) convertView.getTag();
		}

		ShuHuiBean shuHuiBean = list.get(position);
		holder.tv_time.setText(shuHuiBean.getTransTime());
		holder.tv_money.setText(shuHuiBean.getAmount()+"元");
		String status = shuHuiBean.getStatus();
		//0-赎回中；1-赎回成功
		if ("0".equals(status)) {
			holder.tv_state.setText("回款中");
			holder.tv_state.setTextColor(Color.parseColor("#F86402"));
			holder.iv_isover.setVisibility(View.INVISIBLE);
		}else if ("1".equals(status)) {
			holder.tv_state.setText("回款成功");
			holder.tv_state.setTextColor(Color.parseColor("#868686"));
			holder.iv_isover.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}

	class NoticeHolder{
		TextView tv_time;//赎回时间
		TextView tv_money;//赎回金额
		TextView tv_state;//赎回状态
		ImageView iv_isover;//是否已赎回
	}
}
