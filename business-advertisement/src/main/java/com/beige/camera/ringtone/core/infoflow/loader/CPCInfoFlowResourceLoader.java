package com.beige.camera.ringtone.core.infoflow.loader;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.iclicash.advlib.core.AdRequest;
import com.iclicash.advlib.core.ICliBundle;
import com.iclicash.advlib.core.ICliFactory;
import com.iclicash.advlib.core.ICliUtils;
import com.beige.camera.ringtone.core.CPCUtils;
import com.beige.camera.common.feed.bean.AdModel;
import com.beige.camera.ringtone.CPCICliFactoryHolder;
import com.beige.camera.ringtone.CpcAdPlatform;
import com.beige.camera.ringtone.core.infoflow.loader.res.CPCResource;
import com.beige.camera.ringtone.core.loader.ResourceLoader;

public abstract class CPCInfoFlowResourceLoader<T extends CPCResource> extends ResourceLoader<T> {

    private Context context;

    public CPCInfoFlowResourceLoader(AdModel adModel, Context context) {
        super(adModel);
        this.context = context;
    }

    @Override
    protected void onLoad(AdModel adModel) {
        ICliFactory iCliFactory = CPCICliFactoryHolder.getInstance().getICliFactory();
        AdRequest adRequest = iCliFactory.getADRequest();
        adRequest.bindAdContentListener(new ICliUtils.AdContentListener() {
            @Override
            public void onContentDelivered(ICliBundle iCliBundle) {
                adRequest.removeAdContentListener();
                String errorMsg = null;
                if (iCliBundle == null) {
                    errorMsg = "unknown error";
                }else if(iCliBundle.lastError != null){
                    errorMsg = iCliBundle.lastError;
                }
                if (errorMsg != null) {
                    notifyFail(new RuntimeException(errorMsg));
                    return;
                }
                notifySuccess(convert(iCliBundle));
            }
        });
        Bundle bundle = CPCUtils.buildCommonParams(context);
        adRequest.InvokeADV(adModel.getAdId(), CpcAdPlatform.ADTYPE_ONE, adModel.getHeight(), adModel.getWidth(),bundle);
    }

    @NonNull
    protected abstract T convert(ICliBundle iCliBundle);


}
