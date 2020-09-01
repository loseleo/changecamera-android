package com.jifen.dandan.common.base.bean;

/**
 * Author：zhangqiang
 * Date：2019/1/5 15:21:18
 * Email:852286406@share_qq.com
 * Github:https://github.com/holleQiang
 */

public abstract class AbstractApiResult<T> implements IApiResult<T> {

    private int code = -1;
    private String message;
    private long currentTime;
    private T data;
    private String url;


    public int getCode() {
        return code;
    }

    public AbstractApiResult<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public AbstractApiResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public AbstractApiResult<T> setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
        return this;
    }

    public T getData() {
        return data;
    }

    public AbstractApiResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSuccess(){
        return code == 0;
    }

    @Override
    public long getServerTime() {
        return getCurrentTime();
    }
}
