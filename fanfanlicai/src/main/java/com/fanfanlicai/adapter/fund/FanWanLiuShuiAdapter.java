package com.fanfanlicai.adapter.fund;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanfanlicai.bean.ShouYiBean;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.utils.MathUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


public class FanWanLiuShuiAdapter extends BaseAdapter {

    private ArrayList<ShouYiBean> list;
    private Context mContext;

    public FanWanLiuShuiAdapter(Context context, ArrayList<ShouYiBean> list) {
        super();
        this.mContext = context;
        this.list = list;
    }

    public FanWanLiuShuiAdapter(Context context) {
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
            convertView = View.inflate(mContext, R.layout.items_fanwanliushui, null);
            holder.iv_allmoney = (ImageView) convertView.findViewById(R.id.iv_allmoney);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            holder.rl_info = (RelativeLayout) convertView.findViewById(R.id.rl_info);

            holder.tv_detail1 = (TextView) convertView.findViewById(R.id.tv_detail1);
            holder.tv_detail2 = (TextView) convertView.findViewById(R.id.tv_detail2);
            holder.tv_detail3 = (TextView) convertView .findViewById(R.id.tv_detail3);
            holder.tv_detail4 = (TextView) convertView.findViewById(R.id.tv_detail4);
            holder.tv_detail5 = (TextView) convertView.findViewById(R.id.tv_detail5);
            holder.tv_detail6 = (TextView) convertView.findViewById(R.id.tv_detail6);
            holder.tv_detail7 = (TextView) convertView.findViewById(R.id.tv_detail7);
            convertView.setTag(holder);
        } else {
            holder = (NoticeHolder) convertView.getTag();
        }

        ShouYiBean shouYiBean = list.get(position);
//		301：买入饭碗
        if ("301".equals(shouYiBean.getTransType()) || "302".equals(shouYiBean.getTransType())) {
            holder.rl_info.setVisibility(View.VISIBLE);
            holder.tv_detail1.setText("现金");
            holder.tv_detail2.setText(MathUtil.subDouble(shouYiBean.getAmount() - shouYiBean.getFee() - shouYiBean.getUseRedbag(), 2) + "");
            holder.tv_detail3.setText("元   奖金");
            holder.tv_detail4.setText(MathUtil.subDouble(shouYiBean.getFee(), 2) + "");
            holder.tv_detail5.setText("元   红包");
            holder.tv_detail6.setText(MathUtil.subDouble(shouYiBean.getUseRedbag(), 2) + "");
            holder.tv_detail7.setText("元");
        } else {
            holder.rl_info.setVisibility(View.GONE);
        }

        //左侧小图片
        ImageLoader.getInstance().displayImage(shouYiBean.getIconUrl(), holder.iv_allmoney);
        //标题
        holder.tv_title.setText(shouYiBean.getTransName());
        //时间
        holder.tv_time.setText(shouYiBean.getTransTime());
        if (shouYiBean.getAmount() >= 0) {
            holder.tv_money.setText("+" + MathUtil.subDouble(shouYiBean.getAmount(), 2) + "元");
            holder.tv_money.setTextColor(Color.parseColor("#F86402"));
        } else {
            holder.tv_money.setText(MathUtil.subDouble(shouYiBean.getAmount(), 2) + "元");
            holder.tv_money.setTextColor(Color.parseColor("#139fe7"));
        }
        return convertView;
    }

    class NoticeHolder {
        ImageView iv_allmoney;
        TextView tv_title;
        TextView tv_time;
        TextView tv_money;
        RelativeLayout rl_info;

        TextView tv_detail1;
        TextView tv_detail2;
        TextView tv_detail3;
        TextView tv_detail4;
        TextView tv_detail5;
        TextView tv_detail6;
        TextView tv_detail7;

    }
}
