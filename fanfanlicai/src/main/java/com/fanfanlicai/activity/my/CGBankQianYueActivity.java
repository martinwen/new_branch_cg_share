package com.fanfanlicai.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.BaseActivity;
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
import com.fanfanlicai.view.dialog.ChangeFailureDialog;
import com.fanfanlicai.view.dialog.CustomProgressDialog;
import com.fanfanlicai.view.dialog.NormalDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Map;

public class CGBankQianYueActivity extends BaseActivity implements OnClickListener {

    private ImageView iv_bank;
    private TextView tv_bank;
    private TextView tv_banknumber;
    private TextView tv_changbank;
    private TextView tv_yuanyin;
    private TextView tv_state;
    private CustomProgressDialog progressdialog;
    private String isSignCard;
    private String result;
    private String realName;
    private String idNo;
    private String rejectReason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cgbankqianyue);

        progressdialog = new CustomProgressDialog(this, "正在加载数据...");
        initView();
        initData();
    }

    private void initView() {
        TextView bank_back = (TextView) findViewById(R.id.bank_back);
        bank_back.setOnClickListener(this);
        iv_bank = (ImageView) findViewById(R.id.iv_bank);
        tv_bank = (TextView) findViewById(R.id.tv_bank);
        tv_banknumber = (TextView) findViewById(R.id.tv_banknumber);
        tv_changbank = (TextView) findViewById(R.id.tv_changbank);
        tv_changbank.setOnClickListener(this);
        tv_yuanyin = (TextView) findViewById(R.id.tv_yuanyin);
        tv_yuanyin.setOnClickListener(this);
        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_state.setOnClickListener(this);
    }

    private void initData() {

        isSignCard = getIntent().getStringExtra("isSignCard");
        if ("0".equals(isSignCard)) {// 如果还没有签约，去签约
            tv_state.setText("未签约");
        } else {//1-已签约
            tv_state.setText("已签约");
        }

        //获取银行卡信息
        requestServer();

    }

    private void checkBankState() {
        progressdialog.showis();
        Map<String, String> map = SortRequestData.getmap();
        String token = CacheUtils.getString(this, "token", "");
        map.put("token", token);
        String requestData = SortRequestData.sortString(map);
        String signData = SignUtil.sign(requestData);
        map.put("sign", signData);
        VolleyUtil.sendJsonRequestByPost(ConstantUtils.QUERYAPPLYSTATUS_URL, null, map,
                new HttpBackBaseListener() {

                    @Override
                    public void onSuccess(String string) {

                        LogUtils.i("最新的换卡申请状态===" + string);

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


                                    // 银行卡号
                                    result = data.getString("result");
                                    if("-1".equals(result)){//-1  从未申请过换卡
                                        tv_changbank.setText("我要换卡");
                                    }else if("0".equals(result)){//0  待审核
                                        tv_changbank.setText("换卡处理中");
                                    }else if("1".equals(result)){//1  审核通过
                                        tv_changbank.setText("我要换卡");
                                    }else if("2".equals(result)){//2  审核拒绝
                                        tv_changbank.setText("换卡失败，重新申请");
                                        //失败原因控件显示，默认隐藏
                                        tv_yuanyin.setVisibility(View.VISIBLE);
                                        rejectReason = data.getString("rejectReason");

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
                        progressdialog.dismiss();
                    }
                });
    }

    private void requestServer() {
        progressdialog.showis();
        Map<String, String> map = SortRequestData.getmap();
        String token = CacheUtils.getString(this, "token", "");
        map.put("token", token);
        String requestData = SortRequestData.sortString(map);
        String signData = SignUtil.sign(requestData);
        map.put("sign", signData);
        VolleyUtil.sendJsonRequestByPost(ConstantUtils.BANKCARD_URL, null, map,
                new HttpBackBaseListener() {

                    @Override
                    public void onSuccess(String string) {

                        LogUtils.i("我的银行卡===" + string);

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


                                    // 银行卡号
                                    String cardNum = data.getString("cardNum");
                                    tv_banknumber.setText(fixCard(cardNum));

                                    // 开户行
                                    String openBank = data.getString("openBank");
                                    tv_bank.setText(openBank);

                                    // 银行卡图标
                                    String bankPic = data.getString("bankPic");
                                    ImageLoader.getInstance().displayImage(bankPic, iv_bank);

                                    // 真实姓名
                                    realName = data.getString("realName");

                                    // 身份证
                                    idNo = data.getString("idNo");

                                    //查询用户最新的换卡申请状态
                                    checkBankState();

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

    //银行卡星号处理
    private String fixCard(String cardNo) {
        StringBuilder builder = new StringBuilder();
        String start = cardNo.substring(0, 6);
        String end = cardNo.substring(cardNo.length() - 4, cardNo.length());
        builder.append(start);
        builder.append(" ********** ");
        builder.append(end);
        return builder.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bank_back:
                finish();
                break;
            case R.id.tv_changbank:
                if("0".equals(result)){

                    NormalDialog normalDialog = new NormalDialog(this, R.style.YzmDialog, "变更银行卡处理中，暂不可查看。");
                    normalDialog.show();
                }else{
                    Intent intent = new Intent(this, CGBankChangeActivity.class);
                    intent.putExtra("realName",realName);
                    intent.putExtra("idNo",idNo);
                    startActivity(intent);
                }

                break;
            case R.id.tv_yuanyin:
                if("2".equals(result)){
                    ChangeFailureDialog changeFailureDialog = new ChangeFailureDialog(this, R.style.YzmDialog, rejectReason,realName,idNo);
                    changeFailureDialog.show();
                }

                break;
            case R.id.tv_state:
                if ("0".equals(isSignCard)) {// 如果还没有签约，去签约
                    startActivity(new Intent(this, CGBankNotQianYueActivity.class));
                }
                break;

            default:
                break;
        }
    }
}
