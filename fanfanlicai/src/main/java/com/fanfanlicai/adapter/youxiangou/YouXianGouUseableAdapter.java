package com.fanfanlicai.adapter.youxiangou;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.invest.FanHeBuyYxgActivity;
import com.fanfanlicai.bean.YouXianGouBean;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.StrToNumber;
import com.fanfanlicai.utils.StringUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;
import com.fanfanlicai.view.dialog.CGBindCardDialog;
import com.fanfanlicai.view.dialog.CGSignCardDialog;
import com.fanfanlicai.view.dialog.CePingAgainDialog;
import com.fanfanlicai.view.dialog.CePingChanceOverDialog;
import com.fanfanlicai.view.dialog.CePingNotDialog;
import com.fanfanlicai.view.dialog.CustomProgressDialog;

import java.util.ArrayList;
import java.util.Map;

public class YouXianGouUseableAdapter extends BaseAdapter {

	private ArrayList<YouXianGouBean> list;
	private Context mContext;
	private CustomProgressDialog progressdialog;

	public YouXianGouUseableAdapter(Context context, ArrayList<YouXianGouBean> list) {
		super();
		this.mContext = context;
		this.list = list;
		progressdialog = new CustomProgressDialog(mContext, "正在加载数据...");
	}
	public YouXianGouUseableAdapter(Context context) {
		super();
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NoticeHolder holder = null;
		if (convertView == null) {
			holder = new NoticeHolder();
			convertView = View.inflate(mContext, R.layout.items_youxiangou_usable, null);
			holder.tv_youxiangou_usetime = (TextView) convertView.findViewById(R.id.tv_youxiangou_usetime);
			holder.tv_invest_money = (TextView) convertView.findViewById(R.id.tv_invest_money);
			holder.tv_youxiangou_info = (TextView) convertView.findViewById(R.id.tv_youxiangou_info);
			holder.tv_youxiangou_method = (TextView) convertView.findViewById(R.id.tv_youxiangou_method);
			holder.iv_use = (ImageView) convertView.findViewById(R.id.iv_use);
			convertView.setTag(holder);
		}else {
			holder = (NoticeHolder) convertView.getTag();
		}
		final YouXianGouBean youXianGouBean = list.get(position);
		
		//优先购有效期
		//以空格截取字符串
		String splitCreateTime[] = youXianGouBean.getCreateTime().split(" ");
		String createTime = splitCreateTime[0];

		String splitExpiredTime[] = youXianGouBean.getExpiredTime().split(" ");
		String expiredTime = splitExpiredTime[0];

		holder.tv_youxiangou_usetime.setText("有效期："+createTime+"至"+expiredTime);
		
		//起投金额
		holder.tv_invest_money.setText("优先购额度："+youXianGouBean.getAmount()+"元");
		
		//适用产品
		holder.tv_youxiangou_info.setText("适用产品："+youXianGouBean.getProductName());
		
		//获得来源
		holder.tv_youxiangou_method.setText("获得来源："+youXianGouBean.getFrom_source());

		//点击使用
		holder.iv_use.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				requestPut(youXianGouBean.getAmount(),youXianGouBean.getId());

			}
		});
		
		return convertView;
	}


	class NoticeHolder{
		TextView tv_youxiangou_usetime;
		TextView tv_invest_money;
		TextView tv_youxiangou_info;
		TextView tv_youxiangou_method;
		ImageView iv_use;
	}

	private void requestPut(final String amount,final String id) {
		if (!progressdialog.isShowing()) {
			progressdialog.showis();
		}
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(mContext, "token", null);
		map.put("token", token);
		map.put("type", "fh");
		map.put("preferTicketId", id);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.GOTOINVEST_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						LogUtils.i("饭盒买入==="+string);
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

									// 账户余额
									String baseBal = data.getString("baseBal");

									// 奖金余额
									String rewardAcctBal = data.getString("rewardAcctBal");
									CacheUtils.putString(mContext, "rewardAcctBal",rewardAcctBal);

									// 交易流水号
									String seqNo = data.getString("seqNo");


									// 是否开通存管户
									String hasFyAccount = data.getString("hasFyAccount");

									// 是否签约
									String isSignCard = data.getString("isSignCard");

									// 是否测评
									Boolean hasEvln = data.getBoolean("hasEvln");
									// 是否可投资
									Boolean canInvest = data.getBoolean("canInvest");
									// 测评剩余次数
									String overTimes = data.getString("overTimes");

									// 是否开通存管户（绑卡）0-未开通；
									if ("0".equals(hasFyAccount)) {// 如果还没有绑卡，去绑卡

										CGBindCardDialog cgBindCardDialog = new CGBindCardDialog(mContext, R.style.YzmDialog);
										cgBindCardDialog.show();
									}else{//1-已开通
										if ("0".equals(isSignCard)) {// 如果还没有签约，去签约
											CGSignCardDialog cgSignCardDialog = new CGSignCardDialog(mContext, R.style.YzmDialog);
											cgSignCardDialog.show();
										}else{//1-已签约
											if(hasEvln){//评测过
												if(canInvest) {//评测过并可投资

													// 账户余额
													if("-9999".equals(baseBal)) {//银行不能正常返回账户余额
														ToastUtils.toastshort("正在获取账户余额...");
													}else{
														Intent intent = new Intent(mContext, FanHeBuyYxgActivity.class);
														intent.putExtra("baseBal", baseBal);
														intent.putExtra("seqNo", seqNo);
														intent.putExtra("amount", amount);
														mContext.startActivity(intent);
													}
												}else{//评测过不可投资
													if(StrToNumber.strTodouble(overTimes)>0){//还有评测次数
														CePingAgainDialog cePingAgainDialog = new CePingAgainDialog(mContext, R.style.YzmDialog,overTimes);
														cePingAgainDialog.show();
													}else{//没有评测次数
														CePingChanceOverDialog cePingChanceOverDialog = new CePingChanceOverDialog(mContext, R.style.YzmDialog);
														cePingChanceOverDialog.show();
													}
												}
											}else{// 未评测过
												CePingNotDialog cePingNotDialog = new CePingNotDialog(mContext, R.style.YzmDialog);
												cePingNotDialog.show();
											}
										}

									}

								} else {
									ToastUtils.toastshort("加载数据异常！");
								}
							}
						}  else if("666666".equals(code)){

						}else {
							String msg = json.getString("msg");
							ToastUtils.toastshort(msg);
						}
					}

					@Override
					public void onError(VolleyError error) {
						progressdialog.dismiss();
					}
				});
	}

}
