package com.jifen.dandan.ringtone.core.infoflow;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.bykv.vk.openvk.TTAppDownloadListener;
import com.bykv.vk.openvk.TTDrawVfObject;
import com.bykv.vk.openvk.TTNtObject;
import com.bykv.vk.openvk.TTVfManager;
import com.bykv.vk.openvk.TTVfNative;
import com.bykv.vk.openvk.TTVfObject;
import com.bykv.vk.openvk.VfSlot;
import com.jifen.dandan.ringtone.R;
import com.jifen.dandan.ringtone.TTAdManagerHolder;
import com.jifen.dandan.ringtone.core.infoflow.loader.res.TTInfoFlowVideo;
import com.jifen.dandan.ringtone.core.loader.ResourceLoader;
import com.jifen.dandan.common.feed.bean.AdModel;
import com.jifen.dandan.common.utils.ThreadUtils;

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

        TTDrawVfObject ad = ttInfoFlowVideo.getTTDrawFeedOb();
        setDownloadState(ad.getButtonText(), -1, adViewHolder.tvDownload, adViewHolder.tvDialogDownload);
        ad.setActivityForDownloadApp(activity);
        //点击监听器必须在getAdView之前调
        ad.setDrawVideoListener(new TTDrawVfObject.DrawVideoListener() {
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
        ad.registerViewForInteraction(adViewContainer, clickViews, creativeViews, new TTDrawVfObject.VfInteractionListener() {
            @Override
            public void onClicked(View view, TTNtObject ttNtObject) {
                notifyAdClick();
            }

            @Override
            public void onCreativeClick(View view, TTNtObject ttNtObject) {
                notifyAdClick();
            }

            @Override
            public void onShow(TTNtObject ttNtObject) {
                notifyAdDisplay();
            }
        });
        ad.setVideoListener(new TTDrawVfObject.VideoVfListener() {

            @Override
            public void onVideoLoad(TTVfObject ttVfObject) {
                Log.i("Test", "==VideoObListener==onVideoLoad=======");
                notifyVideoLoaded();
            }

            @Override
            public void onVideoError(int i, int i1) {
                Log.i("Test", "==VideoObListener==onVideoError=======");
                notifyVideoError(new RuntimeException(i + "====" + i1));
            }

            @Override
            public void onVideoStartPlay(TTVfObject ttVfObject) {
                Log.i("Test", "==VideoObListener==onVideoObStartPlay=======");
                notifyVideoStartPlay();
            }

            @Override
            public void onVideoPaused(TTVfObject ttVfObject) {
                Log.i("Test", "==VideoObListener==onVideoObPaused=======");
                notifyVideoPaused();
            }

            @Override
            public void onVideoContinuePlay(TTVfObject ttVfObject) {
                Log.i("Test", "==VideoObListener==onVideoObContinuePlay=======");
                notifyVideoContinuePlay();
            }

            @Override
            public void onProgressUpdate(long l, long l1) {
                Log.i("Test", "==VideoObListener==onProgressUpdate=======" + l + "==" + l1);
                notifyVideoProgressUpdate(l, l1);
            }

            @Override
            public void onVideoComplete(TTVfObject ttVfObject) {
                Log.i("Test", "==VideoObListener==onVideoObComplete=======");
                notifyVideoComplete();
            }

        });

        View obView = ad.getVfView();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        obView.setLayoutParams(layoutParams);
        adViewContainer.addView(obView);
    }

    @Override
    protected void onAdViewAttachedToWindow(TTInfoFlowVideo ttInfoFlowVideo, AdViewHolder adViewHolder) {
        super.onAdViewAttachedToWindow(ttInfoFlowVideo, adViewHolder);
        Activity activity = (Activity) adViewHolder.adView.getContext();
        TTDrawVfObject ad = ttInfoFlowVideo.getTTDrawFeedOb();
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

                    TTVfManager ttObManager = TTAdManagerHolder.get();
                    //在合适的时机申请权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题
                    ttObManager.requestPermissionIfNecessary(context);
                    TTVfNative mTTAdNative = ttObManager.createVfNative(context);
                    //step3:创建广告请求参数AdSlot,具体参数含义参考文档
                    VfSlot adSlot = new VfSlot.Builder()
                            .setCodeId(adModel.getAdId())
                            .setSupportDeepLink(true)
                            .setImageAcceptedSize(1080, 1920)
                            .setAdCount(1) //请求广告数量为1到3条
                            .build();
                    //step4:请求广告,对请求回调的广告作渲染处理
                    mTTAdNative.loadDrawVfList(adSlot, new TTVfNative.DrawVfListListener() {
                        @Override
                        public void onError(int code, String message) {
                            notifyFail(new RuntimeException("load tt info flow video ad fail:" + code + ";" + message));
                        }

                        @Override
                        public void onDrawFeedAdLoad(List<TTDrawVfObject> ads) {
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
