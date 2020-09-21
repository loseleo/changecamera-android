package com.beige.camera.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.beige.camera.api.Api;
import com.beige.camera.bean.EffectImageBean;
import com.beige.camera.bean.FunctionBean;
import com.beige.camera.bean.TemplatesConfigBean;
import com.beige.camera.common.utils.RxUtil;
import com.beige.camera.contract.IBodySegView;
import com.beige.camera.contract.IEffectImageView;
import com.beige.camera.contract.IFaceMergeView;
import com.zhangqiang.mvp.Presenter;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author huangchen@qutoutiao.net
 * @version 1.0
 * @date 06/14/2019
 */
public class BodySegPresenter extends Presenter<IBodySegView> {


    public static final String TYPE_TEMPLATE_BACKGROUND = "background";

    private Api api;
    private Context mContext;

    @Inject
    public BodySegPresenter(Context mContext, Api api) {
        this.mContext = mContext;
        this.api = api;
    }


    public void getTemplateConfig(String function) {

        String type = TYPE_TEMPLATE_BACKGROUND;
        api.getTemplateConfig(type)
                .compose(RxUtil.io_main())
                .map(RxUtil.applyApiResult())
                .subscribe(new Observer<TemplatesConfigBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TemplatesConfigBean templatesConfigBean) {
                        IBodySegView attachedView = getAttachedView();
                        if (attachedView != null) {
                            attachedView.onResultTemplatesConfigBean(templatesConfigBean);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        IBodySegView attachedView = getAttachedView();
                        if (attachedView != null) {
                            attachedView.onResultTemplatesConfigBean(new TemplatesConfigBean());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void bodySeg(String image) {
        api.bodySeg(image)
                .compose(RxUtil.io_main())
                .map(RxUtil.applyApiResult())
                .subscribe(new Observer<EffectImageBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(EffectImageBean effectAgeBean) {
                        IBodySegView attachedView = getAttachedView();
                        if (attachedView != null) {
                            attachedView.onResultEffectImage(effectAgeBean.getImage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        IBodySegView attachedView = getAttachedView();
                        if (attachedView != null) {
                            attachedView.onResultEffectImage("");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
