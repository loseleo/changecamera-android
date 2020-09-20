package com.beige.camera.bean;

import java.io.Serializable;

public class VersionInfoBean implements Serializable {

    /**
     * 版本号
     */
    private String versionName;
    /**
     * 版本编号
     */
    private int versionCode;
    /**
     * 版本描述
     */
    private String versionMemo;
    /**
     * 下载地址
     */
    private String url;

    private String forceUpdate ;//0是 不强制更新 1是强制更新
    private String title="发现新版本";
    private String btnMemo="立即更新";


    public String getVersion() {
        return versionName;
    }

    public void setVersion(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionMemo() {
        return versionMemo;
    }

    public void setVersionMemo(String versionMemo) {
        this.versionMemo = versionMemo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(String forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBtnMemo() {
        return btnMemo;
    }

    public void setBtnMemo(String btnMemo) {
        this.btnMemo = btnMemo;
    }

}
