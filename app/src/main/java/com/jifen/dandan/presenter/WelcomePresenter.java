package com.jifen.dandan.presenter;
import android.content.Context;
import android.widget.FrameLayout;

import com.jifen.dandan.ad.api.bean.AdConfigBean;
import com.jifen.dandan.ad.core.AdManager;
import com.jifen.dandan.ad.core.SimpleAdListener;
import com.jifen.dandan.ad.core.splash.OnAdSkipListener;
import com.jifen.dandan.ad.core.splash.OnAdTimeOverListener;
import com.jifen.dandan.ad.core.splash.SplashAd;
import com.jifen.dandan.ad.core.strategy.AdLoader;
import com.jifen.dandan.ad.core.strategy.Callback;
import com.jifen.dandan.ad.dagger.AdComponentHolder;
import com.jifen.dandan.api.Api;
import com.jifen.dandan.common.utils.LogUtils;
import com.jifen.dandan.common.utils.MmkvUtil;
import com.jifen.dandan.common.utils.RxUtil;
import com.jifen.dandan.dagger.MainComponentHolder;
import com.jifen.dandan.contract.WelcomeView;
import com.zhangqiang.mvp.Presenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;

import static com.jifen.dandan.common.utils.RxUtil.io_main;

/**
 * @author huangchen@qutoutiao.net
 * @version 1.0
 * @date 06/14/2019
 */
public class WelcomePresenter extends Presenter<WelcomeView> {


    private Api api;
    private Context mContext;

    @Inject
    public WelcomePresenter(Context mContext, Api api) {
        this.mContext = mContext;
        this.api = api;
    }

    public void showSplashAD(){

        AdComponentHolder.getComponent().getAdApi().getAdConfig("welcome")
                .compose(io_main())
                .timeout(5000, TimeUnit.MILLISECONDS)
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

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void loadSplashAd(AdConfigBean adModel) {

        WelcomeView view = getAttachedView();
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
                LogUtils.e("zhangning","onFail = " + e.getMessage());
            }
        });
    }

}
