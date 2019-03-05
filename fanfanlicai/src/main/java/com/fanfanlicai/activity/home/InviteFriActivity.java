package com.fanfanlicai.activity.home;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.adapter.FindAdapter;
import com.fanfanlicai.bean.FindBean;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.global.ImageLoaderOptions;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Map;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class InviteFriActivity extends BaseActivity implements OnClickListener {

	private CustomProgressDialog progressdialog;
	private TextView reward_back;// 返回
	private TextView tv_check;// 查看详情
	private TextView money_today;// 昨日赚取
	private TextView money_total;// 累计邀请赚取
	private TextView tv_register;// 邀请注册的人数
	private TextView tv_invest;// 邀请投资的人数
	private TextView tv_invitecode;// 邀请码

	private String inviteCode;
	private ImageView iv_saoma;
	private ImageView iv_share;
	private ImageView iv_banner;
	
	private String url;
	private String title;
	private String image;
	private String text;
	private ArrayList<FindBean> list;;
	private FindAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_invitefri);

		progressdialog = new CustomProgressDialog(this, "正在加载数据...");
		initView();
		initData();
	}

	private void initView() {
		
		iv_saoma = (ImageView) findViewById(R.id.iv_saoma);//扫码图片
		reward_back = (TextView) findViewById(R.id.reward_back);// 返回
		reward_back.setOnClickListener(this);
		iv_share = (ImageView) findViewById(R.id.iv_share);
		iv_share.setOnClickListener(this);
		iv_banner = (ImageView) findViewById(R.id.iv_banner);
		tv_check = (TextView) findViewById(R.id.tv_check);// 邀请详情
		tv_check.setOnClickListener(this);
		money_today = (TextView) findViewById(R.id.money_today);// 昨日邀请赚取
		money_total = (TextView) findViewById(R.id.money_total);// 累计邀请赚取
		tv_register = (TextView) findViewById(R.id.tv_register);// 邀请注册人数
		tv_invest = (TextView) findViewById(R.id.tv_invest);// 邀请投资人数
		tv_invitecode = (TextView) findViewById(R.id.tv_invitecode);// 邀请码
	}

	private void initData() {
		//初始化邀请码的值
		inviteCode=CacheUtils.getString(this, CacheUtils.INVITECODE, "");
		// 拿到banner图片
		requestServer();
		// 访问网络
		getDataFromServer();
		
		
		
	}
	
	private void requestServer() {
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(this, "token", null);
		if (!progressdialog.isShowing()) {
			progressdialog.show();
		}
		
		map.put("type", "5");
		map.put("pagination", "false");
		map.put("pageNum", "1");
		map.put("pageSize", "20");
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.FIND_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
						LogUtils.i("邀请页banner图片==="+string);
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
//									 //当前页码
//									String pageNum = data.getString("pageNum");
//									//每页条数
//									String pageSize = data.getString("pageSize");
//									// 总条数
//									int total = data.getInteger("total");
									//list
									JSONArray getList=data.getJSONArray("list");
									ArrayList<FindBean> list = (ArrayList<FindBean>) JSONArray.parseArray(getList.toJSONString(), FindBean.class);
									
									String imgSrc=null;
									if (list.size()!=0) {
										imgSrc = list.get(0).getImgSrc();
									}
									//加载图片
									ImageLoader.getInstance().displayImage(imgSrc,iv_banner,ImageLoaderOptions.fadein_options);
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
						progressdialog.dismiss();
						ToastUtils.toastshort("图片加载失败！");
					}
				});
		
	}

	/**
	 * 字符串生成二维码
	 */
	
	public static Bitmap Create2DCode(String str) throws WriterException {
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, 300, 300);
		int width = matrix.getWidth();
		int height = matrix.getHeight();

		final int WHITE = 0xFFFFFFFF;
		// 整体为黑色
		 final int BLACK = 0xFF000000;
//		final int RED = 0xFFFF0000;
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
//					pixels[y * width + x] = RED;
					 pixels[y * width + x-2] = BLACK ;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixel(0, 0, WHITE);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	private void getDataFromServer() {
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(this, "token", null);
		// 如果没有登录，直接return，不访问网络了
		if (TextUtils.isEmpty(token)) {
			return;
		}
		if (!progressdialog.isShowing()) {
			progressdialog.show();
		}
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.MY_INVITE_REWARD_URL,
				null, map, new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
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
									LogUtils.i("邀请界面返回数据==" + datastr);
									JSONObject data = JSON.parseObject(datastr);
									
									// 邀请码
									inviteCode = data.getString("inviteCode");

									//二维码地址
									String qrcode = data.getString("qrcode");
									//生成二维码图片
									try {
										Bitmap cBitmap = Create2DCode(qrcode);
										if (cBitmap != null) {
											iv_saoma.setImageBitmap(cBitmap);
											iv_saoma.invalidate();
										}
									} catch (WriterException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									
									// 邀请投资的人数
									String inviteInvestCount = data.getString("inviteInvestCount");
									tv_invest.setText(inviteInvestCount);
									
									// 邀请注册的人数
									String inviteRegCount = data.getString("inviteRegCount");
									tv_register.setText(inviteRegCount);
									
									// 昨日邀请赚取
									String yesterdayRewardInviteIncome = data.getString("yesterdayRewardInviteIncome");
									money_today.setText(yesterdayRewardInviteIncome);
											
									// 累计邀请赚取
									String totalRewardInviteIncome = data.getString("totalRewardInviteIncome");
									money_total.setText(totalRewardInviteIncome);
									
									// 分享
									JSONObject jsonObject = data.getJSONObject("share");
									url = jsonObject.getString("url");
									title = jsonObject.getString("title");
									image = jsonObject.getString("image");
									text = jsonObject.getString("text");
									
									// 复制邀请码方法
									linktext(tv_invitecode);
											
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

					}

				});
	}

	private void linktext(TextView textView) {
		SpannableStringBuilder ss = new SpannableStringBuilder(inviteCode);
		ClickableSpan span = new ClickableSpan() {

			@Override
			public void onClick(View widget) {
				ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				cmb.setText(inviteCode);
				ToastUtils.toastshort("邀请码" + inviteCode + "已复制");
			}

			@Override
			public void updateDrawState(TextPaint ds) {
				super.updateDrawState(ds);
				// 去掉下划线
				ds.setUnderlineText(false);
				//设置文字颜色
				ds.setColor(0xffffffff);
			}
		};
		// 参数2：span的开始位置（包含），参数3：span的结束位置 （不包含）
		ss.setSpan(span, 0, inviteCode.length(), 0);
		textView.setText(ss);
		// 让span 可以点击
		textView.setMovementMethod(LinkMovementMethod.getInstance());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reward_back:
			finish();
			break;
		case R.id.iv_share:
			showShare(url, title, image, text);
			break;
		case R.id.tv_check:// 点击详情，跳转到邀请详情页面
			startActivity(new Intent(this, InviteFriendDetailActivity.class));
			break;

		default:
			break;
		}
	}
	
	private void showShare(String url, String title, String image, String text) {	
//		int end = result.indexOf("?");
//		String subUrl = result.substring(0, end);
//		LogUtils.i("拿到的串===="+subUrl);
//		subUrl = subUrl + "?ukey=" + inviteCode + "&type=userInvite";
		
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		//关闭sso授权
		oks.disableSSOWhenAuthorize(); 
		//分享的标题
		oks.setTitle(title);
		
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(url);
				 
		// text是分享文本，所有平台都需要这个字段
		oks.setText(text);
		
		oks.setImageUrl(image);

		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(url);
		
		// 启动分享GUI
		oks.show(this);
	}
}
