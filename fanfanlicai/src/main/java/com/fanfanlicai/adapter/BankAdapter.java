package com.fanfanlicai.adapter;

import java.util.ArrayList;

import com.fanfanlicai.bean.BankListBean;
import com.fanfanlicai.bean.NoticeBean;
import com.fanfanlicai.fanfanlicai.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BankAdapter extends BaseAdapter {

	private ArrayList<BankListBean> list;
	private Context mContext;
	public BankAdapter(Context context,ArrayList<BankListBean> list) {
		super();
		this.mContext = context;
		this.list = list;
	}
	public BankAdapter(Context context) {
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
			convertView = View.inflate(mContext, R.layout.items_bank, null);
			holder.iv_banklogo = (ImageView) convertView.findViewById(R.id.iv_banklogo);
			holder.tv_bank = (TextView) convertView.findViewById(R.id.tv_bank);
			convertView.setTag(holder);
		}else {
			holder = (NoticeHolder) convertView.getTag();
		}
		BankListBean bankListBean = list.get(position);
		ImageLoader.getInstance().displayImage(bankListBean.bankPic, holder.iv_banklogo);
		holder.tv_bank.setText(bankListBean.bankName);
		return convertView;
	}

	class NoticeHolder{
		ImageView iv_banklogo;
		TextView tv_bank;
	}
}
