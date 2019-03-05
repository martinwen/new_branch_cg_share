package com.fanfanlicai.activity.my;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.fragment.fanwandetail.FanWanLiuShuiFragment;
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
import com.nineoldandroids.view.ViewHelper;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lijinliu
 * @date 20180206
 * 饭碗流水
 */
public class DirectInvestLiushuiActivity extends FragmentActivity implements OnClickListener {

    private HorizontalViewPager mVpTotalContainer;
    private List<Fragment> fragments;
    private View indicate_line;
    //返回
    private TextView mTvBtnBack;
    private TextView mTvFwZiChan;
    //全部，买入，回款
    private TextView mTvDiAll;
    private TextView mTvDiBuy;
    private TextView mTvDiPayMent;

    private TotalmoneyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_directinvest_liushui);

        initView();
        initData();
    }

    //方便有盟统计，不用在每个Activity中都从写这两个方法
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
        mTvFwZiChan = (TextView) findViewById(R.id.tv_fanwanzichan);
        mTvDiAll = (TextView) findViewById(R.id.tv_di_all);
        mTvDiBuy = (TextView) findViewById(R.id.tv_di_buy);
        mTvDiPayMent = (TextView) findViewById(R.id.tv_di_payment);
        mTvBtnBack.setOnClickListener(this);
        mTvDiAll.setOnClickListener(this);
        mTvDiBuy.setOnClickListener(this);
        mTvDiPayMent.setOnClickListener(this);
        mVpTotalContainer = (HorizontalViewPager) findViewById(R.id.vp_total_container);

    }

    private void initData() {
        //饭碗资产
        String fwAcctBal = CacheUtils.getString(this, CacheUtils.FWACCTBAL, "0.00");
        mTvFwZiChan.setText(fwAcctBal+"元");

        // 准备数据
        fragments = new ArrayList<Fragment>();
        FanWanLiuShuiFragment allFragment = new FanWanLiuShuiFragment();
        FanWanLiuShuiFragment buyFragment = new FanWanLiuShuiFragment();
        FanWanLiuShuiFragment repayFragment = new FanWanLiuShuiFragment();
        allFragment.setData("3");
        buyFragment.setData("12");
        repayFragment.setData("13");
        fragments.add(allFragment);
        fragments.add(buyFragment);
        fragments.add(repayFragment);
        adapter = new TotalmoneyPagerAdapter(getSupportFragmentManager(), fragments);
        mVpTotalContainer.setAdapter(adapter);
        mVpTotalContainer.setOnPageChangeListener(new MyOnPageChangeListener());
        // 初始化首页界面的数据
        mVpTotalContainer.setCurrentItem(0, false);
        mTvDiAll.setTextColor(getResources().getColor(R.color.global_yellowcolor));
        // 得到屏幕宽度
        int screenW = getWindowManager().getDefaultDisplay().getWidth();
        // 根据屏幕宽度除以fragment的个数初始化指示器宽度
        indicate_line.getLayoutParams().width = screenW / fragments.size();
        indicate_line.requestLayout();
        indicate_line.setVisibility(View.VISIBLE);

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

    @Override
    public void onClick(View v) {
        //初始文字颜色为灰色
        mTvDiAll.setTextColor(getResources().getColor(R.color.text_gray));
        mTvDiBuy.setTextColor(getResources().getColor(R.color.text_gray));
        mTvDiPayMent.setTextColor(getResources().getColor(R.color.text_gray));
        switch (v.getId()) {
            case R.id.invest_back:
                finish();
                break;
            case R.id.tv_di_all:
                mVpTotalContainer.setCurrentItem(0, false);
                mTvDiAll.setTextColor(getResources().getColor(R.color.global_yellowcolor));
                break;
            case R.id.tv_di_buy:
                mVpTotalContainer.setCurrentItem(1, false);
                mTvDiBuy.setTextColor(getResources().getColor(R.color.global_yellowcolor));
                break;
            case R.id.tv_di_payment:
                mVpTotalContainer.setCurrentItem(3, false);
                mTvDiPayMent.setTextColor(getResources().getColor(R.color.global_yellowcolor));
                break;

            default:
                break;
        }
    }
}
