package com.fanfanlicai.fragment.invest;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.invest.XiangMuActivity;
import com.fanfanlicai.adapter.FanWanAdapter;
import com.fanfanlicai.bean.FWBean;
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
import com.fanfanlicai.view.pickerView.TimePickerView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class FanWanInvestFragment extends BaseFragment implements OnClickListener{

	private PullToRefreshListView refreshListView;
	private ListView listView;
	private CustomProgressDialog progressdialog;
	private ArrayList<FWBean> list = new ArrayList<FWBean>();
	private FanWanAdapter adapter;
	private int pagenum=1;
	private int pagesize=20;
	private int pages;
	private RelativeLayout rl_day;
	private TextView tv_day;//日期
	private TextView tv_chaxun;//查询
	private boolean isOpen = false;// 日期选择菜单是否打开
	private ImageView iv_arrow;// 日期选择的按钮
	private PopupWindow pop;// 日期选择的弹窗
	private ListView listView_day;// 银行选择的listview
	private TextView tv_fanwanzichan;//饭碗资产
	
	private TimePickerView pvTime;
	private String currentTime;
	
	@Override
	protected View initView() {
		progressdialog = new CustomProgressDialog(mActivity, "正在获取数据...");
		View view = View.inflate(mActivity, R.layout.fragment_fanwaninvest, null);
		rl_day = (RelativeLayout) view.findViewById(R.id.rl_day);//日期背景条
		rl_day.setOnClickListener(this);
		iv_arrow = (ImageView) view.findViewById(R.id.iv_arrow);
		tv_day = (TextView) view.findViewById(R.id.tv_day);//日期
		tv_chaxun = (TextView) view.findViewById(R.id.tv_chaxun);//查询
		tv_chaxun.setOnClickListener(this);
		tv_fanwanzichan = (TextView) view.findViewById(R.id.tv_fanwanzichan);//饭碗资产
		
		 //时间选择器
        pvTime = new TimePickerView(mActivity, TimePickerView.Type.YEAR_MONTH);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 6, calendar.get(Calendar.YEAR) + 14);//要在setTime 之前才有效果哦
        
        
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

			@Override
            public void onTimeSelect(Date date) {
				//在这里用此方法展示时间（有年和月）
            	tv_day.setText(getTime(date));
            	LogUtils.i("getTime(date)==="+getTime(date));
            	//因后台需要接收查询的时间参数，所以在这里用此方法获取到时间（没有年和月）
            	currentTime = getCurrentTime(date);
            	LogUtils.i("getCurrentTime(date)==="+getCurrentTime(date));
            }
        });
        //弹出时间选择器
        rl_day.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });
		
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

				getDataFromServer(currentTime);
			}
		});
		// 3.获取refreshableView,其实就是ListView
		listView = refreshListView.getRefreshableView();
		adapter = new FanWanAdapter(mActivity,list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new FanWanListener());
		return view;
	}

	@Override
	public void initData() {
		//饭碗资产
		String fwAcctBal = CacheUtils.getString(mActivity, CacheUtils.FWACCTBAL, "0.00");
		tv_fanwanzichan.setText(fwAcctBal+"元");
		// 访问网络，第一次进来传空，会获取所有的饭碗投资记录
		if (refreshListView.getMode()!=Mode.DISABLED) {
			//当切换到此页时会走initdata方法，会导致数据重复添加而显示，所以需要清除数据，又因为当下拉后pagenum自增，请求第二页数据，数据为空，
			//这时不能把之前的list清除掉，所以才这样判断
			list.clear();
			pagenum = 1;
		}
		getDataFromServer("");
	}

	
	public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年-MM月");
        return format.format(date);
    }
	
	public static String getCurrentTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		return format.format(date);
	}
	
	class FanWanListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			FWBean fwBean = list.get(position-1);
			if ("1".equals(fwBean.getStatus())) {
				Intent intent = new Intent(mActivity,XiangMuActivity.class);
				//1代表从饭碗投资界面进入项目界面
				intent.setFlags(1);
				intent.putExtra("transUuid", fwBean.getTransUuid());
				mActivity.startActivity(intent);
			}
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_chaxun:
			//初始化进入后，可能用户已经下拉到最后页，这时需要把查询页码数改为1
			pagenum=1;
			//同理，可能之前用户已经下拉到最后页，已无法刷新，这里需要更改状态
			refreshListView.setMode(Mode.PULL_FROM_END);
			//查询对应月份的数据前需要把之前list获取到的数据清空
			list.clear();
			//查询对应月份的数据
			getDataFromServer(currentTime);
			break;
		default:
			break;
		}
		
	}
	
	private void getDataFromServer(String currentTime) {
		progressdialog.show();
		LogUtils.i("饭碗投资项目时间===" + currentTime);
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(mActivity, "token", null);
		map.put("transTime", currentTime);
		map.put("pageNum", pagenum+"");
		LogUtils.i("pagenum===" + pagenum);
		map.put("pageSize", pagesize+"");
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.FW_INVEST_URL,
				null, map, new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						LogUtils.i("饭碗投资项目===" + string);
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
									ArrayList<FWBean> listadd = (ArrayList<FWBean>) JSONArray.parseArray(getList.toJSONString(), FWBean.class);
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
}
