package com.fanfanlicai.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import com.fanfanlicai.view.dialog.CustomProgressDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Map;

public class CGBankNotQianYueActivity extends BaseActivity implements OnClickListener {

    private ImageView iv_bank;
    private TextView tv_bank;
    private TextView tv_banknumber;
    private EditText et_phone;
    private Button bt_btn;
    private CustomProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cgbanknotqianyue);

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
        et_phone = (EditText) findViewById(R.id.et_phone);
        //下一步
        bt_btn = (Button) findViewById(R.id.bt_btn);
        bt_btn.setOnClickListener(this);
    }

    private void initData() {

        requestServer();

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
            case R.id.bt_btn:
                String phone = et_phone.getText().toString().trim();
                sign(phone);
                break;

            default:
                break;
        }
    }

    private void sign(String phone) {
        progressdialog.showis();
        Map<String, String> map = SortRequestData.getmap();
        String token = CacheUtils.getString(this, "token", "");
        map.put("token", token);
        map.put("phone", phone);
        String requestData = SortRequestData.sortString(map);
        String signData = SignUtil.sign(requestData);
        map.put("sign", signData);
        VolleyUtil.sendJsonRequestByPost(ConstantUtils.SIGNCARD_URL, null, map,
                new HttpBackBaseListener() {

                    @Override
                    public void onSuccess(String string) {

                        LogUtils.i("签约===" + string);

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
                                    String result = data.getString("result");
                                    Intent intent = new Intent(CGBankNotQianYueActivity.this,CGAuthenticationActivity.class);
                                    intent.putExtra("result",result);
                                    startActivity(intent);

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
}
