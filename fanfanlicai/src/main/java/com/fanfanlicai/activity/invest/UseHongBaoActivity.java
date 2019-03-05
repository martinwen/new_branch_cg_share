
package com.fanfanlicai.activity.invest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.adapter.hongbao.HongBaoFwBuyAdapter;
import com.fanfanlicai.bean.HongBaoBean;
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

public class UseHongBaoActivity extends BaseActivity implements OnClickListener {

	private CustomProgressDialog progressdialog;
	private TextView get_back;// 返回
	private TextView tv_unusehongbao;// 不使用红包
	private PullToRefreshListView refreshListView;
	private ListView listView;
	private ArrayList<HongBaoBean> list = new ArrayList<HongBaoBean>();
	private HongBaoFwBuyAdapter adapter;
	private int pagenum=1;
	private int pagesize=20;
	private int pages;
	private String ticketId = null;
	private String type;
	private String reward;
	private String useCondition;//红包使用条件
	private String amount;
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_usehongbao);

		progressdialog = new CustomProgressDialog(this, "正在加载数据...");
		initView();
		initData();
	}

	private void initView() {
		get_back = (TextView) findViewById(R.id.get_back);// 返回
		get_back.setOnClickListener(this);
		tv_unusehongbao = (TextView) findViewById(R.id.tv_unusehongbao);// 返回
		tv_unusehongbao.setOnClickListener(this);
		
		refreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		

	}

	private void initData() {
		intent = getIntent();
		type = intent.getStringExtra("type");
		ticketId = intent.getStringExtra("ticketId");
        amount = intent.getStringExtra("amount");
		getDataFromServer();
		
		
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
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
									long id) {

				HongBaoBean hongBaoBean = list.get(position-1);
				ticketId = hongBaoBean.getId();
				reward = hongBaoBean.getReward();
				useCondition = hongBaoBean.getUseCondition();
                intent.putExtra("ticketId", ticketId);
				intent.putExtra("reward", reward);
				intent.putExtra("useCondition", useCondition);
				setResult(1, intent);
				finish();
			}
		});
		listView.setSelector(android.R.color.transparent);
		// 5.设置adapter
		adapter=new HongBaoFwBuyAdapter(this, list, ticketId);
		listView.setAdapter(adapter);
	}
	
	protected void getDataFromServer() {
		progressdialog.show();
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(this, "token", null);
		// 如果没有登录，直接return，不访问网络了
		if (TextUtils.isEmpty(token)) {
			return;
		}
		map.put("status", 1+"");
		map.put("proCode", type);
		LogUtils.i("type=="+type);
		map.put("amount", amount);
		map.put("pageNum", pagenum+"");
		map.put("pageSize", pagesize+"");
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.REDBAG_URL,
				null, map, new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						LogUtils.i("使用红包=="+string);
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
									ArrayList<HongBaoBean> listadd = (ArrayList<HongBaoBean>) JSONArray.parseArray(getList.toJSONString(), HongBaoBean.class);
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

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_back:
			finish();
			break;
		case R.id.tv_unusehongbao:// 不使用红包
			ticketId = null;
			reward = null;
			intent = getIntent();
			intent.putExtra("ticketId", ticketId);
			intent.putExtra("reward", reward);
			setResult(1, intent);
			finish();
			break;
		default:
			break;
		}
	}
}
