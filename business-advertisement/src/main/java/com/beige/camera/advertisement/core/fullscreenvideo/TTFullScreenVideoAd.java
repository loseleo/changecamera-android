package com.beige.camera.advertisement.core.fullscreenvideo;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.beige.camera.advertisement.TTAdManagerHolder;
import com.beige.camera.advertisement.core.loader.ResourceLoader;
import com.beige.camera.advertisement.core.rewardvideo.RewardVideoAd;
import com.beige.camera.common.base.BaseApplication;
import com.beige.camera.common.feed.bean.AdModel;
import com.beige.camera.common.utils.JsonUtils;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.ScreenUtils;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;

import java.util.HashMap;
import java.util.Map;


public class TTFullScreenVideoAd extends RewardVideoAd<TTFullScreenVideoAd.TTFullScreenVideoResource> {

    public TTFullScreenVideoAd(AdModel adModel, Activity activity) {
        super(activity,adModel);
    }

    @NonNull
    @Override
    protected ResourceLoader<TTFullScreenVideoResource> onCreateResourceLoader(AdModel adModel) {
        return new TTFullScreenVideoResourceLoader(adModel, getActivity());
    }

    @Override
    protected void onSetupAdResource(Activity activity, TTFullScreenVideoResource ttFullScreenVideoResource) {
        com.bytedance.sdk.openadsdk.TTFullScreenVideoAd ttFullScreenVideoAd = ttFullScreenVideoResource.getTTFullScreenVideoAd();

        ttFullScreenVideoAd.setFullScreenVideoAdInteractionListener(new  com.bytedance.sdk.openadsdk.TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {
            boolean isVideoComplete;
            @Override
            public void onAdShow() {
                LogUtils.e("FullVideoAd", "onAdShow");
                notifyAdDisplay();
            }

            @Override
            public void onAdVideoBarClick() {
                LogUtils.e("FullVideoAd", "onAdVideoBarClick");
                notifyAdClick();
            }

            @Override
            public void onAdClose() {
                LogUtils.e("FullVideoAd", "onAdClose");
                if (isVideoComplete) {
                    notifyReward(true);
                }
                getDismissedHelper().notifyAdDismiss();
            }

            @Override
            public void onVideoComplete() {
                LogUtils.e("FullVideoAd", "onVideoComplete");
                isVideoComplete = true;
            }

            @Override
            public void onSkippedVideo() {
                LogUtils.e("FullVideoAd", "skipped");

            }

        });
        //播放激励视频广告
        ttFullScreenVideoAd.showFullScreenVideoAd(activity);
    }

    static class TTFullScreenVideoResource {

        private com.bytedance.sdk.openadsdk.TTFullScreenVideoAd ttFullScreenVideoAd;

        TTFullScreenVideoResource(com.bytedance.sdk.openadsdk.TTFullScreenVideoAd ttFullScreenVideoAd) {
            this.ttFullScreenVideoAd = ttFullScreenVideoAd;
        }

        com.bytedance.sdk.openadsdk.TTFullScreenVideoAd getTTFullScreenVideoAd() {
            return ttFullScreenVideoAd;
        }
    }

    private static class TTFullScreenVideoResourceLoader extends ResourceLoader<TTFullScreenVideoResource> {

        private Activity activity;

        TTFullScreenVideoResourceLoader(AdModel adModel, Activity activity) {
            super(adModel);
            this.activity = activity;
        }

        @Override
        protected void onLoad(AdModel adModel) {

            //step1:初始化sdk
            TTAdManager ttAdManager = TTAdManagerHolder.get();
            //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
            TTAdManagerHolder.get().requestPermissionIfNecessary(activity);
            //step3:创建TTAdNative对象,用于调用广告请求接口
            TTAdNative mTTAdNative = ttAdManager.createAdNative(BaseApplication.getsInstance().getApplicationContext());

            Map<String, String> data = new HashMap<>();
            data.put("adCode", adModel.getAdCode());
            data.put("adId", adModel.getAdId());
            data.put("adChannel", adModel.getAdChannel());

            AdSlot adSlot = new AdSlot.Builder()
                    .setCodeId(adModel.getAdId())
                    .setSupportDeepLink(true)
                    .setAdCount(2)
                    //个性化模板广告需要设置期望个性化模板广告的大小,单位dp,激励视频场景，只要设置的值大于0即可。仅模板广告需要设置此参数
                    .setExpressViewAcceptedSize(500,500)
                    .setImageAcceptedSize(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight())
                    .setOrientation(TTAdConstant.VERTICAL) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                    .build();


            mTTAdNative.loadFullScreenVideoAd(adSlot, new TTAdNative.FullScreenVideoAdListener() {
                @Override
                public void onError(int code, String message) {
                    LogUtils.e("FullVideoAd onError=", message);
                    notifyFail(new RuntimeException("load tt reward video ad fail:" + code + ";" + message));
                }

                @Override
                public void onFullScreenVideoAdLoad(com.bytedance.sdk.openadsdk.TTFullScreenVideoAd ad) {
                    notifySuccess(new TTFullScreenVideoResource(ad));
                    LogUtils.e("RewardVideo", "onFullScreenVideoAdLoad");
                }

                //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
                @Override
                public void onFullScreenVideoCached() {
                    LogUtils.d("FullVideoAd", "FullVideoAd video cached");
                }
            });
        }
    }
}
