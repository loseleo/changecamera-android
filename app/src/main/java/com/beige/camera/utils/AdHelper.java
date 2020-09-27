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

    private boolean isShowFullScreenVideoAd = false;
    private boolean isShowRewardedVideo = false;
    private boolean isShowBannerAdView = false;


    public interface PlayRewardedAdCallback {
       void onDismissed(int action);
       void onFail();
    }

    public void playFullScreenVideoAd(Activity activity,String adType,PlayRewardedAdCallback callback) {
        if(isShowFullScreenVideoAd){
            return;
        }
        isShowFullScreenVideoAd = true;
        AdComponentHolder.getComponent().getAdApi().getAdConfig(adType)
                .compose(io_main())
                .timeout(2000, TimeUnit.MILLISECONDS)
                .map(RxUtil.applyApiResult())
                .subscribe(new Observer<AdConfigBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(AdConfigBean adModel) {
                        AdManager.loadFullScreenAd(activity, adModel.getCandidates(), new Callback<RewardVideoAd>() {
                            @Override
                            public void onAdLoadStart(RewardVideoAd ad) {
                                LogUtils.e("zhangning","onAdLoadStart = "+ad.getAdModel().getAdId());
                                ad.getDismissedHelper().addOnAdDismissListener(new OnDismissedListener() {
                                    @Override
                                    public void onDismissed() {
                                        isShowFullScreenVideoAd = false;
                                        if (callback != null) {
                                            callback.onDismissed(ad.isRewardVerify() ? 1 : 0);
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onFail(Throwable e) {
                                isShowFullScreenVideoAd = false;
                                LogUtils.e("zhangning","onFail e = " + e.getMessage());
                                if (callback != null) {
                                    callback.onFail();
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        isShowFullScreenVideoAd = false;
                        LogUtils.e("zhangning","onAdLoadStart e = " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }



    public void playRewardedVideo(Activity activity,String adType,PlayRewardedAdCallback callback) {
        if(isShowRewardedVideo){
            return;
        }
        isShowRewardedVideo = true;
        AdComponentHolder.getComponent().getAdApi().getAdConfig(adType)
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
                                        isShowRewardedVideo = false;
                                        if (callback != null) {
                                            callback.onDismissed(ad.isRewardVerify() ? 1 : 0);
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onFail(Throwable e) {
                                isShowRewardedVideo = false;
                                if (callback != null) {
                                    callback.onFail();
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        isShowRewardedVideo = false;
                        LogUtils.e("zhangning","onAdLoadStart e = " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void showBannerAdView(String adType, FrameLayout adContainer) {

        if(isShowBannerAdView){
            return;
        }
        isShowBannerAdView = true;
        AdComponentHolder.getComponent().getAdApi().getAdConfig(adType)
                .compose(io_main())
                .timeout(2000, TimeUnit.MILLISECONDS)
                .map(RxUtil.applyApiResult())
                .subscribe(new Observer<AdConfigBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(AdConfigBean adModel) {
                        LogUtils.e("TTInfoFlowAd", "adConfigBean = " + adModel.getCandidates().toString());

                        if (adContainer != null) {
                            adContainer.post(new Runnable() {
                                @Override
                                public void run() {
                                    AdManager.loadInfoFlowAd(adContainer, adModel.getCandidates(), new Callback<InfoFlowAd>() {
                                        @Override
                                        public void onAdLoadStart(InfoFlowAd ad) {
                                        }

                                        @Override
                                        public void onFail(Throwable e) {
                                            isShowBannerAdView = false ;
                                        }
                                    });
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        isShowBannerAdView = false ;
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
