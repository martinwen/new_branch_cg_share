package com.fanfanlicai.adapter.direct;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.fanfanlicai.bean.BidBean;
import com.fanfanlicai.bean.FloatRateBean;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.global.FanFanApplication;
import com.fanfanlicai.utils.MathUtil;

import java.util.ArrayList;

/**
 * @author lijinliu
 * @date 20180202
 * 标的adapter
 */
public class BidAdapter extends BaseAdapter {

    public static final int TYPE_TOPINFO = 0;
    public static final int TYPE_BOTTOMITEM = 1;

    private ArrayList<BidBean> list = new ArrayList<BidBean>();
    private Context mContext;

    private String investDeadline = "";
    private String investTime = "";
    private String endTime = "";
    private String mStatus = "";
    private String unClearCapital = "";
    private String unClearIncome = "";
    private String clearCapital = "";
    private String clearIncome = "";
    //1老饭碗 ，2直投
    private String mFwType;

    public BidAdapter(Context context) {
        super();
        this.mContext = context;
    }

    public void setData(ArrayList<BidBean> list,String fwType, String investDeadline, String investTime, String endTime, String status, String unClearCapital, String unClearIncome, String clearCapital, String clearIncome) {
        this.list = list;
        this.mFwType = fwType;
        this.investDeadline = investDeadline;
        this.investTime = investTime;
        this.endTime = endTime;
        this.mStatus = status;
        this.unClearCapital = unClearCapital;
        this.unClearIncome = unClearIncome;
        this.clearCapital = clearCapital;
        this.clearIncome = clearIncome;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position > 0) {
            return list.get(position - 1);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_TOPINFO;
        } else {
            return TYPE_BOTTOMITEM;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TopHolder topHolder = null;
        NoticeHolder holder = null;
        int type = getItemViewType(position);
        switch (type) {
            case TYPE_TOPINFO:
                if (convertView == null) {
                    topHolder = new TopHolder();
                    convertView = View.inflate(mContext, R.layout.item_directinvest_detail, null);
                    topHolder.ivStatus= (ImageView) convertView.findViewById(R.id.iv_status);
                    topHolder.tvInvestDeadLine = (TextView) convertView.findViewById(R.id.tv_invest_deadline);
                    topHolder.tvInvestTime = (TextView) convertView.findViewById(R.id.tv_investtime);
                    topHolder.tvEndTime = (TextView) convertView.findViewById(R.id.tv_endtime);
                    topHolder.layoutUncapital = (LinearLayout) convertView.findViewById(R.id.layout_uncapital);
                    topHolder.tvUnclearCapital = (TextView) convertView.findViewById(R.id.tv_unclear_capital);
                    topHolder.tvUnclearIncome = (TextView) convertView.findViewById(R.id.tv_unclear_income);
                    topHolder.tvClearCapital = (TextView) convertView.findViewById(R.id.tv_clear_capital);
                    topHolder.tvClearIncome = (TextView) convertView.findViewById(R.id.tv_clear_income);
                    topHolder.tvDayDesc = (TextView) convertView.findViewById(R.id.tv_day_desc);
                    convertView.setTag(topHolder);
                } else {
                    topHolder = (TopHolder) convertView.getTag();
                }
                break;
            case TYPE_BOTTOMITEM:
                if (convertView == null) {
                    holder = new NoticeHolder();
                    convertView = View.inflate(mContext, R.layout.items_bidinfo, null);
                    holder.topView = convertView.findViewById(R.id.top_view);
                    holder.tvBorrwoName = (TextView) convertView.findViewById(R.id.tv_borrwo_name);
                    holder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
                    holder.tvLoanNameDesc = (TextView) convertView.findViewById(R.id.tv_loanname_desc);
                    holder.tvLoanIdCardDesc = (TextView) convertView.findViewById(R.id.tv_loanidcard_desc);
                    holder.tvLoanUserName = (TextView) convertView.findViewById(R.id.tv_loan_username);
                    holder.tvLoanIdCard = (TextView) convertView.findViewById(R.id.tv_loan_idcard);
                    holder.tvInvestAmount = (TextView) convertView.findViewById(R.id.tv_invest_amount);
                    holder.tvInvestDeadline = (TextView) convertView.findViewById(R.id.tv_invest_deadline);
                    convertView.setTag(holder);
                } else {
                    holder = (NoticeHolder) convertView.getTag();
                }
                break;
        }

        switch (type) {
            case TYPE_TOPINFO:
                topHolder.tvInvestDeadLine.setText(investDeadline + "天");
                topHolder.tvInvestTime.setText(investTime);
                topHolder.tvEndTime.setText(endTime);

                topHolder.tvUnclearCapital.setText(unClearCapital + "");
                topHolder.tvUnclearIncome.setText(unClearIncome + "");
                topHolder.tvClearCapital.setText(clearCapital + "");
                topHolder.tvClearIncome.setText(clearIncome + "");

                //状态：1：收益中； 2 ：收益中； 3：已完成"  4: 回款中
                if ("1".equals(mStatus)||"2".equals(mStatus)) {
                    topHolder.ivStatus.setImageResource(R.drawable.icon_interesting);
                } else if ("3".equals(mStatus)) {
                    topHolder.layoutUncapital.setVisibility(View.GONE);
                    topHolder.ivStatus.setImageResource(R.drawable.icon_interest);
                    topHolder.tvUnclearCapital.setTextColor(mContext.getResources().getColor(R.color.text_gray));
                    topHolder.tvUnclearIncome.setTextColor(mContext.getResources().getColor(R.color.text_gray));
                } else if ("4".equals(mStatus)) {
                    topHolder.ivStatus.setImageResource(R.drawable.icon_capitaling);
                    topHolder.tvUnclearCapital.setText(clearCapital + "");
                    topHolder.tvUnclearIncome.setText(clearIncome + "");
                    topHolder.tvClearCapital.setText(unClearCapital + "");
                    topHolder.tvClearIncome.setText(unClearIncome + "");
                }

                if ("2".equals(mFwType)) {
                    topHolder.tvDayDesc.setText("出借期限");
                } else {
                    topHolder.tvDayDesc.setText("锁定期限");
                }

                break;
            case TYPE_BOTTOMITEM:
                BidBean diBean = list.get(position - 1);
                holder.tvBorrwoName.setText(diBean.getBorrowName());
                //状态：0待还款；1已结清； 2提前还款
                if ("2".equals(diBean.getRepayedType())) {
                    holder.tvStatus.setText("已提前还款");
                    holder.tvBorrwoName.setTextColor(mContext.getResources().getColor(R.color.text_gray));
                } else if ("1".equals(diBean.getRepayedType())) {
                    if ("1".equals(mFwType) && "4".equals(mStatus)) {
                        holder.tvStatus.setText("");
                        holder.tvBorrwoName.setTextColor(mContext.getResources().getColor(R.color.text_lightorange));
                    } else {
                        holder.tvStatus.setText("已结清");
                        holder.tvBorrwoName.setTextColor(mContext.getResources().getColor(R.color.text_gray));
                    }
                } else {
                    holder.tvStatus.setText("");
                    holder.tvBorrwoName.setTextColor(mContext.getResources().getColor(R.color.text_lightorange));
                }
                //1：个人借款 2企业借款
                if ("1".equals(diBean.getLoanType())) {
                    holder.tvLoanUserName.setText(diBean.getTrulyUserName());
                    holder.tvLoanIdCard.setText(diBean.getTrulyIdcard());
                    holder.tvLoanNameDesc.setText("借款人  ");
                    holder.tvLoanIdCardDesc.setText("证件号  ");
                } else {
                    holder.tvLoanNameDesc.setText("借贷公司  ");
                    holder.tvLoanIdCardDesc.setText("法定代表人  ");
                    holder.tvLoanUserName.setText(diBean.getTrulyLoanCompany());
                    holder.tvLoanIdCard.setText(diBean.getCompanyLegal());
                }
                holder.tvInvestAmount.setText(diBean.getInvestMoney() + "元");
                if ("1".equals(mFwType)) {
                    holder.tvInvestDeadline.setText(diBean.getBorrowDay() + "天");
                } else {
                    holder.tvInvestDeadline.setText(investDeadline + "天");
                }
                if (position == 1) {
                    holder.topView.setVisibility(View.GONE);
                } else {
                    holder.topView.setVisibility(View.VISIBLE);
                }

                break;
        }
        return convertView;
    }

    class TopHolder {
        ImageView ivStatus;
        TextView tvInvestDeadLine;
        TextView tvInvestTime;
        TextView tvEndTime;

        TextView tvUnclearCapital;
        TextView tvUnclearIncome;
        TextView tvClearCapital;
        TextView tvClearIncome;
        TextView tvDayDesc;

        LinearLayout layoutUncapital;
    }

    class NoticeHolder {
        View topView;
        TextView tvBorrwoName;
        TextView tvStatus;
        TextView tvLoanNameDesc;
        TextView tvLoanIdCardDesc;
        TextView tvLoanUserName;
        TextView tvLoanIdCard;
        TextView tvInvestAmount;
        TextView tvInvestDeadline;
    }
}
