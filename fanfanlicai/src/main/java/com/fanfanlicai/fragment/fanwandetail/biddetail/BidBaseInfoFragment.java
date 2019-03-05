package com.fanfanlicai.fragment.fanwandetail.biddetail;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fanfanlicai.bean.FloatRateBean;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.fragment.BaseFragment;
import com.fanfanlicai.global.FanFanApplication;
import com.fanfanlicai.utils.MathUtil;

import java.util.ArrayList;

/**
 * @author lijinliu
 * @date 20180129
 * 标的基础信息
 */

public class BidBaseInfoFragment extends BaseFragment {
    private String mJsonData;

    private LinearLayout mLayoutTrulyUserName;
    private TextView mTvTrulyUsername;
    private LinearLayout mLayoutTrulyIdCard;
    private TextView mTvTrulyIdCard;
    private LinearLayout mLayoutTrulyLoanCompany;
    private TextView mTvTrulyloanCompany;
    private LinearLayout mLayoutTrulyCompanyLegalPersion;
    private TextView mTvTrulyCompanyLegalPersion;
    private LinearLayout mLayoutBorrowMoney;
    private TextView mTvBorrowMoney;
    private LinearLayout mLayoutBorrowDays;
    private TextView mTvBorrowDays;
    private LinearLayout mLayoutRepayMethod;
    private TextView mTvRepayMethod;
    private LinearLayout mLayoutLoanRepaySource;
    private TextView mTvLoanRepaySource;
    private LinearLayout mLayoutRiskControl;
    private TextView mTvRiskControl;
    private TextView mTvInterestDay;
    private String mInvestDays;
    private TextView mTvDayDesc;
    private String mProductDataShowType;

    private View line_trulyCompany_legalPersion;
    private View line_trulyloan_company;
    private View line_truly_username;
    private View line_truly_idcard;
    private View line_repay_method;
    private View line_repay_source;
    private View line_risk_control;
    private boolean isBuyBefore = false;

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_bid_baseinfo, null);
        mLayoutTrulyUserName = (LinearLayout) view.findViewById(R.id.linearlayout_truly_username);
        mTvTrulyUsername = (TextView) view.findViewById(R.id.tv_truly_username);
        mLayoutTrulyIdCard = (LinearLayout) view.findViewById(R.id.linearlayout_truly_idcard);
        mTvTrulyIdCard = (TextView) view.findViewById(R.id.tv_truly_idcard);
        mLayoutTrulyLoanCompany = (LinearLayout) view.findViewById(R.id.linearlayout_trulyloan_company);
        mTvTrulyloanCompany = (TextView) view.findViewById(R.id.tv_trulyloan_company);
        mLayoutTrulyCompanyLegalPersion = (LinearLayout) view.findViewById(R.id.linearlayout_trulyCompany_legalPersion);
        mTvTrulyCompanyLegalPersion = (TextView) view.findViewById(R.id.tv_trulyCompany_legalPersion);
        mLayoutBorrowMoney = (LinearLayout) view.findViewById(R.id.linearlayout_borrow_money);
        mTvBorrowMoney = (TextView) view.findViewById(R.id.tv_borrow_money);
        mLayoutBorrowDays = (LinearLayout) view.findViewById(R.id.linearlayout_borrow_days);
        mTvBorrowDays = (TextView) view.findViewById(R.id.tv_borrow_days);
        mLayoutRepayMethod = (LinearLayout) view.findViewById(R.id.linearlayout_repay_method);
        mTvRepayMethod = (TextView) view.findViewById(R.id.tv_repay_method);
        mLayoutLoanRepaySource = (LinearLayout) view.findViewById(R.id.linearlayout_loan_repay_source);
        mTvLoanRepaySource = (TextView) view.findViewById(R.id.tv_loan_repay_source);
        mLayoutRiskControl = (LinearLayout) view.findViewById(R.id.linearlayout_risk_control);
        mTvRiskControl = (TextView) view.findViewById(R.id.tv_risk_control);
        mTvInterestDay = (TextView) view.findViewById(R.id.tv_interest_day);
        mTvDayDesc = (TextView) view.findViewById(R.id.tv_day_desc);

        line_trulyCompany_legalPersion = (View) view.findViewById(R.id.line_trulyCompany_legalPersion);
        line_trulyloan_company = (View) view.findViewById(R.id.line_trulyloan_company);
        line_truly_username = (View) view.findViewById(R.id.line_truly_username);
        line_truly_idcard = (View) view.findViewById(R.id.line_truly_idcard);
        line_repay_method = (View) view.findViewById(R.id.line_repay_method);
        line_repay_source = (View) view.findViewById(R.id.line_repay_source);
        line_risk_control = (View) view.findViewById(R.id.line_risk_control);
        return view;
    }

    @Override
    public void initData() {
        JSONObject data = JSON.parseObject(mJsonData);
        // 1为个人  2 为机构
        final String trulyLoanType = data.getString("trulyLoanType");
        // 借款人姓名
        final String trulyUserName = data.getString("trulyUserName");
        // 借款人身份证号码
        final String trulyIdcard = data.getString("trulyIdcard");
        // 借款公司名
        final String trulyLoanCompany = data.getString("trulyLoanCompany");
        // 法人名称
        final String trulyCompanyLegalPersion = data.getString("trulyCompanyLegalPersion");
        // 借款金额
        final String borrowMoney = data.getString("borrowMoney");
        // 借款期限
        final String borrowDays = data.getString("borrowDays");
        // 还款方式
        final String repayMethod = data.getString("repayMethod");
        // 还款来源
        final String loanRepaySource = data.getString("loanRepaySource");
        //还款保证
        final String riskControl = data.getString("riskControl");
        //还款保证 计息日
        final String interestDay = data.getString("interestDay");
        //zt 直投  fw 饭碗
        final String productDataShowType= data.getString("productDataShowType");

        if ("1".equals(trulyLoanType)) {
            mLayoutTrulyLoanCompany.setVisibility(View.GONE);
            mLayoutTrulyCompanyLegalPersion.setVisibility(View.GONE);
            line_trulyCompany_legalPersion.setVisibility(View.GONE);
            line_trulyloan_company.setVisibility(View.GONE);
            mTvTrulyUsername.setText(trulyUserName);
            mTvTrulyIdCard.setText(trulyIdcard);
        } else {
            mLayoutTrulyUserName.setVisibility(View.GONE);
            mLayoutTrulyIdCard.setVisibility(View.GONE);
            line_truly_username.setVisibility(View.GONE);
            line_truly_idcard.setVisibility(View.GONE);
            mTvTrulyloanCompany.setText(trulyLoanCompany);
            mTvTrulyCompanyLegalPersion.setText(trulyCompanyLegalPersion);

        }

        if ("zt".equals(mProductDataShowType)) {
            mTvDayDesc.setText("出借期限：");
        } else {
            mTvDayDesc.setText("剩余期限：");

        }

        mTvBorrowMoney.setText(borrowMoney + "元");
        mTvBorrowDays.setText(borrowDays + "天");
        //mTvBorrowDays.setText(mInvestDays + "天");
        mTvInterestDay.setText(interestDay);

        if (TextUtils.isEmpty(repayMethod)) {
            mLayoutRepayMethod.setVisibility(View.GONE);
            line_repay_method.setVisibility(View.GONE);
        } else {
            mTvRepayMethod.setText(repayMethod);
        }
        if (TextUtils.isEmpty(loanRepaySource)) {
            mLayoutLoanRepaySource.setVisibility(View.GONE);
            line_repay_source.setVisibility(View.GONE);
        } else {
            mTvLoanRepaySource.setText(loanRepaySource);
        }
        if (TextUtils.isEmpty(riskControl)) {
            mLayoutRiskControl.setVisibility(View.GONE);
            line_risk_control.setVisibility(View.GONE);
        } else {
            mTvRiskControl.setText(riskControl);
        }
    }

    public void setData(String jsonData , String investDays , String productDataShowType ,boolean isBuyBefore) {
        this.mInvestDays = investDays;
        this.mJsonData = jsonData;
        this.mProductDataShowType = productDataShowType;
        this.isBuyBefore = isBuyBefore;
    }
}
