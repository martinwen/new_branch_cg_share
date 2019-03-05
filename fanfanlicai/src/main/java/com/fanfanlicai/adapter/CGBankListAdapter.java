package com.fanfanlicai.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanfanlicai.bean.BankListBean;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.utils.MathUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class CGBankListAdapter extends BaseAdapter {

	private ArrayList<BankListBean> list;
	private Context mContext;
	public CGBankListAdapter(Context context, ArrayList<BankListBean> list) {
		super();
		this.mContext = context;
		this.list = list;
	}
	public CGBankListAdapter(Context context) {
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
			convertView = View.inflate(mContext, R.layout.items_cgbanklist, null);
			holder.iv_banklogo = (ImageView) convertView.findViewById(R.id.iv_banklogo);
			holder.tv_bank = (TextView) convertView.findViewById(R.id.tv_bank);
			holder.tv_info = (TextView) convertView.findViewById(R.id.tv_info);
			holder.iv_choose = (ImageView) convertView.findViewById(R.id.iv_choose);
			convertView.setTag(holder);
		}else {
			holder = (NoticeHolder) convertView.getTag();
		}
		BankListBean bankListBean = list.get(position);
		double authPosSingleLimit = bankListBean.authPosSingleLimit;
		String a = MathUtil.subDouble(authPosSingleLimit / 10000, 2)+"";
		double authPosDailyLimit = bankListBean.authPosDailyLimit;
		String b = MathUtil.subDouble(authPosDailyLimit / 10000, 2)+"";

		if(authPosDailyLimit>0){
			holder.tv_info.setText("单笔"+a+"万，"+"单日"+b+"万");

		}else {
			holder.tv_info.setText("单笔"+a+"万，"+"单日不限");

		}

		ImageLoader.getInstance().displayImage(bankListBean.bankPic, holder.iv_banklogo);
		holder.tv_bank.setText(bankListBean.bankName);
		return convertView;
	}

	class NoticeHolder{
		ImageView iv_banklogo;
		TextView tv_bank;
		TextView tv_info;
		ImageView iv_choose;
	}
}
