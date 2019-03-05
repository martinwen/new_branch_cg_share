package com.fanfanlicai.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.invest.FanWanDetailActivity;
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

import java.util.Map;

/**
 * @author lijinliu
 * @date 20180129
 * 直投自动匹配授权
 */
public class AutoMatchingDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private CustomProgressDialog mProgressDialog;
    private TextView mTvBtnCancle;
    private TextView mTvBtnOk;
    private ImageView mIvMatchingSucc;
    private OnDialogClickListener clickListener;

    public AutoMatchingDialog(Context context , OnDialogClickListener clickListener) {
        super(context);
        this.mContext = context;
        this.clickListener = clickListener;
    }

    public AutoMatchingDialog(Context context, int theme , OnDialogClickListener clickListener) {
        super(context, theme);
        this.mContext = context;
        this.clickListener = clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_auto_matching);
        initDialog();
    }


    private void initDialog() {
        mProgressDialog = new CustomProgressDialog(mContext, "数据加载中...");
        mTvBtnOk = (TextView) findViewById(R.id.btn_ok);
        mTvBtnCancle = (TextView) findViewById(R.id.tv_cancel);
        mIvMatchingSucc = (ImageView) findViewById(R.id.iv_matching_succ);
        mTvBtnOk.setOnClickListener(this);
        mTvBtnCancle.setOnClickListener(this);
        mIvMatchingSucc.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                autoAuthorized();
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    public interface OnDialogClickListener {
        void onClickConfirm();
    }

    /**
     * 买入匹配自动授权
     */
    private void autoAuthorized() {
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.showis();
        }
        Map<String, String> map = SortRequestData.getmap();
        String token = CacheUtils.getString(mContext, "token", null);
        map.put("token", token);
        String requestData = SortRequestData.sortString(map);
        String signData = SignUtil.sign(requestData);
        map.put("sign", signData);
        VolleyUtil.sendJsonRequestByPost(ConstantUtils.SETAUTOAUTHORIZED_URL, null, map,
                new HttpBackBaseListener() {

                    @Override
                    public void onSuccess(String string) {
                        LogUtils.i("买入自动匹配授权==="+string);
                        // TODO Auto-generated method stub
                        mProgressDialog.dismiss();
                        JSONObject json = JSON.parseObject(string);
                        String code = json.getString("code");

                        if ("0".equals(code)) {
                            String datastr = json.getString("data");
                            if (!StringUtils.isBlank(datastr)) {
                                String sign = json.getString("sign");
                                boolean isSuccess = SignUtil.verify(sign, datastr);
                                if (isSuccess) {
                                    // 成功 1，其他失败
                                    if ("1".equals(datastr))
                                    {
                                        mIvMatchingSucc.setVisibility(View.VISIBLE);
                                        mTvBtnCancle.setVisibility(View.GONE);
                                        mTvBtnOk.setVisibility(View.GONE);
                                        //3秒后关闭弹窗
                                        mHandler.sendEmptyMessageDelayed(1,3000);
                                    } else {
                                        ToastUtils.toastshort("自动匹配授权失败！");
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
                        mProgressDialog.dismiss();
                    }
                });
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(clickListener!=null)
            {
                clickListener.onClickConfirm();
            }
            dismiss();
        }
    };
}

