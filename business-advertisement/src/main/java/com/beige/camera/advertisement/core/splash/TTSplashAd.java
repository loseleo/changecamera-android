package com.beige.camera.advertisement.core.splash;

import android.content.Context;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.beige.camera.advertisement.TTAdManagerHolder;
import com.beige.camera.common.feed.bean.AdModel;
import com.beige.camera.advertisement.core.loader.ResourceLoader;
import com.beige.camera.common.utils.LogUtils;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;

public class TTSplashAd extends SplashAd<com.bytedance.sdk.openadsdk.TTSplashAd> {


    public TTSplashAd(AdModel adModel, ViewGroup adContainer) {
        super(adModel, adContainer);
    }

    @NonNull
    @Override
    protected ResourceLoader<com.bytedance.sdk.openadsdk.TTSplashAd> onCreateResourceLoader(AdModel adModel) {
        return new TTSplashResLoader(adModel,getAdContainer().getContext());
    }

    @Override
    protected void onSetupAdResource(com.bytedance.sdk.openadsdk.TTSplashAd ad) {
        ViewGroup adContainer = getAdContainer();
        //获取SplashView
        View view = ad.getSplashView();
        adContainer.removeAllViews();
        //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕高
        adContainer.addView(view);
        //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
        //ad.setNotAllowSdkCountdown();

        //设置SplashView的交互监听器
        ad.setSplashInteractionListener(new com.bytedance.sdk.openadsdk.TTSplashAd.AdInteractionListener() {
            @Override
            public void onAdClicked(View view, int i) {
                 notifyAdClick();
            }

            @Override
            public void onAdShow(View view, int i) {
                notifyAdDisplay();
            }

            @Override
            public void onAdSkip() {
                notifyAdSkip();
            }

            @Override
            public void onAdTimeOver() {
                notifyAdTimeOver();
            }
        });
//        if (ad.getInteractionType() == TTObConstant.INTERACTION_TYPE_DOWNLOAD) {
//            ad.setDownloadListener(new TTObAppDownloadListener() {
//                boolean hasShow = false;
//
//                @Override
//                public void onIdle() {
//
//                }
//
//                @Override
//                public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
//                    if (!hasShow) {
//                        hasShow = true;
//                    }
//                }
//
//                @Override
//                public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
//                    showToast("下载暂停...");
//
//                }
//
//                @Override
//                public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
//                    showToast("下载失败...");
//
//                }
//
//                @Override
//                public void onDownloadFinished(long totalBytes, String fileName, String appName) {
//
//                }
//
//                @Override
//                public void onInstalled(String fileName, String appName) {
//
//                }
//            });
//        }
    }

    @Override
    protected void onDestroy() {
    }

    static class TTSplashResLoader extends ResourceLoader<com.bytedance.sdk.openadsdk.TTSplashAd> {

        private static final int AD_TIME_OUT = 3000;
        private Context context;
        private TTAdNative mTTAdNative;

        TTSplashResLoader(AdModel adModel, Context context) {
            super(adModel);
            this.context = context;
        }


        @Override
        protected void onLoad(AdModel adModel) {

            mTTAdNative = TTAdManagerHolder.get().createAdNative(context);
            //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
            AdSlot adSlot = new AdSlot.Builder()
                    .setCodeId(adModel.getAdId())
                    .setSupportDeepLink(true)
                    .setImageAcceptedSize(1080, 1920)
                    .build();
            //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
            mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
                @Override
                @MainThread
                public void onError(int code, String message) {
                    LogUtils.e("TTSplashAd onError=", message);
                    notifyFail(new RuntimeException("load tt splash error:" + code + "," + message));
                }

                @Override
                @MainThread
                public void onTimeout() {
                    LogUtils.e("TTSplashAd onTimeout");
                    notifyFail(new RuntimeException("load tt splash timeout"));
                }

                @Override
                @MainThread
                public void onSplashAdLoad(com.bytedance.sdk.openadsdk.TTSplashAd ad) {
                    LogUtils.e("TTSplashAd onSplashAdLoad");
                    if (ad == null) {
                        notifyFail(new RuntimeException("load tt splash result null"));
                        return;
                    }
                    notifySuccess(ad);
                }
            }, AD_TIME_OUT);
        }

        @Override
        protected void onRelease() {
            super.onRelease();
            mTTAdNative = null;
        }
    }
}
