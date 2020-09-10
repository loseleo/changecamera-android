package com.beige.camera.ringtone.Listener;

import android.support.annotation.Nullable;

public interface AdListener {
    /**
     * 广告点击
     */
    void onAdClick();

    /**
     * 广告展示
     */
    void onAdDisplay();

    /**
     * 广告加载失败
     */
    void onAdFailed(@Nullable Throwable e);
}