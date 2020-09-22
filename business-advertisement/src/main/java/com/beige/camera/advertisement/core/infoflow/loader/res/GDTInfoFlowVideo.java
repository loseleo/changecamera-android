package com.beige.camera.advertisement.core.infoflow.loader.res;

import android.graphics.Bitmap;

import com.beige.camera.advertisement.core.infoflow.BaseInfoFlowVideoAd;
import com.beige.camera.advertisement.core.infoflow.img.BaseImgAd;
import com.qq.e.ads.nativ.NativeUnifiedADData;
import com.qq.e.comm.constants.AdPatternType;

import java.util.List;

public class GDTInfoFlowVideo implements BaseInfoFlowVideoAd.InfoFlowVideoInfoOwner, BaseImgAd.ImgAdRes {

    private NativeUnifiedADData nativeUnifiedADData;

    public GDTInfoFlowVideo(NativeUnifiedADData nativeUnifiedADData) {
        this.nativeUnifiedADData = nativeUnifiedADData;
    }

    public NativeUnifiedADData getNativeUnifiedADData() {
        return nativeUnifiedADData;
    }

    @Override
    public long getVideoDuration() {
        return nativeUnifiedADData.getVideoDuration();
    }

    @Override
    public String getTitle() {
        return nativeUnifiedADData.getTitle();
    }

    @Override
    public CharSequence getDescription() {
        return nativeUnifiedADData.getDesc();
    }

    @Override
    public int getAppScore() {
        return nativeUnifiedADData.getAppScore();
    }

    @Override
    public String getIconUrl() {
        return nativeUnifiedADData.getIconUrl();
    }

    @Override
    public Bitmap getLogo() {
        return null;
    }

    @Override
    public String getImgAdTitle() {
        return nativeUnifiedADData.getDesc();
    }

    @Override
    public String getImgAdImageUrl() {
        int adPatternType = nativeUnifiedADData.getAdPatternType();
        String imageUrl = null;
        if (adPatternType == AdPatternType.NATIVE_3IMAGE) {
            List<String> imgList = nativeUnifiedADData.getImgList();
            if (imgList != null && imgList.size() > 0) {
                imageUrl = imgList.get(0);
            }
        } else {
            imageUrl = nativeUnifiedADData.getImgUrl();
        }
        return imageUrl;
    }
}
