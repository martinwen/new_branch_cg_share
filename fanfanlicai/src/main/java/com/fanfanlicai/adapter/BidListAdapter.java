package com.fanfanlicai.adapter;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanfanlicai.activity.invest.BidInfoActivity;
import com.fanfanlicai.bean.BiaoDeBean;
import com.fanfanlicai.fanfanlicai.R;

import java.util.ArrayList;

/**
 * @author lijinliu
 * @date 20180129
 * 标的列表adapter
 */
public class BidListAdapter extends RecyclerView.Adapter<BidListAdapter.Myholder> {

    private FragmentActivity mContext;
    private ArrayList<BiaoDeBean> list;
    private String mProductDataShowType;
    private String mInvestDays;

    public BidListAdapter(FragmentActivity activity, ArrayList<BiaoDeBean> list ,String showType , String investDays) {
        this.mContext=activity;
        this.list = list;
        this.mProductDataShowType = showType;
        this.mInvestDays = investDays;
    }

    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.items_bidinfo, parent, false);
        Myholder myholder = new Myholder(view);
        return myholder;

    }

    @Override
    public void onBindViewHolder(Myholder holder, int position) {
        final BiaoDeBean biaoDeBean = list.get(position);
        holder.tvBorrwoName.setText(biaoDeBean.getBorrowName());
        holder.tvInvestAmount.setText(biaoDeBean.getBorrowMoney()+"元");
        holder.tvInvestDeadline.setText(biaoDeBean.getTerm()+"天");
        //个人1 ，公司2
        if ("1".equals(biaoDeBean.getTrulyLoanType())) {
            holder.tvLoanNameDesc.setText("借款人  ");
            holder.tvLoanIdCardDesc.setText("证件号  ");
            holder.tvLoanUserName.setText(biaoDeBean.getTrulyUserName());
            holder.tvLoanIdCard.setText(biaoDeBean.getTrulyIdcard());
        } else {
            holder.tvLoanNameDesc.setText("借贷公司  ");
            holder.tvLoanIdCardDesc.setText("法定代表人  ");
            holder.tvLoanUserName.setText(biaoDeBean.getTrulyLoanCompany());
            holder.tvLoanIdCard.setText(biaoDeBean.getTrulyCompanyLegalPersion());
        }
        holder.tvStatus.setVisibility(View.GONE);
        holder.topView.setVisibility(View.GONE);
        holder.viewTopLine.setVisibility(View.GONE);
        holder.viewBottomLine.setVisibility(View.GONE);
        holder.viewBottomLineB.setVisibility(View.VISIBLE);
        ViewGroup parent =  (ViewGroup) holder.viewBottomLineB.getParent();
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, BidInfoActivity.class);
                intent.putExtra("bid", biaoDeBean.getBid());
                intent.putExtra("productDataShowType", mProductDataShowType);
                intent.putExtra("investDays", mInvestDays);
                intent.putExtra("isBuyBefore", true);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class Myholder extends RecyclerView.ViewHolder {

        private View topView;
        private TextView tvBorrwoName;
        private TextView tvStatus;
        private TextView tvLoanNameDesc;
        private TextView tvLoanIdCardDesc;
        private TextView tvLoanUserName;
        private TextView tvLoanIdCard;
        private TextView tvInvestAmount;
        private TextView tvInvestDeadline;
        private ImageView ivArrow;
        private View viewTopLine;
        private View viewBottomLine;
        private View viewBottomLineB;

        public Myholder(View itemView) {
            super(itemView);
            topView = itemView.findViewById(R.id.top_view);
            tvBorrwoName = (TextView) itemView.findViewById(R.id.tv_borrwo_name);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            tvLoanNameDesc = (TextView) itemView.findViewById(R.id.tv_loanname_desc);
            tvLoanIdCardDesc = (TextView) itemView.findViewById(R.id.tv_loanidcard_desc);
            tvLoanUserName = (TextView) itemView.findViewById(R.id.tv_loan_username);
            tvLoanIdCard = (TextView) itemView.findViewById(R.id.tv_loan_idcard);
            tvInvestAmount = (TextView) itemView.findViewById(R.id.tv_invest_amount);
            tvInvestDeadline = (TextView) itemView.findViewById(R.id.tv_invest_deadline);
            ivArrow = (ImageView) itemView.findViewById(R.id.iv_arrow);
            viewTopLine  = itemView.findViewById(R.id.view_top_line);
            viewBottomLine = itemView.findViewById(R.id.view_bottom_line);
            viewBottomLineB = itemView.findViewById(R.id.view_bottom_lineb);
        }
    }
}


