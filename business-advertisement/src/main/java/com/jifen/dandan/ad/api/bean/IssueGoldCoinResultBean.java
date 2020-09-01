package com.jifen.dandan.ad.api.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 发放金币结果
 */
public class IssueGoldCoinResultBean {

    @SerializedName("desc_result")
    private String descResult;

    public String getDescResult() {
        return descResult;
    }

    public void setDescResult(String descResult) {
        this.descResult = descResult;
    }
}
