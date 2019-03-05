package com.fanfanlicai.activity.invest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.bean.BidPicBean;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.fragment.fanwandetail.biddetail.BidBaseInfoFragment;
import com.fanfanlicai.fragment.fanwandetail.biddetail.ContractTempFragment;
import com.fanfanlicai.fragment.fanwandetail.biddetail.BidPicInfoFragment;
import com.fanfanlicai.fragment.totalmoney.TotalmoneyPagerAdapter;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.StringUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;
import com.fanfanlicai.view.HorizontalViewPager;
import com.fanfanlicai.view.dialog.CustomProgressDialog;
import com.nineoldandroids.view.ViewHelper;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lijinliu
 * @date 20180129
 * 标的信息
 */
public class BidInfoActivity extends FragmentActivity implements OnClickListener {

    private CustomProgressDialog progressdialog;
    private HorizontalViewPager vp_total_container;
    private List<Fragment> fragments;
    private View indicate_line;

    //返回
    private TextView mTvBtnBack;
    private LinearLayout mTopLayout;
    //基础信息
    private TextView mTvBidBaseInfo;
    //合同模板
    private TextView mTvContractTemp;
    //项目信息
    private TextView mTvProjectInfo;
    private TotalmoneyPagerAdapter adapter;
    //标ID
    private String mBid;
    private String mProductDataShowType;
    private String mInvestDays;
    private boolean isBuyBefore = false;
    private String mMatchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_biddetail);

        progressdialog = new CustomProgressDialog(this, "数据加载中...");
        initView();
        initEvents();
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
        indicate_line = findViewById(R.id.total_indicate_line);
        mTvBtnBack = (TextView) findViewById(R.id.invest_back);
        mTopLayout = (LinearLayout) findViewById(R.id.top_layout);
        mTvBidBaseInfo = (TextView) findViewById(R.id.tv_bid_baseinfo);
        mTvContractTemp = (TextView) findViewById(R.id.tv_contract_temp);
        mTvProjectInfo = (TextView) findViewById(R.id.tv_project_info);
        vp_total_container = (HorizontalViewPager) findViewById(R.id.vp_total_container);

    }

    private void initEvents() {
        mTvBtnBack.setOnClickListener(this);
        mTvBidBaseInfo.setOnClickListener(this);
        mTvContractTemp.setOnClickListener(this);
        mTvProjectInfo.setOnClickListener(this);
    }

    private void initData() {
        mBid = getIntent().getStringExtra("bid");
        mProductDataShowType = getIntent().getStringExtra("productDataShowType");
        mInvestDays = getIntent().getStringExtra("investDays");
        isBuyBefore = getIntent().getBooleanExtra("isBuyBefore", false);
        mMatchId = getIntent().getStringExtra("matchId");

        if(isBuyBefore){
            mTvContractTemp.setText("协议模板");
        } else {
            mTvContractTemp.setText("出借协议");
        }

        getBidDetailInfo();
    }

    class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //这四句代码主要是让指示器跟随pager页的跳动而显示
            int offsetX = (int) (positionOffset * indicate_line.getWidth());
            int startX = position * indicate_line.getWidth();
            int translationX = startX + offsetX;
            ViewHelper.setTranslationX(indicate_line, translationX);
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }

    //饭桶赎回后刷新界面的数据
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        //初始文字颜色为灰色
        mTvBidBaseInfo.setTextColor(getResources().getColor(R.color.text_gray));
        mTvContractTemp.setTextColor(getResources().getColor(R.color.text_gray));
        mTvProjectInfo.setTextColor(getResources().getColor(R.color.text_gray));
        switch (v.getId()) {
            case R.id.invest_back:
                finish();
                break;
            case R.id.tv_bid_baseinfo:
                vp_total_container.setCurrentItem(0, false);
                mTvBidBaseInfo.setTextColor(getResources().getColor(R.color.global_yellowcolor));
                break;
            case R.id.tv_contract_temp:
                vp_total_container.setCurrentItem(1, false);
                mTvContractTemp.setTextColor(getResources().getColor(R.color.global_yellowcolor));
                break;
            case R.id.tv_project_info:
                vp_total_container.setCurrentItem(2, false);
                mTvProjectInfo.setTextColor(getResources().getColor(R.color.global_yellowcolor));
                break;

            default:
                break;
        }
    }

    /**
     *标的组成(基本信息+图片资料)
     */
    private void getBidDetailInfo() {
        if (!progressdialog.isShowing()) {
            progressdialog.showis();
        }
        Map<String, String> map = SortRequestData.getmap();
        String token = CacheUtils.getString(BidInfoActivity.this, "token", null);
        map.put("token", token);
        map.put("bid", mBid);
        map.put("productDataShowType", mProductDataShowType);
        map.put("investDays", mInvestDays);
        String requestData = SortRequestData.sortString(map);
        String signData = SignUtil.sign(requestData);
        map.put("sign", signData);
        VolleyUtil.sendJsonRequestByPost(ConstantUtils.BORROWDISCLOSUREINFO_URL, null, map,
                new HttpBackBaseListener() {

                    @Override
                    public void onSuccess(String string) {
                        LogUtils.i("标的组成详情==="+string);
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

                                    fragments = new ArrayList<Fragment>();
                                    BidBaseInfoFragment bidBaseInfoFragment = new BidBaseInfoFragment();
                                    ContractTempFragment contractTempFragment = new ContractTempFragment();
                                    contractTempFragment.setData(isBuyBefore, mBid, mProductDataShowType, mMatchId);
                                    BidPicInfoFragment bidPicInfoFragment = new BidPicInfoFragment();
                                    bidBaseInfoFragment.setData(datastr , mInvestDays , mProductDataShowType,isBuyBefore);
                                    fragments.add(bidBaseInfoFragment);
                                    fragments.add(contractTempFragment);

                                    if (data.containsKey("picList")) {
                                        // 图片资源
                                        JSONArray getList = data.getJSONArray("picList");
                                        ArrayList<BidPicBean> list = (ArrayList<BidPicBean>) JSONArray.parseArray(getList.toJSONString(), BidPicBean.class);
                                        if (list != null && list.size() > 0)
                                        {
                                            bidPicInfoFragment.setPicList(list);
                                            fragments.add(bidPicInfoFragment);
                                            mTvProjectInfo.setVisibility(View.VISIBLE);
                                        } else {
                                            mTvProjectInfo.setVisibility(View.GONE);
                                        }
                                    }

                                    adapter = new TotalmoneyPagerAdapter(getSupportFragmentManager(), fragments);
                                    vp_total_container.setAdapter(adapter);
                                    // 监听ViewPager
                                    vp_total_container.setOnPageChangeListener(new MyOnPageChangeListener());
                                    // 初始化首页界面的数据
                                    vp_total_container.setCurrentItem(0, false);
                                    mTvBidBaseInfo.setTextColor(getResources().getColor(R.color.global_yellowcolor));
                                    // 得到屏幕宽度
                                    int screenW = getWindowManager().getDefaultDisplay().getWidth();
                                    // 根据屏幕宽度除以fragment的个数初始化指示器宽度
                                    indicate_line.getLayoutParams().width = screenW / fragments.size();
                                    indicate_line.requestLayout();
                                    indicate_line.setVisibility(View.VISIBLE);
                                    mTopLayout.setVisibility(View.VISIBLE);

                                } else {
                                    ToastUtils.toastshort("加载数据异常！");
                                }
                            }
                        }  else if("666666".equals(code)){

                        }else {
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

}
