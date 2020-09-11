package com.beige.camera.ringtone.core;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;

import com.beige.camera.common.feed.bean.AdModel;
import com.beige.camera.ringtone.core.dismiss.OnDismissedListener;
import com.beige.camera.ringtone.core.infoflow.GDTInfoFlowVideoAd;
import com.beige.camera.ringtone.core.infoflow.InfoFlowAd;
import com.beige.camera.ringtone.core.infoflow.InfoFlowVideoAd;
import com.beige.camera.ringtone.core.infoflow.OnVideoPlayListener;
import com.beige.camera.ringtone.core.infoflow.TTInfoFlowVideoAd;
import com.beige.camera.ringtone.core.infoflow.img.CPCCaImgAd;
import com.beige.camera.ringtone.core.infoflow.img.GDTInfoFlowImgAd;
import com.beige.camera.ringtone.core.infoflow.img.TTInfoFlowImgAd;
import com.beige.camera.ringtone.core.rewardvideo.GDTRewardVideoAd;
import com.beige.camera.ringtone.core.rewardvideo.RewardVideoAd;
import com.beige.camera.ringtone.core.rewardvideo.TTRewardVideoAd;
import com.beige.camera.ringtone.core.splash.CPCCaSplashAd;
import com.beige.camera.ringtone.core.splash.GDTSplashAd;
import com.beige.camera.ringtone.core.splash.OnAdSkipListener;
import com.beige.camera.ringtone.core.splash.SplashAd;
import com.beige.camera.ringtone.core.splash.TTSplashAd;
import com.beige.camera.ringtone.core.strategy.AdLoader;
import com.beige.camera.ringtone.core.strategy.Callback;
import com.beige.camera.ringtone.core.strategy.CallbackWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdManager {


    /**
     * 加载开屏广告
     *
     * @param adContainer
     * @param adModels
     * @param callback
     * @return
     */
    public static AdLoader<SplashAd> loadSplashAd(FrameLayout adContainer, List<AdModel> adModels, Callback<SplashAd> callback) {
        adContainer.removeAllViews();
        return loadAd(adModels, new AdFactory<SplashAd>() {
            @Override
            public SplashAd create(AdModel adModel) {
                SplashAd splashAd = null;
                String adChannel = adModel.getAdChannel();
                if (AdModel.AD_CHANNEL_GDT.equals(adChannel)) {
                    splashAd = new GDTSplashAd(adModel, adContainer);
                } else if (AdModel.AD_CHANNEL_TOUTIAO.equals(adChannel)) {
                    splashAd = new TTSplashAd(adModel, adContainer);
                } else if (AdModel.AD_CHANNEL_CPC_CA.equals(adChannel)) {
                    splashAd = new CPCCaSplashAd(adModel, adContainer);
                }
                if (splashAd != null) {
                    splashAd.addOnAdSkipListener(new OnAdSkipListener() {
                        @Override
                        public void onSkip() {
                            AdReportDataUtils.adSplashSkip(adModel, null);
                        }
                    });
                }
                return splashAd;
            }
        },3000, callback);
    }


    /**
     * 加载大图广告
     *
     * @param adContainer
     * @param adModels
     * @param callback
     * @return
     */
    public static AdLoader<InfoFlowAd> loadBigImgAd(FrameLayout adContainer,
                                                    List<AdModel> adModels,
                                                    Callback<InfoFlowAd> callback) {
        return loadAd(adModels, new AdFactory<InfoFlowAd>() {
            @Override
            public InfoFlowAd create(AdModel adModel) {
                String adChannel = adModel.getAdChannel();
                if (AdModel.AD_CHANNEL_TOUTIAO.equals(adChannel)) {
                    return new TTInfoFlowImgAd(adContainer, adModel);
                } else if (AdModel.AD_CHANNEL_GDT.equals(adChannel)) {
                    return new GDTInfoFlowImgAd(adContainer, adModel);
                } else if (AdModel.AD_CHANNEL_CPC_CA.equals(adChannel)) {
                    return new CPCCaImgAd(adContainer, adModel);
                }
                return null;
            }
        }, callback);
    }


    /**
     * 加载信息流广告
     *
     * @param adContainer
     * @param adModels
     * @param callback
     * @return
     */
    public static AdLoader<InfoFlowAd> loadInfoFlowAd(FrameLayout adContainer, List<AdModel> adModels, Callback<InfoFlowAd> callback, ExtraProvider extraProvider) {
        adContainer.removeAllViews();
        return loadAd(adModels, new AdFactory<InfoFlowAd>() {
            @Override
            public InfoFlowAd create(AdModel adModel) {
                String adChannel = adModel.getAdChannel();
                 if (AdModel.AD_CHANNEL_TOUTIAO.equals(adChannel)) {
                    return new TTInfoFlowVideoAd(adContainer, adModel);
                } else if (AdModel.AD_CHANNEL_GDT.equals(adChannel)) {
                    return new GDTInfoFlowVideoAd(adContainer, adModel);
                } else if(AdModel.AD_CHANNEL_CPC_CA.equals(adChannel)){
//                    return new CPCCaInfoFlowAd(adContainer,adModel);
                }
                return null;
            }
        }, new CallbackWrapper<InfoFlowAd>(callback) {
            @Override
            public void onAdLoadStart(InfoFlowAd ad) {
                super.onAdLoadStart(ad);
                if (ad instanceof InfoFlowVideoAd) {
                    ((InfoFlowVideoAd) ad).addOnVideoPlayListener(new OnVideoPlayListener() {

                        long playStartTime;
                        int playCount = 0;

                        @Override
                        public void onVideoLoad() {
                        }

                        @Override
                        public void onVideoError(Throwable e) {

                        }

                        @Override
                        public void onVideoStartPlay() {
                            playStartTime = System.currentTimeMillis();
                        }

                        @Override
                        public void onVideoPaused() {
                            long duration = computeDuration();
                            reportDuration(duration);
                        }

                        @Override
                        public void onVideoContinuePlay() {
                            playStartTime = System.currentTimeMillis();
                        }

                        @Override
                        public void onProgressUpdate(long max, long progress) {

                        }

                        @Override
                        public void onVideoComplete() {
                            playCount++;
                            long duration = computeDuration();
                            reportDuration(duration);
                        }

                        private long computeDuration() {
                            if (playStartTime > 0) {
                                long duration = System.currentTimeMillis() - playStartTime;
                                playStartTime = 0;
                                return duration;
                            }
                            return 0;
                        }

                        private void reportDuration(long duration) {

                            long reportValidDuration = 1500 ;
                            if (duration < reportValidDuration) {
                                return;
                            }
                            HashMap<String, String> extra = extraProvider.provideExtra();
                            if (extra == null) {
                                extra = new HashMap<>();
                            }
                            extra.put("duration", duration + "");
                            extra.put("play_count", playCount + "");
                            AdModel adModel = ad.getAdModel();
                            AdReportDataUtils.adVideoPlayDuration(adModel, extra);
                        }
                    });
                }
            }
        });
    }

    /**
     * 加载广告
     *
     * @param adModels
     * @param callback
     */
    public static <T extends Ad> AdLoader<T> loadAd(List<AdModel> adModels, AdFactory<T> adAdFactory,long timeout, Callback<T> callback) {

        callback = wrapCallback(callback);

        List<T> infoFlowAds = new ArrayList<>();
        if (adModels != null) {
            for (int i = 0; i < adModels.size(); i++) {
                AdModel adModel = adModels.get(i);
                if (adAdFactory != null) {
                    T infoFlowAd = adAdFactory.create(adModel);
                    if (infoFlowAd != null) {
                        infoFlowAds.add(infoFlowAd);
                    }
                }
            }
        }
        AdLoader<T> adAdLoader = new AdLoader<>(infoFlowAds);
        adAdLoader.setCallback(callback);
        adAdLoader.load();
        return adAdLoader;
    }

    public static <T extends Ad> AdLoader<T> loadAd(List<AdModel> adModels, AdFactory<T> adAdFactory, Callback<T> callback) {
        return loadAd(adModels,adAdFactory,0,callback);
    }

    /**
     * 加载广告
     *
     * @param adModels
     * @param callback
     */
    public static <T extends Ad> AdLoader<T> preloadAd(List<AdModel> adModels, AdFactory<T> adAdFactory, Callback<T> callback) {

        callback = wrapCallback(callback);

        List<T> infoFlowAds = new ArrayList<>();
        if (adModels != null) {
            for (int i = 0; i < adModels.size(); i++) {
                AdModel adModel = adModels.get(i);
                if (adAdFactory != null) {
                    T infoFlowAd = adAdFactory.create(adModel);
                    if (infoFlowAd != null) {
                        infoFlowAds.add(infoFlowAd);
                    }
                }
            }
        }
        AdLoader<T> adAdLoader = new AdLoader<>(infoFlowAds);
        adAdLoader.setCallback(callback);
        adAdLoader.preload();
        return adAdLoader;
    }

    /**
     * 加载激励视频
     *
     * @param activity
     * @param adModels
     */
    public static void loadRewardVideoAd(Activity activity, List<AdModel> adModels, Callback<RewardVideoAd> callback) {

        loadAd(adModels, new RewardVideoAdFactory(activity), new RewardVideoAdCallback<>(callback));
    }


    /**
     * 是否使用个性化金币
     *
     * @param adModels adModels
     * @return
     */
    public static boolean isSmartCoin(List<AdModel> adModels) {
        if (adModels == null || adModels.isEmpty()) {
            return false;
        }
        for (int i = 0; i < adModels.size(); i++) {
            if (adModels.get(i).getSmartCoinEnable() == 1) {
                return true;
            }
        }
        return false;
    }


    private static <T extends Ad> Callback<T> wrapCallback(Callback<T> callback) {

        return new ReportLoadCallback<>(callback);
    }

    private static class ReportLoadCallback<T extends Ad> extends CallbackWrapper<T> {


        ReportLoadCallback(Callback<T> callback) {
            super(callback);
        }

        @Override
        public void onAdLoadStart(T ad) {
            super.onAdLoadStart(ad);
            AdModel adModel = ad.getAdModel();
            AdReportDataUtils.adRequestStart(adModel, null);
            ad.addAdListener(new ReportAdListener(ad));
        }

    }

    private static class ReportAdListener implements Ad.AdListener {

        private Ad ad;
        private int showCount;
        private int clickCount;

        ReportAdListener(Ad ad) {
            this.ad = ad;
        }

        @Override
        public void onAdLoaded() {
            AdModel adModel = ad.getAdModel();
            AdReportDataUtils.adRequestSuccess(adModel,null);
        }

        @Override
        public void onAdDisplay() {
            showCount++;
            AdModel adModel = ad.getAdModel();
            HashMap<String, String> extra = new HashMap<>();
            extra.put("show_count", showCount + "");
            AdReportDataUtils.adShowSuccess(adModel,null);
        }

        @Override
        public void onAdFail(Throwable e) {
            AdModel adModel = ad.getAdModel();
            String errorMsg = e != null ? e.getMessage() : "";
            HashMap<String, String> extra = new HashMap<>();
            AdReportDataUtils.adShowFail(adModel, null, errorMsg);
        }

        @Override
        public void onAdClick() {
            clickCount++;
            AdModel adModel = ad.getAdModel();
            HashMap<String, String> extra = new HashMap<>();
            extra.put("click_count", clickCount + "");
            AdReportDataUtils.adClick(adModel,extra);
        }
    }


    public interface ExtraProvider {

        HashMap<String, String> provideExtra();
    }


    private static class RewardVideoAdFactory implements AdFactory<RewardVideoAd> {

        private Activity activity;

        RewardVideoAdFactory(Activity activity) {
            this.activity = activity;
        }

        @Override
        public RewardVideoAd create(AdModel adModel) {

            final String adChannel = adModel.getAdChannel();
            if (AdModel.AD_CHANNEL_GDT.equals(adChannel)) {
                return new GDTRewardVideoAd(adModel, activity);
            } else if (AdModel.AD_CHANNEL_TOUTIAO.equals(adChannel)) {
                return new TTRewardVideoAd(adModel, activity);
            }
            return null;
        }
    }

    private static class RewardVideoAdCallback<Ad extends RewardVideoAd> extends CallbackWrapper<Ad> {

        RewardVideoAdCallback(@Nullable Callback<Ad> wrapper) {
            super(wrapper);
        }

        @Override
        public void onAdLoadStart(Ad ad) {
            super.onAdLoadStart(ad);
            ad.getDismissedHelper().addOnAdDismissListener(new OnDismissedListener() {
                @Override
                public void onDismissed() {
                    boolean rewardVerify = ad.isRewardVerify();
                    AdModel adModel = ad.getAdModel();
                    if (rewardVerify) {
                        AdReportDataUtils.adVideoPalySuccess(adModel, null);
                    } else {
                        AdReportDataUtils.adVideoPalyFail(adModel, null);
                    }
                }
            });
        }
    }


//    public static AdLoader<TuiaAd> loadTuiaAd(Activity context, List<AdModel> adModels, Callback<TuiaAd> callback) {
//        return loadAd(adModels, new AdFactory<TuiaAd>() {
//            @Override
//            public TuiaAd create(AdModel adModel) {
//                return new TuiaAd(context, adModel);
//            }
//        }, callback);
//    }

}
