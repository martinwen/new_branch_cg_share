package com.fanfanlicai.adapter.direct;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanfanlicai.bean.DirectBean;
import com.fanfanlicai.bean.FWBean;
import com.fanfanlicai.fanfanlicai.R;

import java.util.ArrayList;

/**
 * @author lijinliu
 * @date 20180129
 * 已投-饭碗记录 adapter
 */
public class DirectAdapter extends BaseAdapter {

    private ArrayList<FWBean> list;
    private Context mContext;

    public DirectAdapter(Context context, ArrayList<FWBean> list) {
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
            convertView = View.inflate(mContext, R.layout.items_direct_invest, null);
            holder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
            holder.tvInvestDeadLine = (TextView) convertView.findViewById(R.id.tv_invest_deadline);
            holder.tvInvestAmount = (TextView) convertView.findViewById(R.id.tv_invest_amount);
            holder.tvRecinterest = (TextView) convertView.findViewById(R.id.tv_recinterest);
            holder.tvDiYearRate = (TextView) convertView.findViewById(R.id.tv_di_yearrate);
            holder.tvInvestTime = (TextView) convertView.findViewById(R.id.tv_investtime);
            holder.tvEndTime = (TextView) convertView.findViewById(R.id.tv_endtime);
            convertView.setTag(holder);
        } else {
            holder = (NoticeHolder) convertView.getTag();
        }

        FWBean fwBean = list.get(position);
        holder.tvInvestDeadLine.setText(fwBean.getInvestDeadline() + "天");
        holder.tvInvestAmount.setText(fwBean.getInvestAmount());
        if ("1".equals(fwBean.getType())) {
            holder.tvRecinterest.setText(fwBean.getRecInterest());
        } else {
            holder.tvRecinterest.setText(fwBean.getProRepayInterest());
        }
        holder.tvDiYearRate.setText(fwBean.getYearRate().replace("%", ""));
        holder.tvInvestTime.setText(fwBean.getTransTime());
        //holder.tvInvestTime.setText(fwBean.getTransTimeStr());
        holder.tvEndTime.setText(fwBean.getEndTime());
        String status = fwBean.getStatus();
        //状态：1收益中、2收益中、3已还清、4回款中
        if ("3".equals(status)) {
            holder.tvStatus.setText("已结清");
            holder.tvStatus.setBackgroundResource(R.drawable.bg_directinvest_gray);
            holder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.text_gray));
            holder.tvInvestAmount.setTextColor(mContext.getResources().getColor(R.color.text_gray));
            holder.tvRecinterest.setTextColor(mContext.getResources().getColor(R.color.text_gray));
            holder.tvDiYearRate.setTextColor(mContext.getResources().getColor(R.color.text_gray));
            holder.tvEndTime.setTextColor(mContext.getResources().getColor(R.color.text_gray));
        } else if ("1".equals(status) || "2".equals(status)) {
            holder.tvStatus.setText("收益中");
            holder.tvStatus.setBackgroundResource(R.drawable.bg_directinvest_orange);
            holder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.text_lightorange));
            holder.tvInvestAmount.setTextColor(mContext.getResources().getColor(R.color.text_lightorange));
            holder.tvRecinterest.setTextColor(mContext.getResources().getColor(R.color.text_lightorange));
            holder.tvDiYearRate.setTextColor(mContext.getResources().getColor(R.color.text_lightorange));
            holder.tvEndTime.setTextColor(mContext.getResources().getColor(R.color.text_lightorange));
        }else {
            holder.tvStatus.setText("回款中");
            holder.tvStatus.setBackgroundResource(R.drawable.bg_directinvest_blue);
            holder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.text_blue));
            holder.tvInvestAmount.setTextColor(mContext.getResources().getColor(R.color.text_blue));
            holder.tvRecinterest.setTextColor(mContext.getResources().getColor(R.color.text_blue));
            holder.tvDiYearRate.setTextColor(mContext.getResources().getColor(R.color.text_blue));
            holder.tvEndTime.setTextColor(mContext.getResources().getColor(R.color.text_lightorange));
        }

        return convertView;
    }

    class NoticeHolder {
        TextView tvInvestDeadLine;
        TextView tvInvestAmount;
        TextView tvRecinterest;
        TextView tvDiYearRate;
        TextView tvInvestTime;
        TextView tvEndTime;
        TextView tvStatus;
    }
}
