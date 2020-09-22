package com.beige.camera.advertisement.core;

import android.support.annotation.NonNull;

import com.beige.camera.common.feed.bean.AdModel;
import com.beige.camera.advertisement.core.loader.Callback;
import com.beige.camera.advertisement.core.loader.ResourceLoader;
import com.beige.camera.common.utils.ThreadUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class Ad<T> {

    private final List<AdListener> adListeners = new ArrayList<>();
    private ResourceLoader<T> resourceLoader;
    private boolean hasPendingLoad = false;
    private boolean destroyed = false;

    private AdModel adModel;

    public Ad(AdModel adModel) {
        this.adModel = adModel;
    }

    @NonNull
    protected abstract ResourceLoader<T> onCreateResourceLoader(AdModel adModel);

    public void preload() {
        destroyed = false;
        int state = getResourceLoader().getState();
        if (state == ResourceLoader.STATE_SUCCESS || state == ResourceLoader.STATE_LOADING) {
            return;
        }
        getResourceLoader().load();
    }

    public void load() {
        destroyed = false;
        int state = getResourceLoader().getState();
        if (state == ResourceLoader.STATE_SUCCESS) {
            hasPendingLoad = false;
            setupAdResource(getResourceLoader().getResource());
            return;
        }
        hasPendingLoad = true;
        getResourceLoader().load();
    }

    private void setupAdResource(T resource) {
        ThreadUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isDestroyed()) {
                        return;
                    }
                    onSetupAdResource(resource);
                }catch (Throwable e){
                    notifyAdFail(new RuntimeException("render ad fail : " + e.getMessage()));
                }
            }
        });
    }

    protected abstract void onSetupAdResource(T t);

    public void destroy() {
        if (destroyed) {
            return;
        }
        getResourceLoader().release();
        adListeners.clear();
        destroyed = true;
        onDestroy();
    }

    protected abstract void onDestroy();

    public void addAdListener(AdListener adListener) {
        if (adListener == null) {
            return;
        }
        if (adListeners.contains(adListener)) {
            return;
        }
        adListeners.add(adListener);
    }

    public void removeAdListener(AdListener adListener) {
        adListeners.remove(adListener);
    }

    private void notifyAdLoaded() {
        ThreadUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                for (int i = adListeners.size() - 1; i >= 0; i--) {
                    adListeners.get(i).onAdLoaded();
                }
            }
        });
    }

    protected void notifyAdDisplay() {
        ThreadUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                for (int i = adListeners.size() - 1; i >= 0; i--) {
                    adListeners.get(i).onAdDisplay();
                }
            }
        });
    }

    protected void notifyAdClick() {
        ThreadUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                for (int i = adListeners.size() - 1; i >= 0; i--) {
                    adListeners.get(i).onAdClick();
                }
            }
        });
    }

    protected void notifyAdFail(Throwable e) {
        ThreadUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                for (int i = adListeners.size() - 1; i >= 0; i--) {
                    adListeners.get(i).onAdFail(e);
                }
            }
        });
    }

    public interface AdListener {

        void onAdLoaded();

        void onAdDisplay();

        void onAdClick();

        void onAdFail(Throwable e);
    }

    public AdModel getAdModel() {
        return adModel;
    }

    public T getAdResource() {
        return getResourceLoader().getResource();
    }

    private ResourceLoader<T> getResourceLoader() {
        if (resourceLoader == null) {
            resourceLoader = onCreateResourceLoader(adModel);
            resourceLoader.setCallback(new Callback<T>() {
                @Override
                public void onSuccess(T t) {
                    notifyAdLoaded();
                    if (hasPendingLoad) {
                        hasPendingLoad = false;
                        setupAdResource(t);
                    }
                }

                @Override
                public void onFail(Throwable e) {
                    notifyAdFail(e);
                }
            });
        }
        return resourceLoader;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
