package com.beige.camera.advertisement.core.splash;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.beige.camera.common.feed.bean.AdModel;
import com.beige.camera.advertisement.R;
import com.beige.camera.advertisement.core.loader.ResourceLoader;
import com.beige.camera.common.router.AppNavigator;

public class CPCCaSplashAd extends CountDownSplashAd<AdModel> {


    public CPCCaSplashAd(AdModel adModel, ViewGroup adContainer) {
        super(adModel, adContainer);
    }

    @Override
    protected void onSetupContentView(FrameLayout flAdContainer, AdModel adModel) {

        Context context = flAdContainer.getContext();
        View bodyView = LayoutInflater.from(context).inflate(R.layout.ad_splash_body_cpcca, flAdContainer, false);
        ImageView ivAdImage = bodyView.findViewById(R.id.iv_ad_image);
        ivAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyAdClick();
                AppNavigator.goWebViewActivity(context, adModel.getLocation());
            }
        });

        Glide.with(bodyView).asDrawable().load(adModel.getImageUrl())
                .apply(new RequestOptions().transform(new FitWidth()))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        notifyAdFail(e == null ? new RuntimeException("load splash ad image fail") : e);
                        notifyAdTimeOver();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        notifyAdDisplay();
                        return false;
                    }
                }).into(ivAdImage);
        flAdContainer.removeAllViews();
        flAdContainer.addView(bodyView);
    }

    @Override
    protected int getCountDown() {
        return getAdModel().getCountDown();
    }

    @NonNull
    @Override
    protected ResourceLoader<AdModel> onCreateResourceLoader(AdModel adModel) {
        return new SplashAdResourceLoader(adModel);
    }

    public static class SplashAdResourceLoader extends ResourceLoader<AdModel> {

        SplashAdResourceLoader(AdModel adModel) {
            super(adModel);
        }

        @Override
        protected void onLoad(AdModel adModel) {
            String imageUrl = adModel.getImageUrl();
            if (TextUtils.isEmpty(imageUrl)) {
                notifyFail(new RuntimeException("image url null"));
                return;
            }
            notifySuccess(adModel);
        }
    }

    private static class FitWidth extends FitCenter {

        @Override
        protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
            int bWidth = toTransform.getWidth();
            int bHeight = toTransform.getHeight();
            float bFactor = (float) bWidth / bHeight;
            float factor = (float) outWidth / outHeight;
            if (bFactor < factor) {
                return TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
            } else {
                return TransformationUtils.fitCenter(pool, toTransform, outWidth, outHeight);
            }
        }
    }
}
