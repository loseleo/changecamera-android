package com.beige.camera.advertisement.core.infoflow;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;

import com.beige.camera.advertisement.TTAdManagerHolder;
import com.beige.camera.advertisement.core.fullscreenvideo.TTFullScreenVideoAd;
import com.beige.camera.advertisement.core.infoflow.img.BaseImgAd;
import com.beige.camera.advertisement.core.infoflow.video.VideoInfoOwner;
import com.beige.camera.advertisement.core.loader.ResourceLoader;
import com.beige.camera.common.base.BaseApplication;
import com.beige.camera.common.feed.bean.AdModel;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.ScreenUtils;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTImage;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TTInfoFlowAd extends InfoFlowAd<TTInfoFlowAd.TTInfoFlowAdResource> {


    public TTInfoFlowAd(FrameLayout adContainer, AdModel adModel) {
        super(adContainer, adModel);
    }

    @NonNull
    @Override
    protected ResourceLoader<TTInfoFlowAd.TTInfoFlowAdResource> onCreateResourceLoader(AdModel adModel) {
        return new TTInfoFlowAd.TTInfoFlowAdResourceLoader(adModel, getAdContainer().getContext());
    }

    @Override
    protected void onSetupAdResource(FrameLayout adContainer, TTInfoFlowAd.TTInfoFlowAdResource ttInfoFlowAdResource) {

        TTNativeExpressAd ttNativeExpressAd = ttInfoFlowAdResource.getTTInfoFlowAdAd();

        ttNativeExpressAd.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                LogUtils.e("TTInfoFlowAd", "onAdClicked");
                notifyAdClick();
            }

            @Override
            public void onAdShow(View view, int type) {
                LogUtils.e("TTInfoFlowAd", "onAdShow");
                notifyAdDisplay();
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                LogUtils.e("TTInfoFlowAd", "onRenderFail msg:" + msg+" code:"+code);
//                Log.e("ExpressView","render fail:"+(System.currentTimeMillis() - startTime));
//                TToast.show(mContext, msg+" code:"+code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                LogUtils.e("TTInfoFlowAd","onRenderSuccess: width=" + width + " height=" + height);
                //返回view的宽高 单位 dp
                //在渲染成功回调时展示广告，提升体验
                adContainer.removeAllViews();
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                view.setLayoutParams(layoutParams);
                adContainer.addView(view);
            }
        });

//        //使用默认个性化模板中默认dislike弹出样式
//        ttNativeExpressAd.setDislikeCallback((Activity) getAdContainer().getContext(), new TTAdDislike.DislikeInteractionCallback() {
//            @Override
//            public void onSelected(int position, String value) {
////                TToast.show(mContext, "点击 " + value);
////                //用户选择不喜欢原因后，移除广告展示
//                adContainer.removeAllViews();
//            }
//            @Override
//            public void onCancel() {
//                LogUtils.e("TTInfoFlowAd", "onCancel");
//            }
//
//            @Override
//            public void onRefuse() {
//
//            }
//        });

        //dislike设置
        if (ttNativeExpressAd.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD){
            //可选，下载监听设置
            ttNativeExpressAd.setDownloadListener(new TTAppDownloadListener() {
                @Override
                public void onIdle() {
//                TToast.show(NativeExpressActivity.this, "点击开始下载", Toast.LENGTH_LONG);
                }

                @Override
                public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
//                if (!mHasShowDownloadActive) {
//                    mHasShowDownloadActive = true;
//                    TToast.show(NativeExpressActivity.this, "下载中，点击暂停", Toast.LENGTH_LONG);
//                }
                }

                @Override
                public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
//                TToast.show(NativeExpressActivity.this, "下载暂停，点击继续", Toast.LENGTH_LONG);
                }

                @Override
                public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
//                TToast.show(NativeExpressActivity.this, "下载失败，点击重新下载", Toast.LENGTH_LONG);
                }

                @Override
                public void onInstalled(String fileName, String appName) {
//                TToast.show(NativeExpressActivity.this, "安装完成，点击图片打开", Toast.LENGTH_LONG);
                }

                @Override
                public void onDownloadFinished(long totalBytes, String fileName, String appName) {
//                TToast.show(NativeExpressActivity.this, "点击安装", Toast.LENGTH_LONG);
                }
            });

        }

        ttNativeExpressAd.render();//调用render开始渲染广告

    }


    static class TTInfoFlowAdResource {

        private TTNativeExpressAd ttNativeExpressAd;

        TTInfoFlowAdResource( TTNativeExpressAd ttNativeExpressAd) {
            this.ttNativeExpressAd = ttNativeExpressAd;
        }

        TTNativeExpressAd getTTInfoFlowAdAd() {
            return ttNativeExpressAd;
        }
    }

    private static class TTInfoFlowAdResourceLoader extends ResourceLoader<TTInfoFlowAdResource> {

        private Context mContext;

        TTInfoFlowAdResourceLoader(AdModel adModel, Context mContext) {
            super(adModel);
            this.mContext = mContext;
        }

        @Override
        protected void onLoad(AdModel adModel) {

            //step1:初始化sdk
            TTAdManager ttAdManager = TTAdManagerHolder.get();
            //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
            TTAdManagerHolder.get().requestPermissionIfNecessary(mContext);
            //step3:创建TTAdNative对象,用于调用广告请求接口
            TTAdNative mTTAdNative = ttAdManager.createAdNative(BaseApplication.getsInstance().getApplicationContext());

            Map<String, String> data = new HashMap<>();
            data.put("adCode", adModel.getAdCode());
            data.put("adId", adModel.getAdId());
            data.put("adChannel", adModel.getAdChannel());

            AdSlot adSlot = new AdSlot.Builder()
                    .setCodeId(adModel.getAdId())
                    .setSupportDeepLink(true)
                    .setAdCount(1)
                    //个性化模板广告需要设置期望个性化模板广告的大小,单位dp,激励视频场景，只要设置的值大于0即可。仅模板广告需要设置此参数
                    .setExpressViewAcceptedSize(500,500)
                    .setImageAcceptedSize(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight())
                    .build();


            mTTAdNative.loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
                @Override
                public void onError(int code, String message) {
                    LogUtils.e("TTInfoFlowAd onError=", message);
                    notifyFail(new RuntimeException("load tt TTInfoFlowAd fail:" + code + ";" + message));
                }

                @Override
                public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                    if (ads == null || ads.size() == 0){
                        return;
                    }
                    TTNativeExpressAd mTTAd = ads.get(0);
                    notifySuccess(new TTInfoFlowAdResource(mTTAd));
                    LogUtils.e("TTInfoFlowAd", "onNativeExpressAdLoad");

                }
            }
            );
        }
    }

}
