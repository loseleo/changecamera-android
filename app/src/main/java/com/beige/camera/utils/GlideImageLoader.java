package com.beige.camera.utils;

import android.content.Context;
import android.widget.ImageView;

import com.beige.camera.R;
import com.beige.camera.bean.FunctionBean;
import com.beige.camera.common.feed.bean.AdModel;
import com.beige.camera.common.utils.imageloader.BitmapUtil;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object bannerBean, ImageView imageView) {
        imageView.setPadding(20,0,20,0);
        BitmapUtil.loadImageRound(context, ((AdModel) bannerBean).getImageUrl(), imageView, R.drawable.bg_perview_white,R.drawable.bg_perview_white,8);
    }
}
