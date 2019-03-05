package com.fanfanlicai.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.home.FanHeLiuShuiActivity;
import com.fanfanlicai.activity.invest.BidInfoActivity;
import com.fanfanlicai.bean.RepayMentBean;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.MathUtil;
import com.fanfanlicai.utils.StringUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;
import com.fanfanlicai.view.dialog.CustomProgressDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author lijinliu
 * @author 20180204
 *         标的详情
 */
public class BidDetailActivity extends FragmentActivity implements OnClickListener {
    //返回
    private TextView invest_back;
    private CustomProgressDialog progressdialog;

    private TextView tvInvestDeadLine;
    private TextView tvInvestTime;
    private TextView tvEndTime;
    private ImageView mIvStatus;

    private TextView tvUnclearCapital;
    private TextView tvUnclearIncome;

    private TextView tvTotalRepayCapital;
    private TextView mTvDescLeft;
    private TextView mTvDescRight;
    private TextView mTvRepayCapitalDesc;
    private TextView mTvDayDesc;

    private RelativeLayout mRlBidInfo;
    private RelativeLayout mRlRepaymentRecorde;
    private LinearLayout mLayoutRepayments;

    //标的ID
    private String mBid;
    //买入时间
    private String mTransTime;
    //到期时间
    private String mEndTime;
    // 投资期限
    private String mInvestDeadline;
    //已还款类型：0待还款；1正常还款；2提前还款"
    private String mStatus;
    //投资状态 :1：还款中；3：已完成；4：回款中
    private String mFwStatus;
    //待收收益
    private String mRecInterest;
    // statu=1待收本息；status=3 已结本息
    private String mWaitRepayAmount;
    //交易流水号
    private String mTransUuid;
    //新老饭碗分类 1老，2新
    private String mType;
    // 已结本金
    private String mStrCapital;
    // 已结收益
    private String mStrInterest;
    // 未结本金
    private String mStrUncapital;
    // 未结收益
    private String mStrUninterest;
    private String mMatchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bid_detail);

        initView();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initView() {
        progressdialog = new CustomProgressDialog(BidDetailActivity.this, "正在获取数据...");
        invest_back = (TextView) findViewById(R.id.get_back);
        invest_back.setOnClickListener(this);

        mIvStatus = (ImageView) findViewById(R.id.iv_status);
        tvInvestDeadLine = (TextView) findViewById(R.id.tv_invest_deadline);
        tvInvestTime = (TextView) findViewById(R.id.tv_investtime);
        tvEndTime = (TextView) findViewById(R.id.tv_endtime);
        tvUnclearCapital = (TextView) findViewById(R.id.tv_unclear_capital);
        tvUnclearIncome = (TextView) findViewById(R.id.tv_unclear_income);

        tvTotalRepayCapital = (TextView) findViewById(R.id.tv_total_repaycapital);
        mTvDescLeft = (TextView) findViewById(R.id.tv_desc_left);
        mTvDescRight = (TextView) findViewById(R.id.tv_desc_right);
        mTvRepayCapitalDesc = (TextView) findViewById(R.id.tv_repay_capital_desc);

        mRlBidInfo = (RelativeLayout) findViewById(R.id.rl_bid_info);
        mRlRepaymentRecorde = (RelativeLayout) findViewById(R.id.rl_repayment_recorde);
        mLayoutRepayments = (LinearLayout) findViewById(R.id.layout_repayments);

        mTvDayDesc = (TextView) findViewById(R.id.tv_day_desc);

        mRlBidInfo.setOnClickListener(this);
        mRlRepaymentRecorde.setOnClickListener(this);
    }

    private void initData() {
        mBid = getIntent().getStringExtra("bid");
        mTransUuid = getIntent().getStringExtra("transUuid");
        mInvestDeadline = getIntent().getStringExtra("investDeadline");
        mTransTime = getIntent().getStringExtra("investTime");
        mEndTime = getIntent().getStringExtra("endTime");
        mStatus = getIntent().getStringExtra("statues");
        mFwStatus = getIntent().getStringExtra("fWStatues");
        mType = getIntent().getStringExtra("type");
        mMatchId = getIntent().getStringExtra("matchId");
//Log.e("=====", mStatus+"|"+mFwStatus);
        tvInvestDeadLine.setText(mInvestDeadline + "天");
        tvInvestTime.setText(mTransTime);
        tvEndTime.setText(mEndTime);


        if ("2".equals(mType)) {
            mTvDayDesc.setText("出借期限");
        } else {
            mTvDayDesc.setText("锁定期限");
        }

        getFwRecordRepay();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.get_back:
                finish();
                break;
            case R.id.rl_bid_info:
                intent = new Intent(BidDetailActivity.this, BidInfoActivity.class);
                intent.putExtra("bid", mBid);
                intent.putExtra("productDataShowType", "2".equals(mType) ? "zt" : "fw");
                intent.putExtra("investDays", mInvestDeadline);
                intent.putExtra("isBuyBefore", false);
                intent.putExtra("matchId", mMatchId);
                BidDetailActivity.this.startActivity(intent);
                break;
            case R.id.rl_repayment_recorde:
                intent = new Intent(this, DirectInvestLiushuiActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void getFwRecordRepay() {
        if (!progressdialog.isShowing()) {
            progressdialog.show();
        }
        Map<String, String> map = SortRequestData.getmap();
        String token = CacheUtils.getString(BidDetailActivity.this, "token", null);
        map.put("bid", mBid + "");
        map.put("token", token);
        map.put("type", mType);
        map.put("transUuid", mTransUuid);
        map.put("matchId", mMatchId);
        String requestData = SortRequestData.sortString(map);
        String signData = SignUtil.sign(requestData);
        map.put("sign", signData);
        VolleyUtil.sendJsonRequestByPost(ConstantUtils.GETFWRECORDREPAY_URL, null, map,
                new HttpBackBaseListener() {

                    @Override
                    public void onSuccess(String string) {
                        LogUtils.i("标的详细信息===" + string);
                        // TODO Auto-generated method stub
                        progressdialog.dismiss();
                        JSONObject json = JSON.parseObject(string);
                        String code = json.getString("code");

                        if ("0".equals(code)) {
                            String datastr = json.getString("data");
                            if (StringUtils.isBlank(datastr)) {
                                // datastr为空不验签
                            } else {
                                String sign = json.getString("sign");
                                boolean isSuccess = SignUtil.verify(sign, datastr);
                                if (isSuccess) {// 验签成功
                                    JSONObject data = JSON.parseObject(datastr);
                                    mStrCapital = data.getString("capital");
                                    mStrInterest = data.getString("interest");
                                    mStrUncapital = data.getString("uncapital");
                                    mStrUninterest = data.getString("uninterest");

                                    // 已还款类型：0待还款；1正常还款；2提前还款
                                    if ("0".equals(mStatus)) {
                                        mIvStatus.setImageResource(R.drawable.icon_interesting);
                                        if ("4".equals(mFwStatus)) {
                                            tvUnclearIncome.setText(mStrInterest);
                                            tvUnclearCapital.setText(mStrCapital);
                                            tvTotalRepayCapital.setText(MathUtil.subDouble(Double.parseDouble(mStrInterest) + Double.parseDouble(mStrCapital), 2) + "");
                                        } else {
                                            tvUnclearIncome.setText(mStrUninterest);
                                            tvUnclearCapital.setText(mStrUncapital);
                                            tvTotalRepayCapital.setText(MathUtil.subDouble(Double.parseDouble(mStrUninterest) + Double.parseDouble(mStrUncapital), 2) + "");
                                        }
                                        mTvDescLeft.setText("待收本金(元)");
                                        mTvDescRight.setText("待收收益(元)");
                                        mRlBidInfo.setVisibility(View.VISIBLE);
                                    } else if ("1".equals(mStatus) || "2".equals(mStatus)) {
                                        if ("1".equals(mType) && "4".equals(mFwStatus)) {
                                            mIvStatus.setImageResource(R.drawable.icon_interesting);
                                            tvUnclearIncome.setText(mStrInterest);
                                            tvUnclearCapital.setText(mStrCapital);
                                            tvTotalRepayCapital.setText(MathUtil.subDouble(Double.parseDouble(mStrInterest) + Double.parseDouble(mStrCapital), 2) + "");
                                            mTvDescLeft.setText("待收本金(元)");
                                            mTvDescRight.setText("待收收益(元)");
                                            mRlBidInfo.setVisibility(View.VISIBLE);
                                        } else {
                                            mIvStatus.setImageResource(R.drawable.icon_interest);
                                            mTvDescLeft.setText("已结本金(元)");
                                            mTvDescRight.setText("已结收益(元)");
                                            tvUnclearIncome.setTextColor(getResources().getColor(R.color.text_gray));
                                            tvUnclearCapital.setTextColor(getResources().getColor(R.color.text_gray));
                                            tvTotalRepayCapital.setTextColor(getResources().getColor(R.color.text_gray));
                                            tvUnclearIncome.setText(mStrInterest);
                                            tvUnclearCapital.setText(mStrCapital);
                                            tvTotalRepayCapital.setText(MathUtil.subDouble(Double.parseDouble(mStrInterest) + Double.parseDouble(mStrCapital), 2) + "");
                                            mRlRepaymentRecorde.setVisibility(View.VISIBLE);
                                            mTvRepayCapitalDesc.setText("已结总计：");
                                        }
                                    }

                                    getDataFromServer();
                                } else {
                                    ToastUtils.toastshort("加载数据异常！");
                                }
                            }
                        } else if ("666666".equals(code)) {

                        } else {
                            String msg = json.getString("msg");
                            ToastUtils.toastshort(msg);
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        progressdialog.dismiss();
                    }
                });
    }

    private void getDataFromServer() {
        Map<String, String> map = SortRequestData.getmap();
        String token = CacheUtils.getString(BidDetailActivity.this, "token", null);
        map.put("transUuid", mTransUuid + "");
        map.put("bid", mBid + "");
        map.put("type", mType + "");
        map.put("token", token);
        map.put("matchId", mMatchId);
        String requestData = SortRequestData.sortString(map);
        String signData = SignUtil.sign(requestData);
        map.put("sign", signData);
        VolleyUtil.sendJsonRequestByPost(ConstantUtils.GETBIDDETAILINFO_URL,
                null, map, new HttpBackBaseListener() {

                    @Override
                    public void onSuccess(String string) {
                        LogUtils.i("标的还款列表===" + string);
                        // TODO Auto-generated method stub
                        progressdialog.dismiss();
                        JSONObject json = JSON.parseObject(string);
                        String code = json.getString("code");

                        if ("0".equals(code)) {
                            String datastr = json.getString("data");
                            if (StringUtils.isBlank(datastr)) {
                                // datastr为空不验签
                            } else {
                                String sign = json.getString("sign");
                                boolean isSuccess = SignUtil.verify(sign, datastr);
                                // 验签成功
                                if (isSuccess) {
                                    JSONArray getList = JSON.parseArray(datastr);
                                    ArrayList<RepayMentBean> listadd = (ArrayList<RepayMentBean>) JSONArray.parseArray(getList.toJSONString(), RepayMentBean.class);
                                    for (RepayMentBean repayMentBean : listadd) {
                                        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.item_repay_ment, null);
                                        TextView tvTransTime = (TextView) layout.findViewById(R.id.tv_trans_time);
                                        TextView tvRepayCapital = (TextView) layout.findViewById(R.id.tv_repay_capital);
                                        TextView tvInterest = (TextView) layout.findViewById(R.id.tv_interest);
                                        TextView tvReplayStatus = (TextView) layout.findViewById(R.id.tv_replay_status);
                                        //tvTransTime.setText(repayMentBean.getEndTime());
                                        //回款时间应该显示项目到期时间，而非标的到期时间
                                        tvTransTime.setText(mEndTime);
                                        tvRepayCapital.setText(repayMentBean.getCapital());
                                        tvInterest.setText(repayMentBean.getInterest());
                                        if ("1".equals(repayMentBean.getStatus()) || "4".equals(mFwStatus)) {
                                            tvReplayStatus.setText("待回款");
                                            tvReplayStatus.setTextColor(getResources().getColor(R.color.text_lightorange));
                                        } else {
                                            tvReplayStatus.setText("已回款");
                                            tvReplayStatus.setTextColor(getResources().getColor(R.color.text_gray));
                                            tvTransTime.setTextColor(getResources().getColor(R.color.text_gray));
                                            tvRepayCapital.setTextColor(getResources().getColor(R.color.text_gray));
                                            tvInterest.setTextColor(getResources().getColor(R.color.text_gray));
                                        }
                                        mLayoutRepayments.addView(layout);
                                    }

                                } else {
                                    ToastUtils.toastshort("加载数据异常！");
                                }
                            }
                        } else {
                            String msg = json.getString("msg");
                            ToastUtils.toastshort(msg);
                        }

                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub
                        progressdialog.dismiss();
                        ToastUtils.toastshort("加载数据失败！");
                    }

                });

    }

}
