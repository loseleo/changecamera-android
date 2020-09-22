package com.beige.camera.advertisement.core.infoflow.loader.res;

import android.graphics.Bitmap;

import com.beige.camera.advertisement.core.infoflow.BaseInfoFlowVideoAd;
import com.bytedance.sdk.openadsdk.TTDrawFeedAd;

public class TTInfoFlowVideo implements BaseInfoFlowVideoAd.InfoFlowVideoInfoOwner {

    private TTDrawFeedAd ttDrawFeedOb;

    public TTInfoFlowVideo(TTDrawFeedAd ttDrawFeedOb) {
        this.ttDrawFeedOb = ttDrawFeedOb;
    }

    @Override
    public long getVideoDuration() {
        return (long) (ttDrawFeedOb.getVideoDuration() * 1000);
    }

    public TTDrawFeedAd getTTDrawFeedOb() {
        return ttDrawFeedOb;
    }

    @Override
    public String getTitle() {
        return ttDrawFeedOb.getTitle();
    }

    @Override
    public CharSequence getDescription() {
        return ttDrawFeedOb.getDescription();
    }

    @Override
    public int getAppScore() {
        return ttDrawFeedOb.getAppScore();
    }

    @Override
    public String getIconUrl() {
        return ttDrawFeedOb.getIcon().getImageUrl();
    }

    @Override
    public Bitmap getLogo() {
        return ttDrawFeedOb.getAdLogo();
    }
}

