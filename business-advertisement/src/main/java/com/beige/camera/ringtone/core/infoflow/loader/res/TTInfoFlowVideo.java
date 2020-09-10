package com.beige.camera.ringtone.core.infoflow.loader.res;

import android.graphics.Bitmap;

import com.bykv.vk.openvk.TTDrawVfObject;
import com.beige.camera.ringtone.core.infoflow.BaseInfoFlowVideoAd;

public class TTInfoFlowVideo implements BaseInfoFlowVideoAd.InfoFlowVideoInfoOwner {

    private TTDrawVfObject ttDrawFeedOb;

    public TTInfoFlowVideo(TTDrawVfObject ttDrawFeedOb) {
        this.ttDrawFeedOb = ttDrawFeedOb;
    }

    @Override
    public long getVideoDuration() {
        return (long) (ttDrawFeedOb.getVideoDuration() * 1000);
    }

    public TTDrawVfObject getTTDrawFeedOb() {
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
        return ttDrawFeedOb.getLogo();
    }
}

