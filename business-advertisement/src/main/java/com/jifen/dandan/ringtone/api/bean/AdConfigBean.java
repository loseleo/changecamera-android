package com.jifen.dandan.ringtone.api.bean;

import com.jifen.dandan.common.feed.bean.AdModel;

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
