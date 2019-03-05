package com.fanfanlicai.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanfanlicai.bean.InviteDetailBean;
import com.fanfanlicai.fanfanlicai.R;

public class RewardDetailAdapter extends BaseAdapter {

	private ArrayList<InviteDetailBean> list;
	private Context mContext;

	public RewardDetailAdapter(Context context, ArrayList<InviteDetailBean> list) {
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
			convertView = View.inflate(mContext, R.layout.items_rewarddetail, null);
			holder.reward_number = (TextView) convertView.findViewById(R.id.reward_number);
			holder.reward_is = (TextView) convertView.findViewById(R.id.reward_is);
//			holder.reward_my = (TextView) convertView.findViewById(R.id.reward_my); 
			convertView.setTag(holder);
		}else {
			holder = (NoticeHolder) convertView.getTag();
		}
		InviteDetailBean inviteDetailBean = list.get(position);
		String phone = inviteDetailBean.getPhone();
		holder.reward_number.setText(fixNum(phone));
		holder.reward_is.setText(inviteDetailBean.getIsInvested());
//		holder.reward_my.setText(inviteDetailBean.getReward());
		return convertView;
	}

	class NoticeHolder{
		TextView reward_number;
		TextView reward_is;
//		TextView reward_my;
	}
	
	//手机号码星号处理
	private String fixNum(String phone) {
		return phone.replace(phone.substring(3,8), "*****");
		
	}
}
