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

public class BankListAdapter extends BaseAdapter {

	private ArrayList<BankListBean> list;
	private Context mContext;
	public BankListAdapter(Context context, ArrayList<BankListBean> list) {
		super();
		this.mContext = context;
		this.list = list;
	}
	public BankListAdapter(Context context) {
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
			convertView = View.inflate(mContext, R.layout.items_banklist, null);
			holder.iv_banklogo = (ImageView) convertView.findViewById(R.id.iv_banklogo);
			holder.tv_bank = (TextView) convertView.findViewById(R.id.tv_bank);
			holder.tv_kuaijie_danbi = (TextView) convertView.findViewById(R.id.tv_kuaijie_danbi);
			holder.tv_kuaijie_danri = (TextView) convertView.findViewById(R.id.tv_kuaijie_danri);
			holder.tv_wangyin_danbi = (TextView) convertView.findViewById(R.id.tv_wangyin_danbi);
			holder.tv_wangyin_danri = (TextView) convertView.findViewById(R.id.tv_wangyin_danri);
			convertView.setTag(holder);
		}else {
			holder = (NoticeHolder) convertView.getTag();
		}
		BankListBean bankListBean = list.get(position);
		ImageLoader.getInstance().displayImage(bankListBean.bankPic, holder.iv_banklogo);
		holder.tv_bank.setText(bankListBean.bankName);
		holder.tv_kuaijie_danbi.setText("单笔"+MathUtil.subDouble(bankListBean.quickSingleLimit/10000,2)+"万");
		holder.tv_kuaijie_danri.setText("单日"+MathUtil.subDouble(bankListBean.quickDailyLimit/10000,2)+"万");
		holder.tv_wangyin_danbi.setText("单笔"+MathUtil.subDouble(bankListBean.webSingleLimit/10000,2)+"万");
		holder.tv_wangyin_danri.setText("单日"+MathUtil.subDouble(bankListBean.webDailyLimit/10000,2)+"万");
		return convertView;
	}

	class NoticeHolder{
		ImageView iv_banklogo;
		TextView tv_bank;
		TextView tv_kuaijie_danbi;
		TextView tv_kuaijie_danri;
		TextView tv_wangyin_danbi;
		TextView tv_wangyin_danri;
	}
}
