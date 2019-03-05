package com.fanfanlicai.fragment.totalmoney;

import java.util.ArrayList;
import java.util.Map;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.invest.XiangMuDetailActivity;
import com.fanfanlicai.adapter.XiangmuAdapter;
import com.fanfanlicai.bean.FHInvestingBean;
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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class XiangmuFragment extends BaseFragment {
	
	private PullToRefreshListView refreshListView;
	private ListView listView;
	private CustomProgressDialog progressdialog;
	private ArrayList<FHInvestingBean> list = new ArrayList<FHInvestingBean>();
	private XiangmuAdapter adapter;
	private int pagenum=1;
	private int pagesize=20;
	private int pages;
	
	@Override
	protected View initView() {
		progressdialog = new CustomProgressDialog(mActivity, "正在获取数据...");
		
		View view = View.inflate(mActivity, R.layout.fragment_xiangmu, null);
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
		adapter = new XiangmuAdapter(mActivity,list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new XiangmuListener());
		return view;
	}
	
	@Override
	public void initData() {
		// 访问网络
		getDataFromServer();
	}
	
	
	private void getDataFromServer() {

		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(mActivity, "token", null);
		// 如果没有登录，直接return，不访问网络了
		if (TextUtils.isEmpty(token)) {
			return;
		}
		map.put("pageNum", pagenum+"");
		map.put("pageSize", pagesize+"");
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.LOANS_URL,
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
									LogUtils.i("邀请详情返回数据" + datastr);
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
									ArrayList<FHInvestingBean> listadd = (ArrayList<FHInvestingBean>) JSONArray.parseArray(getList.toJSONString(), FHInvestingBean.class);
									list.addAll(listadd);
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


	class XiangmuListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			System.out.println("position:"+position);
			FHInvestingBean investingBean = list.get(position-1);
			Intent intent = new Intent(mActivity,XiangMuDetailActivity.class);
			intent.putExtra("borrowCode", investingBean.borrowCode);
			intent.putExtra("borrowName", investingBean.borrowName);
			intent.putExtra("trulyUserName", investingBean.trulyUserName);
			intent.putExtra("trulyIdcard", investingBean.trulyIdcard);
			intent.putExtra("borrowMoney", investingBean.borrowMoney+"");
			intent.putExtra("days", investingBean.days+"");
			intent.putExtra("deadlineStr", investingBean.deadlineStr);
			intent.putExtra("id", investingBean.id);
			mActivity.startActivity(intent);
		}
	}
}