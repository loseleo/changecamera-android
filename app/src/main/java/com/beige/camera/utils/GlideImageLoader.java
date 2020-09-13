package com.beige.camera.utils;

import android.content.Context;
import android.widget.ImageView;

import com.beige.camera.bean.FunctionBean;
import com.beige.camera.common.utils.imageloader.BitmapUtil;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object bannerBean, ImageView imageView) {
        imageView.setPadding(20,0,20,0);
        BitmapUtil.loadImageRound(context, ((FunctionBean) bannerBean).getDrawable(), imageView, 10);
    }
}
