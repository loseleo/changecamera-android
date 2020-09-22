package com.beige.camera.advertisement.core.infoflow;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.beige.camera.advertisement.R;
import com.beige.camera.advertisement.TTAdManagerHolder;
import com.beige.camera.advertisement.core.infoflow.loader.res.TTInfoFlowVideo;
import com.beige.camera.advertisement.core.loader.ResourceLoader;
import com.beige.camera.common.feed.bean.AdModel;
import com.beige.camera.common.utils.ThreadUtils;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTDrawFeedAd;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTNativeAd;

import java.util.ArrayList;
import java.util.List;

public class TTInfoFlowVideoAd extends BaseInfoFlowVideoAd<TTInfoFlowVideo> {


    public TTInfoFlowVideoAd(FrameLayout adContainer, AdModel adModel) {
        super(adContainer, adModel);
    }

    @NonNull
    @Override
    protected ResourceLoader<TTInfoFlowVideo> onCreateResourceLoader(AdModel adModel) {
        return new TTInfoFlowVideoLoader(adModel, getAdContainer().getContext());
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.ad_feed_video_tt;
    }

    @Override
    void onSetupAdResource(TTInfoFlowVideo ttInfoFlowVideo, AdViewHolder adViewHolder) {
        Activity activity = (Activity) adViewHolder.adView.getContext();


        FrameLayout adViewContainer = adViewHolder.findViewById(R.id.fl_tt_ad_view);

        TTDrawFeedAd ad = ttInfoFlowVideo.getTTDrawFeedOb();
        setDownloadState(ad.getButtonText(), -1, adViewHolder.tvDownload, adViewHolder.tvDialogDownload);
        ad.setActivityForDownloadApp(activity);
        //点击监听器必须在getAdView之前调
        ad.setDrawVideoListener(new TTDrawFeedAd.DrawVideoListener() {
            @Override
            public void onClickRetry() {

            }

            @Override
            public void onClick() {
                notifyAdClick();
            }
        });
        ad.setCanInterruptVideoPlay(true);
        ad.setPauseIcon(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ad_video_pause), 60);
        List<View> clickViews = new ArrayList<>();
        clickViews.add(adViewHolder.tvTitle);
        clickViews.add(adViewHolder.ivAvatar);
        clickViews.add(adViewHolder.tvDesc);
        clickViews.add(adViewHolder.tvDialogTitle);
        clickViews.add(adViewHolder.ivDialogAvatar);
        clickViews.add(adViewHolder.tvDialogDesc);
        List<View> creativeViews = new ArrayList<>();
        creativeViews.add(adViewHolder.btDownload);
        creativeViews.add(adViewHolder.btDialogDownload);
        creativeViews.add(adViewHolder.findViewById(R.id.view_click));
        ad.registerViewForInteraction(adViewContainer, clickViews, creativeViews, new TTNativeAd.AdInteractionListener() {

            @Override
            public void onAdClicked(View view, TTNativeAd ad) {
                notifyAdClick();
            }

            @Override
            public void onAdCreativeClick(View view, TTNativeAd ad) {
                notifyAdClick();
            }

            @Override
            public void onAdShow(TTNativeAd ad) {
                notifyAdDisplay();
            }

        });

        ad.setVideoAdListener(new TTDrawFeedAd.VideoAdListener() {

            @Override
            public void onVideoLoad(TTFeedAd ttVfObject) {
                Log.i("Test", "==VideoObListener==onVideoLoad=======");
                notifyVideoLoaded();
            }

            @Override
            public void onVideoError(int i, int i1) {
                Log.i("Test", "==VideoObListener==onVideoError=======");
                notifyVideoError(new RuntimeException(i + "====" + i1));
            }

            @Override
            public void onVideoAdStartPlay(TTFeedAd ttVfObject) {
                Log.i("Test", "==VideoObListener==onVideoObStartPlay=======");
                notifyVideoStartPlay();
            }

            @Override
            public void onVideoAdPaused(TTFeedAd ttVfObject) {
                Log.i("Test", "==VideoObListener==onVideoObPaused=======");
                notifyVideoPaused();
            }

            @Override
            public void onVideoAdContinuePlay(TTFeedAd ttVfObject) {
                Log.i("Test", "==VideoObListener==onVideoObContinuePlay=======");
                notifyVideoContinuePlay();
            }

            @Override
            public void onProgressUpdate(long l, long l1) {
                Log.i("Test", "==VideoObListener==onProgressUpdate=======" + l + "==" + l1);
                notifyVideoProgressUpdate(l, l1);
            }

            @Override
            public void onVideoAdComplete(TTFeedAd ttVfObject) {
                Log.i("Test", "==VideoObListener==onVideoObComplete=======");
                notifyVideoComplete();
            }

        });

        View obView = ad.getAdView();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        obView.setLayoutParams(layoutParams);
        adViewContainer.addView(obView);
    }

    @Override
    protected void onAdViewAttachedToWindow(TTInfoFlowVideo ttInfoFlowVideo, AdViewHolder adViewHolder) {
        super.onAdViewAttachedToWindow(ttInfoFlowVideo, adViewHolder);
        Activity activity = (Activity) adViewHolder.adView.getContext();
        TTDrawFeedAd ad = ttInfoFlowVideo.getTTDrawFeedOb();
        ad.setDownloadListener(new TTAppDownloadListener() {

            @Override
            public void onIdle() {
                Log.i("Test", "==TTObAppDownloadListener==onIdle=======");
                setDownloadState(R.string.ad_download_right_now, R.mipmap.ad_btn_state_download, adViewHolder.tvDownload, adViewHolder.tvDialogDownload);
            }

            @Override
            public void onDownloadActive(long l, long l1, String s, String s1) {
                Log.i("Test", "==TTObAppDownloadListener==onDownloadActive=======" + l1 + "====" + l + "===" + s + "====" + s1);
                int progress = (int) ((float) l1 / l * 100);
                adViewHolder.btDownload.setProgress(progress);
                adViewHolder.btDialogDownload.setProgress(progress);
                String progressText = String.format(activity.getResources().getString(R.string.ad_downloading), progress);
                setDownloadState(progressText, -1, adViewHolder.tvDownload, adViewHolder.tvDialogDownload);
            }

            @Override
            public void onDownloadPaused(long l, long l1, String s, String s1) {
                Log.i("Test", "==TTObAppDownloadListener==onDownloadPaused=======" + l1 + "====" + l + "===" + s + "====" + s1);
                setDownloadState(R.string.ad_download_continue, R.mipmap.ad_btn_state_download, adViewHolder.tvDownload, adViewHolder.tvDialogDownload);
            }

            @Override
            public void onDownloadFailed(long l, long l1, String s, String s1) {
                Log.i("Test", "==TTObAppDownloadListener==onDownloadFailed=======" + l1 + "====" + l + "===" + s + "====" + s1);
                setDownloadState(R.string.ad_download_again, R.mipmap.ad_btn_state_download, adViewHolder.tvDownload, adViewHolder.tvDialogDownload);
            }

            @Override
            public void onDownloadFinished(long l, String s, String s1) {
                Log.i("Test", "==TTObAppDownloadListener==onDownloadFinished=======" + l + "===" + s + "====" + s1);
                adViewHolder.btDownload.setProgress(100);
                adViewHolder.btDialogDownload.setProgress(100);
                setDownloadState(R.string.ad_install_right_now, R.mipmap.ad_btn_state_install, adViewHolder.tvDownload, adViewHolder.tvDialogDownload);
            }

            @Override
            public void onInstalled(String s, String s1) {
                Log.i("Test", "==TTObAppDownloadListener==onInstalled=======" + s + "====" + s1);
                adViewHolder.btDownload.setProgress(100);
                adViewHolder.btDialogDownload.setProgress(100);
                setDownloadState(R.string.ad_open_app, R.mipmap.ad_btn_state_open_app, adViewHolder.tvDownload, adViewHolder.tvDialogDownload);
            }
        });
    }

    private static class TTInfoFlowVideoLoader extends ResourceLoader<TTInfoFlowVideo> {

        private Context context;

        TTInfoFlowVideoLoader(AdModel adModel, Context context) {
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
                            .build();
                    //step4:请求广告,对请求回调的广告作渲染处理
                    mTTAdNative.loadDrawFeedAd(adSlot, new TTAdNative.DrawFeedAdListener() {
                        @Override
                        public void onError(int code, String message) {
                            notifyFail(new RuntimeException("load tt info flow video ad fail:" + code + ";" + message));
                        }

                        @Override
                        public void onDrawFeedAdLoad(List<TTDrawFeedAd> ads) {
                            if (ads == null || ads.size() <= 0) {
                                notifyFail(new RuntimeException("null result"));
                                return;
                            }
                            notifySuccess(new TTInfoFlowVideo(ads.get(0)));
                        }
                    });
                }
            });
        }
    }
}
