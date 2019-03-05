package com.fanfanlicai.adapter;

import java.util.ArrayList;

import com.fanfanlicai.bean.FHInvestingBean;
import com.fanfanlicai.bean.FWBean;
import com.fanfanlicai.bean.InviteDetailBean;
import com.fanfanlicai.bean.NoticeBean;
import com.fanfanlicai.fanfanlicai.R;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FanWanAdapter extends BaseAdapter {

	private ArrayList<FWBean> list;
	private Context mContext;
	public FanWanAdapter(Context context, ArrayList<FWBean> list) {
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
			convertView = View.inflate(mContext, R.layout.items_fanwan, null);
			holder.tv_in_value = (TextView) convertView.findViewById(R.id.tv_in_value);
			holder.tv_rate_value = (TextView) convertView.findViewById(R.id.tv_rate_value); 
			holder.tv_invest_value = (TextView) convertView.findViewById(R.id.tv_invest_value); 
			holder.tv_out_value = (TextView) convertView.findViewById(R.id.tv_out_value); 
			holder.tv_day_value = (TextView) convertView.findViewById(R.id.tv_day_value); 
			holder.tv_over = (TextView) convertView.findViewById(R.id.tv_over); 
			holder.tv_over_value = (TextView) convertView.findViewById(R.id.tv_over_value); 
			holder.iv_isover = (ImageView) convertView.findViewById(R.id.iv_isover); 
			holder.iv_arrow = (ImageView) convertView.findViewById(R.id.iv_arrow); 
			convertView.setTag(holder);
		}else {
			holder = (NoticeHolder) convertView.getTag();
		}
		
		FWBean fwBean = list.get(position);
		holder.tv_in_value.setText(fwBean.getTransTime());//买入时间
		holder.tv_rate_value.setText(fwBean.getYearRate());//预期年化率
		holder.tv_invest_value.setText(fwBean.getInvestAmount()+"元");//投资金额
		holder.tv_out_value.setText(fwBean.getEndTime());//到期时间
		holder.tv_day_value.setText(fwBean.getInvestDeadline()+"天");//投资期限
		holder.tv_over_value.setText(fwBean.getWaitRepayAmount()+"元");//待收本息
		String status = fwBean.getStatus();
		//1：还款中；3：已完成
		if ("1".equals(status)) {
			holder.iv_arrow.setVisibility(View.VISIBLE);
			holder.iv_isover.setVisibility(View.INVISIBLE);
			holder.tv_over.setText("待收本息");
		}else if ("3".equals(status)) {
			holder.iv_arrow.setVisibility(View.INVISIBLE);
			holder.iv_isover.setVisibility(View.VISIBLE);
			holder.iv_isover.setImageResource(R.drawable.yijieqing);
			holder.tv_over.setText("已结本息");
		}else if ("4".equals(status)) {
			holder.iv_arrow.setVisibility(View.INVISIBLE);
			holder.iv_isover.setVisibility(View.VISIBLE);
			holder.iv_isover.setImageResource(R.drawable.huikuanzhong);
			holder.tv_over.setText("待收本息");
		}
		
		return convertView;
	}

	class NoticeHolder{
		TextView tv_in_value;//买入时间
		TextView tv_rate_value;//预期年化率
		TextView tv_invest_value;//投资金额
		TextView tv_out_value;//到期时间
		TextView tv_day_value;//投资期限
		TextView tv_over;//待收本息
		TextView tv_over_value;//待收本息
		ImageView iv_isover;//是否已结清
		ImageView iv_arrow;//跳转箭头
	}
}
