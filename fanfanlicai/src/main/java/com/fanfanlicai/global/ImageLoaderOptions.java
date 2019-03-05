package com.fanfanlicai.global;

import android.graphics.Bitmap;

import com.fanfanlicai.fanfanlicai.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public interface ImageLoaderOptions {
	// 圆角的配置项
	DisplayImageOptions round_options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_default_banner)// 加载过程中显示哪张图片
			.showImageForEmptyUri(R.drawable.ic_default_banner)// url为空显示哪张图片
			.showImageOnFail(R.drawable.ic_default_banner)// 加载失败显示哪张图片
			.cacheInMemory(true)// 是否在内存缓存
			.cacheOnDisk(true)// 是否在磁盘缓存
			.imageScaleType(ImageScaleType.EXACTLY)//会按照ImageView的宽高进行进一步的缩放
			.bitmapConfig(Bitmap.Config.RGB_565)//颜色渲染模式，565是节省内存的模式
			.considerExifParams(true)// 会识别图片的方向信息
			// .displayer(new FadeInBitmapDisplayer(500)).build();//渐渐显示
			.displayer(new RoundedBitmapDisplayer(28)).build();// 圆角的显示效果

	// 渐渐显示的配置项
	DisplayImageOptions fadein_options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_default_banner)// 加载过程中显示哪张图片
			.showImageForEmptyUri(R.drawable.ic_default_banner)// url为空显示哪张图片
			.showImageOnFail(R.drawable.ic_default_banner)// 加载失败显示哪张图片
			.cacheInMemory(true)// 是否在内存缓存
			.cacheOnDisk(true)// 是否在磁盘缓存
			.imageScaleType(ImageScaleType.EXACTLY)//会按照ImageView的宽高进行进一步的缩放
			.bitmapConfig(Bitmap.Config.RGB_565)//颜色渲染模式，565是节省内存的模式
			.considerExifParams(true)// 会识别图片的方向信息
			.displayer(new FadeInBitmapDisplayer(500)).build();// 渐渐显示
	// .displayer(new RoundedBitmapDisplayer(28)).build();//圆角的显示效果
}
