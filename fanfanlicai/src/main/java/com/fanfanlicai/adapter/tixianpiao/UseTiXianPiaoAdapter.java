package com.fanfanlicai.adapter.tixianpiao;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanfanlicai.bean.TiXianBean;
import com.fanfanlicai.fanfanlicai.R;

public class UseTiXianPiaoAdapter extends BaseAdapter {

	private ArrayList<TiXianBean> list;
	private Context mContext;
	private String ticketId;
	
	public UseTiXianPiaoAdapter(Context context,ArrayList<TiXianBean> list, String ticketId) {
		super();
		this.mContext = context;
		this.list = list;
		this.ticketId = ticketId;
	}
	public UseTiXianPiaoAdapter(Context context) {
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
			convertView = View.inflate(mContext, R.layout.items_tixianpiao_unusable, null);
			holder.tv_jiaxi_name = (TextView) convertView.findViewById(R.id.tv_jiaxi_name);
			holder.tv_jiaxi_rate = (TextView) convertView.findViewById(R.id.tv_jiaxi_rate);
			holder.tv_jiaxi_endtime = (TextView) convertView.findViewById(R.id.tv_jiaxi_endtime); 
			holder.tv_jiaxi_info = (TextView) convertView.findViewById(R.id.tv_jiaxi_info); 
			holder.tv_jiaxi_method = (TextView) convertView.findViewById(R.id.tv_jiaxi_method); 
			holder.iv_use = (ImageView) convertView.findViewById(R.id.iv_use); 
			convertView.setTag(holder);
		}else {
			holder = (NoticeHolder) convertView.getTag();
		}
		TiXianBean tiXianBean = list.get(position);
		//提现名称
		holder.tv_jiaxi_name.setText(tiXianBean.getName());
		
		//提现票值
		holder.tv_jiaxi_rate.setText(tiXianBean.getAmount()+"元");
		
		//特别说明
		holder.tv_jiaxi_info.setText(tiXianBean.getSpecialDesc());
		
		//获得来源
		holder.tv_jiaxi_method.setText("获得来源："+tiXianBean.getFromSource());
		
		//加息券ID
		final String id = tiXianBean.getId();

		//结束时间 1可使用   "2"使用时间     "3"已过期（失效）
		if ("1".equals(tiXianBean.getStatus())) {
			holder.tv_jiaxi_endtime.setText("失效时间："+tiXianBean.getExpireTime());
			if (id.equals(ticketId)) {
				holder.iv_use.setImageResource(R.drawable.tixianpiao_chose);
			}else {
				holder.iv_use.setImageResource(R.drawable.clickuse);
			}
		}
		
		return convertView;
	}


	class NoticeHolder{
		TextView tv_jiaxi_name;
		TextView tv_jiaxi_rate;
		
		TextView tv_jiaxi_endtime;
		TextView tv_jiaxi_info;
		TextView tv_jiaxi_method;
		ImageView iv_use;
	}
}
