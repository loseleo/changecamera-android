package com.jifen.dandan.ringtone;

import android.util.Log;

import com.iclicash.advlib.core.AdRequest;
import com.iclicash.advlib.core.ICliBundle;
import com.iclicash.advlib.core.ICliFactory;
import com.iclicash.advlib.core.ICliUtils;
import com.iclicash.advlib.ui.banner.ADBanner;
import com.jifen.dandan.common.base.BaseApplication;

import java.util.HashMap;

/**
 * 头条广告平台
 * Author：zhangqiang
 * Date：2018/12/20
 * Email:852286406@share_qq.com
 * Github:https://github.com/holleQiang
 */
public class CpcAdPlatform {

    public static final int ADTYPE_ONE = 1;
    private ICliFactory factory;

    public interface CpcAdListener {
        /**
         * 获取广告配置
         */
        void onContentDelivered(ICliBundle iCliBundle);

        /**
         * 获取广告配置失败
         *
         * @param e
         */
        void onContentFail(Throwable e);
    }

    private static class Inner {
        private static final CpcAdPlatform instance = new CpcAdPlatform();
    }


    public static CpcAdPlatform getInstance() {
        return Inner.instance;
    }

    private CpcAdPlatform() {
    }

    public void getCpcAdContent(
            String adCode,
            String adId,
            String adChannel,
            int adtype,
            int height,
            int width,
            CpcAdListener listener) {
        if (factory == null) {
            factory = new ICliFactory(BaseApplication.getsInstance().getApplicationContext());
        }
        AdRequest adRequest = factory.getADRequest();
        adRequest.bindAdContentListener(new ICliUtils.AdContentListener() {
            @Override
            public void onContentDelivered(ICliBundle iCliBundle) {
                String errorMsg = null;
                if (iCliBundle == null) {
                    errorMsg = "unknown error";
                } else if (iCliBundle.lastError != null) {
                    errorMsg = iCliBundle.lastError;
                }
                if (errorMsg != null) {
                    listener.onContentFail(new RuntimeException(errorMsg));
//                    reportData("ad_request_fail", Action.ACTION_SHOW,adCode,adId,adChannel,errorMsg);
                    return;
                }
                if (listener != null) {
                    listener.onContentDelivered(iCliBundle);
//                    reportData("ad_request_success", Action.ACTION_SHOW,adCode,adId,adChannel);
                }
            }
        });
        adRequest.InvokeADV(adId, adtype, height, width);
    }

    public void setAdBannerView(String adCode,
                                String adId,
                                String adChannel,
                                ICliBundle iCliBundle,
                                ADBanner banner) {
        if (iCliBundle.lastError == null) {
            banner.UpdateView(iCliBundle);
//                    reportData("ad_show_success", Action.ACTION_SHOW,adCode,adId,adChannel);
        } else if (ICliFactory.NO_AD.equals(iCliBundle.lastError)) {
//                    reportData("ad_show_fail", Action.ACTION_SHOW,adCode,adId,adChannel);
            Log.i("aiclk", "no ad");
        }
    }


    public void reportData(String event, String action, String adCode, String adId, String adChannel) {
        HashMap<String, String> extendInfo = new HashMap<>();
        extendInfo.put("adCode", adCode);
        extendInfo.put("adId", adId);
        extendInfo.put("adChannel", adChannel);
//        ReportUtils.onEvent("advertisement",event,action,extendInfo);
    }

    public void reportData(String event, String action, String adCode, String adId, String adChannel, String errorMsg) {
        HashMap<String, String> extendInfo = new HashMap<>();
        extendInfo.put("adCode", adCode);
        extendInfo.put("adId", adId);
        extendInfo.put("adChannel", adChannel);
        extendInfo.put("errorMsg", errorMsg);
//        ReportUtils.onEvent("advertisement",event,action,extendInfo);
    }
}
