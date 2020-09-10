package com.beige.camera.common.base.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Author: maxiaoxun@qutoutiao.net
 * Time: 2019/8/26 19:15
 */
public class BaseResponseBean implements Serializable {

    @SerializedName("code")
    int code = -1;
    @SerializedName("message")
    String message;

    // 和后端同学确认，字段是current_time，若为currentTime则是误写
    @SerializedName(value = "current_time", alternate = "currentTime")
    long currentTime;

    public int getCode() {
        return code;
    }

    public BaseResponseBean setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public BaseResponseBean setMessage(String message) {
        this.message = message;
        return this;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public BaseResponseBean setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
        return this;
    }

    public boolean isSuccess() {
        return code == 0;
    }

    @Override
    public String toString() {
        return "BaseResponseBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", currentTime=" + currentTime +
                '}';
    }
}
