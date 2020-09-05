package com.jifen.dandan.ringtone.manager;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

public class RingVideoManager {

    private PlayerAttachListManager playerAttachListManager;
    private PlayerConfig playerConfig;
    private ViewGroup mParentView;


    private RingVideoManager() {

    }

    private static class RingVideoManagerHolder {
        private static final RingVideoManager INSTANCE = new RingVideoManager();
    }

    public static RingVideoManager getInstance() {
        return RingVideoManagerHolder.INSTANCE;
    }

    private void initView(Context context, ViewGroup parentView) {
        if (playerAttachListManager == null) {
            playerAttachListManager = new PlayerAttachListManager(context);
        }
        if (playerConfig == null) {
            playerConfig = new PlayerConfig.Builder()
                    .setLooping()
                    .setAspectRatio(IRenderView.AR_ASPECT_FILL_PARENT)
                    .isDebug(App.isDebug())
                    .disableAudioFocus()
                    .enableWatchTime()
                    .setRecordPlayingStateEnable(false)
                    .build();
        }
        this.mParentView = parentView;
    }


    public void playVideo(Context context, String url, ViewGroup parentView) {
        initView(context, parentView);
        playVideo(url);
    }

    public void playVideo(String url) {
        if (!haveAttachVideo() && !TextUtils.isEmpty(url)) {
            playerAttachListManager.playerConfig(playerConfig);
            playerAttachListManager.changToNewUri(VideoUrlUtils.convertLocalUrl(url));
            playerAttachListManager.attachView(mParentView);
            playerAttachListManager.addMediaPlayerListener(new SimpleMediaPlayerListener() {
                @Override
                public void onFirstFrameStart() {
                    super.onFirstFrameStart();
                    playerAttachListManager.setMute(true);
                }
            });
            playerAttachListManager.setMute(true);
            playerAttachListManager.go();
        }
    }

    public boolean isPlaying() {
        return playerAttachListManager == null ? false : (playerAttachListManager.isPlaying() || playerAttachListManager.isPrepared());
    }

    public void release() {
        if (playerAttachListManager != null) {
            playerAttachListManager.stop();
            playerAttachListManager.releaseAllVideo();
            playerAttachListManager.release();
            playerAttachListManager.destroy();
            if (mParentView != null) {
                mParentView.setVisibility(View.GONE);
            }
        }
    }

    public boolean haveAttachVideo() {
        boolean ret = false;
        if (mParentView != null) {
            int viewChildSize = mParentView.getChildCount();
            for (int i = 0; i < viewChildSize; i++) {
                View childView = mParentView.getChildAt(i);
                if (childView instanceof QkVideoView) {
                    ret = true;
                    break;
                }
            }
        }
        return ret;
    }
}
