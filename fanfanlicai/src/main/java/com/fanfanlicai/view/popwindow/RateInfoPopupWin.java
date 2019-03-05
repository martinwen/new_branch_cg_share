package com.fanfanlicai.view.popwindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fanfanlicai.fanfanlicai.R;

/**
 * @author lijinliu
 * @date 20180131
 * 收益率弹出窗
 */

public class RateInfoPopupWin extends PopupWindow implements View.OnClickListener {

    private View mPopView;
    private OnItemClickListener mListener;
    private TextView mTvBaseRate;
    private TextView mTvActivityRate;
    private TextView mTvActivityName;
    private TextView mTvJiaxiRate;
    private LinearLayout mLayoutActivity;
    private LinearLayout mLayoutUseJiaxipiao;
    private String mStrActivityName;
    private String mStrActivityRate;
    private String mStrBaseRate;
    private String mStrJiaxiRate;
    private String mStrFloatRate;
    private String mProductDataShowType;


    public RateInfoPopupWin(Context context, String productDataShowType, String baseRate, String acitivityName, String activityRate, String floatRate, String jiaxiRate) {
        super(context);
        this.mProductDataShowType = productDataShowType;
        this.mStrActivityName = acitivityName;
        this.mStrActivityRate = activityRate;
        this.mStrBaseRate = baseRate;
        this.mStrFloatRate = floatRate;
        this.mStrJiaxiRate = jiaxiRate;
        init(context);
        setPopupWindow();
        initData();
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = LayoutInflater.from(context);
        //绑定布局
        mPopView = inflater.inflate(R.layout.popwin_rateinfo, null);
    }

    private void initData() {

        mTvBaseRate = (TextView) mPopView.findViewById(R.id.tv_baserate);
        mTvActivityRate = (TextView) mPopView.findViewById(R.id.tv_activity_rate);
        mTvActivityName = (TextView) mPopView.findViewById(R.id.tv_activity_name);
        mTvJiaxiRate = (TextView) mPopView.findViewById(R.id.tv_jiaxi_rate);
        mLayoutActivity = (LinearLayout) mPopView.findViewById(R.id.layout_activity);
        mLayoutUseJiaxipiao = (LinearLayout) mPopView.findViewById(R.id.layout_use_jiaxipiao);

        if ("zt".equals(mProductDataShowType) && !TextUtils.isEmpty(mStrActivityRate)) {
            Double activityRate = Double.parseDouble(mStrActivityRate);
            if (activityRate > 0) {
                mTvActivityRate.setText(mStrActivityRate + "%");
                mTvActivityName.setText(mStrActivityName+"：");
                mLayoutActivity.setVisibility(View.VISIBLE);
            }
        } else if (!TextUtils.isEmpty(mStrFloatRate)) {
            Double activityRate = Double.parseDouble(mStrFloatRate);
            if (activityRate > 0) {
                mTvActivityRate.setText(mStrFloatRate + "%");
                mTvActivityName.setText("浮动收益率：");
                mLayoutActivity.setVisibility(View.VISIBLE);
            }
        }

        if (!TextUtils.isEmpty(mStrJiaxiRate)) {
            Double activityRate = Double.parseDouble(mStrJiaxiRate);
            if (activityRate > 0) {
                mTvJiaxiRate.setText(mStrJiaxiRate + "%");
                mLayoutUseJiaxipiao.setVisibility(View.VISIBLE);
            }
        }
        mTvBaseRate.setText(mStrBaseRate + "%");
    }

    /**
     * 设置窗口的相关属性
     */
    @SuppressLint("InlinedApi")
    private void setPopupWindow() {
        this.setContentView(mPopView);
        this.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        //this.setAnimationStyle(R.style.mypopwindow_anim_style);// 设置动画
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int height = mPopView.findViewById(R.id.id_pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    /**
     * 定义一个接口，公布出去 在Activity中操作按钮的单击事件
     */
    public interface OnItemClickListener {
        void setOnItemClick(View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (mListener != null) {
            mListener.setOnItemClick(v);
        }
    }

}