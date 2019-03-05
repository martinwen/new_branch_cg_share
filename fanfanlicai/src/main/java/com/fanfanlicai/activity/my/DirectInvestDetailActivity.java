package com.fanfanlicai.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.adapter.direct.BidAdapter;
import com.fanfanlicai.bean.BidBean;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;
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
 * @date 20180129
 * 饭碗记录--出借详情
 */
public class DirectInvestDetailActivity extends FragmentActivity implements OnClickListener {
    //返回
    private TextView invest_back;
    private CustomProgressDialog progressdialog;
    private ListView listView;
    private BidAdapter adapter;

    //买入时间
    private String mTransTime;
    //时分秒买入时间
    private String mTransTimeStr;
    //预期年华率
    private String mYearRate;
    //投资金额
    private String mInvestAmount;
    //到期时间
    private String mEndTime;
    // 投资期限
    private String mInvestDeadline;
    //投资状态 :1：还款中；3：已完成；4：回款中
    private String mStatus;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_directinvest_detail);

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
        progressdialog = new CustomProgressDialog(DirectInvestDetailActivity.this, "正在获取数据...");
        invest_back = (TextView) findViewById(R.id.get_back);
        invest_back.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.list_view);
        adapter = new BidAdapter(DirectInvestDetailActivity.this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener());
    }

    private void initData() {
        mTransTime = getIntent().getStringExtra("transTime");
        mTransTimeStr = getIntent().getStringExtra("transTimeStr");
        mYearRate = getIntent().getStringExtra("yearRate");
        mInvestAmount = getIntent().getStringExtra("investAmount");
        mEndTime = getIntent().getStringExtra("endTime");
        mInvestDeadline = getIntent().getStringExtra("investDeadline");
        mStatus = getIntent().getStringExtra("status");
        mRecInterest = getIntent().getStringExtra("recInterest");
        mWaitRepayAmount = getIntent().getStringExtra("waitRepayAmount");
        mTransUuid = getIntent().getStringExtra("transUuid");
        mType = getIntent().getStringExtra("type");

        getFwRecordRepay();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_back:
                finish();
                break;

            default:
                break;
        }
    }

    class OnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) {
                BidBean bidBean = (BidBean) adapter.getItem(position);
                if (!TextUtils.isEmpty(bidBean.getTransUuid())) {
                    Intent intent = new Intent(DirectInvestDetailActivity.this, BidDetailActivity.class);
                    intent.putExtra("bid", bidBean.getBid());
                    intent.putExtra("transUuid", mTransUuid);
                    /**if ("1".equals(mType)){
                        intent.putExtra("investDeadline", bidBean.getBorrowDay());
                    } else {
                        intent.putExtra("investDeadline", mInvestDeadline);
                    }**/
                    intent.putExtra("investDeadline", mInvestDeadline);
                    intent.putExtra("investTime", mTransTime);
                    intent.putExtra("endTime", mEndTime);
                    intent.putExtra("statues", bidBean.getRepayedType());
                    intent.putExtra("fWStatues", mStatus);
                    intent.putExtra("type", mType);
                    intent.putExtra("matchId", bidBean.getMatchId());
                    DirectInvestDetailActivity.this.startActivity(intent);
                }
            }
        }
    }

    private void getFwRecordRepay() {
        if (!progressdialog.isShowing()) {
            progressdialog.show();
        }
        Map<String, String> map = SortRequestData.getmap();
        String token = CacheUtils.getString(DirectInvestDetailActivity.this, "token", null);
        map.put("token", token);
        map.put("type", mType);
        map.put("transUuid", mTransUuid);
        String requestData = SortRequestData.sortString(map);
        String signData = SignUtil.sign(requestData);
        map.put("sign", signData);
        VolleyUtil.sendJsonRequestByPost(ConstantUtils.GETFWRECORDREPAY_URL, null, map,
                new HttpBackBaseListener() {

                    @Override
                    public void onSuccess(String string) {
                        LogUtils.i("饭碗投资记录还款进度==="+string);
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
                                    getDataFromServer();
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

    private void getDataFromServer() {
        Map<String, String> map = SortRequestData.getmap();
        String token = CacheUtils.getString(DirectInvestDetailActivity.this, "token", null);
        map.put("transUuid", mTransUuid + "");
        map.put("type", mType + "");
        map.put("token", token);
        String requestData = SortRequestData.sortString(map);
        String signData = SignUtil.sign(requestData);
        map.put("sign", signData);
        VolleyUtil.sendJsonRequestByPost(ConstantUtils.GETDIDETAILINFO_URL,
                null, map, new HttpBackBaseListener() {

                    @Override
                    public void onSuccess(String string) {
                        LogUtils.i("直投详情===" + string);
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

                                    JSONArray dataList = JSON.parseArray(datastr);
                                    ArrayList<BidBean> listadd = (ArrayList<BidBean>) JSONArray.parseArray(dataList.toJSONString(), BidBean.class);
                                    //adapter.setData(listadd, tvInvestDeadLine.getText().toString());
                                    adapter.setData(listadd, mType, mInvestDeadline, mTransTimeStr, mEndTime, mStatus, mStrUncapital, mStrUninterest, mStrCapital, mStrInterest);
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
