package com.beige.camera.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.FrameLayout;

import com.beige.camera.bean.FunctionBean;
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

    public static String getBannerAdTypeById(String id) {
        String adType = "";
        if (TextUtils.equals(id, FunctionBean.ID_CHANGE_OLD)) {
            adType = "Finnish_feeds";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_GENDER_BOY)) {
            adType = "Finnish_feeds";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_GENDER_GIRL)) {
            adType = "Finnish_feeds";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_CHILD)) {
            adType = "Finnish_feeds";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_CARTOON)) {
            adType = "Finnish_feeds";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_ANIMAL)) {
            adType = "Finnish_feeds";
        } else if (TextUtils.equals(id, FunctionBean.ID_DETECTION_AGE)) {
            adType = "Finnish_feeds";
        } else if (TextUtils.equals(id, FunctionBean.ID_DETECTION_BABY)) {
            adType = "Finnish_feeds";
        } else if (TextUtils.equals(id, FunctionBean.ID_DETECTION_VS)) {
            adType = "Finnish_feeds";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_BACKGROUND)) {
            adType = "Finnish_feeds";
        } else if (TextUtils.equals(id, FunctionBean.ID_DETECTION_PAST)) {
            adType = "Finnish_feeds";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_CLOTHES)) {
            adType = "Finnish_feeds";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_HAIR)) {
            adType = "Finnish_feeds";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_CUSTOMS)) {
            adType = "Finnish_feeds";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_ANIMALFACE)) {
            adType = "Finnish_feeds";
        } else if (TextUtils.equals(id, "USER_CENTER_ACTIVITY")) {
            adType = "Mine_feeds";
        } else if (TextUtils.equals(id, "CAMERA_ACTIVITY")) {

        }
        return adType;
    }

    public static String getRewardedAdTypeById(String id) {
        String adType = "";
        if (TextUtils.equals(id, FunctionBean.ID_CHANGE_OLD)) {
            adType = "Old_Incentivevideo";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_GENDER_BOY)) {
            adType = "Gender_Incentivevideo";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_GENDER_GIRL)) {
            adType = "Gender_Incentivevideo";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_CHILD)) {
            adType = "Children_Incentivevideo";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_CARTOON)) {
            adType = "Cartoon_Incentivevideo";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_ANIMAL)) {
            adType = "Animal_Incentivevideo";
        } else if (TextUtils.equals(id, FunctionBean.ID_DETECTION_AGE)) {
            adType = "Age_Incentivevideo";
        } else if (TextUtils.equals(id, FunctionBean.ID_DETECTION_BABY)) {
            adType = "Baby_Incentivevideo";
        } else if (TextUtils.equals(id, FunctionBean.ID_DETECTION_VS)) {
            adType = "Compete_Incentivevideo";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_BACKGROUND)) {
            adType = "Background_Incentivevideo";
        } else if (TextUtils.equals(id, FunctionBean.ID_DETECTION_PAST)) {
            adType = "Across_Incentivevideo";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_CLOTHES)) {

        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_HAIR)) {
            adType = "Hairdo_Incentivevideo";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_CUSTOMS)) {
            adType = "Foreign_Incentivevideo";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_ANIMALFACE)) {
            adType = "Orc_Incentivevideo";
        }
        return adType;
    }

    public static String getFullScreenVideoAdTypeById(String id) {
        String adType = "";
        if (TextUtils.equals(id, FunctionBean.ID_CHANGE_OLD)) {
            adType = "Old_Fullvideo";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_GENDER_BOY)) {
            adType = "Gender_Fullvideo";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_GENDER_GIRL)) {
            adType = "Gender_Fullvideo";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_CHILD)) {
            adType = "Children_Fullvideo";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_CARTOON)) {
            adType = "Cartoon_Fullvideo";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_ANIMAL)) {
            adType = "Animal_Fullvideo";
        } else if (TextUtils.equals(id, FunctionBean.ID_DETECTION_AGE)) {
            adType = "Age_Fullvideo";
        } else if (TextUtils.equals(id, FunctionBean.ID_DETECTION_BABY)) {
            adType = "Baby_Fullvideo";
        } else if (TextUtils.equals(id, FunctionBean.ID_DETECTION_VS)) {
            adType = "Compete_Fullvideo";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_BACKGROUND)) {
            adType = "Background_Fullvideo";
        } else if (TextUtils.equals(id, FunctionBean.ID_DETECTION_PAST)) {
            adType = "Across_Fullvideo";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_CLOTHES)) {
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_HAIR)) {
            adType = "Hairdo_Fullvideo";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_CUSTOMS)) {
            adType = "Foreign_Fullvideo";
        } else if (TextUtils.equals(id, FunctionBean.ID_CHANGE_ANIMALFACE)) {
            adType = "Orc_Fullvideo";
        } else if (TextUtils.equals(id, "CAMERA_ACTIVITY")) {
            adType = "Camera_Fullvideo";
        }

        return adType;
    }

    public void playFullScreenVideoAd(Activity activity, String adType, PlayRewardedAdCallback callback) {
        if (isShowFullScreenVideoAd) {
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
                                LogUtils.e("zhangning", "onAdLoadStart = " + ad.getAdModel().getAdId());
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
                                LogUtils.e("zhangning", "onFail e = " + e.getMessage());
                                if (callback != null) {
                                    callback.onFail();
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        isShowFullScreenVideoAd = false;
                        LogUtils.e("zhangning", "onAdLoadStart e = " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void playRewardedVideo(Activity activity, String adType, PlayRewardedAdCallback callback) {
        if (isShowRewardedVideo) {
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
                        LogUtils.e("zhangning", "onAdLoadStart e = " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void showBannerAdView(String adType, FrameLayout adContainer) {

        if (isShowBannerAdView) {
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
                                            isShowBannerAdView = false;
                                        }
                                    });
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        isShowBannerAdView = false;
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
