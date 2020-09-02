package com.jifen.dandan.ringtone.core.strategy;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import com.jifen.dandan.ringtone.core.Ad;

public class CallbackWrapper<T extends Ad> implements Callback<T> {

    @Nullable
    private Callback<T> wrapper;

    public CallbackWrapper(@Nullable Callback<T> wrapper) {
        this.wrapper = wrapper;
    }

    @CallSuper
    @Override
    public void onAdLoadStart(T ad) {
        if (wrapper != null) {
            wrapper.onAdLoadStart(ad);
        }
    }

    @Override
    public void onFail(Throwable e) {
        if (wrapper != null) {
            wrapper.onFail(e);
        }
    }

}
