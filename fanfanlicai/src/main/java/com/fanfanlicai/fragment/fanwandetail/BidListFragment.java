package com.fanfanlicai.fragment.fanwandetail;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.adapter.BidListAdapter;
import com.fanfanlicai.bean.BiaoDeBean;
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
import com.fanfanlicai.view.dialog.CustomProgressDialog;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author lijinliu
 * @date 20180129
 * 标的组成
 */

public class BidListFragment extends BaseFragment {
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    private CustomProgressDialog progressdialog;
    private int pagenum=1;
    private int pagesize=10;
    private int pages;
    private ArrayList<BiaoDeBean> list = new ArrayList<BiaoDeBean>();
    private BidListAdapter adapter;
    private String pid;
    private String mProductDataShowType;
    private String mInvestDays;

    @Override
    protected View initView() {
        progressdialog = new CustomProgressDialog(mActivity, "数据加载中...");

        View view = View.inflate(mActivity, R.layout.fragment_bidlist, null);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        adapter = new BidListAdapter(getActivity(), list, mProductDataShowType, mInvestDays);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setAdapter(adapter);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 直接请求
                pagenum++;
                if (pagenum > pages) {
                    swipeRefresh.setEnabled(false);
                }
                requestServer();
            }
        });
        return view;
    }

    @Override
    public void initData() {
        //加载数据
        if (list == null || list.size() <= 0) {
            requestServer();
        }
    }

    private void requestServer() {
        String url;
        Map<String, String> map = SortRequestData.getmap();
        String token = CacheUtils.getString(mActivity, "token", null);
        progressdialog.showis();
        if ("zt".equals(mProductDataShowType)) {
            map.put("pid", pid);
            url = ConstantUtils.GETPACKAGEINFOLIST_URL;
        } else {
            url = ConstantUtils.GETFWPACKAGEINFOLIST_URL;
        }
        map.put("pageNum", pagenum+"");
        map.put("pageSize", pagesize+"");
        map.put("token", token);
        String requestData = SortRequestData.sortString(map);
        String signData = SignUtil.sign(requestData);
        map.put("sign", signData);
        VolleyUtil.sendJsonRequestByPost(url, null, map,
                new HttpBackBaseListener() {

                    @Override
                    public void onSuccess(String string) {
                        LogUtils.longStr("标的组成列表===", string);
                        swipeRefresh.setRefreshing(false);
                        progressdialog.dismiss();
                        JSONObject json = JSON.parseObject(string);
                        String code = json.getString("code");

                        if ("0".equals(code)) {
                            String datastr = json.getString("data");
                            if (StringUtils.isBlank(datastr)) {
                                // datastr为空不验签
                            } else {
                                String sign = json.getString("sign");
                                boolean isSuccess = SignUtil.verify(sign,datastr);
                                if (isSuccess) {// 验签成功
                                    JSONObject data = JSON.parseObject(datastr);
                                    //当前页码
                                    String pageNum = data.getString("pageNum");
                                    //每页条数
                                    String pageSize = data.getString("pageSize");
                                    //总页数
                                    pages = data.getInteger("pages");
                                    // 总条数
                                    int total = data.getInteger("total");
                                    //list
                                    JSONArray getList=data.getJSONArray("list");
                                    ArrayList<BiaoDeBean> listadd = (ArrayList<BiaoDeBean>) JSONArray.parseArray(getList.toJSONString(), BiaoDeBean.class);
                                    list.addAll(listadd);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    ToastUtils.toastshort("获取列表异常！");
                                }
                            }
                        } else {
                            String msg = json.getString("msg");
                            ToastUtils.toastshort(msg);
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        swipeRefresh.setRefreshing(false);
                        progressdialog.dismiss();
                        ToastUtils.toastshort("获取列表失败！");
                    }
                });
    }

    public void setData(String pid, String productDataShowType , String investDays) {
        this.pid = pid;
        this.mProductDataShowType = productDataShowType;
        this.mInvestDays = investDays;
    }
}
