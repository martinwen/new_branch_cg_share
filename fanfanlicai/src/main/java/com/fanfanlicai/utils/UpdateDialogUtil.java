package com.fanfanlicai.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;

/**
 * Created by li on 2016/4/29 17:12. 版本更新所需要的工具类
 */
public class UpdateDialogUtil {
	private Context context;

	private String newVerName;
	private String newApkUrl;
	private String desc;
	private String intcurrent;
	private ProgressBar progressBar_update;
	private TextView version_baifenbi;
	private TextView version_newcode;
	private TextView version_newstr;
	private TextView update;
	private TextView cancel;
	private DownSoft downSoft;
	private boolean forced;

	public UpdateDialogUtil(Context context) {
		this.context = context;
		intcurrent = DeviceUtil.getVersionname();
	}

	private NoUpdateListener noUpdateListener;

	public void setNoUpdateListener(NoUpdateListener noUpdateListener) {
		this.noUpdateListener = noUpdateListener;
	}

	/**
	 * handle 消息处理对象
	 */
	private class MyHandler extends Handler {

		public MyHandler(Context context) {
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				// 更新
				showLoadDialog();
				break;
			case 2:
				// 不更新
				noUpdateListener.noupdate();
				break;

			default:
				break;
			}
		}
	}

	public void goupdate() {
		new Thread(new Runnable() { // 开启线程上传文件

					@Override
					public void run() {
						getServerVerCode();
					}
				}).start();
	}

	private MyHandler handle = new MyHandler(context);

	/**
	 * 获取服务器上apk版本号(其他信息)
	 */
	public void getServerVerCode() {
		Map<String, String> map = SortRequestData.getmap();
		String token = CacheUtils.getString(context, "token", null);
		map.put("token", token);
		String requestData = SortRequestData.sortString(map);
		String signData = SignUtil.sign(requestData);
		map.put("sign", signData);
		VolleyUtil.sendJsonRequestByPost(ConstantUtils.CHECKUPDATE_URL, null, map,
				new HttpBackBaseListener() {

					@Override
					public void onSuccess(String string) {
//						LogUtils.i("检查更新==="+string);
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
									newVerName = data.getString("version");
									if (intcurrent.compareToIgnoreCase(newVerName) < 0) {
										// 需要版本更新
										handle.sendMessage(handle.obtainMessage(1));
									} else {
										handle.sendMessage(handle.obtainMessage(2));
									}
									newApkUrl = data.getString("url");
									desc = data.getString("description");
									forced = data.getBoolean("forced");
								} else {
									handle.sendMessage(handle.obtainMessage(2));
								}
							}
						} else {
							String msg = json.getString("msg");
							ToastUtils.toastshort(msg);
							handle.sendMessage(handle.obtainMessage(2));
						}
					}

					@Override
					public void onError(VolleyError error) {
						handle.sendMessage(handle.obtainMessage(2));
					}
				});
	}
		
	
	private void showLoadDialog() {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_update);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		// 进度条
		progressBar_update = (ProgressBar) dialog
				.findViewById(R.id.progressBar_update);
		// 进度条显示
		version_baifenbi = (TextView) dialog
				.findViewById(R.id.version_baifenbi);
		// 当前版本号
		version_newcode = (TextView) dialog.findViewById(R.id.version_newcode);
		version_newcode.setText("V" + newVerName);
		version_newstr = (TextView) dialog.findViewById(R.id.version_newstr);
		if (desc.contains("|")) {//接口文档说明        | 表示换行
			LogUtils.i("desc=="+desc);
			String replaceDesc = desc.replaceAll("\\|", "\n");
			LogUtils.i("desc========"+desc);
			version_newstr.setText(replaceDesc);
		}else {
			version_newstr.setText(desc);
		}
		update = (TextView) dialog.findViewById(R.id.update);
		cancel = (TextView) dialog.findViewById(R.id.cancel);

		if (forced) {
			cancel.setVisibility(View.GONE);
		}else {
			cancel.setVisibility(View.VISIBLE);
			cancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					if(downSoft!=null){
						downSoft.cancel(true);
					}
					handle.sendMessage(handle.obtainMessage(2));
				}
			});
		}
		
		update.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				// 更新
				int i = progressBar_update.getProgress();
				if (i == 0) {
					if (null == downSoft) {
						downSoft = new DownSoft();
						downSoft.execute();
					}
				} else {
					if (i == 100) {
						
					} else {
						ToastUtils.toastshort("正在下载,请稍后...");
					}
				}
			}
		});
		
		
		dialog.show();

	}

	public class DownSoft extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			if ("".equals(newApkUrl))
				return null;

			URL getUrl;
			try {
				getUrl = new URL(newApkUrl);
				HttpURLConnection connection = (HttpURLConnection) getUrl
						.openConnection();
				// 解决有的服务器获取的文件流的长度为 -1的 关闭gzip 压缩
				connection.setRequestProperty("Accept-Encoding", "identity");
				connection.connect();
				long length = connection.getContentLength();
				InputStream is = connection.getInputStream();
				FileOutputStream fileOutputStream = null;
				if (is != null) {
					File file = new File(
							Environment.getExternalStorageDirectory(),
							"fanfanjf.apk");
					if (file.exists()) {
						file.deleteOnExit();
					}
					fileOutputStream = new FileOutputStream(file);

					byte[] buf = new byte[1024];
					int ch = -1;
					int count = 0;
					while ((ch = is.read(buf)) != -1) {
						fileOutputStream.write(buf, 0, ch);
						count += ch;
						if (length > 0) {
							int d = (int) (((long)count * 100) / (long)length);
							publishProgress(d);
						}
					}
				}
				fileOutputStream.flush();
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Void result) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.fromFile(new File(Environment
					.getExternalStorageDirectory(), "fanfanjf.apk")),
					"application/vnd.android.package-archive");
			context.startActivity(intent);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			progressBar_update.setProgress(values[0]);
			version_baifenbi.setText(values[0] + "%");
			
		}
	}

	public interface NoUpdateListener {
		void noupdate();
	}
}
