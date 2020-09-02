package com.jifen.dandan.ringtone.core.infoflow.img;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.jifen.dandan.common.feed.bean.AdModel;
import com.jifen.dandan.ringtone.R;
import com.jifen.dandan.ringtone.core.infoflow.loader.GDTInfoFlowVideoLoader;
import com.jifen.dandan.ringtone.core.infoflow.loader.res.GDTInfoFlowVideo;
import com.jifen.dandan.ringtone.core.loader.ResourceLoader;
import com.qq.e.ads.nativ.NativeADEventListener;
import com.qq.e.ads.nativ.NativeUnifiedADData;
import com.qq.e.ads.nativ.widget.NativeAdContainer;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.util.AdError;

import java.util.ArrayList;
import java.util.List;

public class GDTInfoFlowImgAd extends BaseImgAd<GDTInfoFlowVideo> {


    public GDTInfoFlowImgAd(FrameLayout adContainer, AdModel adModel) {
        super(adContainer, adModel);
    }

    @Override
    protected void onSetupAdResource(FrameLayout adContainer, GDTInfoFlowVideo gdtAdResource) {
        NativeUnifiedADData ad = gdtAdResource.getNativeUnifiedADData();
        int adPatternType = ad.getAdPatternType();
        if (adPatternType != AdPatternType.NATIVE_1IMAGE_2TEXT
                && adPatternType != AdPatternType.NATIVE_2IMAGE_2TEXT
                && adPatternType != AdPatternType.NATIVE_3IMAGE) {
            notifyAdFail(new RuntimeException("not support this type:" + adPatternType));
            return;
        }
        super.onSetupAdResource(adContainer, gdtAdResource);
    }

    @Override
    protected void onSetupAdResource(FrameLayout adContainer, GDTInfoFlowVideo gdtInfoFlowVideo, AdViewHolder adViewHolder) {
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(adViewHolder.ivAdImage);
        clickableViews.add(adViewHolder.tvAdTitle);
        NativeUnifiedADData ad = gdtInfoFlowVideo.getNativeUnifiedADData();
        // 将布局与广告进行绑定
        ad.bindAdToView(adContainer.getContext(), (NativeAdContainer) adViewHolder.adView, null, clickableViews);
        // 设置广告事件监听
        ad.setNativeAdEventListener(new NativeADEventListener() {
            @Override
            public void onADExposed() {
                notifyAdDisplay();
            }

            @Override
            public void onADClicked() {
                notifyAdClick();
            }

            @Override
            public void onADError(AdError error) {
                String errorMsg = "错误回调 error code :" + error.getErrorCode()
                        + "  error msg: " + error.getErrorMsg();
                notifyAdFail(new RuntimeException(errorMsg));
            }

            @Override
            public void onADStatusChanged() {

            }
        });
    }

    @Override
    protected View onCreateAdView(FrameLayout adContainer) {
        return LayoutInflater.from(adContainer.getContext()).inflate(R.layout.ad_feed_img_gdt, adContainer, false);
    }


    @NonNull
    @Override
    protected ResourceLoader<GDTInfoFlowVideo> onCreateResourceLoader(AdModel adModel) {
        return new GDTInfoFlowVideoLoader(adModel, getAdContainer().getContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        GDTInfoFlowVideo adResource = getAdResource();
        if (adResource != null) {
            adResource.getNativeUnifiedADData().resume();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        GDTInfoFlowVideo adResource = getAdResource();
        if (adResource != null) {
            adResource.getNativeUnifiedADData().destroy();
        }
    }
}
