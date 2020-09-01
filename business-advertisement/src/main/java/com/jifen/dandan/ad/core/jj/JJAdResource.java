package com.jifen.dandan.ad.core.jj;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 竞价物料接口
 */
public interface JJAdResource {

    String CHANNEL_CPC = "CPC";
    String CHANNEL_GDT = "GDT";
    String CHANNEL_TOUTIAO = "TouTiao";
    String CHANNEL_UNKNOWN = "unknown";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({CHANNEL_CPC,CHANNEL_GDT,CHANNEL_TOUTIAO, CHANNEL_UNKNOWN})
    @interface Channel{ }

    /**
     * “CPC”， “GDT”， “TouTiao”
     * @return
     */
    @Channel
    String getSuccessChannel();

    String getSuccessAdId();
}
