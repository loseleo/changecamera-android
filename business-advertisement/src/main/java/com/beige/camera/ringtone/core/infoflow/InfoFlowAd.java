package com.beige.camera.ringtone.core.infoflow;

import android.widget.FrameLayout;

import com.beige.camera.common.feed.bean.AdModel;
import com.beige.camera.ringtone.core.Ad;

public abstract class InfoFlowAd<T> extends Ad<T> {

    private FrameLayout adContainer;

    public InfoFlowAd(FrameLayout adContainer,AdModel adModel) {
        super(adModel);
        this.adContainer = adContainer;
    }

    @Override
    protected void onDestroy() {
        adContainer.removeAllViews();
    }

    public FrameLayout getAdContainer() {
        return adContainer;
    }

    @Override
    protected void onSetupAdResource(T resource) {
        onSetupAdResource(adContainer,resource);
    }

    protected abstract void onSetupAdResource(FrameLayout adContainer, T t);

    public void setupAdResource(FrameLayout adContainer){
        this.adContainer = adContainer;
        if (isDestroyed()) {
            return;
        }
        onSetupAdResource(adContainer,getAdResource());
    }

    public final void resume(){
        onResume();
    }

    protected void onResume() {

    }

    public final void pause() {
        onPause();
    }

    protected void onPause() {

    }

    @Override
    public T getAdResource() {
        return super.getAdResource();
    }
}
