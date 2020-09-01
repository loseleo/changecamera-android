package com.jifen.dandan.common.feed.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: maxiaoxun@qutoutiao.net
 * Time: 2019/9/6 21:12
 */
public class AdModel implements Serializable {

    public static final String AD_CHANNEL_CPC = "cpc";
    public static final String AD_CHANNEL_TOUTIAO = "toutiao";
    public static final String AD_CHANNEL_GDT = "gdt";
    public static final String AD_CHANNEL_CPC_RTB = "cpc_rtb";
    /**
     * 互动广告
     */
    public static final String AD_CHANNEL_CPC_CA = "cpc_ca";

    @SerializedName("ad_code")
    private String adCode; //广告位

    @SerializedName("ad_id")
    private String adId; //广告位id

    @SerializedName("ad_channel")
    private String adChannel; //广告渠道 cpc/toutiao

    @SerializedName("ad_unique_slot_id")
    private String adSlotid; //物理id

    @SerializedName("width")
    private int width;

    @SerializedName("height")
    private int height;
    /**
     * 开屏广告出现的冷却时间
     */
    @SerializedName("cool_down")
    private int coolDown;
    /**
     * 开屏广告倒计时
     */
    @SerializedName("count_down")
    private int countDown;
    /**
     * 0 广告 1 原生 2 webView
     */
    private int action;
    /**
     * 开屏广告图片地址
     */
    @SerializedName("image_url")
    private String imageUrl;
    /**
     * 互动广告标题
     */
    private String title;
    /**
     * 开屏广告跳转链接
     */
    @SerializedName("location")
    private String location;
    /**
     * 是否使用个性化金币(1 使用)
     */
    @SerializedName("smart_coin_enable")
    private int smartCoinEnable;

    /**
     * 唤起广告发放金币系数
     */
    private int percent;

    private Map<String, String> extras;

    public void putExtra(String key, String value) {
        if (extras == null) {
            extras = new HashMap<>();
        }
        extras.put(key, value);
    }

    public String getExtra(String key) {
        if (extras == null) {
            return null;
        }
        return extras.get(key);
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getAdChannel() {
        return adChannel;
    }

    public void setAdChannel(String adChannel) {
        this.adChannel = adChannel;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getCoolDown() {
        return coolDown;
    }

    public void setCoolDown(int coolDown) {
        this.coolDown = coolDown;
    }

    public int getCountDown() {
        return countDown;
    }

    public void setCountDown(int countDown) {
        this.countDown = countDown;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getSmartCoinEnable() {
        return smartCoinEnable;
    }

    public void setSmartCoinEnable(int smartCoinEnable) {
        this.smartCoinEnable = smartCoinEnable;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdSlotid() {
        return adSlotid;
    }

    public void setAdSlotid(String adSlotid) {
        this.adSlotid = adSlotid;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }


}
