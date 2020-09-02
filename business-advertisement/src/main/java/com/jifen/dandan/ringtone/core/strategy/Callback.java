package com.jifen.dandan.ringtone.core.strategy;

import com.jifen.dandan.ringtone.core.Ad;

public interface Callback<T extends Ad> {

    void onAdLoadStart(T ad);

    void onFail(Throwable e);
}
