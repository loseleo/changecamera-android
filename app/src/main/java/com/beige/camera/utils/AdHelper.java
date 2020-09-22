package com.beige.camera.utils;

import android.app.Activity;
import android.widget.FrameLayout;

import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.RxUtil;
import com.beige.camera.advertisement.api.bean.AdConfigBean;
import com.beige.camera.advertisement.core.AdManager;
import com.beige.camera.advertisement.core.dismiss.OnDismissedListener;
import com.beige.camera.advertisement.core.infoflow.InfoFlowAd;
import com.beige.camera.advertisement.core.rewardvideo.RewardVideoAd;
import com.beige.camera.advertisement.core.strategy.Callback;
import com.beige.camera.advertisement.dagger.AdComponentHolder;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.beige.camera.common.utils.RxUtil.io_main;

public class AdHelper {

    public interface PlayRewardedAdCallback {
       void onDismissed(int action);
       void onFail();
    }

    public static void playRewardedVideo(Activity activity,String adType,PlayRewardedAdCallback callback) {

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
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public static void showBannerAdView(String adType, FrameLayout adContainer) {

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
                });
    }


}
