package com.fanfanlicai.view.banner;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.fanfanlicai.fanfanlicai.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by Sai on 15/8/4.
 * 网络图片加载例子
 */
public class NetworkImageHolderView implements Holder<String> {
	private ImageView imageView;
	private DisplayImageOptions options;
   /* private ImageView imageView;
    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context,int position, String data) {
        imageView.setImageResource(R.drawable.ic_default_adimage);
        ImageLoader.getInstance().displayImage(data,imageView);
    }*/
	 @Override
	    public View createView(Context context) {
	        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
	        imageView = new ImageView(context);
	        imageView.setImageResource(R.drawable.ic_default_banner);
	        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
	        options = new DisplayImageOptions.Builder()
	                //.showImageOnLoading(R.drawable.loading_bank)         // 设置图片下载期间显示的图片
	                .showStubImage(R.drawable.ic_default_banner)
	                .showImageForEmptyUri(R.drawable.ic_default_banner) // 设置图片Uri为空或是错误的时候显示的图片
	                .showImageOnFail(R.drawable.ic_default_banner) // 设置图片加载或解码过程中发生错误显示的图片
	                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)//图片缩放类型 ImageView.ScaleType.FIT_CENTER
	                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
	                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
	                .build();
	        return imageView;
	    }

	    @Override
	    public void UpdateUI(Context context,int position, String data) {

	        ImageLoader.getInstance().displayImage(data,imageView,options);
	    }
}
