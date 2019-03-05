package com.fanfanlicai.fragment.setting;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.setting.SetPassWordActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.fragment.BaseFragment;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.StringUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;
import com.fanfanlicai.view.ProgressBar.RoundProgressBar;
import com.fanfanlicai.view.dialog.CustomProgressDialog;
import com.fanfanlicai.view.dialog.YzmDialog;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wenguangjun on 2017/4/25.
 */

public class CashPhoneFragment extends BaseFragment implements View.OnClickListener,TextWatcher {

    private ImageView iv_yanzhengma;//获取验证码图片
    private MyCount myCount;
    private TextView tv_count;//倒计时
    private RoundProgressBar pw_spinner;//圆弧进度条
    private Button bt_password;//下一步按钮
    private TextView et_name;//真实姓名
    private TextView tv_yuyin;//获取语音验证码
    private CustomProgressDialog progressdialog;
    private EditText et_number;
    private EditText et_yanzhengma;
    private String phone;
    private String get_money;
    private String ticketId;
    private String useDefaultTicket;
    private boolean isIdNoExist = false;//输入的身份证号是否存在
    private String randomId;

    @Override
    protected View initView() {
        progressdialog = new CustomProgressDialog(mActivity, "正在发送，请稍后......");
        get_money=mActivity.getIntent().getStringExtra("get_money");
        ticketId=mActivity.getIntent().getStringExtra("ticketId");
        useDefaultTicket=mActivity.getIntent().getStringExtra("useDefaultTicket");

        View view = View.inflate(mActivity, R.layout.fragment_cashphone, null);
        iv_yanzhengma = (ImageView) view.findViewById(R.id.iv_yanzhengma);//获取验证码图片
        iv_yanzhengma.setOnClickListener(this);
        tv_count = (TextView) view.findViewById(R.id.tv_count);//倒计时
        tv_count.setOnClickListener(this);
        pw_spinner=(RoundProgressBar)view.findViewById(R.id.pw_spinner);//圆弧进度条
        bt_password = (Button) view.findViewById(R.id.bt_password);//下一步按钮
        bt_password.setOnClickListener(this);
        et_name = (TextView) view.findViewById(R.id.et_name);//真实姓名
        tv_yuyin = (TextView) view.findViewById(R.id.tv_yuyin);//获取语音验证码
        tv_yuyin.setOnClickListener(this);
        et_number = (EditText) view.findViewById(R.id.et_number);
        et_yanzhengma = (EditText) view.findViewById(R.id.et_yanzhengma);

        et_number.addTextChangedListener(this);
        et_yanzhengma.addTextChangedListener(this);
        return view;
    }

    @Override
    public void initData() {

        // 默认按钮不可点击
        bt_password.setBackgroundResource(R.drawable.anniu_chang_bukedianji);
        bt_password.setClickable(false);
        //设置真实姓名
        String realName = CacheUtils.getString(mActivity, CacheUtils.REALNAME, "");
        phone = CacheUtils.getString(mActivity, CacheUtils.CARDPHONE, "");
        et_name.setText(realName);

        //解决进入设置提现密码界面没有弹出软键盘的问题
        et_number.setFocusable(true);
        et_number.setFocusableInTouchMode(true);
        et_number.requestFocus();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() { //让软键盘延时弹出，以更好的加载Activity

            public void run() {
                InputMethodManager inputManager = (InputMethodManager)et_number.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_number, 0);
            }

        }, 500);


    }

    //为了使所有edittext不为空时按钮才可点击，实现了TextWatcher接口，下面三个方法是必须实现的
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (StringUtils.isNotBlank(et_number.getText().toString())
                &&StringUtils.isNotBlank(et_yanzhengma.getText().toString())
                )
        {
            //当输入框都不为空时，按钮可点击
            bt_password.setBackgroundResource(R.drawable.button_long_selector);
            bt_password.setPressed(false);
            bt_password.setClickable(true);
        }else {
            bt_password.setBackgroundResource(R.drawable.anniu_chang_bukedianji);
            bt_password.setClickable(false);
        }
    }

    @Override
    public void onClick(View v) {
        String idNo = et_number.getText().toString().trim();
        switch (v.getId()) {
            case R.id.iv_yanzhengma:// 点击获取验证码，弹出dialog输入提示
                if (TextUtils.isEmpty(idNo)) {// 身份证号为空
                    ToastUtils.toastshort("请输入身份证号");
                    return;
                }
                check(idNo,0);

                break;
            case R.id.tv_yuyin:// 获取语音验证码
                if (TextUtils.isEmpty(idNo)) {// 身份证号为空
                    ToastUtils.toastshort("请输入身份证号");
                    return;
                }
                check(idNo,1);

                break;
            case R.id.bt_password://下一步按钮

                String code = et_yanzhengma.getText().toString().trim();
                if (TextUtils.isEmpty(idNo)) {// 身份证号为空
                    ToastUtils.toastshort("请输入身份证号");
                    return;
                }
                if (TextUtils.isEmpty(code)) {// 验证码为空
                    ToastUtils.toastshort("请点击获取验证码");
                    return;
                }

                progressdialog.show();
                Map<String, String> map = SortRequestData.getmap();
                String token = CacheUtils.getString(mActivity, "token",null);
                map.put("token", token);
                map.put("idNo", idNo);
                map.put("code", code);
                map.put("randomId", randomId);
                map.put("codeType", 4+"");
                String requestData = SortRequestData.sortString(map);
                String signData = SignUtil.sign(requestData);
                map.put("sign", signData);
                VolleyUtil.sendJsonRequestByPost(ConstantUtils.VERIFYIDNO_URL, null,
                        map, new HttpBackBaseListener() {

                            @Override
                            public void onSuccess(String string) {
                                // TODO Auto-generated method stub
                                progressdialog.dismiss();
                                JSONObject json = JSON.parseObject(string);
                                String code = json.getString("code");

                                if ("0".equals(code)) {
                                    getActivity().finish();
                                    //跳转到设置密码界面
                                    Intent intent = new Intent(mActivity,SetPassWordActivity.class);
                                    intent.putExtra("get_money", get_money);
                                    intent.putExtra("ticketId", ticketId);
                                    intent.putExtra("useDefaultTicket", useDefaultTicket);
                                    startActivity(intent);

                                } else {
                                    String msg = json.getString("msg");
                                    ToastUtils.toastshort(msg);
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {
                                progressdialog.dismiss();
                                ToastUtils.toastshort("网络错误！");
                            }
                        });

                break;
            default:
                break;
        }
    }

    private void check(String idNo, final int sendType) {
        String token = CacheUtils.getString(mActivity, "token", "");
        Map<String, String> map = SortRequestData.getmap();
        map.put("idNo", idNo);
        map.put("token", token);
        String requestData = SortRequestData.sortString(map);
        String signData = SignUtil.sign(requestData);
        map.put("sign", signData);

        VolleyUtil.sendJsonRequestByPost(ConstantUtils.VERIFYUSERIDNO_URL, null, map,
                new HttpBackBaseListener() {

                    @Override
                    public void onSuccess(String string) {
                        LogUtils.i("哈哈哈哈=="+string);
                        JSONObject json = JSON.parseObject(string);
                        String code = json.getString("code");
                        if("0".equals(code)){
                            YzmDialog yzmDialog_yuyin = new YzmDialog(mActivity, R.style.YzmDialog, phone);
                            yzmDialog_yuyin
                                    .setOnYzmDialogDismissListener(new YzmDialog.OnYzmDialogDismissListener() {

                                        @Override
                                        public void OnYzmDialogDismiss(String answer) {
                                            progressdialog.show();
                                            progressdialog.setContent("正在获取语音验证码...");
                                            String token = CacheUtils.getString(mActivity, "token", null);
                                            Map<String, String> map = SortRequestData.getmap();
                                            map.put("phone", phone);
                                            map.put("sendType", sendType+"");
                                            map.put("codeType", 4+"");
                                            map.put("captchaResult", answer);
                                            map.put("token", token);
                                            String requestData = SortRequestData.sortString(map);
                                            String signData = SignUtil.sign(requestData);
                                            map.put("sign", signData);
                                            VolleyUtil.sendJsonRequestByPost(ConstantUtils.SMS_URL, null, map, new HttpBackBaseListener() {

                                                @Override
                                                public void onSuccess(String string) {
                                                    progressdialog.dismiss();
                                                    JSONObject json = JSON.parseObject(string);
                                                    String code = json.getString("code");
                                                    if("0".equals(code)){
                                                        String datastr = json.getString("data");
                                                        JSONObject data = JSON.parseObject(datastr);
                                                        randomId = data.getString("randomId");

                                                        iv_yanzhengma.setVisibility(View.GONE);
                                                        myCount = new MyCount(60000, 1000);// 设置倒计时时间为60秒，间隔为1秒
                                                        myCount.start();
                                                    }else{
                                                        String msg = json.getString("msg");
                                                        ToastUtils.toastshort(msg);
                                                    }
                                                }

                                                @Override
                                                public void onError(VolleyError error) {
                                                    progressdialog.dismiss();
                                                    ToastUtils.toastshort("获取验证码失败");
                                                }
                                            });
                                        }
                                    });
                            yzmDialog_yuyin.show();
                        }else{
                            ToastUtils.toastshort("身份证不正确，请重新输入");
                            return;
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        LogUtils.i("接口有问题啊==");
                    }
                });
    }

    class MyCount extends CountDownTimer {

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            //倒计时完要做的事情
            tv_count.setEnabled(true);
            tv_count.setClickable(true);
            iv_yanzhengma.setImageResource(R.drawable.yanzhengmaagain);//改为重新获取图片
            iv_yanzhengma.setVisibility(View.VISIBLE);//让重新获取图片显示
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_count.setText(millisUntilFinished / 1000 + "");//60秒倒计时
            tv_count.setTextSize(10);
            pw_spinner.setProgress((int)(millisUntilFinished / 1000));//圆弧进度条
            tv_count.setClickable(false);
        }

    }
}
