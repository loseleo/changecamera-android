package com.jifen.dandan.ringtone.core;

import android.content.Context;
import android.text.TextUtils;

import com.iclicash.advlib.BuildConfig;
import com.iclicash.advlib.core.ICliFactory;
import com.iclicash.advlib.core.IMultiReporter;
import com.jifen.dandan.common.feed.bean.AdModel;
import com.jifen.dandan.common.base.BaseApplication;


public class MinikitReportUtils {

    private ICliFactory factory;

    private static MinikitReportUtils minikitReportUtils;

    public MinikitReportUtils() {
        //app全局初始化一次ICliFactory 即可
        factory = ICliFactory.obtainInstance(BaseApplication.getsInstance().getBaseContext(), BuildConfig.VERSION_NAME);
    }

    public static synchronized MinikitReportUtils getInstance() {
        if (minikitReportUtils == null) {
            minikitReportUtils = new MinikitReportUtils();
        }
        return minikitReportUtils;
    }

    //广告位请求广告时
    public void reportRequest(AdModel adModel) {
        if ( adModel == null || TextUtils.equals(adModel.getAdChannel(), "cpc_rtb")) {
            return;
        }
        IMultiReporter reporter = factory.createMultiReporter();
        if (reporter != null ) {
            IMultiReporter.Builder builder = getBuilder(adModel, reporter);
            reporter.reportRequest(builder);
        }
    }

    //各类DSP响应广告上报
    public void reportResponse(AdModel adModel) {
        if (adModel == null || TextUtils.equals(adModel.getAdChannel(), "cpc_rtb")) {
            return;
        }
        IMultiReporter reporter = factory.createMultiReporter();
        if (reporter != null) {
            IMultiReporter.Builder builder = getBuilder(adModel, reporter);
            reporter.reportResponse(builder);
        }
    }


    //app准备填充广告素材,或者有媒体自己的竞价逻辑准备阶段
    public void reportBiddingResult(AdModel adModel) {
        if ( adModel == null || TextUtils.equals(adModel.getAdChannel(), "cpc_rtb")) {
            return;
        }
        IMultiReporter reporter = factory.createMultiReporter();
        if (reporter != null) {
            IMultiReporter.Builder builder = getBuilder(adModel, reporter);
            reporter.reportBiddingResult(builder);
        }
    }

    //展现广告时
    public void reportDspShow(AdModel adModel) {
        if ( adModel == null || TextUtils.equals(adModel.getAdChannel(), "cpc_rtb")) {
            return;
        }
        IMultiReporter reporter = factory.createMultiReporter();
        if (reporter != null) {
            IMultiReporter.Builder builder = getBuilder(adModel, reporter);
            reporter.reportDspShow(builder);
        }
    }

    //展示广告时
    public void reportDspClick(AdModel adModel) {
        if ( adModel == null || TextUtils.equals(adModel.getAdChannel(), "cpc_rtb")) {
            return;
        }
        IMultiReporter reporter = factory.createMultiReporter();
        if (reporter != null ) {
            IMultiReporter.Builder builder = getBuilder(adModel, reporter);
            reporter.reportDspClick(builder);
        }
    }


    private IMultiReporter.Builder getBuilder(AdModel adModel, IMultiReporter reporter) {
        String adSlotid = adModel.getAdSlotid();
        String adChannel = adModel.getAdChannel();
        if (TextUtils.equals(adChannel, "cpc")) {
            adChannel = "aiclk";
        }
        Context context = BaseApplication.getsInstance().getBaseContext();
        String searchid = reporter.generateID(context, adSlotid); //广告请求链路生成唯一searchid
        return new IMultiReporter.Builder()
                .setAdsrc(adChannel)//dsp来源
                .setSearchid(searchid)//唯一searchid
                .setSlotid(adSlotid)//物理广告位
                .setDspSlotid(adModel.getAdId());
    }

}
