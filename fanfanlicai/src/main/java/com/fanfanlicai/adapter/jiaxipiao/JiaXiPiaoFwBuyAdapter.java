package com.fanfanlicai.adapter.jiaxipiao;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanfanlicai.bean.JiaXiBean;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.global.FanFanApplication;
import com.fanfanlicai.view.dialog.FanJuFwBuyDialog;

import java.util.ArrayList;

public class JiaXiPiaoFwBuyAdapter extends BaseAdapter {

	private ArrayList<JiaXiBean> list;
	private Context mContext;
	public JiaXiPiaoFwBuyAdapter(Context context,ArrayList<JiaXiBean> list) {
		super();
		this.mContext = context;
		this.list = list;
	}
	public JiaXiPiaoFwBuyAdapter(Context context) {
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
			convertView = View.inflate(mContext, R.layout.items_jiaxipiao_fwusable, null);
			holder.tv_jiaxi_name = (TextView) convertView.findViewById(R.id.tv_jiaxi_name);
			holder.tv_jiaxi_rate = (TextView) convertView.findViewById(R.id.tv_jiaxi_rate);
			holder.tv_jiaxi_endtime = (TextView) convertView.findViewById(R.id.tv_jiaxi_endtime); 
			holder.tv_jiaxi_method = (TextView) convertView.findViewById(R.id.tv_jiaxi_method); 
			holder.iv_use = (ImageView) convertView.findViewById(R.id.iv_use); 
			convertView.setTag(holder);
		}else {
			holder = (NoticeHolder) convertView.getTag();
		}
		final JiaXiBean jiaXiBean = list.get(position);
		//加息名称
		holder.tv_jiaxi_name.setText(jiaXiBean.getName());
		
		//加息利率
		final double jiaxi_rate = jiaXiBean.getReward();
		holder.tv_jiaxi_rate.setText(jiaxi_rate+"%");
		
		//加息券ID
		final String id = jiaXiBean.getId();
		if (id.equals(FanFanApplication.jiaxi_ID)) {
			holder.iv_use.setImageResource(R.drawable.jiaxi_chose);
		}else {
			holder.iv_use.setImageResource(R.drawable.clickuse);
		}
		
		//结束时间
		holder.tv_jiaxi_endtime.setText("失效时间："+jiaXiBean.getExpiredTime());
		
		//获得来源
		holder.tv_jiaxi_method.setText("获得来源："+jiaXiBean.getSource());
		
		//点击使用
		holder.iv_use.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ("1".equals(jiaXiBean.getUpgrade())) {//不可升级:1，可升级:2
					//点击使用
					FanFanApplication.jiaxi_rate = jiaxi_rate;
					FanFanApplication.jiaxi_ID = id;
					Activity activity = (Activity) mContext;
					activity.finish();
				} else if ("2".equals(jiaXiBean.getUpgrade())) {
					FanJuFwBuyDialog fanJuDialog = new FanJuFwBuyDialog(mContext, R.style.YzmDialog);
					fanJuDialog.show();
					fanJuDialog.setOnFanJuFwBuyDialogDismissListener(new FanJuFwBuyDialog.OnFanJuFwBuyDialogDismissListener() {
						@Override
						public void OnFanJuFwBuyDialogDismiss() {
							FanFanApplication.jiaxi_rate = jiaxi_rate;
							FanFanApplication.jiaxi_ID = id;
							Activity activity = (Activity) mContext;
							activity.finish();
						}
					});
				}
			}
		});
		
		return convertView;
	}

	class NoticeHolder{
		TextView tv_jiaxi_name;
		TextView tv_jiaxi_rate;
		TextView tv_jiaxi_endtime;
		TextView tv_jiaxi_method;
		ImageView iv_use;
	}
}
