package com.fanfanlicai.adapter.jiaxipiao;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanfanlicai.bean.JiaXiBean;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.view.dialog.CustomProgressDialog;
import com.fanfanlicai.view.dialog.JiaXiPiaoDialog;

public class JiaXiPiaoFhUseableAdapter extends BaseAdapter {

	private ArrayList<JiaXiBean> list;
	private Context mContext;
	
	private CustomProgressDialog progressdialog;
	
	
	public JiaXiPiaoFhUseableAdapter(Context context,ArrayList<JiaXiBean> list) {
		super();
		this.mContext = context;
		this.list = list;
	}
	public JiaXiPiaoFhUseableAdapter(Context context) {
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
			convertView = View.inflate(mContext, R.layout.items_jiaxipiao_usable, null);
			holder.tv_jiaxi_name = (TextView) convertView.findViewById(R.id.tv_jiaxi_name);
			holder.tv_jiaxi_rate = (TextView) convertView.findViewById(R.id.tv_jiaxi_rate);
			holder.tv_jiaxi_day = (TextView) convertView.findViewById(R.id.tv_jiaxi_day);
			holder.tv_jiaxi_endtime = (TextView) convertView.findViewById(R.id.tv_jiaxi_endtime); 
			holder.tv_jiaxi_info = (TextView) convertView.findViewById(R.id.tv_jiaxi_info); 
			holder.tv_jiaxi_method = (TextView) convertView.findViewById(R.id.tv_jiaxi_method); 
			holder.iv_use = (ImageView) convertView.findViewById(R.id.iv_use); 
			convertView.setTag(holder);
		}else {
			holder = (NoticeHolder) convertView.getTag();
		}
		JiaXiBean jiaXiBean = list.get(position);
		//加息名称
		holder.tv_jiaxi_name.setText(jiaXiBean.getName());
		
		//加息利率
		if (jiaXiBean.getReward()==0) {
			holder.tv_jiaxi_rate.setVisibility(View.INVISIBLE);
		}else {
			holder.tv_jiaxi_rate.setVisibility(View.VISIBLE);
			holder.tv_jiaxi_rate.setText(jiaXiBean.getReward()+"%");
		}
		
		//加息天数	
		holder.tv_jiaxi_day.setText("加息天数："+jiaXiBean.getDays()+"天");
		
		//结束时间
		holder.tv_jiaxi_endtime.setText("失效时间："+jiaXiBean.getExpiredTime());
		
		//特别说明
		holder.tv_jiaxi_info.setText("特别说明："+jiaXiBean.getRemark());
		
		//获得来源
		holder.tv_jiaxi_method.setText("获得来源："+jiaXiBean.getSource());
		
		final String ticketId = jiaXiBean.getId();
		
		holder.iv_use.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//点击使用
				JiaXiPiaoDialog jiaXiPiaoDialog = new JiaXiPiaoDialog(mContext, R.style.YzmDialog,ticketId);
				jiaXiPiaoDialog.show();
			}
		});
		
		return convertView;
	}


	class NoticeHolder{
		TextView tv_jiaxi_name;
		TextView tv_jiaxi_rate;
		TextView tv_jiaxi_day;
		TextView tv_jiaxi_endtime;
		TextView tv_jiaxi_info;
		TextView tv_jiaxi_method;
		ImageView iv_use;
	}
}
