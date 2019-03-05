package com.fanfanlicai.pagers.jiaxipiao;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.adapter.jiaxipiao.JiaXiPiaoFhUnusedAdapter;
import com.fanfanlicai.bean.JiaXiBean;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.pagers.BasePager;
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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class FHunusedPager extends BasePager{

	public FHunusedPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}


	private PullToRefreshListView refreshListView;
	private ListView listView;
	private CustomProgressDialog progressdialog;
	private ArrayList<JiaXiBean> list;
	private JiaXiPiaoFhUnusedAdapter adapter;
	private int pagenum=1;
	private int pagesize=20;
	private int pages;
	private String code;
	
	@Override
	protected View initView() {
		list = new ArrayList<JiaXiBean>();
		progressdialog = new CustomProgressDialog(mContext, "正在获取数据...");
		View view = View.inflate(mContext, R.layout.fragment_useable, null);
		
		refreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
		// 1.设置刷新模式,上拉和下拉刷新都有
		refreshListView.setMode(Mode.PULL_FROM_END);
		// 2.设置刷新监听器
		refreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			// 下拉刷新和加载更多都会走这个方法
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// 直接请求
				pagenum++;
				if(pagenum>pages){
					refreshListView.setMode(Mode.DISABLED);
				}
				getDataFromServer();

			}
		});
		// 3.获取refreshableView,其实就是ListView
		listView = refreshListView.getRefreshableView();
		listView.setSelector(android.R.color.transparent);
		// 5.设置adapter
		adapter=new JiaXiPiaoFhUnusedAdapter(mContext, list);
		listView.setAdapter(adapter);
		LogUtils.i("失效的的initView");
		return view;
	}

	@Override
	public void initData() {
		// 访问网络
		LogUtils.i("失效的initData");
		if (refreshListView.getMode()!=Mode.DISABLED) {
			//当切换到此页时会走initdata方法，会导致数据重复添加而显示，所以需要清楚数据，又因为当下拉后pagenum自增，请求第二页数据，数据为空，
			//这时不能把之前的list清除掉，所以才这样判断
			list.clear();
			pagenum = 1;
		}
		getDataFromServer();
	}
	
	
	private void getDataFromServer() {
		progressdialog.show();
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(mContext, "token", null);
		// 如果没有登录，直接return，不访问网络了
		if (TextUtils.isEmpty(token)) {
			return;
		}
		map.put("status", 3+"");
		map.put("actCode", code);
		map.put("proCode", "fh");
		map.put("pageNum", pagenum+"");
		map.put("pageSize", pagesize+"");
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.RATECOUPON_URL,
				null, map, new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						LogUtils.i("饭盒已失效==="+string);
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
									ArrayList<JiaXiBean> listadd = (ArrayList<JiaXiBean>) JSONArray.parseArray(getList.toJSONString(), JiaXiBean.class);
									list.addAll(listadd);
									adapter.notifyDataSetChanged();
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
						// TODO Auto-generated method stub
						refreshListView.onRefreshComplete();
						progressdialog.dismiss();
						ToastUtils.toastshort("加载数据失败！");
					}

				});
	
	}

	public void setCode(String code) {
		this.code = code;
	}
}
