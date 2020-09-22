package com.beige.camera.advertisement.core.strategy;

import com.beige.camera.advertisement.core.Ad;

public interface Callback<T extends Ad> {

    void onAdLoadStart(T ad);

    void onFail(Throwable e);
}
