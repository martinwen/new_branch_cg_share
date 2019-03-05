package com.fanfanlicai.activity.home;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fanfanlicai.fanfanlicai.R;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class ShareFragFanJu extends DialogFragment {

    PlatformActionListener platformActionListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            ShareFragFanJu.this.getDialog().dismiss();
            //大部分的回调方法都处于网络线程，因此可以简单默认为回调方法都不在主线程.
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "分享成功！", Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            Log.e("TAG", throwable.getMessage());
            Toast.makeText(getActivity(), "分享失败！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(Platform platform, int i) {
        }
    };

    private View rootView;
    private String url;
    private String title;
    private String image;
    private String text;
    private View.OnClickListener SHARE_LISTENER = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.layout_share_wechat: {
                    Wechat.ShareParams sp = new Wechat.ShareParams();
                    sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
                    sp.setTitle(title); //分享标题
                    sp.setText(text);   //分享文本
                    sp.setImageUrl(image);
                    sp.setUrl(url);   //网友点进链接后，可以看到分享的详情
                    Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                    wechat.setPlatformActionListener(platformActionListener); // 设置分享事件回调
                    // 执行分享
                    wechat.share(sp);
                    break;
                }
                case R.id.layout_share_moments: {
                    WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
                    sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性

                    sp.setTitle(title);  //分享标题
                    sp.setText(text);   //分享文本
                    sp.setUrl(url);   //网友点进链接后，可以看到分享的详情
                    sp.setImageUrl(image);
//

                    //3、非常重要：获取平台对象
                    Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                    wechatMoments.setPlatformActionListener(platformActionListener); // 设置分享事件回调
                    // 执行分享
                    wechatMoments.share(sp);
                    break;
                }
                case R.id.layout_share_collect: {
                    // 分享弹窗消失
                    dismiss();
                    // 刷新界面
                    FanJuActivity activity = (FanJuActivity)getActivity();
                    activity.initdata();

//                    ToastUtils.toastshort("刷新咯~~~~~");
                    break;
                }
            }
        }
    };

    public ShareFragFanJu() {
        // Required empty public constructor

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (rootView == null) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            rootView = inflater.inflate(R.layout.fragment_share_dqg_fanju, null, false);

            RelativeLayout layoutShareWechat = (RelativeLayout) rootView.findViewById(R.id.layout_share_wechat);
            layoutShareWechat.setOnClickListener(SHARE_LISTENER);
            RelativeLayout layoutShareMoments = (RelativeLayout) rootView.findViewById(R.id.layout_share_moments);
            layoutShareMoments.setOnClickListener(SHARE_LISTENER);
            RelativeLayout layoutShareCollect = (RelativeLayout) rootView.findViewById(R.id.layout_share_collect);
            layoutShareCollect.setOnClickListener(SHARE_LISTENER);



        }
        ShareSDK.initSDK(activity);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        Window window = dialog.getWindow();
        dialog.setCanceledOnTouchOutside(true);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        window.setLayout(dm.widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
//        window.setWindowAnimations(R.style.BottomDialogStyle);
        //设置背景透明，不设置dialog可能会出现黑边
        window.setBackgroundDrawableResource(R.drawable.a);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((ViewGroup) (rootView.getParent())).removeView(rootView);
    }

    //设置数据，这个方法参数根据实际需要修改
    public void setData(String url, String title, String image, String text) {
        this.url = url;
        this.title = title;
        this.image = image;
        this.text = text;
    }
}