package com.beige.camera.presenter;

import android.content.Context;

import com.beige.camera.api.Api;
import com.beige.camera.bean.EffectAgeBean;
import com.beige.camera.bean.EffectImageBean;
import com.beige.camera.common.utils.RxUtil;
import com.beige.camera.contract.IEffectImageView;
import com.zhangqiang.mvp.Presenter;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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
