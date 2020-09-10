package com.beige.camera.ringtone.core.splash;

import android.view.ViewGroup;

import com.beige.camera.common.feed.bean.AdModel;
import com.beige.camera.ringtone.core.Ad;

import java.util.ArrayList;
import java.util.List;

public abstract class SplashAd<T> extends Ad<T> {


    private ViewGroup adContainer;
    private final List<OnAdSkipListener> onAdSkipListeners = new ArrayList<>();
    private final List<OnAdTimeOverListener> onAdTimeOverListeners = new ArrayList<>();

    public SplashAd(AdModel adModel,ViewGroup adContainer) {
        super(adModel);
        this.adContainer = adContainer;
    }


    public void addOnAdSkipListener(OnAdSkipListener onAdSkipListener) {
        if (onAdSkipListeners.contains(onAdSkipListener)) {
            return;
        }
        onAdSkipListeners.add(onAdSkipListener);
    }

    public void removeOnAdSkipListener(OnAdSkipListener onAdSkipListener) {
        onAdSkipListeners.remove(onAdSkipListener);
    }

    protected void notifyAdSkip() {
        for (int i = onAdSkipListeners.size() - 1; i >= 0; i--) {
            onAdSkipListeners.get(i).onSkip();
        }
    }

    public void addOnAdTimeOverListener(OnAdTimeOverListener onAdTimeOverListener) {
        if (onAdTimeOverListeners.contains(onAdTimeOverListener)) {
            return;
        }
        onAdTimeOverListeners.add(onAdTimeOverListener);
    }

    public void removeOnAdTimeOverListener(OnAdTimeOverListener onAdTimeOverListener) {
        onAdTimeOverListeners.remove(onAdTimeOverListener);
    }

    protected void notifyAdTimeOver() {
        for (int i = onAdTimeOverListeners.size() - 1; i >= 0; i--) {
            onAdTimeOverListeners.get(i).onAdTimeOver();
        }
    }

    public ViewGroup getAdContainer() {
        return adContainer;
    }
}
