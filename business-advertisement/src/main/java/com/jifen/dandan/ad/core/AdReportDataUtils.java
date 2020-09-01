package com.jifen.dandan.ad.core;

import com.jifen.dandan.common.feed.bean.AdModel;
//import com.jifen.dandan.common.utils.ReportUtils;
//import com.jifen.open.biz.login.ui.report.Action;

import java.util.HashMap;

public class AdReportDataUtils {



    public static  void adShowFail(AdModel adModel, HashMap<String, String> extra,String errorMsg){
        if (extra == null) {
            extra = new HashMap<>();
        }
        extra.put("errorMsg", errorMsg);
//        reportData("ad_show_fail", Action.ACTION_SHOW,adModel, extra);
    }

    public static  void adClick(AdModel adModel, HashMap<String, String> extra){
//        reportData("ad_click", Action.ACTION_CLICK, adModel, extra);
        MinikitReportUtils.getInstance().reportDspClick(adModel);
    }

    public static  void adShowSuccess(AdModel adModel, HashMap<String, String> extra){
//        reportData("ad_show_success", Action.ACTION_SHOW,adModel, extra);
        MinikitReportUtils.getInstance().reportDspShow(adModel);
    }

    public static  void adRequestSuccess(AdModel adModel, HashMap<String, String> extra){
//        reportData("ad_request_success", Action.ACTION_SHOW,adModel, extra);
        MinikitReportUtils.getInstance().reportResponse(adModel);
        MinikitReportUtils.getInstance().reportBiddingResult(adModel);
    }

    public static  void adRequestStart(AdModel adModel, HashMap<String, String> extra){
//        reportData("ad_request_start", Action.ACTION_SHOW,adModel, extra);
        MinikitReportUtils.getInstance().reportRequest(adModel);
    }

    public static  void adVideoPalyFail(AdModel adModel, HashMap<String, String> extra){
//        reportData("ad_video_paly_fail", Action.ACTION_SHOW,adModel, extra);
    }

    public static  void adVideoPalySuccess(AdModel adModel, HashMap<String, String> extra){
//        reportData("ad_video_paly_success", Action.ACTION_SHOW,adModel, extra);
    }

    public static  void adVideoPlayDuration(AdModel adModel, HashMap<String, String> extra){
//        reportData("ad_video_play_duration", Action.ACTION_SHOW,adModel, extra);
    }

    public static  void adSplashSkip(AdModel adModel, HashMap<String, String> extra){
//        reportData("ad_splash_skip", Action.ACTION_SHOW,adModel, extra);
    }

    private static void reportData(String event, String action, AdModel adModel, HashMap<String, String> extra) {

        HashMap<String, String> extendInfo;
        if (extra != null) {
            extendInfo = extra;
        }else {
            extendInfo = new HashMap<>();
        }
        if (adModel != null) {
            extendInfo.put("adCode", adModel.getAdCode());
            extendInfo.put("adId", adModel.getAdId());
            extendInfo.put("adChannel", adModel.getAdChannel());
            extendInfo.put("smartCoinEnable", adModel.getSmartCoinEnable()+"");
        }
//        ReportUtils.onEvent("advertisement", event, action, extendInfo);
    }

}
