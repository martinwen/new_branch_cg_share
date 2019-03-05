
package com.fanfanlicai.activity.invest;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.bean.ImageInfo;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.view.tupian.GvAdapter;
import com.fanfanlicai.view.tupian.PicShowDialog;

import java.util.ArrayList;
import java.util.List;

public class TuPianActivity extends BaseActivity implements OnClickListener {

	private TextView get_back;// 返回
	private GridView gv_pic;
	private List<ImageInfo> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tupian);

		initView();
		initData();
	}

	private void initView() {
		get_back = (TextView) findViewById(R.id.get_back);// 返回
		get_back.setOnClickListener(this);
		gv_pic = (GridView) findViewById(R.id.gv_pic);
	}

	private void initData() {
		String picuUrl = getIntent().getStringExtra("picuUrl");
		LogUtils.i("picuUrl=="+picuUrl);
		String[] urls = picuUrl.split(",");
		list=new ArrayList<>();
		for (int i=0;i<urls.length;i++){
			ImageInfo imageInfo=new ImageInfo(urls[i],200,200);
			list.add(imageInfo);
		}
		gv_pic.setSelector(android.R.color.transparent);
		gv_pic.setAdapter(new GvAdapter(this,list));
		gv_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				PicShowDialog dialog=new PicShowDialog(TuPianActivity.this,list,position);
				dialog.show();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_back:
			finish();
			break;
		}
	}
}
