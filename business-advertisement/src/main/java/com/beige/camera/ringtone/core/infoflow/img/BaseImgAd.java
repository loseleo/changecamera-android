package com.beige.camera.ringtone.core.infoflow.img;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.beige.camera.common.feed.bean.AdModel;
import com.beige.camera.ringtone.R;
import com.beige.camera.ringtone.core.infoflow.InfoFlowAd;
import com.beige.camera.common.utils.ScreenUtils;

public abstract class BaseImgAd<T extends BaseImgAd.ImgAdRes> extends InfoFlowAd<T> {


    public BaseImgAd(FrameLayout adContainer, AdModel adModel) {
        super(adContainer, adModel);
    }

    @CallSuper
    @Override
    protected void onSetupAdResource(FrameLayout adContainer, T t) {

        Context context = adContainer.getContext();
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return;
        }
        String imageUrl = t.getImgAdImageUrl();
        if (TextUtils.isEmpty(imageUrl)) {
            notifyAdFail(new RuntimeException("img null"));
            return;
        }

        View adView = onCreateAdView(adContainer);
        AdViewHolder adViewHolder = new AdViewHolder();
        adViewHolder.adView = adView;
        adViewHolder.tvAdTitle = findTitleView(adView);
        adViewHolder.ivAdImage = findImgView(adView);
        adViewHolder.ivAdBgImage = findBgImgView(adView);
        adViewHolder.tvAdTitle.setText(t.getImgAdTitle());
        Glide.with(context)
                .asDrawable()
                .load(imageUrl)
                .into(new DrawableImageViewTarget(adViewHolder.ivAdImage) {
                    @Override
                    public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        super.onResourceReady(resource, transition);
                        onImageLoaded(imageUrl,adViewHolder);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        onImgLoadFail(new RuntimeException("load cpc ca ad fail"));
                    }
                });

        onSetupAdResource(adContainer, t, adViewHolder);

        adContainer.removeAllViews();
        adContainer.addView(adView);
        ViewGroup.LayoutParams layoutParams = adView.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin = (int) ScreenUtils.dpToPx(16);
            ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin = (int) ScreenUtils.dpToPx(16);
            adView.setLayoutParams(layoutParams);
        }
    }

    protected abstract void onSetupAdResource(FrameLayout adContainer, T t, AdViewHolder adViewHolder);

    protected void onImgLoadFail(RuntimeException e) {

    }

    protected void onImageLoaded(String imageUrl, AdViewHolder adViewHolder) {
        adViewHolder.ivAdImage.setDrawingCacheEnabled(true);
        Bitmap drawingCache = adViewHolder.ivAdImage.getDrawingCache(true);
        Palette.from(drawingCache).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@NonNull Palette palette) {
                if (palette == null) {
                    //实际上可能为空
                    return;
                }
                Palette.Swatch swatch = palette.getLightVibrantSwatch();
                if (swatch == null) {
                    swatch = palette.getMutedSwatch();
                }
                if (swatch == null) {
                    return;
                }
                int rgb = swatch.getRgb();
                adViewHolder.ivAdBgImage.setBackgroundColor(rgb);
            }
        });
    }

    protected abstract View onCreateAdView(FrameLayout adContainer);

    protected TextView findTitleView(View adView) {
        return adView.findViewById(R.id.tv_title);
    }

    protected ImageView findImgView(View adView) {
        return adView.findViewById(R.id.iv_ad_image);
    }

    protected ImageView findBgImgView(View adView) {
        return adView.findViewById(R.id.iv_ad_image_bg);
    }

    class AdViewHolder {

        View adView;
        TextView tvAdTitle;
        ImageView ivAdImage;
        ImageView ivAdBgImage;
    }


    public interface ImgAdRes {

        String getImgAdTitle();

        String getImgAdImageUrl();
    }
}
