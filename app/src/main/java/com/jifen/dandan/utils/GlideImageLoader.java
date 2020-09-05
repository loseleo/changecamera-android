package com.jifen.dandan.utils;

import android.content.Context;
import android.widget.ImageView;

import com.jifen.dandan.common.utils.imageloader.BitmapUtil;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
//        Glide.with(context).load(path, context).into(imageView);
        BitmapUtil.loadImageRound(context, (String) path, imageView, 0, 0, 10);

//        Glide.with(context)
//                .load(path)
//                .into(imageView);
    }
}
