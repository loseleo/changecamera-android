package com.jifen.dandan.common.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Author: maxiaoxun@qutoutiao.net
 * Time: 2019/9/2 11:38
 */
public class GlobalConfigModel implements Serializable {

    private boolean report; //开关，是否上报播放时长
    @SerializedName("report_interval")
    private long reportInterval; //	定时上报间隔
    @SerializedName("report_valid_duration")
    private long reportValidDuration;// 有效时长的阀值
    @SerializedName("coin_dtu")
    private String coinDtu;
    @SerializedName("feed_dtu")
    private String feedDtu;
    private String dtus;
    @SerializedName("index_url")
    private String indexUrl;
    @SerializedName("feed_type")
    private int feedType;
    @SerializedName("wallet_url")
    private String walletUrl;
    /**
     * 0 不显示 1 新版样式(金币特殊版) 2 旧版样式(金币普通版)
     */
    @SerializedName("reward_timer_type")
    private int rewardTimerType;
    @SerializedName("first_install_popup_enable")
    private boolean firstInstallPopupEnable;

    @SerializedName("first_install_popup_type")
    private int first_install_popup_type;

    @SerializedName("lock_config")
    private LockConfig mLockConfig;

    @SerializedName("privacy_version")
    private String privacyVersion;

    @SerializedName("live_enable")
    private boolean liveEnable; //开关，是否可以使用直播功能

    @SerializedName("avatar_animation_enable")
    private boolean avatarAnimationEnable;

    // 开关，是否可以使用h5离线化功能
    @SerializedName("h5_offline_enable")
    private boolean h5OfflineEnable;

    /**
     * 新用户是否开启开屏广告
     */
    @SerializedName("open_screen_adt_new_user_switch")
    private boolean openScreenAdtNewUserSwitch;

    /**
     * 新人红包奖励
     */
    @SerializedName("newer_reward")
    private String newerReward;

    /**
     * 静态资源文件/比如皮肤
     */
    @SerializedName("static_package_url")
    private String staticPackageUrl;

    @SerializedName("adv_timer_enable")
    private boolean advTimerEnable;
    //是否启用新播放器
    @SerializedName("new_player_enable")
    private boolean newPlayerEnable;

    // 举报缘由
    @SerializedName("feed_accuse_reasons")
    private List<String> feedAccuseReasons;

    @SerializedName("member_accuse_reasons")
    private List<String> memberAccuseReasons;

    @SerializedName("kol_url")
    private String kolUrl;

    @SerializedName("bottom_guide_enable")
    private boolean bottomGuideEnable;

    @SerializedName("sample_rate")
    private int sampleRate;

    @SerializedName("new_welfare_enable")
    private boolean newWelfareEnable;

    @SerializedName("coin_block")
    private boolean coinBlock;
    @SerializedName("bubble_guide_enable")
    private boolean bubbleGuideEnable;
    @SerializedName("medal_popup_enable")
    private boolean medalPopupEnable;

    @SerializedName("feedback_page_url")
    private String feedbackPageUrl;

    @SerializedName("message_page_url")
    private String messagePageUrl;

    @SerializedName("select_position")
    private String selectPosition;

    public String getPrivacyVersion() {
        return privacyVersion;
    }

    public void setPrivacyVersion(String privacyVersion) {
        this.privacyVersion = privacyVersion;
    }

    public boolean isReport() {
        return report;
    }

    public void setReport(boolean report) {
        this.report = report;
    }

    public String getDtus() {
        return dtus;
    }

    public void setDtus(String dtus) {
        this.dtus = dtus;
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public int getFeedType() {
        return feedType;
    }

    public void setFeedType(int feedType) {
        this.feedType = feedType;
    }

    public String getCoinDtu() {
        return coinDtu;
    }

    public GlobalConfigModel setCoinDtu(String coinDtu) {
        this.coinDtu = coinDtu;
        return this;
    }

    public String getFeedDtu() {
        return feedDtu;
    }

    public GlobalConfigModel setFeedDtu(String feedDtu) {
        this.feedDtu = feedDtu;
        return this;
    }

    public long getReportInterval() {
        return reportInterval;
    }

    public GlobalConfigModel setReportInterval(long reportInterval) {
        this.reportInterval = reportInterval;
        return this;
    }

    public long getReportValidDuration() {
        return reportValidDuration;
    }

    public GlobalConfigModel setReportValidDuration(long reportValidDuration) {
        this.reportValidDuration = reportValidDuration;
        return this;
    }

    public String getWalletUrl() {
        return walletUrl;
    }

    public GlobalConfigModel setWalletUrl(String walletUrl) {
        this.walletUrl = walletUrl;
        return this;
    }

    public int getRewardTimerType() {
        return rewardTimerType;
    }

    public GlobalConfigModel setRewardTimerType(int rewardTimerType) {
        this.rewardTimerType = rewardTimerType;
        return this;
    }

    public boolean isFirstInstallPopupEnable() {
        return firstInstallPopupEnable;
    }

    public GlobalConfigModel setFirstInstallPopupEnable(boolean firstInstallPopupEnable) {
        this.firstInstallPopupEnable = firstInstallPopupEnable;
        return this;
    }

    public int getFirst_install_popup_type() {
        return first_install_popup_type;
    }

    public void setFirst_install_popup_type(int first_install_popup_type) {
        this.first_install_popup_type = first_install_popup_type;
    }

    public LockConfig getmLockConfig() {
        return mLockConfig;
    }

    public void setmLockConfig(LockConfig mLockConfig) {
        this.mLockConfig = mLockConfig;
    }

    public boolean isLiveEnable() {
        return liveEnable;
    }

    public GlobalConfigModel setLiveEnable(boolean liveEnable) {
        this.liveEnable = liveEnable;
        return this;
    }

    public boolean isOpenScreenAdtNewUserSwitch() {
        return openScreenAdtNewUserSwitch;
    }

    public void setOpenScreenAdtNewUserSwitch(boolean openScreenAdtNewUserSwitch) {
        this.openScreenAdtNewUserSwitch = openScreenAdtNewUserSwitch;
    }

    public boolean isAvatarAnimationEnable() {
        return avatarAnimationEnable;
    }

    public GlobalConfigModel setAvatarAnimationEnable(boolean avatarAnimationEnable) {
        this.avatarAnimationEnable = avatarAnimationEnable;
        return this;
    }

    public boolean isH5OfflineEnable() {
        return h5OfflineEnable;
    }

    public GlobalConfigModel setH5OfflineEnable(boolean h5OfflineEnable) {
        this.h5OfflineEnable = h5OfflineEnable;
        return this;
    }

    public String getNewerReward() {
        return newerReward;
    }

    public void setNewerReward(String newerReward) {
        this.newerReward = newerReward;
    }

    public String getStaticPackageUrl() {
        return staticPackageUrl;
    }

    public GlobalConfigModel setStaticPackageUrl(String staticPackageUrl) {
        this.staticPackageUrl = staticPackageUrl;
        return this;
    }

    public boolean isAdvTimerEnable() {
        return advTimerEnable;
    }

    public void setAdvTimerEnable(boolean advTimerEnable) {
        this.advTimerEnable = advTimerEnable;
    }

    public boolean isNewPlayerEnable() {
        return newPlayerEnable;
    }

    public void setNewPlayerEnable(boolean newPlayerEnable) {
        this.newPlayerEnable = newPlayerEnable;
    }

    public List<String> getFeedAccuseReasons() {
        return feedAccuseReasons;
    }

    public void setFeedAccuseReasons(List<String> feedAccuseReasons) {
        this.feedAccuseReasons = feedAccuseReasons;
    }

    public String getKolUrl() {
        return kolUrl;
    }

    public void setKolUrl(String kolUrl) {
        this.kolUrl = kolUrl;
    }

    public List<String> getMemberAccuseReasons() {
        return memberAccuseReasons;
    }

    public GlobalConfigModel setMemberAccuseReasons(List<String> memberAccuseReasons) {
        this.memberAccuseReasons = memberAccuseReasons;
        return this;
    }

    public boolean isBottomGuideEnable() {
        return bottomGuideEnable;
    }

    public GlobalConfigModel setBottomGuideEnable(boolean bottomGuideEnable) {
        this.bottomGuideEnable = bottomGuideEnable;
        return this;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public boolean isNewWelfareEnable() {
        return newWelfareEnable;
    }

    public void setNewWelfareEnable(boolean newWelfareWnable) {
        this.newWelfareEnable = newWelfareWnable;
    }

    public boolean isCoinBlock() {
        return coinBlock;
    }

    public void setCoinBlock(boolean coinBlock) {
        this.coinBlock = coinBlock;
    }

    public boolean isBubbleGuideEnable() {
        return bubbleGuideEnable;
    }

    public void setBubbleGuideEnable(boolean bubbleGuideEnable) {
        this.bubbleGuideEnable = bubbleGuideEnable;
    }

    public boolean isMedalPopupEnable() {
        return medalPopupEnable;
    }

    public void setMedalPopupEnable(boolean medalPopupEnable) {
        this.medalPopupEnable = medalPopupEnable;
    }

    public String getFeedbackPageUrl() {
        return feedbackPageUrl;
    }

    public void setFeedbackPageUrl(String feedbackPageUrl) {
        this.feedbackPageUrl = feedbackPageUrl;
    }

    public String getMessagePageUrl() {
        return messagePageUrl;
    }

    public void setMessagePageUrl(String messagePageUrl) {
        this.messagePageUrl = messagePageUrl;
    }

    public String getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(String selectPosition) {
        this.selectPosition = selectPosition;
    }
}
