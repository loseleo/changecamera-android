package com.beige.camera.advertisement.core.infoflow.img;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.beige.camera.common.feed.bean.AdModel;
import com.beige.camera.advertisement.R;
import com.beige.camera.advertisement.TTAdManagerHolder;
import com.beige.camera.advertisement.core.loader.ResourceLoader;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.ThreadUtils;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTImage;
import com.bytedance.sdk.openadsdk.TTNativeAd;


import java.util.ArrayList;
import java.util.List;

public class TTInfoFlowImgAd extends BaseImgAd<TTInfoFlowImgAd.ImgAdRes> {


    public TTInfoFlowImgAd(FrameLayout adContainer, AdModel adModel) {
        super(adContainer, adModel);
    }

    @Override
    protected void onSetupAdResource(FrameLayout adContainer, ImgAdRes imgAdRes) {

        TTFeedAd ttFeedAd = imgAdRes.getTtFeedAd();
        // 3 大图 2小图 4 组图 5 视频 其它：未知类型
        int imageMode = ttFeedAd.getImageMode();
        if (imageMode != 3 && imageMode !=2 && imageMode != 4 && imageMode != 5) {

            notifyAdFail(new RuntimeException("not support this ad imageMode:" + imageMode));
            return;
        }
        super.onSetupAdResource(adContainer, imgAdRes);

    }

    @Override
    protected void onSetupAdResource(FrameLayout adContainer, ImgAdRes imgAdRes, AdViewHolder adViewHolder) {
        TTFeedAd ttFeedAd = imgAdRes.getTtFeedAd();
        Context context = adContainer.getContext();
        if (context instanceof Activity) {
//            ttFeedAd.setDownloadListener((Activity) context);
        }

        // 可以被点击的view, 也可以把convertView放进来意味整个item可被点击，点击会跳转到落地页
        List<View> clickViewList = new ArrayList<View>();
        clickViewList.add(adViewHolder.ivAdImage);
        clickViewList.add(adViewHolder.tvAdTitle);
        // 创意点击区域的view 点击根据不同的创意进行下载或拨打电话动作
        //如果需要点击图文区域也能进行下载或者拨打电话动作，请将图文区域的view传入creativeViewList
        List<View> creativeViewList = new ArrayList<>();
        creativeViewList.add(adViewHolder.ivAdImage);
        creativeViewList.add(adViewHolder.tvAdTitle);
        // 注册普通点击区域，创意点击区域。重要! 这个涉及到广告计费及交互，必须正确调用。convertView必须使用ViewGroup。
        ttFeedAd.registerViewForInteraction((ViewGroup) adViewHolder.adView, clickViewList, creativeViewList, new TTNativeAd.AdInteractionListener() {
            @Override
            public void onAdClicked(View view, TTNativeAd ttNtObject) {
                notifyAdClick();
            }

            @Override
            public void onAdCreativeClick(View view, TTNativeAd ttNtObject) {
                notifyAdClick();
            }

            @Override
            public void onAdShow(TTNativeAd ttNtObject) {
                notifyAdDisplay();
            }
        });
    }

    @Override
    protected View onCreateAdView(FrameLayout adContainer) {
        return LayoutInflater.from(adContainer.getContext()).inflate(R.layout.ad_feed_img_tt, adContainer, false);
    }

    @NonNull
    @Override
    protected ResourceLoader<ImgAdRes> onCreateResourceLoader(AdModel adModel) {
        return new TTInfoFlowImgLoader(adModel, getAdContainer().getContext());
    }

    public static class ImgAdRes implements BaseImgAd.ImgAdRes {

        private TTFeedAd ttFeedAd;

        public ImgAdRes(TTFeedAd ttFeedAd) {
            this.ttFeedAd = ttFeedAd;
        }

        public TTFeedAd getTtFeedAd() {
            return ttFeedAd;
        }

        @Override
        public String getImgAdTitle() {
            return ttFeedAd.getDescription();
        }

        @Override
        public String getImgAdImageUrl() {
            List<TTImage> imageList = ttFeedAd.getImageList();
            if (imageList == null || imageList.size() <= 0) {
                return null;
            }
            return imageList.get(0).getImageUrl();
        }
    }


    private static class TTInfoFlowImgLoader extends ResourceLoader<ImgAdRes> {

        private Context context;

        TTInfoFlowImgLoader(AdModel adModel, Context context) {
            super(adModel);
            this.context = context;
        }

        @Override
        protected void onLoad(AdModel adModel) {

            ThreadUtils.runOnUIThread(new Runnable() {
                @Override
                public void run() {

                    TTAdManager ttObManager = TTAdManagerHolder.get();
                    //在合适的时机申请权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题
                    ttObManager.requestPermissionIfNecessary(context);
                    TTAdNative mTTAdNative = ttObManager.createAdNative(context);
                    //step3:创建广告请求参数AdSlot,具体参数含义参考文档
                    AdSlot adSlot = new AdSlot.Builder()
                            .setCodeId(adModel.getAdId())
                            .setSupportDeepLink(true)
                            .setImageAcceptedSize(1080, 1920)
                            .setAdCount(1) //请求广告数量为1到3条
                            .setImageAcceptedSize(640,320) //这个参数设置即可，不影响个性化模板广告的size
                            .build();

                    //step4:请求广告,对请求回调的广告作渲染处理
                    mTTAdNative.loadFeedAd(adSlot, new TTAdNative.FeedAdListener() {
                        @Override
                        public void onError(int code, String message) {
                            LogUtils.e("TTInfoFlowImgAd onError=", message);
                            notifyFail(new RuntimeException("load tt info flow video ad fail:" + code + ";" + message));
                        }

                        @Override
                        public void onFeedAdLoad(List<TTFeedAd> ads) {
                            if (ads == null || ads.size() <= 0) {
                                LogUtils.e("TTInfoFlowImgAd null result");
                                notifyFail(new RuntimeException("null result"));
                                return;
                            }
                            notifySuccess(new ImgAdRes(ads.get(0)));
                        }
                    });
                }
            });
        }
    }
}
