package com.beige.camera.ringtone.api.bean;

import com.beige.camera.common.feed.bean.AdModel;

import java.util.List;

public class AdConfigBean extends AdModel{

    private List<AdModel> candidates;

    public List<AdModel> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<AdModel> candidates) {
        this.candidates = candidates;
    }
}
