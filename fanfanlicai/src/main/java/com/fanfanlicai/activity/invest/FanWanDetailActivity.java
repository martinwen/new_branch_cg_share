
package com.fanfanlicai.activity.invest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.activity.MainActivity;
import com.fanfanlicai.bean.FloatRateBean;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.fragment.fanwandetail.BidListFragment;
import com.fanfanlicai.fragment.fanwandetail.InterestMethodFragment;
import com.fanfanlicai.fragment.fanwandetail.ProjectInfoFragment;
import com.fanfanlicai.global.FanFanApplication;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.DensityUtil;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.MathUtil;
import com.fanfanlicai.utils.StrToNumber;
import com.fanfanlicai.utils.StringUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;
import com.fanfanlicai.view.dialog.AutoMatchingDialog;
import com.fanfanlicai.view.dialog.CGBindCardDialog;
import com.fanfanlicai.view.dialog.CGSignCardDialog;
import com.fanfanlicai.view.dialog.CePingAgainDialog;
import com.fanfanlicai.view.dialog.CePingChanceOverDialog;
import com.fanfanlicai.view.dialog.CePingNotDialog;
import com.fanfanlicai.view.dialog.CustomProgressDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lijinliu
 * @date 20180139
 * 饭碗买入详情
 */
public class FanWanDetailActivity extends BaseActivity implements OnClickListener {

    private CustomProgressDialog progressdialog;
    private String[] titles = new String[]{"项目介绍", "计息方式", "标的组成"};
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Fragment> fragments;
    // 返回
    private TextView mbtnBack;
    private TextView mTvTitle;
    //收益计算
    private ImageView mBtnIncomeCalculation;
    private ImageView mIvIsDirectinvest;
    private TextView mTvAddrate;
    private TextView mTvBaserate;
    private TextView mTvTerm;
    private TextView mTvRepaymentModel;
    private TextView mTvMininvestAmount;
    private TextView mTvPublishedTime;
    private TextView mTvEndTime;
    private TextView mTvBorrowMoney;
    private TextView mTvDayDesc;
    private ImageView mIvTopBg;
    //顶部布局
    private RelativeLayout mTopLayout;
    private Button mBtnBuy;

    //1.00 活动利率
    private Double mActivityRate;
    //8.90 基础利率
    private Double mBaseRate;
    //1970-01-01 到期回款
    private String mEndTime;
    //0.00 加息票值
    private Double mFwAddRate;
    // 0, 加息票id
    private String mFwAddRateId;
    // 1000, 最低起投金额
    private String mMinInvestAmount;
    //直投第031期 产品名称
    private String mName;
    private String mProductTypeName;
    // 36, 直投产品ID
    private String mPid;
    //zt 数据展示类型
    private String mProductDataShowType;
    //type 金fw_gold、饭碗-银fw_silver、饭碗-铜fw_copper、饭碗-铁fw_iron、饭盒fh、饭桶ft
    private String mProductCode;
    //2018-01-05 开始募集时间
    private String mPublishedTime;
    //一次性还本付息 还款方式
    private String mRepaymentMethod;
    private String mJiaxi;
    // 60,投资天数
    private String mTerm;
    // 60,可投金额
    private String mRemainingMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fanwan_detail);

        progressdialog = new CustomProgressDialog(this, "数据加载中...");
        initView();
        initEvents();
        initInentData();
        initData();
    }

    private void initView() {
        mbtnBack = (TextView) findViewById(R.id.get_back);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mBtnIncomeCalculation = (ImageView) findViewById(R.id.iv_income_calculation);
        mIvIsDirectinvest = (ImageView) findViewById(R.id.iv_is_directinvest);
        mTvAddrate = (TextView) findViewById(R.id.tv_addrate);
        mTvBaserate = (TextView) findViewById(R.id.tv_baserate);
        mTvTerm = (TextView) findViewById(R.id.tv_term);
        mTvRepaymentModel = (TextView) findViewById(R.id.tv_repayment_model);
        mTvMininvestAmount = (TextView) findViewById(R.id.tv_mininvest_amount);
        mTvPublishedTime = (TextView) findViewById(R.id.tv_published_time);
        mTvEndTime = (TextView) findViewById(R.id.tv_end_time);
        mTvBorrowMoney = (TextView) findViewById(R.id.tv_borrow_money);
        mBtnBuy = (Button) findViewById(R.id.bt_buy);
        mTvDayDesc = (TextView) findViewById(R.id.tv_day_desc);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        //750x480
        mIvTopBg = (ImageView) findViewById(R.id.iv_top_bg);
        mTopLayout = (RelativeLayout) findViewById(R.id.top_layout);
        ViewTreeObserver vto = mTopLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mTopLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int ScreenWidth = DensityUtil.screenWidth(FanWanDetailActivity.this);
                int imgHeight = (ScreenWidth * 48) / 75;
                int rlHeight = imgHeight - DensityUtil.dip2px(FanWanDetailActivity.this, 84);
                mIvTopBg.getLayoutParams().height = imgHeight;
                mTopLayout.setLayoutParams(new LinearLayout.LayoutParams(ScreenWidth, rlHeight));
            }
        });

    }

    private void initInentData() {
        String datastr = getIntent().getStringExtra("jsonData");
        JSONObject data = JSON.parseObject(datastr);
        mActivityRate = data.getDouble("activityRate");
        mBaseRate = data.getDouble("baseRate");
        mEndTime = data.getString("endTime");
        mFwAddRate = data.getDouble("fwAddRate");
        mFwAddRateId = data.getString("fwAddRateId");
        mMinInvestAmount = data.getString("minInvestAmount");
        mName = data.getString("name");
        mProductTypeName = data.getString("productTypeName");
        mPid = data.getString("pid");
        mProductDataShowType = data.getString("productDataShowType");
        mProductCode = getIntent().getStringExtra("productCode");
        mPublishedTime = data.getString("publishedTime");
        mRepaymentMethod = data.getString("repaymentMethod");
        mRemainingMoney = data.getString("remainingMoney");
        ;
        mTerm = data.getString("term");
        if ("zt".equals(mProductDataShowType)) {
            mIvIsDirectinvest.setVisibility(View.GONE);
            mJiaxi = MathUtil.subDouble((mActivityRate  + FanFanApplication.jiaxi_rate), 2) + "";

            mTvDayDesc.setText("出借期限");
        } else {
            mIvIsDirectinvest.setVisibility(View.VISIBLE);
            JSONArray getList = data.getJSONArray("floatRate");
            ArrayList<FloatRateBean> list = (ArrayList<FloatRateBean>) JSONArray.parseArray(getList.toJSONString(), FloatRateBean.class);
            for (FloatRateBean rateBean : list) {
                if (mTerm.equals(rateBean.getInvestDays())) {
                    mJiaxi = MathUtil.subDouble(( mFwAddRate + rateBean.getFloatRate() + FanFanApplication.jiaxi_rate), 2) + "";
                }
            }
            mTvDayDesc.setText("锁定期限");
        }
        mTvTitle.setText(mProductTypeName+"");
        mTvAddrate.setText("+" + mJiaxi + "%");
        mTvBaserate.setText(MathUtil.subDouble((mBaseRate), 2) + "%");
        mTvTerm.setText(mTerm + "天");
        mTvRepaymentModel.setText(mRepaymentMethod);
        mTvMininvestAmount.setText(mMinInvestAmount + "元");
        mTvPublishedTime.setText(mPublishedTime);
        mTvEndTime.setText(mEndTime);
        mTvBorrowMoney.setText(mRemainingMoney);
    }

    private void initEvents() {
        mbtnBack.setOnClickListener(this);
        mBtnBuy.setOnClickListener(this);
        mBtnIncomeCalculation.setOnClickListener(this);
    }

    private void initData() {
        fragments = new ArrayList<Fragment>();
        String token = CacheUtils.getString(FanWanDetailActivity.this, "token", null);

        ProjectInfoFragment projectInfo = new ProjectInfoFragment();
        projectInfo.setStrMsg(token, "", mProductDataShowType);

        InterestMethodFragment interestCalculation = new InterestMethodFragment();
        interestCalculation.setStrMsg(token, "", mProductDataShowType);

        BidListFragment bidListFragment = new BidListFragment();
        bidListFragment.setData(mPid, mProductDataShowType, mTerm);
        fragments.add(projectInfo);
        fragments.add(interestCalculation);
        fragments.add(bidListFragment);

        TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        //关联一下指示器
        mTabLayout.setupWithViewPager(mViewPager);
    }


    private class TabPagerAdapter extends FragmentPagerAdapter {

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        FanFanApplication.jiaxi_rate = 0;
        FanFanApplication.jiaxi_ID = null;
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.get_back:
                finish();
                //返回后，需要把加息票的值和id变成初始状态，并刷新饭碗界面
                FanFanApplication.jiaxi_rate = 0;
                FanFanApplication.jiaxi_ID = null;
                intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.bt_buy:
                buyCheck();
                break;
            case R.id.iv_income_calculation:
                String token = CacheUtils.getString(FanWanDetailActivity.this, "token", null);
                intent = new Intent(FanWanDetailActivity.this, FanWanRateNewActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("investDays", mTerm);
                intent.putExtra("proCode", mProductCode);
                intent.putExtra("productType", mProductDataShowType);
                intent.putExtra("pid", mPid);
                FanWanDetailActivity.this.startActivity(intent);
                break;
            default:
                break;
        }
    }


    /**
     * 直投买入第一步
     */
    private void buyCheck() {
        if (!progressdialog.isShowing()) {
            progressdialog.showis();
        }
        Map<String, String> map = SortRequestData.getmap();
        String token = CacheUtils.getString(FanWanDetailActivity.this, "token", null);
        map.put("token", token);
        map.put("type", mProductCode);
        map.put("investDays", mTerm);
        map.put("fwAddRateId", mFwAddRateId);
        //直投产品ID，债权转让传空
        if ("zt".equals(mProductDataShowType)) {
            map.put("productId", mPid);
            //直投开关
            map.put("fwSwitch", "true");
        } else {
            map.put("fwSwitch", "false");
        }
        String requestData = SortRequestData.sortString(map);
        String signData = SignUtil.sign(requestData);
        map.put("sign", signData);
        //Log.e("buy-Parmers:", map.toString());
        VolleyUtil.sendJsonRequestByPost(ConstantUtils.GOTOZTINVEST_URL, null, map,
                new HttpBackBaseListener() {

                    @Override
                    public void onSuccess(String string) {
                        LogUtils.i("饭碗买入===" + string);
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
                                boolean isSuccess = SignUtil.verify(sign,
                                        datastr);
                                if (isSuccess) {// 验签成功
                                    JSONObject data = JSON.parseObject(datastr);
                                    // 账户余额
                                    final String baseBal = data.getString("baseBal");
                                    // 奖金余额
                                    final String rewardAcctBal = data.getString("rewardAcctBal");
                                    CacheUtils.putString(FanWanDetailActivity.this, "rewardAcctBal", rewardAcctBal);
                                    // 交易流水号
                                    final String seqNo = data.getString("seqNo");
                                    // 投资天数
                                    final String investDays = data.getString("investDays");
                                    // 类型 (直投产品zt)
                                    final String type = data.getString("type");//
                                    // 单人单日投资上限
                                    final String maxDailyInvestAmount = data.getString("maxDailyInvestAmount");
                                    // 单笔投资最小值
                                    final String singleMinInvestAmount = data.getString("singleMinInvestAmount");
                                    // 单笔投资上限
                                    final String singleMaxInvestAmount = data.getString("singleMaxInvestAmount");
                                    // 是否开通存管户
                                    final String hasFyAccount = data.getString("hasFyAccount");
                                    // 是否签约
                                    final String isSignCard = data.getString("isSignCard");
                                    // 是否测评
                                    Boolean hasEvln = data.getBoolean("hasEvln");
                                    // 是否可投资
                                    Boolean canInvest = data.getBoolean("canInvest");
                                    // 测评剩余次数
                                    String overTimes = data.getString("overTimes");
                                    //直投开关
                                    //final String fwSwitch = data.getString("fwSwitch");
                                    // 是否已经自动授权  1 是  0 否
                                    final String hasAutoAuthorized = data.getString("hasAutoAuthorized");
                                    final String activityRate = data.getString("activityRate");
                                    final String activityName = data.getString("activityName");
                                    // 是否开通存管户（绑卡）0-未开通；
                                    if ("0".equals(hasFyAccount)) {// 如果还没有绑卡，去绑卡

                                        CGBindCardDialog cgBindCardDialog = new CGBindCardDialog(FanWanDetailActivity.this, R.style.YzmDialog);
                                        cgBindCardDialog.show();
                                    } else {//1-已开通
                                        //评测过
                                        if (hasEvln) {
                                            //评测过并可投资
                                            if (canInvest) {
                                                if ("0".equals(isSignCard)) {// 如果还没有签约，去签约
                                                    CGSignCardDialog cgSignCardDialog = new CGSignCardDialog(FanWanDetailActivity.this, R.style.YzmDialog);
                                                    cgSignCardDialog.show();
                                                } else {//1-已签约
                                                    //是否买入匹配自动授权 0 未授权
                                                    if ("0".equals(hasAutoAuthorized)) {
                                                        AutoMatchingDialog matchingDialog = new AutoMatchingDialog(FanWanDetailActivity.this, R.style.YzmDialog, new AutoMatchingDialog.OnDialogClickListener() {
                                                            @Override
                                                            public void onClickConfirm() {
                                                                buyCheck();
                                                            }
                                                        });
                                                        matchingDialog.show();
                                                    } else {
                                                        if ("-9999".equals(baseBal)) {
                                                            // 账户余额 ,银行不能正常返回账户余额-9999
                                                            ToastUtils.toastshort("正在获取账户余额...");
                                                        } else {
                                                            startBuyActivity(baseBal, seqNo, investDays, type, maxDailyInvestAmount, singleMinInvestAmount, singleMaxInvestAmount, activityName, activityRate);
                                                        }
                                                    }
                                                }
                                            } else {
                                                //评测过不可投资
                                                //还有评测次数
                                                if (StrToNumber.strTodouble(overTimes) > 0) {
                                                    CePingAgainDialog cePingAgainDialog = new CePingAgainDialog(FanWanDetailActivity.this, R.style.YzmDialog, overTimes);
                                                    cePingAgainDialog.show();
                                                } else {//没有评测次数
                                                    CePingChanceOverDialog cePingChanceOverDialog = new CePingChanceOverDialog(FanWanDetailActivity.this, R.style.YzmDialog);
                                                    cePingChanceOverDialog.show();
                                                }
                                            }
                                            } else {// 未评测过
                                                CePingNotDialog cePingNotDialog = new CePingNotDialog(FanWanDetailActivity.this, R.style.YzmDialog);
                                                cePingNotDialog.show();
                                            }


                                    }

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

    private void startBuyActivity(String baseBal, String seqNo, String investDays, String type, String maxDailyInvestAmount, String singleMinInvestAmount, String singleMaxInvestAmount, String activityName, String activityRate) {
        Intent intent = new Intent(FanWanDetailActivity.this, FanWanBuyNewActivity.class);
        intent.putExtra("baseBal", baseBal);
        intent.putExtra("seqNo", seqNo);
        intent.putExtra("investDays", investDays);
        intent.putExtra("type", type);
        intent.putExtra("baseRate", mBaseRate + "");
        intent.putExtra("jiaxi", MathUtil.subDouble((Double.parseDouble(mJiaxi) - FanFanApplication.jiaxi_rate), 2) + "");// + "");
        intent.putExtra("maxDailyInvestAmount", maxDailyInvestAmount);
        intent.putExtra("singleMinInvestAmount", singleMinInvestAmount);
        intent.putExtra("singleMaxInvestAmount", singleMaxInvestAmount);
        intent.putExtra("fwSwitch", "zt".equals(mProductDataShowType) ? "true" : "false");
        intent.putExtra("productId", mPid);
        intent.putExtra("remainingMoney", mRemainingMoney);
        intent.putExtra("activityRate", activityRate);
        intent.putExtra("activityName", activityName);
        intent.putExtra("productDataShowType", mProductDataShowType);
        intent.putExtra("endTime", mEndTime);
        FanWanDetailActivity.this.startActivity(intent);
    }
}
