package com.fanfanlicai.adapter.hongbao;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanfanlicai.bean.HongBaoBean;
import com.fanfanlicai.fanfanlicai.R;

import java.util.ArrayList;

public class HongBaoUnuseableAdapter extends BaseAdapter {

	private ArrayList<HongBaoBean> list;
	private Context mContext;

	public HongBaoUnuseableAdapter(Context context, ArrayList<HongBaoBean> list) {
		super();
		this.mContext = context;
		this.list = list;
	}
	public HongBaoUnuseableAdapter(Context context) {
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
			convertView = View.inflate(mContext, R.layout.items_hongbao_unusable, null);
			holder.tv_hongbao_name = (TextView) convertView.findViewById(R.id.tv_hongbao_name);
			holder.tv_hongbao_usetime = (TextView) convertView.findViewById(R.id.tv_hongbao_usetime);
			holder.tv_invest_money = (TextView) convertView.findViewById(R.id.tv_invest_money);
			holder.tv_hongbao_info = (TextView) convertView.findViewById(R.id.tv_hongbao_info);
			holder.tv_hongbao_method = (TextView) convertView.findViewById(R.id.tv_hongbao_method);
			holder.iv_unused = (ImageView) convertView.findViewById(R.id.iv_unused);
			convertView.setTag(holder);
		}else {
			holder = (NoticeHolder) convertView.getTag();
		}
		HongBaoBean hongBaoBean = list.get(position);
		//红包名称+红包金额
		holder.tv_hongbao_name.setText(hongBaoBean.getName()+hongBaoBean.getReward()+"元");

		//起投金额
		holder.tv_invest_money.setText("最低出借金额："+hongBaoBean.getUseCondition()+"元");

		//适用产品
		holder.tv_hongbao_info.setText("适用产品："+hongBaoBean.getProductName());

		//获得来源
		holder.tv_hongbao_method.setText("获得来源："+hongBaoBean.getFromSource());

		//结束时间   2已使用、3已过期
		if ("2".equals(hongBaoBean.getStatus())) {
			//红包使用时间
			holder.tv_hongbao_usetime.setText("使用时间："+hongBaoBean.getUseTime());
			holder.iv_unused.setImageResource(R.drawable.used);
		}else if ("3".equals(hongBaoBean.getStatus())) {
			//红包使用时间
			holder.tv_hongbao_usetime.setText("失效时间："+hongBaoBean.getExpiredTime());
			holder.iv_unused.setImageResource(R.drawable.passed);
		}
		
		return convertView;
	}


	class NoticeHolder{
		TextView tv_hongbao_name;
		TextView tv_hongbao_usetime;
		TextView tv_invest_money;
		TextView tv_hongbao_info;
		TextView tv_hongbao_method;
		ImageView iv_unused;
	}
}
