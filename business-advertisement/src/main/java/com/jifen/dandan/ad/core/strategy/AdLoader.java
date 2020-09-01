package com.jifen.dandan.ad.core.strategy;

import com.jifen.dandan.ad.core.Ad;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class AdLoader<T extends Ad> {

    public static final int STATE_IDLE = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_LOADED = 2;
    public static final int STATE_FAIL = 3;
    private AtomicInteger mState = new AtomicInteger(STATE_IDLE);
    private AtomicBoolean aborted = new AtomicBoolean(false);
    private T loadedAd;
    private T loadingAd;
    private List<T> ads;
    private Callback<T> callback;

    public AdLoader(List<T> ads) {
        this.ads = ads;
    }

    public void load(){
        reset();
        mState.set(STATE_LOADING);
        loadAdOrderly(0,ads,callback);
    }

    public void preload(){
        reset();
        mState.set(STATE_LOADING);
        preloadAdOrderly(0,ads,callback);
    }

    private void loadAdOrderly(int index, List<T> ads, Callback<T> callback) {

        T t = configOrderly(index, ads, callback, new Runnable() {
            @Override
            public void run() {
                loadAdOrderly(index + 1, ads, callback);
            }
        });
        if (t != null) {
            t.load();
        }
    }

    private void preloadAdOrderly(int index, List<T> ads, Callback<T> callback) {

        T t = configOrderly(index, ads, callback, new Runnable() {
            @Override
            public void run() {
                preloadAdOrderly(index + 1, ads, callback);
            }
        });
        if (t != null) {
            t.preload();
        }
    }

    private T configOrderly(int index, List<T> ads, Callback<T> callback,Runnable nextCall) {
        if (index >= ads.size()) {
            mState.set(STATE_FAIL);
            if (callback != null) {
                callback.onFail(new RuntimeException("all ad failed"));
            }
            return null;
        }

        T t = ads.get(index);
        loadingAd = t;
        if (callback != null) {
            callback.onAdLoadStart(t);
        }
        t.addAdListener(new Ad.AdListener() {
            //防止部分sdk多次回调错误影响逻辑
            boolean hasAdFailCalled = false;
            boolean adLoaded = false;
            @Override
            public void onAdLoaded() {
                loadedAd = t;
                loadingAd = null;
                adLoaded = true;
                mState.set(STATE_LOADED);
            }

            @Override
            public void onAdDisplay() {

            }

            @Override
            public void onAdFail(Throwable e) {
                if (hasAdFailCalled) {
                    return;
                }
                hasAdFailCalled = true;
                loadingAd = null;

                if (aborted.get()) {
                    return;
                }
                //防止load成功之后因为其他原因的失败造成重试
                if (adLoaded) {
                    return;
                }
                if (nextCall != null) {
                    nextCall.run();
                }

            }

            @Override
            public void onAdClick() {
            }
        });
        return t;
    }

    public void setCallback(Callback<T> callback) {
        this.callback = callback;
    }

    public int getState(){
        return mState.get();
    }

    public T getLoadedAd() {
        return loadedAd;
    }

    public void abort(){
        aborted.set(true);
        if (loadingAd != null) {
            loadingAd.destroy();
            loadingAd = null;
        }
    }

    private void reset(){
        aborted.set(false);
        mState.set(STATE_IDLE);
        loadedAd = null;
        loadingAd = null;
    }
}
