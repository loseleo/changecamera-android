package com.jifen.dandan.ad.core.loader;

import com.jifen.dandan.common.feed.bean.AdModel;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class ResourceLoader<T> {

    public static final int STATE_IDLE = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_SUCCESS = 2;
    public static final int STATE_FAIL = 3;
    private AtomicInteger mState = new AtomicInteger(STATE_IDLE);
    private Callback<T> callback;
    private AdModel adModel;
    private T resource;

    public ResourceLoader(AdModel adModel) {
        this.adModel = adModel;
    }

    public void load() {
        release();
        mState.set(STATE_LOADING);
        try {
            onLoad(adModel);
        }catch (Throwable e){
            notifyFail(e);
        }
    }

    protected abstract void onLoad(AdModel adModel);

    public void setCallback(Callback<T> callback) {
        this.callback = callback;
    }

    protected void notifySuccess(T t) {
        resource = t;
        mState.set(STATE_SUCCESS);
        if (callback != null) {
            callback.onSuccess(t);
        }
    }

    protected void notifyFail(Throwable e) {
        mState.set(STATE_FAIL);
        if (callback != null) {
            callback.onFail(e);
        }
    }

    public int getState() {
        return mState.get();
    }

    public T getResource() {
        return resource;
    }

    public void release(){
        resource = null;
        mState.set(STATE_IDLE);
        onRelease();
    }

    protected void onRelease() {

    }

    public AdModel getAdModel() {
        return adModel;
    }
}
