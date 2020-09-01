package com.jifen.dandan.ad.core.strategy;

import com.jifen.dandan.ad.core.Ad;

public interface Callback<T extends Ad> {

    void onAdLoadStart(T ad);

    void onFail(Throwable e);
}
