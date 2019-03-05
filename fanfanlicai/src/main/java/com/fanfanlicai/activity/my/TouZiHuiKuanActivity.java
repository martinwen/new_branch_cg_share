package com.fanfanlicai.activity.my;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.adapter.ZhanNeiXinAdapter;
import com.fanfanlicai.bean.ZhanNeiXinBean;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.StringUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;
import com.fanfanlicai.view.dialog.CustomProgressDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Map;


public class TouZiHuiKuanActivity extends BaseActivity implements View.OnClickListener {

    private PopupWindow pop;
    private TextView tv_title;

    private LinearLayout ll_sign;
    private TextView tv_sign;
    private TextView tv_all;
    private TextView tv_cancel;
    private PullToRefreshListView refreshListView;
    private ListView listView;
    private CustomProgressDialog progressdialog;
    private ArrayList<ZhanNeiXinBean> list = new ArrayList<ZhanNeiXinBean>();
    private ZhanNeiXinAdapter adapter;
    private int pagenum=1;
    private int pagesize=20;
    private int pages;
    // 是否全选
    private boolean selectAll;

    private int num;//记录点击的条目位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_zhan_nei_xin);
        progressdialog = new CustomProgressDialog(this, "正在获取数据...");
        initView();
        initData();
    }

    private void initView() {
        TextView tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("出借回款");
        tv_title.setOnClickListener(this);

        ll_sign = (LinearLayout) findViewById(R.id.ll_sign);
        tv_sign = (TextView) findViewById(R.id.tv_sign);
        tv_sign.setOnClickListener(this);
        tv_all = (TextView) findViewById(R.id.tv_all);
        tv_all.setOnClickListener(this);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);
        refreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);

        // 1.设置刷新模式,上拉和下拉刷新都有
        refreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        // 2.设置刷新监听器
        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            // 下拉刷新和加载更多都会走这个方法
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // 直接请求
                pagenum++;
                if(pagenum>pages){
                    refreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
                }
                getDataFromServer();

            }
        });
        // 3.获取refreshableView,其实就是ListView
        listView = refreshListView.getRefreshableView();
        // 5.设置adapter
        adapter=new ZhanNeiXinAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new NoticeListener());
    }

    private void initData() {
        getDataFromServer();
    }

    class NoticeListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            num=position - 1;
            ZhanNeiXinBean zhanNeiXinBean = list.get(position - 1);
            //公告详情页面
            String id_items = zhanNeiXinBean.getId();

            Intent intent = new Intent(TouZiHuiKuanActivity.this,ZhanNeiXinDetailActivity.class);
            intent.putExtra("id_items", id_items);
            startActivityForResult(intent,1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(list.size()>0){
                list.get(num).setStatus("2");
                adapter.notifyDataSetChanged();
                listView.setSelection(num);
            }
        }
    }

    private void getDataFromServer() {
        Map<String, String> map = SortRequestData.getmap();
        String token = CacheUtils.getString(this, "token", null);
        map.put("pageNum", pagenum+"");
        map.put("pageSize", pagesize+"");
        map.put("type", "3");
        map.put("token", token);
        String requestData = SortRequestData.sortString(map);
        String signData = SignUtil.sign(requestData);
        map.put("sign", signData);
        VolleyUtil.sendJsonRequestByPost(ConstantUtils.STATIONLETTER_LIST_URL,
                null, map, new HttpBackBaseListener() {

                    @Override
                    public void onSuccess(String string) {
                        refreshListView.onRefreshComplete();
                        // TODO Auto-generated method stub
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
                                    ArrayList<ZhanNeiXinBean> listadd = (ArrayList<ZhanNeiXinBean>) JSONArray.parseArray(getList.toJSONString(), ZhanNeiXinBean.class);
                                    list.addAll(listadd);
                                    adapter.setList(list,selectAll);
                                    adapter.setsubClickListener(new ZhanNeiXinAdapter.SubClickListener() {
                                        @Override
                                        public void OntopicClickListener() {
                                            for (int i = 0; i < ZhanNeiXinAdapter.getIsSelected().size(); i++) {
                                                if (ZhanNeiXinAdapter.getIsSelected().get(i)) {
                                                    ll_sign.setVisibility(View.VISIBLE);
                                                    return;
                                                }
                                            }
                                            ll_sign.setVisibility(View.GONE);
                                        }
                                    });

                                    adapter.notifyDataSetChanged();

                                } else {
                                    ToastUtils.toastshort("加载数据异常！");
                                }
                            }
                        } else {
                            ToastUtils.toastshort("加载数据失败！");
                        }

                    }

                    @Override
                    public void onError(VolleyError error) {
                        // TODO Auto-generated method stub
                        refreshListView.onRefreshComplete();
                        ToastUtils.toastshort("加载数据失败！");
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_title:
                View inflate = View.inflate(this, R.layout.activity_pop, null);
                TextView tv_up = (TextView) inflate.findViewById(R.id.tv_up);
                tv_up.setText("账户操作");
                tv_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pop.dismiss();
                        startActivity(new Intent(TouZiHuiKuanActivity.this,ZhangHuCaoZuoActivity.class));
                        finish();
                    }
                });
                TextView tv_middle = (TextView) inflate.findViewById(R.id.tv_middle);
                tv_middle.setText("会员权益");
                tv_middle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pop.dismiss();
                        startActivity(new Intent(TouZiHuiKuanActivity.this,HuiYuanQuanYiActivity.class));
                        finish();
                    }
                });
                TextView tv_down = (TextView) inflate.findViewById(R.id.tv_down);
                tv_down.setText("全部");
                tv_down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pop.dismiss();
                        startActivity(new Intent(TouZiHuiKuanActivity.this,ZhanNeiXinActivity.class));
                        finish();
                    }
                });
                pop = new PopupWindow(inflate, dip2px(100), dip2px(110));
                // 设置焦点
                pop.setFocusable(true);
                // 设置背景，为了点击外面时，把Popupwindow消失
                pop.setBackgroundDrawable(new BitmapDrawable());
                // 展示Popupwindow,显示在输入框的下面
                pop.showAsDropDown(tv_title, 0, 0);
                break;
            case R.id.tv_sign:
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < list.size(); i++) {
                    if (ZhanNeiXinAdapter.getIsSelected().get(i)) {
                        sb.append(list.get(i).getId()).append(",");
                    }
                }
                String articleIds = sb.toString();
                articleIds = articleIds.substring(0,articleIds.length()-1);
                sign(articleIds);
                break;
            case R.id.tv_all:
                selectAll = true;
                // 遍历list的长度，将MyAdapter中的map值全部设为true
                for (int i = 0; i < list.size(); i++) {
                    ZhanNeiXinAdapter.getIsSelected().put(i, true);
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.tv_cancel:
                selectAll = false;
                // 遍历list的长度，将已选的按钮设为未选
                for (int i = 0; i < list.size(); i++) {
                    if (ZhanNeiXinAdapter.getIsSelected().get(i)) {
                        ZhanNeiXinAdapter.getIsSelected().put(i, false);
                    }
                }
                adapter.notifyDataSetChanged();
                ll_sign.setVisibility(View.GONE);
                break;
        }
    }

    private void sign(String articleIds) {
        progressdialog.show();
        Map<String, String> map = SortRequestData.getmap();
        String token = CacheUtils.getString(this, "token", null);
        map.put("token", token);
        map.put("letterIds", articleIds);
        map.put("selectAll", selectAll?"1":"0");
        String requestData = SortRequestData.sortString(map);
        String signData = SignUtil.sign(requestData);
        map.put("sign", signData);
        VolleyUtil.sendJsonRequestByPost(ConstantUtils.STATIONLETTER_SETHAVEREADOPER_URL, null, map,
                new HttpBackBaseListener() {

                    @Override
                    public void onSuccess(String string) {
                        progressdialog.dismiss();

                        JSONObject json = JSON.parseObject(string);
                        String  code= json.getString("code");
                        if("0".equals(code)){
                            for (int i = 0; i < list.size(); i++) {
                                if (ZhanNeiXinAdapter.getIsSelected().get(i)) {
                                    list.get(i).setStatus("2");
                                    ZhanNeiXinAdapter.getIsSelected().put(i, false);
                                }
                            }
                            adapter.notifyDataSetChanged();
                            ll_sign.setVisibility(View.GONE);

                            selectAll = false;
                        }else{
                            String msg = json.getString("msg");
                            ToastUtils.toastshort(msg);
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        progressdialog.dismiss();
                        ToastUtils.toastshort("网络异常");
                    }
                });
    }

    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
