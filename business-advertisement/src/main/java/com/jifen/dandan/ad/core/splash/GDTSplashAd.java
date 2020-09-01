package com.jifen.dandan.ad.core.splash;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ViewGroup;

import com.jifen.dandan.common.feed.bean.AdModel;
import com.jifen.dandan.ad.core.loader.ResourceLoader;
import com.jifen.dandan.common.BuildConfig;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;

public class GDTSplashAd extends SplashAd<SplashAD> {

    public GDTSplashAd(AdModel adModel, ViewGroup adContainer) {
        super(adModel, adContainer);
    }

    /**
     * 拉取开屏广告，开屏广告的构造方法有3种，详细说明请参考开发者文档。
     *
     * @param @activity        展示广告的activity
     * @param @adContainer     展示广告的大容器
     * @param @skipContainer   自定义的跳过按钮：传入该view给SDK后，SDK会自动给它绑定点击跳过事件。SkipView的样式可以由开发者自由定制，其尺寸限制请参考activity_splash.xml或者接入文档中的说明。
     * @param @appId           应用ID
     * @param @posId           广告位ID
     * @param @adListener      广告状态监听器
     * @param @fetchDelay      拉取广告的超时时长：取值范围[3000, 5000]，设为0表示使用广点通SDK默认的超时时长。
     */
    @NonNull
    @Override
    protected ResourceLoader<SplashAD> onCreateResourceLoader(AdModel adModel) {
        return new ResourceLoader<SplashAD>(adModel) {

            SplashAD splashAD;

            @Override
            protected void onLoad(AdModel adModel) {
                ViewGroup adContainer = getAdContainer();
                splashAD = new SplashAD((Activity) adContainer.getContext(),
                        null,
                        BuildConfig.AD_GDT_APP_ID,
                        getAdModel().getAdId(), new SplashADListener() {
                    @Override
                    public void onADDismissed() {
                        notifyAdTimeOver();
                    }

                    @Override
                    public void onNoAD(AdError adError) {
                        notifyFail(new RuntimeException("load gdt splash ad error :" + adError.getErrorCode()
                                + ",msg:" + adError.getErrorMsg()));
                    }

                    @Override
                    public void onADPresent() {
                        notifySuccess(splashAD);
                    }

                    @Override
                    public void onADClicked() {
                        Log.i("AD_DEMO", "SplashADClicked clickUrl: "
                                + (splashAD.getExt() != null ? splashAD.getExt().get("clickUrl") : ""));
                        notifyAdClick();
                    }

                    @Override
                    public void onADTick(long l) {

                    }

                    @Override
                    public void onADExposure() {

                    }

                    @Override
                    public void onADLoaded(long l) {

                    }
                }, 0);
                splashAD.fetchAndShowIn(adContainer);
            }
        };
    }

    @Override
    protected void onSetupAdResource(SplashAD splashAD) {
        notifyAdDisplay();
    }

    @Override
    protected void onDestroy() {

    }
}
