package com.fanfanlicai.adapter.hongbao;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanfanlicai.bean.HongBaoBean;
import com.fanfanlicai.fanfanlicai.R;

import java.util.ArrayList;

public class HongBaoUseableAdapter extends BaseAdapter {

	private ArrayList<HongBaoBean> list;
	private Context mContext;
	
	public HongBaoUseableAdapter(Context context, ArrayList<HongBaoBean> list) {
		super();
		this.mContext = context;
		this.list = list;
	}
	public HongBaoUseableAdapter(Context context) {
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
			convertView = View.inflate(mContext, R.layout.items_hongbao_usable, null);
			holder.tv_hongbao_name = (TextView) convertView.findViewById(R.id.tv_hongbao_name);
			holder.tv_hongbao_usetime = (TextView) convertView.findViewById(R.id.tv_hongbao_usetime);
			holder.tv_invest_money = (TextView) convertView.findViewById(R.id.tv_invest_money);
			holder.tv_hongbao_info = (TextView) convertView.findViewById(R.id.tv_hongbao_info);
			holder.tv_hongbao_method = (TextView) convertView.findViewById(R.id.tv_hongbao_method);
			holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
			convertView.setTag(holder);
		}else {
			holder = (NoticeHolder) convertView.getTag();
		}
		HongBaoBean hongBaoBean = list.get(position);
		//红包名称
		holder.tv_hongbao_name.setText(hongBaoBean.getName());
		
		//红包有效期
		//以空格截取字符串
		String splitCreateTime[] = hongBaoBean.getCreateTime().split(" ");
		String createTime = splitCreateTime[0];

		String splitExpiredTime[] = hongBaoBean.getExpiredTime().split(" ");
		String expiredTime = splitExpiredTime[0];

		holder.tv_hongbao_usetime.setText("有效期："+createTime+"至"+expiredTime);
		
		//起投金额
		holder.tv_invest_money.setText("最低出借金额："+hongBaoBean.getUseCondition()+"元");
		
		//适用产品
		holder.tv_hongbao_info.setText("适用产品："+hongBaoBean.getProductName());
		
		//获得来源
		holder.tv_hongbao_method.setText("获得来源："+hongBaoBean.getFromSource());

		//红包金额
		holder.tv_money.setText(hongBaoBean.getReward()+"元");
		
		return convertView;
	}


	class NoticeHolder{
		TextView tv_hongbao_name;
		TextView tv_hongbao_usetime;
		TextView tv_invest_money;
		TextView tv_hongbao_info;
		TextView tv_hongbao_method;
		TextView tv_money;
	}
}
