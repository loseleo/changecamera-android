package com.jifen.dandan.ringtone.core.infoflow.loader;

import android.content.Context;

import com.jifen.dandan.common.feed.bean.AdModel;
import com.jifen.dandan.ringtone.core.infoflow.loader.res.GDTInfoFlowVideo;
import com.jifen.dandan.common.BuildConfig;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.NativeADUnifiedListener;
import com.qq.e.ads.nativ.NativeUnifiedAD;
import com.qq.e.ads.nativ.NativeUnifiedADData;
import com.qq.e.comm.util.AdError;

import java.util.List;

public class GDTInfoFlowVideoLoader extends com.jifen.dandan.ringtone.core.loader.ResourceLoader<GDTInfoFlowVideo> {


    private Context context;

    public GDTInfoFlowVideoLoader(AdModel adModel, Context context) {
        super(adModel);
        this.context = context;
    }

    @Override
    protected void onLoad(AdModel adModel) {


        NativeUnifiedAD nativeUnifiedAD = new NativeUnifiedAD(context, BuildConfig.AD_GDT_APP_ID, adModel.getAdId(), nativeADUnifiedListener);
        /**
         * 如果广告位支持视频广告，强烈建议在调用loadData请求广告前，调用下面两个方法，有助于提高视频广告的eCPM值 <br/>
         * 如果广告位仅支持图文广告，则无需调用
         */

        /**
         * 设置本次拉取的视频广告，从用户角度看到的视频播放策略<p/>
         *
         * "用户角度"特指用户看到的情况，并非SDK是否自动播放，与自动播放策略AutoPlayPolicy的取值并非一一对应 <br/>
         *
         * 例如开发者设置了VideoOption.AutoPlayPolicy.NEVER，表示从不自动播放 <br/>
         * 但满足某种条件(如晚上10点)时，开发者调用了startVideo播放视频，这在用户看来仍然是自动播放的
         */
        nativeUnifiedAD.setVideoPlayPolicy(VideoOption.VideoPlayPolicy.AUTO); // 本次拉回的视频广告，从用户的角度看是自动播放的

        /**
         * 设置在视频广告播放前，用户看到显示广告容器的渲染者是SDK还是开发者 <p/>
         *
         * 一般来说，用户看到的广告容器都是SDK渲染的，但存在下面这种特殊情况： <br/>
         *
         * 1. 开发者将广告拉回后，未调用bindMediaView，而是用自己的ImageView显示视频的封面图 <br/>
         * 2. 用户点击封面图后，打开一个新的页面，调用bindMediaView，此时才会用到SDK的容器 <br/>
         * 3. 这种情形下，用户先看到的广告容器就是开发者自己渲染的，其值为VideoADContainerRender.DEV
         * 4. 如果觉得抽象，可以参考NativeADUnifiedDevRenderContainerActivity的实现
         */
        nativeUnifiedAD.setVideoADContainerRender(VideoOption.VideoADContainerRender.SDK); // 视频播放前，用户看到的广告容器是由SDK渲染的

        nativeUnifiedAD.loadData(1);
    }


    private NativeADUnifiedListener nativeADUnifiedListener = new NativeADUnifiedListener() {
        @Override
        public void onADLoaded(List<NativeUnifiedADData> list) {
            if (list != null && !list.isEmpty()) {
                notifySuccess(new GDTInfoFlowVideo(list.get(0)));
            } else {
                notifyFail(new RuntimeException("gdt result null"));
            }
        }

        @Override
        public void onNoAD(AdError adError) {

            String errorMsg;
            if (adError == null) {
                errorMsg = "gdt info flow video load error";
            } else {
                errorMsg = "gdt info flow video load error,code=" + adError.getErrorCode() + ",msg=" + adError.getErrorMsg();
            }
            notifyFail(new RuntimeException(errorMsg));
        }
    };
}
