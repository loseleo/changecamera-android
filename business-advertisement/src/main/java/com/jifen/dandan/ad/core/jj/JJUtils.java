package com.jifen.dandan.ad.core.jj;

import com.iclicash.advlib.core.ICliBundle;

/**
 * 竞价工具类
 */
public class JJUtils {

    @JJAdResource.Channel
    public static String getSuccessChannel(ICliBundle iCliBundle){
        return iCliBundle != null ? iCliBundle.tbundle.getString("convertorName") : JJAdResource.CHANNEL_UNKNOWN;
    }

    public static String getSuccessAdId(ICliBundle iCliBundle){
        return iCliBundle != null ? iCliBundle.tbundle.getString("dspSlotid") : null;
    }
}
