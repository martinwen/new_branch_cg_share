package com.fanfanlicai.fragment.fanwandetail.biddetail;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.fanfanlicai.bean.BidPicBean;
import com.fanfanlicai.bean.ImageInfo;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.fragment.BaseFragment;
import com.fanfanlicai.view.tupian.GvAdapter;
import com.fanfanlicai.view.tupian.PicShowDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lijinliu
 * @date 20180129
 * 标的项目图片信息
 */

public class BidPicInfoFragment extends BaseFragment {

    private GridView mGridView;
    private List<ImageInfo> mImageList;
    private List<BidPicBean> mBidPicBeans;

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_bidpicinfo, null);
        mGridView = (GridView) view.findViewById(R.id.gv_pic);
        return view;
    }

    @Override
    public void initData() {
        mImageList = new ArrayList<>();
        for (BidPicBean bidPicBean : mBidPicBeans) {
            ImageInfo imageInfo = new ImageInfo(bidPicBean.getPicUrl(), 200, 200);
            mImageList.add(imageInfo);
        }
        mGridView.setSelector(android.R.color.transparent);
        mGridView.setAdapter(new GvAdapter(mActivity, mImageList));
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PicShowDialog dialog = new PicShowDialog(mActivity, mImageList, position);
                dialog.show();
            }
        });
    }

    public void setPicList(List<BidPicBean> bidPics) {
        this.mBidPicBeans = bidPics;
    }
}
