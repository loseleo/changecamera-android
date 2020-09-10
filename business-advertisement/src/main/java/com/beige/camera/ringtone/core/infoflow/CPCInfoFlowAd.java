package com.beige.camera.ringtone.core.infoflow;

import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.iclicash.advlib.__remote__.framework.videoplayer.MediaStateChangeListener;
import com.iclicash.advlib.core.ICliBundle;
import com.iclicash.advlib.ui.adapter.PlayerDeckAdapter;
import com.iclicash.advlib.ui.banner.ADBanner;
import com.beige.camera.ringtone.core.goldcoin.GoldCoinOwner;
import com.beige.camera.ringtone.core.goldcoin.GoldCoinUtils;
import com.beige.camera.ringtone.core.infoflow.video.VideoInfoOwner;
import com.beige.camera.ringtone.core.loader.ResourceLoader;
import com.beige.camera.common.feed.bean.AdModel;
import com.beige.camera.ringtone.core.infoflow.loader.CPCInfoFlowResourceLoader;
import com.beige.camera.ringtone.core.infoflow.loader.res.CPCResource;

public class CPCInfoFlowAd extends InfoFlowVideoAd<CPCInfoFlowAd.CPCInfoFlowResource> {


    private MediaStateChangeListener mediaStateChangeListener;
    private PlayerDeckAdapter playerDeckAdapter;

    public CPCInfoFlowAd(FrameLayout adContainer, AdModel adModel) {
        super(adContainer, adModel);
    }


    @NonNull
    @Override
    protected ResourceLoader<CPCInfoFlowResource> onCreateResourceLoader(AdModel adModel) {
        return new CPCInfoFlowResourceLoader<CPCInfoFlowResource>(adModel, getAdContainer().getContext()) {
            @NonNull
            @Override
            protected CPCInfoFlowResource convert(ICliBundle iCliBundle) {
                return new CPCInfoFlowResource(iCliBundle);
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (playerDeckAdapter != null && mediaStateChangeListener != null) {
            playerDeckAdapter.removeMediaStateChangeListener(mediaStateChangeListener);
        }
        mediaStateChangeListener = null;
        playerDeckAdapter = null;
        getAdContainer().removeAllViews();
    }

    @Override
    protected void onSetupAdResource(FrameLayout adContainer, CPCInfoFlowResource resource) {
        ADBanner mADBanner = new ADBanner(adContainer.getContext()) {

            float lastX, lastY;
            int touchSlop = -1;
            boolean dragged;

            @Override
            public boolean dispatchTouchEvent(MotionEvent event) {
                if (touchSlop == -1) {
                    touchSlop = ViewConfiguration.get(adContainer.getContext()).getScaledTouchSlop();
                }
                int action = event.getAction();
                float currX = event.getX();
                float currY = event.getY();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = currX;
                        lastY = currY;
                        dragged = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dx = currX - lastX;
                        float dy = currY - lastY;
                        float xDiff = Math.abs(dx);
                        float yDiff = Math.abs(dy);
                        if (!dragged) {
                            if (Math.abs(xDiff) > touchSlop || yDiff > touchSlop) {
                                dragged = true;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!dragged) {
                            notifyAdClick();
                        }
                        break;
                }
                return super.dispatchTouchEvent(event);
            }
        };
        mADBanner.UpdateView(resource.getiCliBundle());
        playerDeckAdapter = new PlayerDeckAdapter(mADBanner, true);
        mediaStateChangeListener = new MediaStateChangeListener() {

            @Override
            public void onPlayintStateChanged(int state, long progress) {
                if (state == MediaStateChangeListener.STATUS_START) {
                    notifyVideoStartPlay();
                } else if (state == MediaStateChangeListener.STATUS_PAUSE
                        || state == MediaStateChangeListener.STATUS_STOP) {
                    notifyVideoPaused();
                } else if (state == MediaStateChangeListener.STATUS_RESUME) {
                    notifyVideoContinuePlay();
                } else if (state == MediaStateChangeListener.STATUS_FINISH) {
                    notifyVideoComplete();
                } else if (state == MediaStateChangeListener.STATUS_PLAYING) {
                    notifyVideoProgressUpdate(-1, progress);
                } else if (state == MediaStateChangeListener.STATUS_PREPARE) {
                    notifyVideoLoaded();
                }
            }
        };
        playerDeckAdapter.addMediaStateChangeListener(mediaStateChangeListener);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mADBanner.setLayoutParams(layoutParams);
        adContainer.removeAllViews();
        adContainer.addView(mADBanner);
        notifyAdDisplay();
    }

    public static class CPCInfoFlowResource extends CPCResource implements VideoInfoOwner, GoldCoinOwner {


        CPCInfoFlowResource(ICliBundle iCliBundle) {
            super(iCliBundle);
        }

        @Override
        public long getVideoDuration() {
            return getiCliBundle().video_duration * 1000;
        }

        @Override
        public int getGoldCoin() {
            return GoldCoinUtils.getGoldCoin(getiCliBundle());
        }
    }
}
