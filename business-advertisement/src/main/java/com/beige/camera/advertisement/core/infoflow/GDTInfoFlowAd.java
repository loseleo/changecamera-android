package com.beige.camera.advertisement.core.infoflow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.FrameLayout;

import com.beige.camera.advertisement.core.loader.ResourceLoader;
import com.beige.camera.common.feed.bean.AdModel;
import com.beige.camera.common.utils.LogUtils;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.ads.nativ.NativeExpressMediaListener;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.util.AdError;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GDTInfoFlowAd extends InfoFlowAd<GDTInfoFlowAd.GDTInfoFlowAdResource> {


    public GDTInfoFlowAd(FrameLayout adContainer, AdModel adModel) {
        super(adContainer, adModel);
    }

    @NonNull
    @Override
    protected ResourceLoader<GDTInfoFlowAd.GDTInfoFlowAdResource> onCreateResourceLoader(AdModel adModel) {
        return new GDTInfoFlowAd.GDTInfoFlowAdResourceLoader(adModel, getAdContainer().getContext());
    }

    @Override
    protected void onSetupAdResource(FrameLayout adContainer, GDTInfoFlowAd.GDTInfoFlowAdResource gdtInfoFlowAdResource) {

        NativeExpressADView nativeExpressADView = gdtInfoFlowAdResource.getGDTInfoFlowAdAd();

        if (nativeExpressADView.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
            nativeExpressADView.setMediaListener(new NativeExpressMediaListener() {
                @Override
                public void onVideoCached(NativeExpressADView nativeExpressADView) {

                }

                @Override
                public void onVideoComplete(NativeExpressADView nativeExpressADView) {

                }

                @Override
                public void onVideoError(NativeExpressADView nativeExpressADView, AdError adError) {

                }

                @Override
                public void onVideoInit(NativeExpressADView nativeExpressADView) {

                }

                @Override
                public void onVideoLoading(NativeExpressADView nativeExpressADView) {

                }

                @Override
                public void onVideoPageClose(NativeExpressADView nativeExpressADView) {

                }

                @Override
                public void onVideoPageOpen(NativeExpressADView nativeExpressADView) {

                }

                @Override
                public void onVideoPause(NativeExpressADView nativeExpressADView) {

                }

                @Override
                public void onVideoReady(NativeExpressADView nativeExpressADView, long l) {

                }

                @Override
                public void onVideoStart(NativeExpressADView nativeExpressADView) {

                }
            });
        }
        nativeExpressADView.render();
        if (adContainer.getChildCount() > 0) {
            adContainer.removeAllViews();
        }
        // 需要保证 View 被绘制的时候是可见的，否则将无法产生曝光和收益。
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        nativeExpressADView.setLayoutParams(layoutParams);
        adContainer.addView(nativeExpressADView);

    }


    static class GDTInfoFlowAdResource {

        private NativeExpressADView nativeExpressADView;

        GDTInfoFlowAdResource(NativeExpressADView nativeExpressADView) {
            this.nativeExpressADView = nativeExpressADView;
        }

        NativeExpressADView getGDTInfoFlowAdAd() {
            return nativeExpressADView;
        }
    }

    private class GDTInfoFlowAdResourceLoader extends ResourceLoader<GDTInfoFlowAdResource> {

        private Context mContext;

        GDTInfoFlowAdResourceLoader(AdModel adModel, Context mContext) {
            super(adModel);
            this.mContext = mContext;
        }

        @Override
        protected void onLoad(AdModel adModel) {


            Map<String, String> data = new HashMap<>();
            data.put("adCode", adModel.getAdCode());
            data.put("adId", adModel.getAdId());
            data.put("adChannel", adModel.getAdChannel());

            NativeExpressAD nativeExpressAD = new NativeExpressAD(mContext, new ADSize(340, ADSize.AUTO_HEIGHT), adModel.getAdId(), new NativeExpressAD.NativeExpressADListener() {
                @Override
                public void onADClicked(NativeExpressADView nativeExpressADView) {
                    LogUtils.e("TTInfoFlowAd", "onAdClicked");
                    notifyAdClick();
                }

                @Override
                public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {

                }

                @Override
                public void onADClosed(NativeExpressADView nativeExpressADView) {

                }

                @Override
                public void onADExposure(NativeExpressADView nativeExpressADView) {
                    LogUtils.e("FullVideoAd", "onAdShow");
                    notifyAdDisplay();
                }

                @Override
                public void onADLeftApplication(NativeExpressADView nativeExpressADView) {

                }

                @Override
                public void onADLoaded(List<NativeExpressADView> adList) {
                    NativeExpressADView nativeExpressADView = adList.get(0);
                    notifySuccess(new GDTInfoFlowAdResource(nativeExpressADView));
                }

                @Override
                public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {

                }

                @Override
                public void onRenderFail(NativeExpressADView nativeExpressADView) {
                    LogUtils.e("TTInfoFlowAd onRenderFail");
                    notifyFail(new RuntimeException("load gdt gdtInfoFlowAd onRenderFail"));
                }

                @Override
                public void onRenderSuccess(NativeExpressADView nativeExpressADView) {

                    LogUtils.e("TTInfoFlowAd", "onNativeExpressAdLoad");
                }

                @Override
                public void onNoAD(AdError adError) {
                    String msg = String.format(Locale.getDefault(), "onNoAD, error code: %d, error msg: %s",
                            adError.getErrorCode(), adError.getErrorMsg());
                    LogUtils.e("GDTInfoFlowAd ", "onNoAD " + msg);
                    notifyFail(new RuntimeException(msg));
                }
            });

            // 注意：如果您在平台上新建原生模板广告位时，选择了支持视频，那么可以进行个性化设置（可选）
            nativeExpressAD.setVideoOption(new VideoOption.Builder()
                    .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI) // WIFI 环境下可以自动播放视频
                    .setAutoPlayMuted(true) // 自动播放时为静音
                    .build()); //

            nativeExpressAD.setVideoPlayPolicy(VideoOption.VideoPlayPolicy.AUTO); // 本次拉回的视频广告，从用户的角度看是自动播放的
            nativeExpressAD.loadAD(1);
        }
    }

}
