package com.fanfanlicai.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.adapter.CGBankListAdapter;
import com.fanfanlicai.bean.BankListBean;
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
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Map;

public class CGBankListActivity extends BaseActivity implements OnClickListener {

	private PullToRefreshListView refreshListView;
	private ListView listView;
	private CustomProgressDialog progressdialog;
	private ArrayList<BankListBean> list = new ArrayList<BankListBean>();
	private CGBankListAdapter adapter;
	private int pagenum=1;
	private int pages;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cgbanklist);

		progressdialog = new CustomProgressDialog(this, "正在获取数据...");
		initView();
		initData();
	}
	
	private void initView() {

		TextView get_back = (TextView) findViewById(R.id.get_back);
		get_back.setOnClickListener(this);
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
		adapter=new CGBankListAdapter(this, list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new NoticeListener());
	}

	private void initData() {
		getDataFromServer();
	}

	private void getDataFromServer() {
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(this, "token", null);
		progressdialog.show();
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.BANKLIST_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						LogUtils.i("银行列表="+string);
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
									ArrayList<BankListBean> listadd = (ArrayList<BankListBean>) JSONArray.parseArray(datastr, BankListBean.class);
									list.addAll(listadd);
									adapter.notifyDataSetChanged();
								} else {
									ToastUtils.toastshort("银行列表获取异常");
								}
							}
						} else {
							ToastUtils.toastshort("银行列表获取失败");
						}
					}

					@Override
					public void onError(VolleyError error) {
						refreshListView.onRefreshComplete();
						progressdialog.dismiss();
						ToastUtils.toastshort("网络请求失败");
					}
				});
	}

	class NoticeListener implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
								long id) {
			ImageView iv_choose = (ImageView) view.findViewById(R.id.iv_choose);
			iv_choose.setImageResource(R.drawable.checkbox_checked);
			BankListBean bankListBean = list.get(position - 1);
			//公告详情页面
			Intent intent = new Intent();
			intent.putExtra("bankName", bankListBean.getBankName());
			intent.putExtra("bankNo", bankListBean.getBankNo());
			setResult(2, intent);
			finish();

		}
	}

	@Override
	public void onClick(View v) {
		 switch (v.getId()) {
			 case R.id.get_back:
				finish();
				break;

		 default:
		 break;
		 }
	}
}
