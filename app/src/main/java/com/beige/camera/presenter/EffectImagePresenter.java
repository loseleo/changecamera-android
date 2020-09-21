package com.beige.camera.presenter;

import android.app.Activity;
import android.content.Context;
import android.widget.FrameLayout;

import com.beige.camera.api.Api;
import com.beige.camera.bean.EffectAgeBean;
import com.beige.camera.bean.EffectImageBean;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.RxUtil;
import com.beige.camera.contract.IEffectImageView;
import com.beige.camera.contract.IFaceMergeView;
import com.beige.camera.contract.IWelcomeView;
import com.beige.camera.ringtone.api.bean.AdConfigBean;
import com.beige.camera.ringtone.core.AdManager;
import com.beige.camera.ringtone.core.SimpleAdListener;
import com.beige.camera.ringtone.core.dismiss.OnDismissedListener;
import com.beige.camera.ringtone.core.infoflow.InfoFlowAd;
import com.beige.camera.ringtone.core.rewardvideo.RewardVideoAd;
import com.beige.camera.ringtone.core.splash.OnAdSkipListener;
import com.beige.camera.ringtone.core.splash.OnAdTimeOverListener;
import com.beige.camera.ringtone.core.splash.SplashAd;
import com.beige.camera.ringtone.core.strategy.AdLoader;
import com.beige.camera.ringtone.core.strategy.Callback;
import com.beige.camera.ringtone.dagger.AdComponentHolder;
import com.zhangqiang.mvp.Presenter;

import java.util.List;
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
public class EffectImagePresenter extends Presenter<IEffectImageView> {


    private Api api;
    private Context mContext;

    @Inject
    public EffectImagePresenter(Context mContext, Api api) {
        this.mContext = mContext;
        this.api = api;
    }

    public void getEffectAge(String image) {
        api.getEffectAge(image)
                .compose(RxUtil.io_main())
                .map(RxUtil.applyApiResult())
                .subscribe(new Observer<EffectAgeBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(EffectAgeBean effectAgeBean) {
                        IEffectImageView attachedView = getAttachedView();
                        if (attachedView != null) {
                            attachedView.onResultAge(effectAgeBean.getAge());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        IEffectImageView attachedView = getAttachedView();
                        if (attachedView != null) {
                            int age = (int) (10 + Math.random() * (1000 - 10 + 1));
                            attachedView.onResultAge(age + "");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void getImageSelieAnime(String image,String actionType) {
        api.getImageSelieAnime(image)
                .compose(RxUtil.io_main())
                .map(RxUtil.applyApiResult())
                .subscribe(new Observer<EffectImageBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(EffectImageBean effectAgeBean) {
                        IEffectImageView attachedView = getAttachedView();
                        if (attachedView != null) {
                            attachedView.onResultEffectImage(effectAgeBean.getImage(),actionType);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        IEffectImageView attachedView = getAttachedView();
                        if (attachedView != null) {
                            attachedView.onResultEffectImage("",actionType);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void getFaceEditAttr(String image, String actionType) {
        //选值 TO_KID; TO_OLD; TO_FEMALE; TO_MALE
        api.getFaceEditAttr(image, actionType)
                .compose(RxUtil.io_main())
                .map(RxUtil.applyApiResult())
                .subscribe(new Observer<EffectImageBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(EffectImageBean effectAgeBean) {
                        IEffectImageView attachedView = getAttachedView();
                        if (attachedView != null) {
                            attachedView.onResultEffectImage(effectAgeBean.getImage(),actionType);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        IEffectImageView attachedView = getAttachedView();
                        if (attachedView != null) {
                            attachedView.onResultEffectImage("",actionType);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void getImageStyleTrans(String image, String actionType) {
        api.getImageStyleTrans(image, actionType)
                .compose(RxUtil.io_main())
                .map(RxUtil.applyApiResult())
                .subscribe(new Observer<EffectImageBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(EffectImageBean effectAgeBean) {
                        IEffectImageView attachedView = getAttachedView();
                        if (attachedView != null) {
                            attachedView.onResultEffectImage(effectAgeBean.getImage(),actionType);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        IEffectImageView attachedView = getAttachedView();
                        if (attachedView != null) {
                            attachedView.onResultEffectImage("",actionType);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
