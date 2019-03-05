package com.fanfanlicai.adapter.jiaxipiao;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanfanlicai.activity.MainActivity;
import com.fanfanlicai.bean.JiaXiBean;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.global.FanFanApplication;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.view.dialog.FanJuDialog;

import java.util.ArrayList;

public class JiaXiPiaoFwUseableAdapter extends BaseAdapter {

	private ArrayList<JiaXiBean> list;
	private Context mContext;
	public JiaXiPiaoFwUseableAdapter(Context context,ArrayList<JiaXiBean> list) {
		super();
		this.mContext = context;
		this.list = list;
	}
	public JiaXiPiaoFwUseableAdapter(Context context) {
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
		
		//结束时间
		holder.tv_jiaxi_endtime.setText("失效时间："+jiaXiBean.getExpiredTime());
		
		//获得来源
		holder.tv_jiaxi_method.setText("获得来源："+jiaXiBean.getSource());
		
		holder.iv_use.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FanFanApplication.jiaxi_rate = jiaxi_rate;
				FanFanApplication.jiaxi_ID = id;
				if("1".equals(jiaXiBean.getUpgrade())){//不可升级:1，可升级:2
					ConstantUtils.touziflag = 1;
					ConstantUtils.fanheorfanwanorfantongflag = 1;
					Intent intent = new Intent(mContext, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					mContext.startActivity(intent);
				}else if("2".equals(jiaXiBean.getUpgrade())){
					FanJuDialog fanJuDialog = new FanJuDialog(mContext, R.style.YzmDialog);
					fanJuDialog.show();
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
