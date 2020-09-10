package com.beige.camera.utils;

import android.content.Context;
import android.widget.ImageView;

import com.beige.camera.common.utils.imageloader.BitmapUtil;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {

        imageView.setPadding(20,0,20,0);
        BitmapUtil.loadImageRound(context, (String) path, imageView, 0, 0, 10);
    }
}
