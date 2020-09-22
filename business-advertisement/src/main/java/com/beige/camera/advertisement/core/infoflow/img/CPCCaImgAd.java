package com.beige.camera.advertisement.core.infoflow.img;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.beige.camera.common.feed.bean.AdModel;
import com.beige.camera.advertisement.R;
import com.beige.camera.advertisement.core.loader.ResourceLoader;
import com.beige.camera.common.router.AppNavigator;


public class CPCCaImgAd extends BaseImgAd<CPCCaImgAd.CPCCaRes> {


    public CPCCaImgAd(FrameLayout adContainer, AdModel adModel) {
        super(adContainer, adModel);
    }


    @NonNull
    @Override
    protected ResourceLoader<CPCCaRes> onCreateResourceLoader(AdModel adModel) {
        return new ResourceLoader<CPCCaRes>(adModel) {
            @Override
            protected void onLoad(AdModel adModel) {
                String imageUrl = adModel.getImageUrl();
                String title = adModel.getTitle();
                if (TextUtils.isEmpty(imageUrl) || TextUtils.isEmpty(title)) {
                    notifyFail(new RuntimeException("参数不合法"));
                    return;
                }
                notifySuccess(new CPCCaRes(adModel));
            }
        };
    }


    @Override
    protected void onSetupAdResource(FrameLayout adContainer, CPCCaRes cpcCaRes, AdViewHolder adViewHolder) {
        adViewHolder.adView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppNavigator.goWebViewActivity(v.getContext(), getAdModel().getLocation());
                notifyAdClick();
            }
        });
    }

    @Override
    protected void onImgLoadFail(RuntimeException e) {
        super.onImgLoadFail(e);
        notifyAdFail(e);
    }

    @Override
    protected void onImageLoaded(String imageUrl, AdViewHolder adViewHolder) {
        super.onImageLoaded(imageUrl, adViewHolder);
        notifyAdDisplay();
    }

    @Override
    protected View onCreateAdView(FrameLayout adContainer) {
        return LayoutInflater.from(adContainer.getContext()).inflate(R.layout.ad_feed_img_cpc_ca,
                adContainer,
                false);
    }



    public static class CPCCaRes implements BaseImgAd.ImgAdRes {

        private AdModel adModel;

        CPCCaRes(AdModel adModel) {
            this.adModel = adModel;
        }

        @Override
        public String getImgAdTitle() {
            return adModel.getTitle();
        }

        @Override
        public String getImgAdImageUrl() {
            return adModel.getImageUrl();
        }
    }
}
