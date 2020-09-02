package com.jifen.dandan.ringtone.core.infoflow;

import android.widget.FrameLayout;

import com.jifen.dandan.common.feed.bean.AdModel;
import com.jifen.dandan.ringtone.core.infoflow.video.VideoInfoOwner;

import java.util.ArrayList;
import java.util.List;

public abstract class InfoFlowVideoAd<T extends VideoInfoOwner> extends InfoFlowAd<T> {

    private List<OnVideoPlayListener> onVideoPlayListeners = new ArrayList<>();

    public InfoFlowVideoAd(FrameLayout adContainer, AdModel adModel) {
        super(adContainer, adModel);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onVideoPlayListeners.clear();
    }

    public void addOnVideoPlayListener(OnVideoPlayListener onVideoPlayListener){
        if (onVideoPlayListeners == null) {
            onVideoPlayListeners = new ArrayList<>();
        }
        if (onVideoPlayListeners.contains(onVideoPlayListener)) {
            return;
        }
        onVideoPlayListeners.add(onVideoPlayListener);
    }

    public void removeOnVideoPlayListener(OnVideoPlayListener onVideoPlayListener){
        if (onVideoPlayListeners == null) {
            return;
        }
        onVideoPlayListeners.remove(onVideoPlayListener);
    }

    protected void notifyVideoLoaded(){
        for (int i = onVideoPlayListeners.size() - 1; i >= 0; i--) {
            onVideoPlayListeners.get(i).onVideoLoad();
        }
    }

    protected void notifyVideoError(Throwable e){
        for (int i = onVideoPlayListeners.size() - 1; i >= 0; i--) {
            onVideoPlayListeners.get(i).onVideoError(e);
        }
    }

    protected void notifyVideoStartPlay(){
        for (int i = onVideoPlayListeners.size() - 1; i >= 0; i--) {
            onVideoPlayListeners.get(i).onVideoStartPlay();
        }
    }

    protected void notifyVideoPaused(){
        for (int i = onVideoPlayListeners.size() - 1; i >= 0; i--) {
            onVideoPlayListeners.get(i).onVideoPaused();
        }
    }

    protected void notifyVideoContinuePlay(){
        for (int i = onVideoPlayListeners.size() - 1; i >= 0; i--) {
            onVideoPlayListeners.get(i).onVideoContinuePlay();
        }
    }

    protected void notifyVideoProgressUpdate(long max,long progress){
        for (int i = onVideoPlayListeners.size() - 1; i >= 0; i--) {
            onVideoPlayListeners.get(i).onProgressUpdate(max,progress);
        }
    }

    protected void notifyVideoComplete(){
        for (int i = onVideoPlayListeners.size() - 1; i >= 0; i--) {
            onVideoPlayListeners.get(i).onVideoComplete();
        }
    }

    @Override
    public T getAdResource() {
        return super.getAdResource();
    }
}
