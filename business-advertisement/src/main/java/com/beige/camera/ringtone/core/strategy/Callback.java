package com.beige.camera.ringtone.core.strategy;

import com.beige.camera.ringtone.core.Ad;

public interface Callback<T extends Ad> {

    void onAdLoadStart(T ad);

    void onFail(Throwable e);
}
