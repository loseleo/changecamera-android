package com.beige.camera.advertisement.core.fullscreenvideo;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.CompoundButton;

import com.beige.camera.advertisement.core.loader.ResourceLoader;
import com.beige.camera.advertisement.core.rewardvideo.RewardVideoAd;
import com.beige.camera.common.feed.bean.AdModel;
import com.beige.camera.common.utils.LogUtils;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.ads.interstitial2.UnifiedInterstitialMediaListener;
import com.qq.e.ads.rewardvideo.RewardVideoAD;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.util.AdError;

import java.util.Locale;

public class GDTFullScreenVideoAd extends RewardVideoAd<GDTFullScreenVideoAd.GDTFullScreenVideoAdResource> {

    public GDTFullScreenVideoAd(AdModel adModel, Activity activity) {
        super(activity, adModel);
    }

    @NonNull
    @Override
    protected ResourceLoader<GDTFullScreenVideoAdResource> onCreateResourceLoader(AdModel adModel) {
        return new GDTFullScreenVideoResourceLoader(adModel, this);
    }

    @Override
    protected void onSetupAdResource(Activity activity, GDTFullScreenVideoAdResource gdtRewardVideoAdResource) {
        gdtRewardVideoAdResource.getRewardVideoAD().show();
    }


    public static class GDTFullScreenVideoAdResource {

        private UnifiedInterstitialAD unifiedInterstitialAD;

        GDTFullScreenVideoAdResource(UnifiedInterstitialAD unifiedInterstitialAD) {
            this.unifiedInterstitialAD = unifiedInterstitialAD;
        }

        UnifiedInterstitialAD getRewardVideoAD() {
            return unifiedInterstitialAD;
        }
    }

    private static class GDTFullScreenVideoResourceLoader extends ResourceLoader<GDTFullScreenVideoAdResource> {

        private GDTFullScreenVideoAd gdtRewardVideoAd;
        private UnifiedInterstitialAD unifiedInterstitialAD;

        GDTFullScreenVideoResourceLoader(AdModel adModel, GDTFullScreenVideoAd gdtRewardVideoAd) {
            super(adModel);
            this.gdtRewardVideoAd = gdtRewardVideoAd;
        }

        @Override
        protected void onLoad(AdModel adModel) {
            unifiedInterstitialAD = new UnifiedInterstitialAD(gdtRewardVideoAd.getActivity(), adModel.getAdId(), new UnifiedInterstitialADListener() {
                @Override
                public void onADReceive() {
                    LogUtils.e("GDTFullScreenVideoAd ", "onADReceive");
                    notifySuccess(new GDTFullScreenVideoAdResource(unifiedInterstitialAD));
                    // onADReceive之后才能调用getAdPatternType()
                    if(unifiedInterstitialAD.getAdPatternType() == AdPatternType.NATIVE_VIDEO){
                        unifiedInterstitialAD.setMediaListener(new UnifiedInterstitialMediaListener() {
                            @Override
                            public void onVideoComplete() {

                            }

                            @Override
                            public void onVideoError(AdError adError) {

                            }

                            @Override
                            public void onVideoInit() {

                            }

                            @Override
                            public void onVideoLoading() {

                            }

                            @Override
                            public void onVideoPageClose() {

                            }

                            @Override
                            public void onVideoPageOpen() {

                            }

                            @Override
                            public void onVideoPause() {

                            }

                            @Override
                            public void onVideoReady(long l) {

                            }

                            @Override
                            public void onVideoStart() {

                            }
                        });
                    }
                }

                @Override
                public void onNoAD(AdError error) {

                    String msg = String.format(Locale.getDefault(), "onNoAD, error code: %d, error msg: %s",
                            error.getErrorCode(), error.getErrorMsg());
                    LogUtils.e("GDTFullScreenVideoAd ", "onNoAD " + msg);
                    notifyFail(new RuntimeException(msg));
                }

                @Override
                public void onVideoCached() {

                }

                @Override
                public void onADOpened() {
                    LogUtils.e("GDTFullScreenVideoAd ", "onADOpened ");
                }

                @Override
                public void onADExposure() {
                    gdtRewardVideoAd.notifyAdDisplay();
                    LogUtils.e("GDTFullScreenVideoAd ", "onADExposure ");
                }

                @Override
                public void onADClicked() {
                    LogUtils.e("GDTFullScreenVideoAd ", "onADClicked ");
                    gdtRewardVideoAd.notifyAdClick();
                }

                @Override
                public void onADLeftApplication() {
                    LogUtils.e("GDTFullScreenVideoAd ", "onADLeftApplication ");
                }

                @Override
                public void onADClosed() {
                    LogUtils.e("GDTFullScreenVideoAd ", "onADClosed");
                    gdtRewardVideoAd.getDismissedHelper().notifyAdDismiss();
                }

            });
//            VideoOption.Builder builder = new VideoOption.Builder();
//            VideoOption option = builder.build();
//            if (!btnNoOption.isChecked()) {
//                option = builder.setAutoPlayMuted(btnMute.isChecked())
//                        .setAutoPlayPolicy(networkSpinner.getSelectedItemPosition()).build();
//            }
//            iad.setVideoOption(option);
//            iad.setMaxVideoDuration(getMaxVideoDuration());
            unifiedInterstitialAD.loadAD();
        }
    }
}
