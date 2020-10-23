package com.beige.camera.presenter;
import android.content.Context;
import android.widget.FrameLayout;

import com.beige.camera.advertisement.api.bean.AdConfigBean;
import com.beige.camera.advertisement.core.AdManager;
import com.beige.camera.advertisement.core.SimpleAdListener;
import com.beige.camera.advertisement.core.splash.OnAdSkipListener;
import com.beige.camera.advertisement.core.splash.OnAdTimeOverListener;
import com.beige.camera.advertisement.core.splash.SplashAd;
import com.beige.camera.advertisement.core.strategy.AdLoader;
import com.beige.camera.advertisement.core.strategy.Callback;
import com.beige.camera.advertisement.dagger.AdComponentHolder;
import com.beige.camera.api.Api;
import com.beige.camera.common.feed.bean.AdModel;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.RxUtil;
import com.beige.camera.contract.IWelcomeView;
import com.zhangqiang.mvp.Presenter;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.beige.camera.common.utils.RxUtil.io_main;

/**
 * @author huangchen@qutoutiao.net
 * @version 1.0
 * @date 06/14/2019
 */
public class WelcomePresenter extends Presenter<IWelcomeView> {


    private Api api;
    private Context mContext;

    @Inject
    public WelcomePresenter(Context mContext, Api api) {
        this.mContext = mContext;
        this.api = api;
    }

    public void showSplashAD(){

        AdComponentHolder.getComponent().getAdApi().getAdConfig("Screen_cs")
                .compose(io_main())
                .timeout(2000, TimeUnit.MILLISECONDS)
                .map(RxUtil.applyApiResult())
                .subscribe(new Observer<AdConfigBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AdConfigBean adConfigBean) {
                        LogUtils.e("zhangning","adConfigBean = " + adConfigBean.getCandidates().toString());
                        loadSplashAd(adConfigBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        IWelcomeView view = getAttachedView();
                        if(view != null){
                            view.goHome();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void loadSplashAd(AdConfigBean adModel) {
        IWelcomeView view = getAttachedView();
        if(view == null){
          return;
        }
        FrameLayout adContainer = view.getAdContainer();
        AdLoader<SplashAd> splashAdAdLoader = AdManager.loadSplashAd(adContainer, adModel.getCandidates(), new Callback<SplashAd>() {
            @Override
            public void onAdLoadStart(SplashAd ad) {
                ad.addOnAdSkipListener(new OnAdSkipListener() {
                    @Override
                    public void onSkip() {
                        view.goHome();
                    }
                });
                ad.addOnAdTimeOverListener(new OnAdTimeOverListener() {
                    @Override
                    public void onAdTimeOver() {
                        view.goHome();
                    }
                });
                ad.addAdListener(new SimpleAdListener() {
                    @Override
                    public void onAdLoaded() {
                    }
                });
            }

            @Override
            public void onFail(Throwable e) {
                if(view != null){
                    view.goHome();
                }
                LogUtils.e("zhangning","onFail = " + e.getMessage());
            }
        });
    }

}
