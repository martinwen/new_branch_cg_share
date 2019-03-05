package com.fanfanlicai.fragment.fanwandetail;

import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.adapter.fund.FanWanLiuShuiAdapter;
import com.fanfanlicai.bean.ShouYiBean;
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
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author lijinliu
 * @date 20180204
 * 饭碗流水
 */

public class FanWanLiuShuiFragment extends BaseFragment {

    //3全部 ， 12买入，13回款
    private String mType = "3";
    private ArrayList<ShouYiBean> mList = new ArrayList<ShouYiBean>();
    private FanWanLiuShuiAdapter mAdapter;
    private ListView mNewListView;
    private RefreshLayout refreshLayout;

    private int pagenum = 1;
    private int pagesize = 20;
    private int pages;
    private boolean isRefersh = true;

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_fanwanliushui, null);
        // 4.设置adapter
        mAdapter = new FanWanLiuShuiAdapter(mActivity, mList);
        mNewListView = (ListView) view.findViewById(R.id.list_view);
        mNewListView.setAdapter(mAdapter);
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pagenum = 1;
                isRefersh = true;
                refreshLayout.resetNoMoreData();
                mList.clear();
                //refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                getDataFromServer();
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                //refreshlayout.finishLoadmore(2000/*,false*/);//传入false表示加载失败
                pagenum++;
                isRefersh = false;
                getDataFromServer();
            }
        });
        return view;
    }

    @Override
    public void initData() {
        // 访问网络
        if (mList == null || mList.size() <= 0)
        {
            getDataFromServer();
        }
    }

    protected void getDataFromServer() {
        Map<String, String> map = SortRequestData.getmap();
        String token = CacheUtils.getString(mActivity, "token", null);
        // 如果没有登录，直接return，不访问网络了
        if (TextUtils.isEmpty(token)) {
            return;
        }
        map.put("type", mType);
        map.put("pageNum", pagenum + "");
        map.put("pageSize", pagesize + "");
        map.put("token", token);
        String requestData = SortRequestData.sortString(map);
        String signData = SignUtil.sign(requestData);
        map.put("sign", signData);
        VolleyUtil.sendJsonRequestByPost(ConstantUtils.TRANSLIST_URL,
                null, map, new HttpBackBaseListener() {

                    @Override
                    public void onSuccess(String string) {
                        // TODO Auto-generated method stub
                        JSONObject json = JSON.parseObject(string);
                        String code = json.getString("code");
                        LogUtils.i("饭碗资产流水=====" + string);
                        if ("0".equals(code)) {
                            String datastr = json.getString("data");
                            if (StringUtils.isBlank(datastr)) {
                                // datastr为空不验签
                            } else {
                                String sign = json.getString("sign");
                                boolean isSuccess = SignUtil.verify(sign, datastr);
                                if (isSuccess) {// 验签成功

                                    JSONObject data = JSON.parseObject(datastr);
                                    //当前页码
                                    //String pageNum = data.getString("pageNum");
                                    //每页条数
                                    String pageSize = data.getString("pageSize");
                                    //总页数
                                    pages = data.getInteger("pages");
                                    // 总条数
                                    int total = data.getInteger("total");
                                    //list
                                    JSONArray getList = data.getJSONArray("list");
                                    ArrayList<ShouYiBean> listadd = (ArrayList<ShouYiBean>) JSONArray.parseArray(getList.toJSONString(), ShouYiBean.class);

                                    mList.addAll(listadd);
                                    mAdapter.notifyDataSetChanged();
                                } else {
                                    ToastUtils.toastshort("加载数据异常！");
                                }
                            }
                        } else {
                            String msg = json.getString("msg");
                            ToastUtils.toastshort(msg);
                        }

                        if(isRefersh) {
                            refreshLayout.finishRefresh();
                        }else {
                            refreshLayout.finishLoadmore();
                        }

                        if (pagenum >= pages) {
                            refreshLayout.finishLoadmoreWithNoMoreData();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub
                        if(isRefersh) {
                            refreshLayout.finishRefresh();
                        }else {
                            refreshLayout.finishLoadmore();
                        }
                        ToastUtils.toastshort("加载数据失败！");
                    }

                });
    }

    public void setData(String type) {
        this.mType = type;
    }
}
