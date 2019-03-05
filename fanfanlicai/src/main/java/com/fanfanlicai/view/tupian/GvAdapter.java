package com.fanfanlicai.view.tupian;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.fanfanlicai.bean.ImageInfo;
import com.fanfanlicai.fanfanlicai.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;


/**
 * Created by xiaoke on 2016/6/28.
 */
public class GvAdapter extends BaseAdapter {
    private Context context;
    private List<ImageInfo> list;
    // 图片缓存 默认 等
    private DisplayImageOptions optionsImag = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.ic_default_banner)
            .showImageOnFail(R.drawable.ic_default_banner).cacheInMemory(true).cacheOnDisk(true)
            .considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .displayer(new RoundedBitmapDisplayer(28)).build();

    public GvAdapter(Context context, List<ImageInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
        convertView = View.inflate(context, R.layout.item_image_view, null);
        ImageView iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
        ImageLoader.getInstance().displayImage(list.get(position).getUrl(), iv_image, optionsImag);
        return convertView;
    }
}
