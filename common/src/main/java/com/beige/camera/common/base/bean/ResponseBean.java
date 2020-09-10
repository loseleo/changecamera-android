package com.beige.camera.common.base.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author wuyufeng 2019/10/08.
 */
public class ResponseBean<T> extends BaseResponseBean implements IApiResult<T> {

    @SerializedName("data")
    T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public long getServerTime() {
        return getCurrentTime();
    }

}
