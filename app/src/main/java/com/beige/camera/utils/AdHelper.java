package com.beige.camera.utils;

import android.app.Activity;
import android.widget.FrameLayout;

import com.beige.camera.common.feed.bean.AdModel;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.RxUtil;
import com.beige.camera.advertisement.api.bean.AdConfigBean;
import com.beige.camera.advertisement.core.AdManager;
import com.beige.camera.advertisement.core.dismiss.OnDismissedListener;
import com.beige.camera.advertisement.core.infoflow.InfoFlowAd;
import com.beige.camera.advertisement.core.rewardvideo.RewardVideoAd;
import com.beige.camera.advertisement.core.strategy.Callback;
import com.beige.camera.advertisement.dagger.AdComponentHolder;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.beige.camera.common.utils.RxUtil.io_main;

public class AdHelper {

    public interface PlayRewardedAdCallback {
       void onDismissed(int action);
       void onFail();
    }

    public static void playFullScreenVideoAd(Activity activity,String adType,PlayRewardedAdCallback callback) {

        AdModel adModel = new AdModel();
//        adModel.setAdCode("945489981");
//        adModel.setAdId("945489981");
//        adModel.setAdChannel(AdModel.AD_CHANNEL_TOUTIAO);
        adModel.setAdCode("3041831491796621");
        adModel.setAdId("3041831491796621");
        adModel.setAdChannel(AdModel.AD_CHANNEL_GDT);
        adModel.setAction(0);

        ArrayList<AdModel> adModels = new ArrayList<>();
        adModels.add(adModel);

        AdManager.loadFullScreenAd(activity, adModels, new Callback<RewardVideoAd>() {
            @Override
            public void onAdLoadStart(RewardVideoAd ad) {
                LogUtils.e("zhangning","onAdLoadStart = "+ad.getAdModel().getAdId());
                ad.getDismissedHelper().addOnAdDismissListener(new OnDismissedListener() {
                    @Override
                    public void onDismissed() {
                        if (callback != null) {
                            callback.onDismissed(ad.isRewardVerify() ? 1 : 0);
                        }
                    }
                });
            }

            @Override
            public void onFail(Throwable e) {
                LogUtils.e("zhangning","onFail e = " + e.getMessage());
                if (callback != null) {
                    callback.onFail();
                }
            }
        });
    }



    public static void playRewardedVideo(Activity activity,String adType,PlayRewardedAdCallback callback) {

        AdModel adModel = new AdModel();
//        adModel.setAdCode("945489982");
//        adModel.setAdId("945489982");
//        adModel.setAdChannel(AdModel.AD_CHANNEL_TOUTIAO);
//        adModel.setAdCode("3061239401492589");
//        adModel.setAdId("3061239401492589");
        adModel.setAdCode("6031237421692525");
        adModel.setAdId("6031237421692525");
        adModel.setAdChannel(AdModel.AD_CHANNEL_GDT);
        adModel.setAction(0);

        ArrayList<AdModel> adModels = new ArrayList<>();
        adModels.add(adModel);

        AdManager.loadRewardVideoAd(activity, adModels, new Callback<RewardVideoAd>() {
            @Override
            public void onAdLoadStart(RewardVideoAd ad) {
                LogUtils.e("zhangning","onAdLoadStart = "+ad.getAdModel().getAdId());
                ad.getDismissedHelper().addOnAdDismissListener(new OnDismissedListener() {
                    @Override
                    public void onDismissed() {
                        if (callback != null) {
                            callback.onDismissed(ad.isRewardVerify() ? 1 : 0);
                        }
                    }
                });
            }

            @Override
            public void onFail(Throwable e) {
                LogUtils.e("zhangning","onFail e = " + e.getMessage());
                if (callback != null) {
                    callback.onFail();
                }
            }
        });

//        String adCode = "945489981";
        /*AdComponentHolder.getComponent().getAdApi().getAdConfig(adType)
                .compose(io_main())
                .timeout(2000, TimeUnit.MILLISECONDS)
                .map(RxUtil.applyApiResult())
                .subscribe(new Observer<AdConfigBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AdConfigBean adModel) {
                        AdManager.loadRewardVideoAd(activity, adModel.getCandidates(), new Callback<RewardVideoAd>() {
                            @Override
                            public void onAdLoadStart(RewardVideoAd ad) {
                                ad.getDismissedHelper().addOnAdDismissListener(new OnDismissedListener() {
                                    @Override
                                    public void onDismissed() {
                                        if (callback != null) {
                                            callback.onDismissed(ad.isRewardVerify() ? 1 : 0);
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onFail(Throwable e) {
                                if (callback != null) {
                                    callback.onFail();
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("zhangning","onAdLoadStart e = " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });*/
    }


    public static void showBannerAdView(String adType, FrameLayout adContainer) {

        AdModel adModel = new AdModel();
        adModel.setAdCode("945497259");
        adModel.setAdId("945497259");
        adModel.setAction(0);
        adModel.setAdChannel(AdModel.AD_CHANNEL_TOUTIAO);
        ArrayList<AdModel> adModels = new ArrayList<>();
        adModels.add(adModel);
        if (adContainer != null) {
            adContainer.post(new Runnable() {
                @Override
                public void run() {
                    AdManager.loadBigImgAd(adContainer, adModels, new Callback<InfoFlowAd>() {
                        @Override
                        public void onAdLoadStart(InfoFlowAd ad) {
                            LogUtils.e("zhangning","onAdLoadStart = "+ad.getAdModel().getAdId());
                        }

                        @Override
                        public void onFail(Throwable e) {
                            LogUtils.e("zhangning","onFail e = " + e.getMessage());
                        }
                    });
                }
            });
        }


        /*AdComponentHolder.getComponent().getAdApi().getAdConfig(adType)
                .compose(io_main())
                .timeout(2000, TimeUnit.MILLISECONDS)
                .map(RxUtil.applyApiResult())
                .subscribe(new Observer<AdConfigBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(AdConfigBean adModel) {
                        LogUtils.e("zhangning", "adConfigBean = " + adModel.getCandidates().toString());
                        if (adContainer != null) {
                            adContainer.post(new Runnable() {
                                @Override
                                public void run() {
                                    AdManager.loadBigImgAd(adContainer, adModel.getCandidates(), new Callback<InfoFlowAd>() {
                                        @Override
                                        public void onAdLoadStart(InfoFlowAd ad) {
                                        }

                                        @Override
                                        public void onFail(Throwable e) {

                                        }
                                    });
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });*/
    }


}
