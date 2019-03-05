package com.fanfanlicai.pagers;

import android.content.Context;
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
import com.fanfanlicai.activity.registerandlogin.HomeLoginActivity;
import com.fanfanlicai.adapter.FindAdapter;
import com.fanfanlicai.bean.FindBean;
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
import com.fanfanlicai.view.dialog.CustomProgressDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Map;

public class FindPager extends BasePager {

	private ArrayList<FindBean> list;
	private PullToRefreshListView refreshListView;
	private ListView listView;
	private FindAdapter adapter;
	private CustomProgressDialog progressdialog;
	private int pagenum=1;
	private int pagesize=25;
	private int pages;

	public FindPager(Context context) {
		super(context);
	}

	@Override
	protected View initView() {
		list = new ArrayList<FindBean>();
		progressdialog = new CustomProgressDialog(mContext, "正在加载图片...");
		View view = View.inflate(mContext, R.layout.pager_find, null);
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
		 
		adapter = new FindAdapter(mContext, list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new FindListener());
		LogUtils.i("发现页initView===");
		return view;
	}


	@Override
	public void initData() {
		// 访问网络
		LogUtils.i("发现页initData===");
		if (refreshListView.getMode()!=Mode.DISABLED) {
			//当切换到发现页时会走initdata方法，会导致数据重复添加而显示，所以需要清除数据，又因为当下拉后pagenum自增，请求第二页数据，数据为空，
			//这时不能把之前的list清除掉，所以才这样判断
			list.clear();
			pagenum = 1;
		}
		getDataFromServer();
	}
	
	private void getDataFromServer() {

		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(mContext, "token", null);
		progressdialog.showis();
		map.put("type", "4");
		map.put("pagination", "true");
		map.put("pageNum", pagenum+"");
		map.put("pageSize", pagesize+"");
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.FIND_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						LogUtils.i("发现页==="+string);
						if (string.length() > 4000) {
							for (int i = 0; i < string.length(); i += 4000) {
								//当前截取的长度<总长度则继续截取最大的长度来打印
								if (i + 4000 < string.length()) {
									LogUtils.i("发现页===" + i, string.substring(i, i + 4000));
								} else {
									//当前截取的长度已经超过了总长度，则打印出剩下的全部信息
									LogUtils.i("发现页===" + i, string.substring(i, string.length()));
								}
							}
						} else {
							//直接打印
							LogUtils.i("发现页===", string);
						}
						refreshListView.onRefreshComplete();
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
									ArrayList<FindBean> listadd = (ArrayList<FindBean>) JSONArray.parseArray(getList.toJSONString(), FindBean.class);
									list.addAll(listadd);
									adapter.notifyDataSetChanged();
								} else {
									ToastUtils.toastshort("图片加载异常！");
								}
							}
						} else {
							String msg = json.getString("msg");
							ToastUtils.toastshort(msg);
						}
					}

					@Override
					public void onError(VolleyError error) {
						refreshListView.onRefreshComplete();
						progressdialog.dismiss();
						ToastUtils.toastshort("图片加载失败！");
					}
				});
	}
	
	class FindListener implements OnItemClickListener{
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			String token = CacheUtils.getString(mContext, "token", null);

			FindBean findBean = list.get(position-1);
			String url = findBean.getLinkUrl();
			String title = findBean.getTitle();
			String isNeedLogin = findBean.getIsNeedLogin();

			if ("1".equals(isNeedLogin)) {
				if (TextUtils.isEmpty(token)) {
					mContext.startActivity(new Intent(mContext,HomeLoginActivity.class));
				}else {
					//跳转到url对应的网址
					if (url != null&&!"".equals(url)) {
						Intent intent=new Intent(mContext,WebviewActivity.class);
						intent.putExtra("url", url);
						intent.putExtra("title", title);
						mContext.startActivity(intent);
					}
				}
			}else{
				//跳转到url对应的网址
				if (url != null&&!"".equals(url)) {
					Intent intent=new Intent(mContext,WebviewActivity.class);
					intent.putExtra("url", url);
					intent.putExtra("title", title);
					mContext.startActivity(intent);
				}
			}
		}
		
	}
}
