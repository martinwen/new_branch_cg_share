package com.fanfanlicai.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.fanfanlicai.bean.FindBean;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.global.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FindAdapter extends BaseAdapter {

	private ArrayList<FindBean> list;
	private Context mContext;
	public FindAdapter(Context context,ArrayList<FindBean> list) {
		super();
		this.mContext = context;
		this.list = list;
	}
	public FindAdapter(Context context) {
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
		GuangGaoHolder holder = null;
		if (convertView == null) {
			holder = new GuangGaoHolder();
			convertView = View.inflate(mContext, R.layout.items_image, null);
			holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
			holder.iv_over = (ImageView) convertView.findViewById(R.id.iv_over);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			convertView.setTag(holder);
		}else {
			holder = (GuangGaoHolder) convertView.getTag();
		}
		FindBean findBean = list.get(position);
		String url = findBean.getImgSrc();
		String title = findBean.getTitle();
		String status = findBean.getStatus();
		
		if ("1".equals(status)) {
			holder.iv_over.setVisibility(View.INVISIBLE);
		}else if ("3".equals(status)) {
			holder.iv_over.setVisibility(View.VISIBLE);
		}
		holder.tv_title.setText(title);
		holder.iv_image.setScaleType(ScaleType.FIT_XY);
		//加载图片
		ImageLoader.getInstance().displayImage(url,holder.iv_image,ImageLoaderOptions.fadein_options);
		return convertView;
	}

	class GuangGaoHolder{
		ImageView iv_image;
		ImageView iv_over;
		TextView tv_title;
	}
}
