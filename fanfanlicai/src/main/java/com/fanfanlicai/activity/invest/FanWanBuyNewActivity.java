
package com.fanfanlicai.activity.invest;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.activity.NomalWebviewActivity;
import com.fanfanlicai.activity.my.ChongZhiActivity;
import com.fanfanlicai.activity.my.TiXianPiaoActivity;
import com.fanfanlicai.bean.FloatRateBean;
import com.fanfanlicai.bean.HongBaoBean;
import com.fanfanlicai.bean.JiaXiBean;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.global.FanFanApplication;
import com.fanfanlicai.pagers.WebviewActivity;
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
import com.fanfanlicai.view.dialog.CustomProgressDialog;
import com.fanfanlicai.view.dialog.DiaologForNewFwBuy;
import com.fanfanlicai.view.dialog.FwBuyAmontInfoDialog;
import com.fanfanlicai.view.popwindow.RateInfoPopupWin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author lijinliu
 * @date 20180129
 * 新饭碗买入
 */
public class FanWanBuyNewActivity extends BaseActivity implements OnClickListener {

    private CustomProgressDialog progressdialog;
    private TextView get_back;// 返回
    private Button bt_buy;// 买入
    private EditText et_buy_money;// 买入金额
    private TextView tv_jiangjin;// 奖金余额
    private ImageView iv_usereward;// 奖金使用状态图标
    private TextView mTvZong;// 买入总计
    private TextView tv_day;// 投资天数
    private TextView mTvRateBase;//预期年华（基本）
    private TextView tv_fanpiao;//使用饭票
    private TextView tv_hongbao_money;//红包
    private TextView tv_hongbao;//使用红包
    private TextView tv_zhanghuyue;// 账户余额
    private TextView mTvChongzhi;// 充值金额
    private TextView mTvRemainingMoney;
    private TextView mTvztChujieXieyi;
    private TextView mTvWeituoShouquan;
    private TextView mTvfwZqxieyZhuangrang;
    private TextView mTvEndTime;
    private TextView mTvDayDesc;
    private ImageView mIvGou;//是否同意协议
    private ImageView mIvInvestRisk;
    private boolean isAgreed = true;

    private String jiangjin;// 奖金
    private boolean isGou = true;// 当可点击时，奖金已经勾上
    private String day_value;
    private String type;// 类型
    private String seqNo;
    private String maxDailyInvestAmount;
    private String singleMinInvestAmount;
    private String singleMaxInvestAmount;
    private String baseBal;
    private String buyMoney;
    private String mRemainingMoney;
    private String mProductDataShowType;

    private String baseRate;
    private String jiaxi;
    private String ticketId = null;
    private String mReward = null;
    private String useCondition = null;
    private String mFwSwitch;
    private String mProductId;
    private String mActivityRate;
    private String mActivityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fanwanbuy_new);

        progressdialog = new CustomProgressDialog(this, "正在加载数据...");
        initView();
        initData();
    }

    //FanHeBuyActivity启动模式是singleTask，充值成功返回后会回调此方法
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        //更新账户余额
        baseBal = getIntent().getStringExtra("baseBal");
        tv_zhanghuyue.setText(baseBal + "元");
        //根据输入的金额改变按钮状态
        if (StrToNumber.strTodouble(buyMoney) > StrToNumber.strTodouble(baseBal)) {
            bt_buy.setText("余额不足，立即去充值");
        } else {
            bt_buy.setText("确认支付");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (1 == requestCode) {
            if (1 == resultCode) {
                ticketId = data.getStringExtra("ticketId");
                mReward = data.getStringExtra("reward");
                useCondition = data.getStringExtra("useCondition");

                //使用红包
                if (mReward != null) {
                    tv_hongbao_money.setText(MathUtil.subString(mReward, 2) + "元");
                } else {
                    tv_hongbao_money.setText("0.00元");
                }

                //买入总计
                String buyMoney = et_buy_money.getText().toString().trim();
                double zongZc;
                if (isGou) {
                    zongZc = StrToNumber.strTodouble(buyMoney) - StrToNumber.strTodouble(mReward) - StrToNumber.strTodouble(CacheUtils.getString(FanWanBuyNewActivity.this, CacheUtils.REWARDACCTBAL, ""));
                } else {
                    zongZc = StrToNumber.strTodouble(buyMoney) - StrToNumber.strTodouble(mReward);
                }
                if (zongZc <= 0) {
                    mTvZong.setText("0.00元");
                } else {
                    mTvZong.setText(MathUtil.subDouble(zongZc, 2) + "元");
                }
                if (zongZc > StrToNumber.strTodouble(baseBal)) {
                    bt_buy.setText("余额不足，立即去充值");
                } else {
                    bt_buy.setText("确认支付");
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        get_back = (TextView) findViewById(R.id.get_back);// 返回
        get_back.setOnClickListener(this);
        bt_buy = (Button) findViewById(R.id.bt_buy);// 买入按钮
        bt_buy.setOnClickListener(this);
        iv_usereward = (ImageView) findViewById(R.id.iv_usereward);// 奖金使用状态图标
        iv_usereward.setOnClickListener(this);
        et_buy_money = (EditText) findViewById(R.id.et_buy_money);// 买入金额
        tv_jiangjin = (TextView) findViewById(R.id.tv_jiangjin);// 奖金余额
        mTvZong = (TextView) findViewById(R.id.tv_zong);// 投资天数
        tv_day = (TextView) findViewById(R.id.tv_day);// 买入总计
        mTvRateBase = (TextView) findViewById(R.id.tv_rate_base);//预期年华（基本）
        tv_fanpiao = (TextView) findViewById(R.id.tv_fanpiao);//使用饭票
        tv_fanpiao.setOnClickListener(this);
        tv_hongbao_money = (TextView) findViewById(R.id.tv_hongbao_money);//红包
        tv_hongbao = (TextView) findViewById(R.id.tv_hongbao);//使用红包
        tv_hongbao.setOnClickListener(this);

        tv_zhanghuyue = (TextView) findViewById(R.id.tv_zhanghuyue);// 账户余额
        mTvChongzhi = (TextView) findViewById(R.id.tv_chongzhi);// 充值金额
        mTvChongzhi.setOnClickListener(this);

        mTvRemainingMoney = (TextView) findViewById(R.id.tv_remaining_money);
        mTvztChujieXieyi = (TextView) findViewById(R.id.tv_zt_chujie_xieyi);
        mTvztChujieXieyi.setOnClickListener(this);
        mTvWeituoShouquan = (TextView) findViewById(R.id.tv_weituo_shouquan);
        mTvWeituoShouquan.setOnClickListener(this);
        mTvfwZqxieyZhuangrang = (TextView) findViewById(R.id.tv_fw_zqxiey_zhuangrang);
        mTvfwZqxieyZhuangrang.setOnClickListener(this);

        mTvEndTime = (TextView) findViewById(R.id.tv_end_time);
        mIvGou = (ImageView) findViewById(R.id.iv_gou);
        mIvInvestRisk = (ImageView)findViewById(R.id.iv_invest_risk);
        mIvGou.setOnClickListener(this);
        mTvDayDesc = (TextView) findViewById(R.id.tv_day_desc);
    }

    private void initData() {
        //从上个界面获取到intent
        Intent intent = getIntent();
        // 加息
        baseRate = intent.getStringExtra("baseRate");
        jiaxi = intent.getStringExtra("jiaxi");
        mTvRateBase.setText(MathUtil.subDouble((Double.parseDouble(baseRate) + Double.parseDouble(jiaxi) ) + FanFanApplication.jiaxi_rate, 2)+ "%");
        // seqNo
        seqNo = intent.getStringExtra("seqNo");
        // 投资天数
        day_value = intent.getStringExtra("investDays");
        tv_day.setText(day_value + "天");
        // 类型
        type = intent.getStringExtra("type");
        // 单笔投资最大值
        maxDailyInvestAmount = intent.getStringExtra("maxDailyInvestAmount");
        // 单笔投资最小值
        singleMinInvestAmount = intent.getStringExtra("singleMinInvestAmount");
        // 单笔投资上限
        singleMaxInvestAmount = intent.getStringExtra("singleMaxInvestAmount");

        //账户余额
        baseBal = intent.getStringExtra("baseBal");
        tv_zhanghuyue.setText(baseBal + "元");

        //获取到奖金
        jiangjin = CacheUtils.getString(this, CacheUtils.REWARDACCTBAL, "");
        tv_jiangjin.setText(MathUtil.subString(jiangjin, 2) + "元");

        mRemainingMoney = intent.getStringExtra("remainingMoney");
        mTvRemainingMoney.setText(mRemainingMoney + "元");

        mFwSwitch = intent.getStringExtra("fwSwitch");
        mProductId = intent.getStringExtra("productId");
        mProductDataShowType = intent.getStringExtra("productDataShowType");
        if ("zt".equals(mProductDataShowType)) {
            mTvDayDesc.setText("出借期限");
            mTvztChujieXieyi.setVisibility(View.VISIBLE);
            mTvWeituoShouquan.setVisibility(View.GONE);
            mTvfwZqxieyZhuangrang.setVisibility(View.GONE);
        } else {
            mTvDayDesc.setText("锁定期限");
            mTvztChujieXieyi.setVisibility(View.GONE);
            mTvWeituoShouquan.setVisibility(View.VISIBLE);
            mTvfwZqxieyZhuangrang.setVisibility(View.VISIBLE);
        }
        mActivityRate = intent.getStringExtra("activityRate");
        mActivityName = intent.getStringExtra("activityName");

        findViewById(R.id.iv_question_mark).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                RateInfoPopupWin popupWin = new RateInfoPopupWin(FanWanBuyNewActivity.this, mProductDataShowType, baseRate, mActivityName, mActivityRate, jiaxi, FanFanApplication.jiaxi_rate + "");
                popupWin.showAsDropDown(FanWanBuyNewActivity.this.findViewById(R.id.tv_rate_base), 0, DensityUtil.dip2px(FanWanBuyNewActivity.this, 10));
            }
        });
        mTvEndTime.setText("(到期日 " + intent.getStringExtra("endTime") + ")");
        if (StrToNumber.strTodouble(singleMinInvestAmount) * 2 > StrToNumber.strTodouble(mRemainingMoney)) {
            et_buy_money.setHint("请输入不小于" + mRemainingMoney + "元的金额");
        } else {
            et_buy_money.setHint("请输入不小于" + singleMinInvestAmount + "元的金额");
        }
        initEvents();
    }

    private void initEvents() {
        et_buy_money.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    //mIvInvestRisk.setVisibility(View.GONE);
                } else {
                    // 此处为失去焦点时的处理内容
                    //mIvInvestRisk.setVisibility(View.VISIBLE);
                }
            }
        });

        // 给买入金额设置监听，为了使奖金使用状态图标根据不同的输入值变化而变化
        et_buy_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        et_buy_money.setText(s);
                        et_buy_money.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    et_buy_money.setText(s);
                    et_buy_money.setSelection(2);
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        et_buy_money.setText(s.subSequence(0, 1));
                        et_buy_money.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //只要值改变，红包id和值就清零
                ticketId = null;
                mReward = null;
                useCondition = null;
                tv_hongbao_money.setText("0.00元");


                //奖金图标的显示根据输入值的变化而变化，总计也是根据输入值的变化而变化的
                buyMoney = et_buy_money.getText().toString().trim();

                if (!TextUtils.isEmpty(buyMoney)) {
                    double zongZc;
                    if (StrToNumber.strTodouble(buyMoney) >= StrToNumber.strTodouble(CacheUtils.getString(FanWanBuyNewActivity.this, CacheUtils.REWARDACCTBAL, ""))) {
                        iv_usereward.setImageResource(R.drawable.usereward_yes);
                        // 买入总计
                        zongZc = StrToNumber.strTodouble(buyMoney) - StrToNumber.strTodouble(mReward) - StrToNumber.strTodouble(CacheUtils.getString(FanWanBuyNewActivity.this, CacheUtils.REWARDACCTBAL, ""));
                        //勾选，获取奖金值
                        jiangjin = CacheUtils.getString(FanWanBuyNewActivity.this, CacheUtils.REWARDACCTBAL, "");
                    } else {//不勾选奖金
                        iv_usereward.setImageResource(R.drawable.usereward);
                        // 买入总计
                        zongZc = StrToNumber.strTodouble(buyMoney) - StrToNumber.strTodouble(mReward);
                        //不勾选，把奖金改为0
                        jiangjin = "0.00";
                    }
                    if (zongZc <= 0) {
                        mTvZong.setText("0.00元");
                    } else {
                        mTvZong.setText(MathUtil.subDouble(zongZc, 2) + "元");
                    }
                    //根据输入的金额改变按钮状态
                    zongZc = StrToNumber.strTodouble(buyMoney) - StrToNumber.strTodouble(mReward) - StrToNumber.strTodouble(CacheUtils.getString(FanWanBuyNewActivity.this, CacheUtils.REWARDACCTBAL, ""));
                    if (zongZc > StrToNumber.strTodouble(baseBal)) {
                        bt_buy.setText("余额不足，立即去充值");
                    } else {
                        bt_buy.setText("确认支付");
                    }
                } else {
                    // 买入总计
                    mTvZong.setText("0.00元");
                }
            }
        });
    }

    @Override
    public void onResume() {//返回回来需要刷新界面
        // TODO Auto-generated method stub
        super.onResume();
        mTvRateBase.setText(MathUtil.subDouble((Double.parseDouble(baseRate) + Double.parseDouble(jiaxi) ) + FanFanApplication.jiaxi_rate, 2)+ "%");
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        ticketId = null;
        mReward = null;
        useCondition = null;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.get_back:
                finish();
                //返回后，需要把加息票的值和id变成初始状态，并刷新饭碗界面
                ticketId = null;
                mReward = null;
                useCondition = null;
                break;
            case R.id.iv_usereward:
                if (StrToNumber.strTodouble(buyMoney) >= StrToNumber.strTodouble(CacheUtils.getString(FanWanBuyNewActivity.this, CacheUtils.REWARDACCTBAL, ""))) {
                    isGou = !isGou;
                    CacheUtils.putBoolean(this, "isGou", isGou);
                    double zongZc;
                    if (isGou) {//勾选奖金
                        iv_usereward.setImageResource(R.drawable.usereward_yes);
                        //买入总计
                        zongZc = StrToNumber.strTodouble(buyMoney) - StrToNumber.strTodouble(mReward)  - StrToNumber.strTodouble(CacheUtils.getString(FanWanBuyNewActivity.this, CacheUtils.REWARDACCTBAL, ""));
                        if (zongZc <= 0) {
                            mTvZong.setText("0.00元");
                        } else {
                            mTvZong.setText(MathUtil.subDouble(zongZc, 2) + "元");
                        }
                        //勾选，获取奖金值
                        jiangjin = CacheUtils.getString(FanWanBuyNewActivity.this, CacheUtils.REWARDACCTBAL, "");
                    } else {//不勾选奖金
                        iv_usereward.setImageResource(R.drawable.usereward);
                        //不勾选，把奖金改为0
                        jiangjin = "0.00";

                        //一开始勾选了红包，如果去掉奖金后，输入值加奖金得钱小于红包的使用条件，则红包id和值就清零
                        if (StrToNumber.strTodouble(buyMoney) < StrToNumber.strTodouble(useCondition)) {
                            ticketId = null;
                            mReward = null;
                            useCondition = null;
                            tv_hongbao_money.setText("0.00元");
                        }
                        //买入总计
                        zongZc = StrToNumber.strTodouble(buyMoney) - StrToNumber.strTodouble(mReward);
                        if (zongZc <= 0) {
                            mTvZong.setText("0.00元");
                        } else {
                            mTvZong.setText(MathUtil.subDouble(zongZc, 2) + "元");
                        }
                    }
                    if (zongZc > StrToNumber.strTodouble(baseBal)) {
                        bt_buy.setText("余额不足，立即去充值");
                    } else {
                        bt_buy.setText("确认支付");
                    }
                }
                break;
            case R.id.tv_chongzhi:
                intent = new Intent(FanWanBuyNewActivity.this, ChongZhiActivity.class);
                intent.putExtra("from", "fw");
                startActivity(intent);
                break;
            case R.id.bt_buy://买入
                if (!isAgreed) {
                    ToastUtils.toastshort("您还未同意饭饭买入协议！");
                    return;
                }
                double zongZc = StrToNumber.strTodouble(buyMoney) - StrToNumber.strTodouble(mReward) - (isGou ? StrToNumber.strTodouble(CacheUtils.getString(FanWanBuyNewActivity.this, CacheUtils.REWARDACCTBAL, "")) : 0.00);
                if (zongZc < 0) {
                    ToastUtils.toastshort("您的单笔买入金额不能小于奖金金额加红包金额!");
                    return;
                }

                buyMoney = et_buy_money.getText().toString().trim();
                //amount指输入金额和奖金之和
                final String amountBuy = MathUtil.subDouble(StrToNumber.strTodouble(buyMoney), 2) + "";
                //剩余 买入金额
                double chargeBalance = StrToNumber.strTodouble(mRemainingMoney) - StrToNumber.strTodouble(buyMoney) ;
                if (chargeBalance > 0 && chargeBalance < StrToNumber.strTodouble(singleMinInvestAmount)) {
                    FwBuyAmontInfoDialog fwBuyDialog = new FwBuyAmontInfoDialog(FanWanBuyNewActivity.this, R.style.YzmDialog);
                    fwBuyDialog.show();
                    return;
                }

                if (!TextUtils.isEmpty(buyMoney)) {
                    //如果买入金额小于最小单笔限额，走正常买入
                    if (StrToNumber.strTodouble(buyMoney) < StrToNumber.strTodouble(singleMinInvestAmount)) {
                        ToastUtils.toastshort("您的单笔买入金额不能小于" + singleMinInvestAmount);
                        return;
                    }
//					if (StrToNumber.strTodouble(buyMoney) > StrToNumber.strTodouble(singleMaxInvestAmount)) {
//						ToastUtils.toastshort("您的单笔买入金额不能大于"+singleMaxInvestAmount);
//						return;
//					}
                    //根据输入的金额，点击按钮进入不同页面
                    if (zongZc > StrToNumber.strTodouble(baseBal)) {
                        intent = new Intent(FanWanBuyNewActivity.this, ChongZhiActivity.class);
                        intent.putExtra("from", "fw");
                        startActivity(intent);
                    } else {
                            hasHongBaoForUse(buyMoney, amountBuy);
                    }

                } else {
                    ToastUtils.toastshort("请输入不小于" + singleMinInvestAmount + "的买入金额");
                }
                break;
            case R.id.tv_fanpiao://使用加息票
                intent = new Intent(this, UseFanPiaoActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_hongbao://使用红包
                //amount指输入金额和奖金之和
                String amount = MathUtil.subDouble(StrToNumber.strTodouble(buyMoney) + StrToNumber.strTodouble(jiangjin), 2) + "";
                if (!TextUtils.isEmpty(amount)) {
                    if (StrToNumber.strTodouble(amount) < StrToNumber.strTodouble(singleMinInvestAmount)) {
                        ToastUtils.toastshort("请输入不小于" + singleMinInvestAmount + "的买入金额");
                    } else {
                        intent = new Intent(this, UseHongBaoActivity.class);
                        intent.putExtra("type", type);
                        intent.putExtra("ticketId", ticketId);
                        intent.putExtra("amount", amount);
                        startActivityForResult(intent, 1);
                    }

                } else {
                    ToastUtils.toastshort("请输入不小于" + singleMinInvestAmount + "的买入金额");
                }

                break;
            case R.id.tv_zt_chujie_xieyi:
                intent = new Intent(FanWanBuyNewActivity.this,NomalWebviewActivity.class);
                intent.putExtra("title", "饭饭金服出借协议");
                intent.putExtra("url", ConstantUtils.DEBTCONSULTATIONZT_URL);
                startActivity(intent);
                break;
            case R.id.tv_weituo_shouquan:
                intent = new Intent(FanWanBuyNewActivity.this,NomalWebviewActivity.class);
                intent.putExtra("title", "授权委托书");
                intent.putExtra("url", ConstantUtils.AGREEMENTWARRANT_URL);
                startActivity(intent);
                break;
            case R.id.tv_fw_zqxiey_zhuangrang:
                intent = new Intent(FanWanBuyNewActivity.this,NomalWebviewActivity.class);
                intent.putExtra("title", "饭饭金服债权转让协议");
                intent.putExtra("url", ConstantUtils.DEBTCONSULTATIONFW_URL);
                startActivity(intent);
                break;
            case R.id.iv_gou:
                isAgreed = !isAgreed;
                if (isAgreed) {
                    mIvGou.setImageResource(R.drawable.gou_hou);
                } else {
                    mIvGou.setImageResource(R.drawable.gou_qian);
                }
                break;
            default:
                break;
        }
    }

    private void hasHongBaoForUse(final String buyMoney, final String amountBuy) {
        progressdialog.show();
        Map<String, String> map = SortRequestData.getmap();
        String token = CacheUtils.getString(this, "token", null);
        // 如果没有登录，直接return，不访问网络了
        if (TextUtils.isEmpty(token)) {
            return;
        }
        map.put("status", 1 + "");
        map.put("proCode", type);
        map.put("amount", amountBuy);
        map.put("pageNum", "1");
        map.put("pageSize", "1");
        map.put("token", token);
        String requestData = SortRequestData.sortString(map);
        String signData = SignUtil.sign(requestData);
        map.put("sign", signData);
        VolleyUtil.sendJsonRequestByPost(ConstantUtils.REDBAG_URL,
                null, map, new HttpBackBaseListener() {

                    @Override
                    public void onSuccess(String string) {
                        LogUtils.i("是否有红包可用==" + string);
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
                                    //list
                                    JSONArray getList = data.getJSONArray("list");
                                    ArrayList<HongBaoBean> hongBaoList = (ArrayList<HongBaoBean>) JSONArray.parseArray(getList.toJSONString(), HongBaoBean.class);

                                    if (hongBaoList.size() != 0 && ticketId == null) {//买入时有可用红包并且目前还没有选中任何红包时弹窗提醒
                                        DiaologForNewFwBuy hasHongBaoOrJiaXiPiaoDialog = new DiaologForNewFwBuy(FanWanBuyNewActivity.this,  R.style.YzmDialog, buyMoney, jiangjin, ticketId, seqNo, jiaxi, mReward, mFwSwitch, mProductId);
                                        hasHongBaoOrJiaXiPiaoDialog.show();
                                    } else {
                                        hasJiaXiPiaoForUse(buyMoney);
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


    private void hasJiaXiPiaoForUse(final String buyMoney) {
        progressdialog.show();
        Map<String, String> map = SortRequestData.getmap();
        String token = CacheUtils.getString(this, "token", null);
        // 如果没有登录，直接return，不访问网络了
        if (TextUtils.isEmpty(token)) {
            return;
        }
        map.put("status", 1 + "");
        map.put("proCode", type);
        map.put("pageNum", "1");
        map.put("pageSize", "1");
        map.put("token", token);
        String requestData = SortRequestData.sortString(map);
        String signData = SignUtil.sign(requestData);
        map.put("sign", signData);
        VolleyUtil.sendJsonRequestByPost(ConstantUtils.RATECOUPON_URL,
                null, map, new HttpBackBaseListener() {

                    @Override
                    public void onSuccess(String string) {
                        LogUtils.i("饭碗可使用=选择==" + string);
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
                                    //list
                                    JSONArray getList = data.getJSONArray("list");
                                    ArrayList<JiaXiBean> jiaXiPiaoList = (ArrayList<JiaXiBean>) JSONArray.parseArray(getList.toJSONString(), JiaXiBean.class);
                                    if (jiaXiPiaoList.size() != 0 && FanFanApplication.jiaxi_rate == 0) {//买入时有可用加息票并且目前还没有选中任何加息票时弹窗提醒
                                        DiaologForNewFwBuy hasHongBaoOrJiaXiPiaoDialog = new DiaologForNewFwBuy(FanWanBuyNewActivity.this, R.style.YzmDialog, buyMoney, jiangjin, ticketId, seqNo, jiaxi, mReward, mFwSwitch, mProductId);
                                        hasHongBaoOrJiaXiPiaoDialog.show();
                                    } else {
                                        getDataFromServer(buyMoney, jiangjin, seqNo);
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


    /**
     *
     * @param buyMoney
     * @param jiangjin
     * @param seqNo
     */
    private void getDataFromServer(final String buyMoney, final String jiangjin, final String seqNo) {
        final long startTime = System.currentTimeMillis();
        //获取uuid
        if (!progressdialog.isShowing()) {
            progressdialog.show();
        }
        Map<String, String> map = SortRequestData.getmap();
        String token = CacheUtils.getString(FanWanBuyNewActivity.this, "token", null);
        map.put("token", token);
        //买入 红包满减
        double amount = StrToNumber.strTodouble(buyMoney) - StrToNumber.strTodouble(mReward) - StrToNumber.strTodouble(jiangjin);
        if (amount <= 0) {
            map.put("amount", "0.00");
        } else {
            map.put("amount", MathUtil.subDouble(amount, 2) + "");
        }
        map.put("buyTotalAmount", MathUtil.subDouble(StrToNumber.strTodouble(buyMoney), 2) + "");
        map.put("reward", jiangjin);
        map.put("fwAddRateId", FanFanApplication.jiaxi_ID);
        map.put("redbagTicketId", ticketId);
        map.put("redbagAmount", mReward);
        map.put("seqNo", seqNo);
        map.put("fwSwitch", mFwSwitch);
        map.put("productId", mProductId);
//Log.e("Buy-Main:",map.toString());
        String requestData = SortRequestData.sortString(map);
        String signData = SignUtil.sign(requestData);
        map.put("sign", signData);
        VolleyUtil.sendJsonRequestByPost(ConstantUtils.SUBMITZTINVEST_URL, null, map,
                new HttpBackBaseListener() {

                    @Override
                    public void onSuccess(String string) {
                        LogUtils.i("饭碗确认支付===" + string);
                        long endTime = System.currentTimeMillis();
                        if (endTime - startTime > 12000) {
                            progressdialog.dismiss();
                            Intent intent = new Intent(FanWanBuyNewActivity.this, BuyShenQingActivity.class);
                            startActivity(intent);
                        } else {
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


                                        // 买入总计
                                        String investTotalAmount = data.getString("investTotalAmount");

                                        Intent intent = new Intent(FanWanBuyNewActivity.this, FanHeBuySuccessActivity.class);
                                        intent.putExtra("zong_money", investTotalAmount);
                                        startActivity(intent);

                                    } else {
                                        ToastUtils.toastshort("加载数据异常！");
                                    }
                                }
                            } else if ("4003".equals(code)) {

                                Intent intent = new Intent(FanWanBuyNewActivity.this, BuyShenQingActivity.class);
                                startActivity(intent);

                            } else if ("666666".equals(code)) {

                            } else {
                                String msg = json.getString("msg");
                                ToastUtils.toastshort(msg);
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        progressdialog.dismiss();
                        ToastUtils.toastshort("网络请求失败");
                    }
                });
    }

}
