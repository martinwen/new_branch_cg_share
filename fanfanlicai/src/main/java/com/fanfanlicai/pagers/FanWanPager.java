package com.fanfanlicai.pagers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.home.InviteFriActivity;
import com.fanfanlicai.activity.home.LeiJiShouYiActivity;
import com.fanfanlicai.activity.invest.FanWanDetailActivity;
import com.fanfanlicai.activity.invest.FanWanRateActivity;
import com.fanfanlicai.activity.my.InvestActivity;
import com.fanfanlicai.activity.registerandlogin.HomeLoginActivity;
import com.fanfanlicai.bean.FloatRateBean;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.global.FanFanApplication;
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
import com.fanfanlicai.view.ProgressBar.ColorArcProgressBar;
import com.fanfanlicai.view.dialog.CustomProgressDialog;
import com.fanfanlicai.view.lockPattern.LockPatternUtil;

import java.util.ArrayList;
import java.util.Map;

public class FanWanPager extends BasePager implements OnClickListener {

    private CustomProgressDialog progressdialog;
    private TextView tv_base;//基本利率
    private TextView tv_jiaxi;//加息
    private TextView tv_shouyixiangqing;//收益详情
    private TextView tv_day;//投资期限
    private Button bt_buy;//买入
    private RelativeLayout rl_daishou;//待收收益
    private RelativeLayout rl_jiangjin;//奖金余额
    private RelativeLayout rl_leiji;//累计收益
    private RelativeLayout rl_zongzichan;//饭碗资产
    private TextView tv_daishou;
    private TextView tv_jiangjin;
    private TextView tv_leiji;
    private TextView tv_fanwan;
    private ColorArcProgressBar fanwanbar;//进度条
    private String day_value = "30";
    private PopupWindow pop;// 日期选择的弹窗
    private ListView listView;
    private LinearLayout ll_fanwanbg;//饭碗背景图片
    private TextView tv_fanwanname;//饭碗名字
    private TextView tv_shouyi;//预期年化收益率（文字颜色）
    private TextView tv_touzi;//投资天数（文字颜色）
    private TextView tv_tian;//天（文字颜色）
    private TextView tv_daoqi_info;//到期日（文字颜色）
    private TextView tv_daoqi;//到期日
    private TextView tv_xiangmuyue;//项目余额
    private TextView tv_min_invest;//起投金额
    private String mBorrowSalableBal;
    private String mMinInvestAmount;
    private double baseRate;
    //饭碗活动加息
    private double activityRate;
    //饭碗活动加息券ID(饭碗买入第一步需要此参数)
    private String mPid = "0";
    private String fwAddRateId = "0";
    private ArrayList<FloatRateBean> list = new ArrayList<FloatRateBean>();
    private String type = "fw_iron";

    private int positon;

    public FanWanPager(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected View initView() {
        LogUtils.i("饭碗投资initView");
        View view = View.inflate(mContext, R.layout.pager_invest_fanwan, null);
        progressdialog = new CustomProgressDialog(mContext, "正在加载数据...");
        fanwanbar = (ColorArcProgressBar) view.findViewById(R.id.fanwanbar);
        tv_base = (TextView) view.findViewById(R.id.tv_base);
        tv_jiaxi = (TextView) view.findViewById(R.id.tv_jiaxi);
        tv_shouyixiangqing = (TextView) view.findViewById(R.id.tv_shouyixiangqing);
        tv_shouyixiangqing.setOnClickListener(this);
        tv_day = (TextView) view.findViewById(R.id.tv_day);
        tv_day.setOnClickListener(this);
        ll_fanwanbg = (LinearLayout) view.findViewById(R.id.ll_fanwanbg);
        tv_fanwanname = (TextView) view.findViewById(R.id.tv_fanwanname);
        tv_shouyi = (TextView) view.findViewById(R.id.tv_shouyi);
        tv_touzi = (TextView) view.findViewById(R.id.tv_touzi);
        tv_tian = (TextView) view.findViewById(R.id.tv_tian);
        tv_daoqi_info = (TextView) view.findViewById(R.id.tv_daoqi_info);
        tv_daoqi = (TextView) view.findViewById(R.id.tv_daoqi);
        bt_buy = (Button) view.findViewById(R.id.bt_buy);
        bt_buy.setOnClickListener(this);
        rl_daishou = (RelativeLayout) view.findViewById(R.id.rl_daishou);
        rl_jiangjin = (RelativeLayout) view.findViewById(R.id.rl_jiangjin);
        rl_leiji = (RelativeLayout) view.findViewById(R.id.rl_leiji);
        rl_zongzichan = (RelativeLayout) view.findViewById(R.id.rl_zongzichan);
        rl_daishou.setOnClickListener(this);
        rl_jiangjin.setOnClickListener(this);
        rl_leiji.setOnClickListener(this);
        rl_zongzichan.setOnClickListener(this);
        tv_daishou = (TextView) view.findViewById(R.id.tv_daishou);
        tv_jiangjin = (TextView) view.findViewById(R.id.tv_jiangjin);
        tv_leiji = (TextView) view.findViewById(R.id.tv_leiji);
        tv_fanwan = (TextView) view.findViewById(R.id.tv_fanwan);
        tv_xiangmuyue = (TextView) view.findViewById(R.id.tv_xiangmuyue);
        tv_min_invest = (TextView) view.findViewById(R.id.tv_min_invest);
        return view;
    }

    @Override
    public void initData() {
        LogUtils.i("饭碗投资initData");
        getDataFromServer("");
    }

    private void getDataFromServer(String investDays) {
        if (!progressdialog.isShowing()) {
            progressdialog.showis();
        }
        Map<String, String> map = SortRequestData.getmap();
        String token = CacheUtils.getString(mContext, "token", null);
        map.put("token", token);
        map.put("investDays", investDays);
        String requestData = SortRequestData.sortString(map);
        String signData = SignUtil.sign(requestData);
        map.put("sign", signData);
        VolleyUtil.sendJsonRequestByPost(ConstantUtils.FWINVESTINFO_URL, null, map, new HttpBackBaseListener() {
            @Override
            public void onSuccess(String string) {
                LogUtils.longStr("饭碗投资页面===", string);
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
                            String token = CacheUtils.getString(mContext, "token", null);
                            // 基础年化收益率
                            baseRate = data.getDouble("baseRate");
                            tv_base.setText(MathUtil.subDouble((baseRate), 2) + "%");

                            // 默认显示第几天
                            String homeDay = data.getString("homeDay");
                            day_value = homeDay;
                            tv_day.setText(day_value);//投资期限

                            day_value = TextUtils.isEmpty(day_value) ? "60" : day_value;
                            fanwanbar.setCurrentValues(Float.parseFloat(day_value));
                            for (int i = 0; i < list_day.length; i++) {
                                if (day_value.equals(list_day[i])) {
                                    positon = i;
                                }
                            }

                            /**直投**/
                            if (data.containsKey("productDataShowType") && "zt".equals(data.getString("productDataShowType"))) {
                                mPid = data.getString("pid");
                                type = data.getString("productCode");
                                activityRate = data.getDouble("activityRate");
                                mMinInvestAmount = data.getString("minInvestAmount");
                                mBorrowSalableBal = data.getString("borrowSalableBal");

                                //zTjiaxi = MathUtil.subDouble(activityRate, 2) + "";
                                if (!TextUtils.isEmpty(token) && FanFanApplication.jiaxi_rate != 0) {
                                    tv_jiaxi.setText(MathUtil.subDouble(activityRate + FanFanApplication.jiaxi_rate, 2) + "%");//加息
                                    fanwanbar.setValue(MathUtil.subDouble((baseRate + activityRate) + FanFanApplication.jiaxi_rate, 2) + "%");//总收益率
                                } else {
                                    tv_jiaxi.setText(MathUtil.subDouble((activityRate), 2) + "%");//加息
                                    fanwanbar.setValue(MathUtil.subDouble((baseRate + activityRate), 2) + "%");//总收益率
                                }
                                tv_daoqi.setText(data.getString("endTime"));//到期日
                            } else {
                                /**老饭碗**/
                                JSONArray getList = data.getJSONArray("floatRate");
                                list = (ArrayList<FloatRateBean>) JSONArray.parseArray(getList.toJSONString(), FloatRateBean.class);

                                type = list.get(positon).getProCode();
                                mPid = "0";

                                switch (type) {
                                    case "fw_iron":
                                        activityRate = data.getDouble("fwIronActivityRate");
                                        mBorrowSalableBal = data.getString("fwIronBorrowSalableBal");
                                        mMinInvestAmount = data.getString("fwIronMinInvestAmount");
                                        fwAddRateId = data.getString("fwIronAddRateId");
                                        break;
                                    case "fw_copper":
                                        activityRate = data.getDouble("fwCopperActivityRate");
                                        mBorrowSalableBal = data.getString("fwCopperBorrowSalableBal");
                                        mMinInvestAmount = data.getString("fwCopperMinInvestAmount");
                                        fwAddRateId = data.getString("fwCopperAddRateId");
                                        break;
                                    case "fw_silver":
                                        activityRate = data.getDouble("fwSilverActivityRate");
                                        mBorrowSalableBal = data.getString("fwSilverBorrowSalableBal");
                                        mMinInvestAmount = data.getString("fwSilverMinInvestAmount");
                                        fwAddRateId = data.getString("fwSilverAddRateId");
                                        break;
                                    case "fw_gold":
                                        activityRate = data.getDouble("fwGoldActivityRate");
                                        mBorrowSalableBal = data.getString("fwGoldBorrowSalableBal");
                                        mMinInvestAmount = data.getString("fwGoldMinInvestAmount");
                                        fwAddRateId = data.getString("fwGoldAddRateId");
                                        break;
                                }
                                //zZjiaxi = MathUtil.subDouble((activityRate + list.get(positon).getFloatRate()), 2) + "";
                                //一进页面就初始化浮动收益率
                                if (!TextUtils.isEmpty(token) && FanFanApplication.jiaxi_rate != 0) {
                                    tv_jiaxi.setText(MathUtil.subDouble((activityRate + list.get(positon).getFloatRate()) + FanFanApplication.jiaxi_rate, 2) + "%");//加息
                                    fanwanbar.setValue(MathUtil.subDouble((baseRate + activityRate + list.get(positon).getFloatRate()) + FanFanApplication.jiaxi_rate, 2) + "%");//总收益率
                                } else {
                                    tv_jiaxi.setText(MathUtil.subDouble((activityRate + list.get(positon).getFloatRate()), 2) + "%");//加息
                                    fanwanbar.setValue(MathUtil.subDouble((baseRate + activityRate + list.get(positon).getFloatRate()), 2) + "%");//总收益率
                                }
                                tv_daoqi.setText(list.get(positon).getEndTime());//到期日
                            }

                            /** 已登录**/
                            if (!TextUtils.isEmpty(token)) {
                                // 饭碗待收收益
                                String waitRepayAmount = data.getString("waitRepayAmount");
                                tv_daishou.setText(MathUtil.subString(waitRepayAmount, 2) + "");

                                // 奖金余额
                                String rewardAcctBal = data.getString("rewardAcctBal");
                                tv_jiangjin.setText(MathUtil.subString(rewardAcctBal, 2) + "");
                                CacheUtils.putString(mContext, "rewardAcctBal", rewardAcctBal);

                                // 饭碗累计收益
                                String totalIncome = data.getString("totalIncome");
                                tv_leiji.setText(MathUtil.subString(totalIncome, 2) + "");

                                // 饭碗资产
                                String fwAcctBal = data.getString("fwAcctBal");
                                CacheUtils.putString(mContext, "fwAcctBal", fwAcctBal);
                                tv_fanwan.setText(MathUtil.subString(fwAcctBal, 2) + "");
                            } else {
                                tv_daishou.setText("0.00");
                                tv_jiangjin.setText("0.00");
                                tv_leiji.setText("0.00");
                                tv_fanwan.setText("0.00");
                                fwAddRateId = "0";
                            }

                            refershUiData(type);
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
                progressdialog.dismiss();
            }
        });
    }

    /**
     * 饭碗项目介绍 计息方式等信息
     */
    private void getFwProductInfo() {
        if (!progressdialog.isShowing()) {
            progressdialog.showis();
        }
        Map<String, String> map = SortRequestData.getmap();
        String token = CacheUtils.getString(mContext, "token", null);
        map.put("token", token);
        map.put("pid", mPid);
        map.put("fwAddRateId", fwAddRateId);
        map.put("investDays", day_value);
        map.put("proCode", type);
        String requestData = SortRequestData.sortString(map);
        String signData = SignUtil.sign(requestData);
        map.put("sign", signData);
        VolleyUtil.sendJsonRequestByPost(ConstantUtils.GETFWPRODUCTINFOD_URL, null, map,
                new HttpBackBaseListener() {
                    @Override
                    public void onSuccess(String string) {
                        LogUtils.i("饭碗项目信息===" + string);
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
                                if (isSuccess) {
                                    Intent intent = new Intent(mContext, FanWanDetailActivity.class);
                                    intent.putExtra("jsonData", datastr);
                                    intent.putExtra("productCode", type);
                                    mContext.startActivity(intent);
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

    @Override
    public void onClick(View v) {

        String token = CacheUtils.getString(mContext, "token", null);
        switch (v.getId()) {
            case R.id.tv_shouyixiangqing:
                if (TextUtils.isEmpty(token)) {//未登录时，点击进入登录页面
                    mContext.startActivity(new Intent(mContext, HomeLoginActivity.class));
                } else {//登录
                    Intent intent = new Intent(mContext, FanWanRateActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra("investDays", day_value);
                    intent.putExtra("proCode", type);
                    mContext.startActivity(intent);
                }
                break;
            case R.id.tv_day://投资期限
                /**初始化**/
                if (listView == null) {
                    listView = initListview();
                }
                // 记住点击位置
                listView.setSelection(positon - 1);
                // 点击时，弹出Popupwindow
                pop = new PopupWindow(listView, LockPatternUtil.dip2px(mContext, 40), LockPatternUtil.dip2px(mContext, 90));
                // 设置焦点
                pop.setFocusable(true);
                // 设置背景，为了点击外面时，把Popupwindow消失
                pop.setBackgroundDrawable(new BitmapDrawable());
                // 展示Popupwindow,显示在输入框的下面
                pop.showAsDropDown(tv_day, -LockPatternUtil.dip2px(mContext, 3), -LockPatternUtil.dip2px(mContext, 55));
                pop.setOnDismissListener(new OnDismissListener() {

                    @Override
                    public void onDismiss() {// 选择投资天数后，progessbar进度条显示天数，弹窗消失
                        fanwanbar.setCurrentValues(Float.parseFloat(day_value));
                    }
                });
                break;
            case R.id.bt_buy://买入
                if (TextUtils.isEmpty(token)) {
                    mContext.startActivity(new Intent(mContext, HomeLoginActivity.class));
                } else {//登录
                    //只要是从这个界面点进去的登录都需要标记为1，这样登陆成功后才能跳转回来，并刷新数据
                    ConstantUtils.touziflag = 1;
                    ConstantUtils.fanheorfanwanorfantongflag = 1;
                    getFwProductInfo();
                }
                break;
            case R.id.rl_daishou://待收收益
                if (TextUtils.isEmpty(token)) {
                    mContext.startActivity(new Intent(mContext, HomeLoginActivity.class));
                } else {//登录
                    ConstantUtils.yitouxiangmuflag = 1;
                    mContext.startActivity(new Intent(mContext, InvestActivity.class));
                }
                break;

            case R.id.rl_jiangjin://奖金余额
                if (TextUtils.isEmpty(token)) {
                    mContext.startActivity(new Intent(mContext, HomeLoginActivity.class));
                } else {//登录
                    mContext.startActivity(new Intent(mContext, InviteFriActivity.class));
                }
                break;
            case R.id.rl_leiji://累计收益
                if (TextUtils.isEmpty(token)) {
                    mContext.startActivity(new Intent(mContext, HomeLoginActivity.class));
                } else {//登录
                    ConstantUtils.shouyiflag = 1;
                    mContext.startActivity(new Intent(mContext, LeiJiShouYiActivity.class));
                }
                break;
            case R.id.rl_zongzichan://饭碗资产
                if (TextUtils.isEmpty(token)) {
                    mContext.startActivity(new Intent(mContext, HomeLoginActivity.class));
                } else {//登录
                    ConstantUtils.yitouxiangmuflag = 1;
                    mContext.startActivity(new Intent(mContext, InvestActivity.class));
                }
                break;
            default:
                break;
        }
    }

    private ListView initListview() {
        listView = new ListView(mContext);
        // 去掉点击效果
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        // 去掉分割线
        listView.setDividerHeight(0);
        // 去掉滑动块
        listView.setVerticalScrollBarEnabled(false);
        // 设置背景
        listView.setBackgroundResource(R.drawable.listview_fw_background);
        // 设置Adapter
        listView.setAdapter(new ArrayAdapter<String>(mContext, R.layout.items_investday, R.id.tv_day, list_day));
        // 设置条目点击事件
        listView.setOnItemClickListener(new MyOnItemClickListener());

        return listView;
    }

    String[] list_day = {"30", "60", "90", "180", "270", "360"};

    // 根据点击选择投资天数
    private class MyOnItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // 选中天数后弹窗消失
            if (pop != null) {
                pop.dismiss();
            }
            System.out.println("点击条目了：" + position);
            //选择完日期后，把新位置赋予当前位置，方便下一次点击定位到最新位置
            positon = position;
            day_value = list_day[position];
            tv_day.setText(day_value);//投资期限

            getDataFromServer(day_value);
        }
    }

    private void refershUiData(String fwType) {
        if ("fw_iron".equals(fwType)) {
            ll_fanwanbg.setBackgroundResource(R.drawable.tiefanwan);
            tv_fanwanname.setText("铁饭碗");
            tv_fanwanname.setTextColor(0xff85909B);
            tv_shouyi.setTextColor(0xff65666b);
            tv_touzi.setTextColor(0xff65666b);
            tv_tian.setTextColor(0xff65666b);
            tv_daoqi_info.setTextColor(0xff65666b);
        } else if ("fw_copper".equals(fwType)) {
            ll_fanwanbg.setBackgroundResource(R.drawable.tongfanwan);
            tv_fanwanname.setText("铜饭碗");
            tv_fanwanname.setTextColor(0xffCB9624);
            tv_shouyi.setTextColor(0xff7d5c10);
            tv_touzi.setTextColor(0xff7d5c10);
            tv_tian.setTextColor(0xff7d5c10);
            tv_daoqi_info.setTextColor(0xff7d5c10);
        }
        if ("fw_silver".equals(fwType)) {
            ll_fanwanbg.setBackgroundResource(R.drawable.yinfanwan);
            tv_fanwanname.setText("银饭碗");
            tv_fanwanname.setTextColor(0xff85909B);
            tv_shouyi.setTextColor(0xff65666b);
            tv_touzi.setTextColor(0xff65666b);
            tv_tian.setTextColor(0xff65666b);
            tv_daoqi_info.setTextColor(0xff65666b);
        }
        if ("fw_gold".equals(fwType)) {
            ll_fanwanbg.setBackgroundResource(R.drawable.jinfanwan);
            tv_fanwanname.setText("金饭碗");
            tv_fanwanname.setTextColor(0xffFFB904);
            tv_shouyi.setTextColor(0xff7d5c10);
            tv_touzi.setTextColor(0xff7d5c10);
            tv_tian.setTextColor(0xff7d5c10);
            tv_daoqi_info.setTextColor(0xff7d5c10);
        }

        tv_xiangmuyue.setText(mBorrowSalableBal + "万元");
        tv_min_invest.setText(mMinInvestAmount + "元");
        // 买入按钮状态根据项目余额是否为0来决定
        if ("0.00".equals(mBorrowSalableBal) || "0".equals(mBorrowSalableBal)) {
            bt_buy.setBackgroundResource(R.drawable.anniu_chang_yishouqing);
            bt_buy.setClickable(false);
            bt_buy.setText("已售罄");
        } else {
            bt_buy.setBackgroundResource(R.drawable.button_long_selector);
            bt_buy.setClickable(true);
            bt_buy.setText("买入");
        }
    }
}
