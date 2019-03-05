package com.fanfanlicai.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.bean.FTBean;
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
import com.fanfanlicai.view.dialog.CGSignCardDialog;
import com.fanfanlicai.view.dialog.CustomProgressDialog;
import com.fanfanlicai.view.dialog.FanTongDialog;
import com.fanfanlicai.view.dialog.NormalDialog;

import java.util.ArrayList;
import java.util.Map;

public class FanTongAdapter extends BaseAdapter {
	
	private CustomProgressDialog progressdialog;
	private ArrayList<FTBean> list;
	private Context mContext;
	
	public FanTongAdapter(Context context, ArrayList<FTBean> list) {
		super();
		this.mContext = context;
		this.list = list;
		progressdialog = new CustomProgressDialog(mContext, "正在加载数据...");
	}


	public FanTongAdapter(Context context) {
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
			convertView = View.inflate(mContext, R.layout.items_fantong, null);
			holder.tv_code = (TextView) convertView.findViewById(R.id.tv_code);//项目编号
			holder.tv_in_value = (TextView) convertView.findViewById(R.id.tv_in_value);//买入时间
			holder.tv_rate_value = (TextView) convertView.findViewById(R.id.tv_rate_value); //预期年化率
			holder.tv_out_value = (TextView) convertView.findViewById(R.id.tv_out_value); //已赚收益值（或赎回时间值）
			holder.tv_invest_value = (TextView) convertView.findViewById(R.id.tv_invest_value); //投资金额
			holder.tv_day_value = (TextView) convertView.findViewById(R.id.tv_day_value); //已投天数
			holder.tv_cash = (TextView) convertView.findViewById(R.id.tv_cash); //赎回按钮
			holder.rl_over = (RelativeLayout) convertView.findViewById(R.id.rl_over); //已赎回才显示的赎回内容
			holder.tv_over_value = (TextView) convertView.findViewById(R.id.tv_over_value); //赎回手续费值
			holder.iv_isover = (ImageView) convertView.findViewById(R.id.iv_isover); //已赎回图案
			holder.iv_arrow = (ImageView) convertView.findViewById(R.id.iv_arrow); //箭头
			convertView.setTag(holder);
		}else {
			holder = (NoticeHolder) convertView.getTag();
		}
		
		final FTBean ftBean = list.get(position);
		holder.tv_code.setText("项目编号："+ftBean.getPro_code());//项目编号
		holder.tv_in_value.setText(ftBean.getTrans_time());//买入时间
		holder.tv_rate_value.setText(ftBean.getYear_rate()+"%");//预期年化率
		holder.tv_out_value.setText(ftBean.getTotal_interest()+"元");//已赚收益值
		holder.tv_invest_value.setText(ftBean.getInvest_amount()+"元");//投资金额
		holder.tv_day_value.setText(ftBean.getDays()+"天");//已投天数
		String status = ftBean.getStatus();
		//1投资成功；2已赎回
		if ("1".equals(status)) {
			holder.iv_arrow.setVisibility(View.VISIBLE);
			holder.iv_isover.setVisibility(View.INVISIBLE);
			holder.rl_over.setVisibility(View.INVISIBLE);
			holder.tv_cash.setVisibility(View.VISIBLE);
			if(ftBean.isCanRedeemFlag()){
				holder.tv_cash.setBackground(mContext.getResources().getDrawable(R.drawable.shape_chaxun));
			}else{
				holder.tv_cash.setBackground(mContext.getResources().getDrawable(R.drawable.shape_chaxun_gray));
			}
			holder.tv_cash.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(ftBean.isCanRedeemFlag()){
						getInfo(ftBean.getId());
					}else {
						NormalDialog normalDialog = new NormalDialog(mContext, R.style.YzmDialog, ftBean.getForbidRedeemTime());
						normalDialog.show();
					}

				}
			});
		}else if ("2".equals(status)) {
			holder.iv_arrow.setVisibility(View.INVISIBLE);
			holder.iv_isover.setVisibility(View.VISIBLE);
			holder.iv_isover.setImageResource(R.drawable.yishuhui);
			holder.rl_over.setVisibility(View.VISIBLE);
			holder.tv_cash.setVisibility(View.INVISIBLE);
			holder.tv_over_value.setText(ftBean.getRec_fee()+"元");//赎回手续费值
		}else if ("3".equals(status)) {
			holder.iv_arrow.setVisibility(View.INVISIBLE);
			holder.iv_isover.setVisibility(View.VISIBLE);
			holder.iv_isover.setImageResource(R.drawable.huikuanzhong);
			holder.rl_over.setVisibility(View.VISIBLE);
			holder.tv_cash.setVisibility(View.INVISIBLE);
			holder.tv_over_value.setText(ftBean.getRec_fee()+"元");//赎回手续费值
		}
		return convertView;
	}

	
	protected void getInfo(final String id) {

		progressdialog.show();
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(mContext, "token", "");
		map.put("ftId", id);
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.QUERYFTRECFEE_URL,
				null, map, new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						LogUtils.i("饭桶赎回手续费===" + string);
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
									
									//赎回费率
									String feeRate = data.getString("feeRate");
									
									 //赎回手续费
									String fee = data.getString("fee");
									
									// 投资天数
									String investDays = data.getString("investDays");
									
									// 赎回本息
									String totalAmount = data.getString("totalAmount");

									// 赎回时间
									String recAvgTimeStr = data.getString("recAvgTimeStr");

									// 是否签约
									String isSignCard = data.getString("isSignCard");
									if ("0".equals(isSignCard)) {// 如果还没有签约，去签约
										CGSignCardDialog cgSignCardDialog = new CGSignCardDialog(mContext, R.style.YzmDialog);
										cgSignCardDialog.show();
									}else{//1-已签约

										FanTongDialog fanTongDialog = new FanTongDialog(mContext,
												R.style.YzmDialog,investDays,totalAmount,feeRate,fee,recAvgTimeStr,id);
										fanTongDialog.show();
									}

								} else {
									ToastUtils.toastshort("加载数据异常！");
								}
							}
						} else if("666666".equals(code)){

						}else {
							String msg = json.getString("msg");
							ToastUtils.toastshort(msg);
						}

					}

					@Override
					public void onError(VolleyError error) {
						// TODO Auto-generated method stub
						progressdialog.dismiss();
						ToastUtils.toastshort("加载数据失败！");
					}

				});
	
	
		
	}

	class NoticeHolder{
		TextView tv_code;//项目编号
		TextView tv_in_value;//买入时间
		TextView tv_rate_value;//预期年化率
		TextView tv_out_value;//已赚收益值（或赎回时间值）
		TextView tv_invest_value;//投资金额
		TextView tv_day_value;//已投天数
		TextView tv_cash;//赎回按钮
		RelativeLayout rl_over;
		TextView tv_over_value;//赎回手续费值
		ImageView iv_isover;//是否已结清
		ImageView iv_arrow;//跳转箭头
	}
}
