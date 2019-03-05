package com.fanfanlicai.fragment.forgetpassword;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.registerandlogin.HomeLoginActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.fragment.BaseFragment;
import com.fanfanlicai.sign.Base64;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.StringUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;
import com.fanfanlicai.view.ProgressBar.RoundProgressBar;
import com.fanfanlicai.view.dialog.CustomProgressDialog;
import com.fanfanlicai.view.dialog.YzmDialog;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wenguangjun on 2017/4/26.
 */

public class ForgetEmailFragment extends BaseFragment implements View.OnClickListener,TextWatcher {
    private ImageView iv_eye;
    private ImageView iv_eye2;
    private EditText et_mima;
    private EditText et_mima_confirm;
    private ImageView iv_yanzhengma;
    private TextView tv_count;
    private MyCount myCount;
    private RoundProgressBar pw_spinner;
    private boolean isOpen = false;
    private boolean isOpen2 = false;
    private TextView tv_yuyin;
    private CustomProgressDialog customProgressDialog;
    private EditText et_mail;
    private String email;
    private Button bt_homelogin;
    private EditText ed_code;
    private ImageView iv_cancel;// 邮箱后面的取消按钮
    private String randomId;
    @Override
    protected View initView() {
        customProgressDialog = new CustomProgressDialog(mActivity, "正在加载数据......");
        View view = View.inflate(mActivity, R.layout.fragment_forgetemail, null);
        iv_eye = (ImageView) view.findViewById(R.id.iv_eye);//密码输入框后的眼睛图片
        iv_eye.setOnClickListener(this);
        iv_eye2 = (ImageView) view.findViewById(R.id.iv_eye2);//密码输入框后的眼睛图片
        iv_eye2.setOnClickListener(this);
        et_mail = (EditText) view.findViewById(R.id.et_mail);//邮箱输入框
        et_mima = (EditText) view.findViewById(R.id.et_mima);//密码输入框
        et_mima.setTransformationMethod(PasswordTransformationMethod
                .getInstance());
        et_mima_confirm = (EditText) view.findViewById(R.id.et_mima_confirm);//密码输入框
        et_mima_confirm.setTransformationMethod(PasswordTransformationMethod
                .getInstance());
        ed_code = (EditText) view.findViewById(R.id.ed_code);//验证码输入框
        iv_yanzhengma = (ImageView) view.findViewById(R.id.iv_yanzhengma);//获取验证码
        iv_yanzhengma.setOnClickListener(this);
        tv_yuyin = (TextView) view.findViewById(R.id.tv_yuyin);//获取验证码
        tv_yuyin.setOnClickListener(this);
        tv_count = (TextView) view.findViewById(R.id.tv_count);//倒计时
        tv_count.setOnClickListener(this);
        pw_spinner=(RoundProgressBar)view.findViewById(R.id.pw_spinner);//圆弧进度条
        iv_cancel = (ImageView) view.findViewById(R.id.iv_cancel);// 邮箱后面的取消按钮
        iv_cancel.setOnClickListener(this);

        bt_homelogin=(Button)view.findViewById(R.id.bt_homelogin);//确定按钮
        bt_homelogin.setOnClickListener(this);

        et_mail.addTextChangedListener(this);
        et_mima.addTextChangedListener(this);
        et_mima_confirm.addTextChangedListener(this);
        ed_code.addTextChangedListener(this);
        return view;
    }

    @Override
    public void initData() {
        //默认按钮不可点击
        bt_homelogin.setBackgroundResource(R.drawable.anniu_chang_bukedianji);
        bt_homelogin.setClickable(false);


        // 密码输入框失去焦点监听，当输入格式不符合时，toast提示
        et_mima.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                String mima = et_mima.getText().toString().trim();
                if (hasFocus) {
                } else {
//                    if (!isMima(mima)) {
//                        ToastUtils.toastshort("密码长度应在6-15位之间");
//                    }
//                    if (isContainChinese(mima)) {
//                        ToastUtils.toastshort("密码不能包含中文");
//                    }
                }
            }
        });
    }

    public static boolean isContainChinese(String str) {

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static boolean isMobile(String phone) {
        return phone.length() == 11;
    }

    public static boolean isMima(String mima) {
        return mima.length() >= 6 && mima.length() <= 15;
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

        if (StringUtils.isNotBlank(et_mail.getText().toString())
                &&StringUtils.isNotBlank(et_mima.getText().toString())
                &&StringUtils.isNotBlank(ed_code.getText().toString()))
        {
            //当输入框都不为空时，按钮可点击
            bt_homelogin.setBackgroundResource(R.drawable.button_long_selector);
            bt_homelogin.setPressed(false);
            bt_homelogin.setClickable(true);
        }else {
            bt_homelogin.setBackgroundResource(R.drawable.anniu_chang_bukedianji);
            bt_homelogin.setClickable(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel:// 手机号码后面的取消按钮
                et_mail.getText().clear();
                break;
            case R.id.iv_eye:
                isOpen = !isOpen;//根据点击改变眼睛的闭合状态
                if (isOpen) {
                    //眼睛打开，输入框里的密码显示
                    iv_eye.setImageResource(R.drawable.eye_open2);
                    et_mima.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
                } else {
                    //眼睛关闭，输入框里的密码隐藏
                    iv_eye.setImageResource(R.drawable.eye_close2);
                    et_mima.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());
                }
                // 切换后将EditText光标置于末尾
                CharSequence charSequence = et_mima.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
                break;
            case R.id.iv_eye2:
                isOpen2 = !isOpen2;//根据点击改变眼睛的闭合状态
                if (isOpen2) {
                    //眼睛打开，输入框里的密码显示
                    iv_eye2.setImageResource(R.drawable.eye_open2);
                    et_mima_confirm.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
                } else {
                    //眼睛关闭，输入框里的密码隐藏
                    iv_eye2.setImageResource(R.drawable.eye_close2);
                    et_mima_confirm.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());
                }
                // 切换后将EditText光标置于末尾
                CharSequence charSequence2 = et_mima_confirm.getText();
                if (charSequence2 instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence2;
                    Selection.setSelection(spanText, charSequence2.length());
                }
                break;
            case R.id.iv_yanzhengma:// 短信验证码
                email = et_mail.getText().toString().trim();
                if (StringUtils.isBlank(email)) {
                    ToastUtils.toastshort("请输入邮箱");
                    return;
                }
                checkEmail(email);


                break;
            case R.id.bt_homelogin://确定

                email = et_mail.getText().toString().trim();
                String mima = et_mima.getText().toString().trim();
                String mima_confirm = et_mima_confirm.getText().toString().trim();
                String code = ed_code.getText().toString().trim();

                if (StringUtils.isBlank(email)) {
                    ToastUtils.toastshort("请输入邮箱");
                    return;
                }
                if (StringUtils.isBlank(mima)) {
                    ToastUtils.toastshort("请输入新密码");
                    return;
                }
                if (StringUtils.isBlank(code)) {
                    ToastUtils.toastshort("请点击获取验证码");
                    return;
                }
                if (!mima.equals(mima_confirm)) {
                    ToastUtils.toastshort("两次输入的密码不一致");
                    return;
                }
                goset(mima,code);
                break;

            default:
                break;
        }
    }

    //点击验证码后判断邮箱是否存在
    protected void checkEmail(final String email) {
        Map<String, String> map = SortRequestData.getmap();
        map.put("email", email);
        String requestData = SortRequestData.sortString(map);
        String signData = SignUtil.sign(requestData);
        map.put("sign", signData);

        VolleyUtil.sendJsonRequestByPost(ConstantUtils.ISEMAILEXIST_URL, null, map,
                new HttpBackBaseListener() {

                    @Override
                    public void onSuccess(String string) {
                        JSONObject json = JSON.parseObject(string);
                        String code = json.getString("code");
                        if("0".equals(code)){
                            getcode(email);
                        }else{
                            String msg = json.getString("msg");
                            ToastUtils.toastshort(msg);
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        ToastUtils.toastshort("网络错误");
                        return;
                    }
                });
    }

    /**
     * 调用接口去设置新的密码
     */
    private void goset(final String mima,String code) {
        customProgressDialog.show();
        Map<String, String> map = SortRequestData.getmap();
        map.put("email", email);
        map.put("code", code);
        map.put("randomId", randomId);
        map.put("password", Base64.encode(SignUtil.encrypt(mima)));
        String requestData = SortRequestData.sortString(map);
        String signData = SignUtil.sign(requestData);
        map.put("sign", signData);
        VolleyUtil.sendJsonRequestByPost(ConstantUtils.FINDPWD_URL, null, map,
                new HttpBackBaseListener() {

                    @Override
                    public void onSuccess(String string) {
                        customProgressDialog.dismiss();

                        JSONObject json = JSON.parseObject(string);
                        String  code= json.getString("code");
                        if("0".equals(code)){
                            String token = json.getString("token");
                            CacheUtils.putString(mActivity, "token",token);
                            CacheUtils.putString(mActivity, CacheUtils.LOGINPASSWORD,Base64.encode(SignUtil.encrypt(mima)));
                            Intent intent=new Intent(mActivity,HomeLoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置
                            startActivity(intent);
                            getActivity().finish();
                        }else{
                            String msg = json.getString("msg");
                            ToastUtils.toastshort(msg);
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        customProgressDialog.dismiss();
                        ToastUtils.toastshort("找回密码失败");
                    }
                });
    }


    /**
     * 获取验证码
     */
    private void getcode(final String email) {
        YzmDialog yzmDialog = new YzmDialog(mActivity, R.style.YzmDialog, email);
        yzmDialog
                .setOnYzmDialogDismissListener(new YzmDialog.OnYzmDialogDismissListener() {

                    @Override
                    public void OnYzmDialogDismiss(String answer) {
                        //customProgressDialog.setContent("正在获取验证码...");
                        customProgressDialog.show();
                        Map<String, String> map = SortRequestData.getmap();
                        String token = CacheUtils.getString(mActivity, "token", null);
                        map.put("email", email);
                        map.put("sendType", 2+"");
                        map.put("codeType", 2+"");
                        map.put("captchaResult", answer);
                        map.put("token",token);
                        String requestData = SortRequestData.sortString(map);
                        String signData = SignUtil.sign(requestData);
                        map.put("sign", signData);
                        VolleyUtil.sendJsonRequestByPost(ConstantUtils.SMS_URL, null, map, new HttpBackBaseListener() {

                            @Override
                            public void onSuccess(String string) {
                                customProgressDialog.dismiss();
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
                                customProgressDialog.dismiss();
                                ToastUtils.toastshort("获取验证码失败");
                            }
                        });
                    }
                });
        yzmDialog.show();

    }
    class MyCount extends CountDownTimer {

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            //倒计时完要做的事情
            tv_count.setClickable(true);
            iv_yanzhengma.setImageResource(R.drawable.yanzhengmaagain);//改为重新获取图片
            iv_yanzhengma.setVisibility(View.VISIBLE);//让重新获取图片显示
            // 倒计时完让语音验证码可点击
            tv_yuyin.setClickable(true);
            //获取验证码过程使手机号输入框重新获得焦点
            et_mail.setEnabled(true);
            et_mail.setCursorVisible(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_count.setText(millisUntilFinished / 1000 + "");//60秒倒计时
            tv_count.setTextSize(10);
            pw_spinner.setProgress((int)(millisUntilFinished / 1000));//圆弧进度条
            tv_count.setClickable(false);
            //获取验证码过程使语音验证码点击失效
            tv_yuyin.setClickable(false);
            //获取验证码过程使手机号输入框失去焦点
            et_mail.setEnabled(false);
            et_mail.setCursorVisible(false);
        }

    }
}
