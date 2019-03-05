package com.fanfanlicai.activity.my;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.BitmapUtil;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.StringUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.UploadUtil;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;
import com.fanfanlicai.view.dialog.CustomProgressDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.ref.WeakReference;
import java.util.Map;


public class CGBankChangeForPhotoActivity extends BaseActivity implements OnClickListener {

	private CustomProgressDialog progressdialog;
	private Button bt_btn;
	private ImageView iv_left;
	private ImageView iv_right;
	private String whichOne;
	private String cardNum;
	private String cityId;
	private String bankNo;
	private String file1;
	private String file2;
	private Uri uri;
	private String filePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cgbankchangeforphoto);

		progressdialog=new CustomProgressDialog(this, "上传照片中...");
		initView();
		initData();
	}

	private void initData() {
		cardNum = getIntent().getStringExtra("cardNum");
		cityId = getIntent().getStringExtra("cityId");
		bankNo = getIntent().getStringExtra("bankNo");
	}

	private void initView() {
		TextView get_back = (TextView) findViewById(R.id.get_back);
		get_back.setOnClickListener(this);
		bt_btn = (Button) findViewById(R.id.bt_btn);
		bt_btn.setOnClickListener(this);
		iv_left = (ImageView) findViewById(R.id.iv_left);
		iv_left.setOnClickListener(this);
		iv_right = (ImageView) findViewById(R.id.iv_right);
		iv_right.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		 switch (v.getId()) {
		 	 case R.id.get_back:
				finish();
				break;
			 case R.id.iv_left:
				 whichOne = "left";
				 onClick_Img();
				 break;
			 case R.id.iv_right:
				 if(TextUtils.isEmpty(file1)){
					 ToastUtils.toastshort("左侧图片未上传完，请稍候");
				 }else{
					 whichOne = "right";
					 onClick_Img();
				 }

				 break;
			 case R.id.bt_btn:
				 if(TextUtils.isEmpty(file1)){
					 ToastUtils.toastshort("左侧图片未上传完，请稍候");
				 }else if(TextUtils.isEmpty(file2)){
					 ToastUtils.toastshort("右侧图片未上传完，请稍候");
				 }else{
					 LogUtils.i("左侧图片=="+file1+"  右侧图片=="+file2);
					 change();
				 }

				 break;
		 default:
		 break;
		 }
	}

	private void change() {
		progressdialog.show();
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(this, "token","");
		map.put("token", token);
		map.put("cardNum", cardNum);
		map.put("file1", file1);
		map.put("file2", file2);
		map.put("bankNo", bankNo);
		map.put("cityId", cityId);
		LogUtils.i("cardNum="+cardNum+"  file1="+file1+"  file2="+file2+"  bankNo="+bankNo+"  cityId="+cityId);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		progressdialog.show();
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.APPLYCHANGECARD_URL, null,
				map, new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
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
									String oldBankName = data.getString("oldBankName");
									String oldCardNum = data.getString("oldCardNum");
									String newBankName = data.getString("newBankName");
									String newCardNum = data.getString("newCardNum");
									Intent intent = new Intent(CGBankChangeForPhotoActivity.this,CGBankChangeSuccessActivity.class);
									intent.putExtra("oldBankName",oldBankName);
									intent.putExtra("oldCardNum",oldCardNum);
									intent.putExtra("newBankName",newBankName);
									intent.putExtra("newCardNum",newCardNum);
									startActivity(intent);
								} else {
									ToastUtils.toastshort("数据错误！");
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
						ToastUtils.toastshort("网络异常！");
					}
				});
	}

	/**
	 * 弹出用户选择头像提示框
	 */
	void onClick_Img() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.view_dialog_capture);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCanceledOnTouchOutside(true);

		LinearLayout menu_capture_r1 = (LinearLayout) dialog
				.findViewById(R.id.menu_capture_r1);
		LinearLayout menu_capture_r2 = (LinearLayout) dialog
				.findViewById(R.id.menu_capture_r2);
		Button dialog_contorl_b1 = (Button) dialog
				.findViewById(R.id.dialog_contorl_b1);

		menu_capture_r1.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				dialog.dismiss();
				onClick_Capture();
			}
		});
		menu_capture_r2.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				dialog.dismiss();
				onClick_Pick();
			}
		});
		dialog_contorl_b1.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * 点击选择本地文件
	 */
	void onClick_Pick() {
//		PhotoPickActivity.PhotoCrop crop = new PhotoPickActivity.PhotoCrop(3, 4, 600, 800);
//		Bundle mBundle = new Bundle();
//		mBundle.putSerializable(PhotoPickActivity.PHOTO_CROPKEY, crop);

		Intent intent = new Intent(this,
				PhotoPickActivity.class);
		intent.putExtra(PhotoPickActivity.PHOTO_TYPEKEY,
				PhotoPickActivity.PHOTO_REQ_ALBUM);
		intent.putExtra(PhotoPickActivity.PHOTO_STORAGEKEY, Environment
				.getExternalStorageDirectory().toString());
//		intent.putExtras(mBundle);
		startActivityForResult(intent, PhotoPickActivity.PHOTO_REQ_ALBUM);
	}

	/**
	 * 点击拍照
	 */
	void onClick_Capture() {
//		PhotoPickActivity.PhotoCrop crop = new PhotoPickActivity.PhotoCrop(3, 4, 600, 800);
//		Bundle mBundle = new Bundle();
//		mBundle.putSerializable(PhotoPickActivity.PHOTO_CROPKEY, crop);

		Intent intent = new Intent(this,
				PhotoPickActivity.class);
		intent.putExtra(PhotoPickActivity.PHOTO_TYPEKEY,
				PhotoPickActivity.PHOTO_REQ_CAPTURE);
		intent.putExtra(PhotoPickActivity.PHOTO_STORAGEKEY, Environment
				.getExternalStorageDirectory().toString());
//		intent.putExtras(mBundle);
		startActivityForResult(intent, PhotoPickActivity.PHOTO_REQ_CAPTURE);
	}

	Bitmap bitmap = null;
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PhotoPickActivity.PHOTO_REQ_ALBUM
				|| requestCode == PhotoPickActivity.PHOTO_REQ_CAPTURE) {

			if (resultCode == PhotoPickActivity.PHOTO_BACK_URI) {
				uri = (Uri) data.getExtras().get("data");
				//把这个没有经过压缩的bitmap传到后台
				bitmap = BitmapUtil.getRotateBitmap(this,uri);

				//拿到返回的uri的物理路径，展示到界面的图片需要压缩，这个参数在压缩bitmap方法中用到
				filePath = BitmapUtil.getRealFilePath(this, uri);
			}

			if(null!=bitmap){

				UploadUtil uploadUtil = UploadUtil.getInstance();
				// 设置监听器监听上传状态
				uploadUtil
						.setOnUploadProcessListener(new UploadUtil.OnUploadProcessListener() {

							@Override
							public void onUploadProcess(int uploadSize) {

							}

							@Override
							public void onUploadDone(int responseCode,
													 String message) {
								LogUtils.i("onUploadDone==="+message);
								Message msg = Message.obtain();
								msg.what = 10;
								msg.arg1 = responseCode;
								msg.obj = message;
								handler.sendMessage(msg);
							}

							@Override
							public void initUpload(int fileSize) {
							}
						});
				progressdialog.show();
				Map<String, String> map = SortRequestData.getmap();
				String token = CacheUtils.getString(this, "token", null);
				map.put("token", token);
				String requestData = SortRequestData.sortString(map);
				String signData = SignUtil.sign(requestData);
				map.put("sign", signData);
				uploadUtil.uploadFile(bitmap, "myfiles",
						ConstantUtils.IMAGEFILEUPLOAD_URL, map);

			}
		}
	}

	private MyHandler handler = new MyHandler(this);

	/**
	 * handle 消息处理对象 使用弱引用持有外部activity的引用 防止内存泄露的情况产生
	 */
	private class MyHandler extends Handler {
		private WeakReference<Context> reference;

		public MyHandler(Context context) {
			reference = new WeakReference<>(context);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 10:

					if (1 == msg.arg1) {
						progressdialog.dismiss();
						JSONObject json = JSON.parseObject((String) msg.obj);
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
									String result = data.getString("result");

									if("left".equals(whichOne)){
										//展示的图需要经过压缩，不然不会显示
										iv_left.setImageBitmap(resizePhoto());
										file1 = result;
										//此处bitmap置为null是要解决点击右边图片但没拍照或选择照片时重走onActivityResult方法
										//这时会把左边的图片再赋给右边
										bitmap = null;
									}else if("right".equals(whichOne)){
										//展示的图需要经过压缩，不然不会显示
										iv_right.setImageBitmap(resizePhoto());
										file2 = result;
									}
								} else {
									ToastUtils.toastshort("头像上传失败！");
								}
							}
						}else {
							String message = json.getString("msg");
							ToastUtils.toastshort(message);
						}
					} else {
						ToastUtils.toastshort("头像上传失败！");
					}
					break;
				default:
					break;
			}
			super.handleMessage(msg);
		}

	}

	//为了防止图片过大，对图片进行压缩
	private Bitmap resizePhoto(){
		//options里面储存了图片的高度和宽度
		BitmapFactory.Options options = new BitmapFactory.Options();
		//不会加载，只会获取图片的一个尺寸
		options.inJustDecodeBounds = true;

		//读取文件
		BitmapFactory.decodeFile(filePath ,options);
		//改变图片的大小
		double ratio = Math.max(options.outWidth *1.0d/1024f,options.outHeight *1.0d/1024);
		LogUtils.i("ratio=="+ratio);
		options.inSampleSize =(int) Math.ceil(ratio);
		LogUtils.i("inSampleSize=="+(int) Math.ceil(ratio));
		//设置后会加载图片
		options.inJustDecodeBounds = false;
		//图片压缩完成
		return  BitmapFactory.decodeFile(filePath ,options);
	}
}
