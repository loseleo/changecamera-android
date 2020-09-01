package com.jifen.dandan.ad.core.infoflow;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jifen.dandan.common.feed.bean.AdModel;
import com.jifen.dandan.ad.R;
import com.jifen.dandan.ad.core.infoflow.loader.GDTInfoFlowVideoLoader;
import com.jifen.dandan.ad.core.infoflow.loader.res.GDTInfoFlowVideo;
import com.jifen.dandan.ad.core.loader.ResourceLoader;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.MediaView;
import com.qq.e.ads.nativ.NativeADEventListener;
import com.qq.e.ads.nativ.NativeADMediaListener;
import com.qq.e.ads.nativ.NativeUnifiedADData;
import com.qq.e.ads.nativ.widget.NativeAdContainer;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.util.AdError;

import java.util.ArrayList;
import java.util.List;

public class GDTInfoFlowVideoAd extends BaseInfoFlowVideoAd<GDTInfoFlowVideo> {

    private static final String TAG = "GDTInfoFlowVideoAd";

    public GDTInfoFlowVideoAd(FrameLayout adContainer, AdModel adModel) {
        super(adContainer, adModel);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.ad_feed_video_gdt;
    }

    @Override
    void onSetupAdResource(GDTInfoFlowVideo gdtInfoFlowVideo, AdViewHolder adViewHolder) {
        setDownloadState(R.string.ad_look_details, -1, adViewHolder.tvDownload, adViewHolder.tvDialogDownload);

        NativeUnifiedADData ad = gdtInfoFlowVideo.getNativeUnifiedADData();
        ad.setVideoMute(false);

        Activity activity = (Activity) adViewHolder.adView.getContext();
        Context appContext = activity.getApplicationContext();

        ImageView ivCover = adViewHolder.findViewById(R.id.iv_cover);
        Glide.with(appContext).asDrawable().load(ad.getImgUrl()).into(ivCover);

        ImageView ivBg = adViewHolder.findViewById(R.id.iv_bg);
//        ivBg.setImage(ad.getImgUrl());

        MediaView mediaView = adViewHolder.findViewById(R.id.ad_view_gdt_media);
        if (ad.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
            ivCover.setVisibility(View.INVISIBLE);
            mediaView.setVisibility(View.VISIBLE);
        } else {
            ivCover.setVisibility(View.VISIBLE);
            mediaView.setVisibility(View.INVISIBLE);
        }

        NativeAdContainer nativeAdContainer = adViewHolder.findViewById(R.id.ad_gdt_container);
        List<View> creativeViews = new ArrayList<>();
        creativeViews.add(adViewHolder.btDownload);
        creativeViews.add(adViewHolder.btDialogDownload);
        creativeViews.add(adViewHolder.findViewById(R.id.view_click));
        // 将布局与广告进行绑定
        ad.bindAdToView(activity, nativeAdContainer, null, creativeViews);
        // 设置广告事件监听
        ad.setNativeAdEventListener(new NativeADEventListener() {
            @Override
            public void onADExposed() {
                Log.d(TAG, "广告曝光");
                notifyAdDisplay();
            }

            @Override
            public void onADClicked() {
                Log.d(TAG, "广告被点击");
                notifyAdClick();
            }

            @Override
            public void onADError(AdError error) {
                Log.d(TAG, "错误回调 error code :" + error.getErrorCode()
                        + "  error msg: " + error.getErrorMsg());
                notifyAdFail(new RuntimeException("错误回调 error code :" + error.getErrorCode()
                        + "  error msg: " + error.getErrorMsg()));
            }

            @Override
            public void onADStatusChanged() {
                Log.d(TAG, "广告状态变化");
                renderDownloadStatus(ad, adViewHolder);
            }
        });
        if (ad.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
            // 视频广告需对MediaView进行绑定，MediaView必须为容器mContainer的子View
            ad.bindMediaView(mediaView, new VideoOption.Builder()
                            .setAutoPlayMuted(true).setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI).build(),
                    // 视频相关回调
                    new NativeADMediaListener() {
                        @Override
                        public void onVideoInit() {
                            Log.d(TAG, "onVideoInit: ");
                        }

                        @Override
                        public void onVideoLoading() {
                            Log.d(TAG, "onVideoLoading: ");
                        }

                        @Override
                        public void onVideoReady() {
                            Log.d(TAG, "onVideoReady: ");
                        }

                        @Override
                        public void onVideoLoaded(int videoDuration) {
                            Log.d(TAG, "onVideoLoaded: ");
                            notifyVideoLoaded();
                        }

                        @Override
                        public void onVideoStart() {
                            Log.d(TAG, "onVideoStart: ");
                            notifyVideoStartPlay();
                        }

                        @Override
                        public void onVideoPause() {
                            Log.d(TAG, "onVideoPause: ");
                            notifyVideoPaused();
                        }

                        @Override
                        public void onVideoResume() {
                            Log.d(TAG, "onVideoResume: ");
                            notifyVideoContinuePlay();
                        }

                        @Override
                        public void onVideoCompleted() {
                            Log.d(TAG, "onVideoCompleted: ");
                            notifyVideoComplete();
                        }

                        @Override
                        public void onVideoError(AdError error) {
                            Log.d(TAG, "onVideoError: ");
                            notifyVideoError(new RuntimeException(error.getErrorCode() + ";" + error.getErrorMsg()));
                        }

                        @Override
                        public void onVideoStop() {

                        }

                        @Override
                        public void onVideoClicked() {

                        }
                    });
        }
    }

    private void renderDownloadStatus(NativeUnifiedADData ad, AdViewHolder adViewHolder) {

        if (!ad.isAppAd()) {
            setDownloadState("浏览", -1, adViewHolder.tvDownload, adViewHolder.tvDialogDownload);
            return;
        }
        switch (ad.getAppStatus()) {
            case 0:
                adViewHolder.btDownload.setProgress(100);
                adViewHolder.btDialogDownload.setProgress(100);
                setDownloadState(R.string.ad_download_right_now, R.mipmap.ad_btn_state_download, adViewHolder.tvDownload, adViewHolder.tvDialogDownload);
                break;
            case 1:
                adViewHolder.btDownload.setProgress(100);
                adViewHolder.btDialogDownload.setProgress(100);
                setDownloadState(R.string.ad_open_app, R.mipmap.ad_btn_state_open_app, adViewHolder.tvDownload, adViewHolder.tvDialogDownload);
                break;
            case 2:
                adViewHolder.btDownload.setProgress(100);
                adViewHolder.btDialogDownload.setProgress(100);
                setDownloadState(R.string.ad_update_app, R.mipmap.ad_btn_state_open_app, adViewHolder.tvDownload, adViewHolder.tvDialogDownload);
                break;
            case 4:
                int progress = ad.getProgress();
                adViewHolder.btDownload.setProgress(progress);
                adViewHolder.btDialogDownload.setProgress(progress);
                String progressText = String.format(adViewHolder.tvDownload.getResources().getString(R.string.ad_downloading), progress);
                setDownloadState(progressText, -1, adViewHolder.tvDownload, adViewHolder.tvDialogDownload);
                break;
            case 8:
                adViewHolder.btDownload.setProgress(100);
                adViewHolder.btDialogDownload.setProgress(100);
                setDownloadState(R.string.ad_install_right_now, R.mipmap.ad_btn_state_install, adViewHolder.tvDownload, adViewHolder.tvDialogDownload);
                break;
            case 16:
                setDownloadState(R.string.ad_download_again, R.mipmap.ad_btn_state_download, adViewHolder.tvDownload, adViewHolder.tvDialogDownload);
                break;
            default:
                setDownloadState(R.string.ad_look_details, -1, adViewHolder.tvDownload, adViewHolder.tvDialogDownload);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GDTInfoFlowVideo resource = getAdResource();
        if (resource != null) {
            resource.getNativeUnifiedADData().resume();
            resource.getNativeUnifiedADData().resumeVideo();
        }
    }

    @NonNull
    @Override
    protected ResourceLoader<GDTInfoFlowVideo> onCreateResourceLoader(AdModel adModel) {
        return new GDTInfoFlowVideoLoader(adModel, getAdContainer().getContext().getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GDTInfoFlowVideo resource = getAdResource();
        if (resource != null) {
            resource.getNativeUnifiedADData().destroy();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        GDTInfoFlowVideo resource = getAdResource();
        if (resource != null) {
            resource.getNativeUnifiedADData().pauseVideo();
        }
    }
}
