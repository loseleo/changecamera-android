package com.jifen.dandan.ad.core.infoflow;

public interface OnVideoPlayListener {

    void onVideoLoad();

    void onVideoError(Throwable e);

    void onVideoStartPlay();

    void onVideoPaused();

    void onVideoContinuePlay();

    void onProgressUpdate(long max, long progress);

    void onVideoComplete();
}
