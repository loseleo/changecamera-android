package com.beige.camera.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.beige.camera.api.Api;
import com.beige.camera.bean.EffectAgeBean;
import com.beige.camera.bean.EffectImageBean;
import com.beige.camera.bean.FunctionBean;
import com.beige.camera.bean.TemplatesConfigBean;
import com.beige.camera.common.utils.RxUtil;
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
public class FaceMergePresenter extends Presenter<IFaceMergeView> {


    public static final  String TYPE_TEMPLATE_BACKGROUND = "background";
    public static final  String TYPE_TEMPLATE_CUSTOMS = "exotic";
    public static final  String TYPE_TEMPLATE_ANIMALFACE = "animal_face";
    public static final  String TYPE_TEMPLATE_ANIMAL = "animal";
    public static final  String TYPE_TEMPLATE_HAIR = "hairstyle";
    public static final  String TYPE_TEMPLATE_CLOTHES = "hairstyle";
    public static final  String TYPE_TEMPLATE_PAST = "past_life";
    public static final  String TYPE_TEMPLATE_BABYFACE = "baby_face";

    private Api api;
    private Context mContext;

    @Inject
    public FaceMergePresenter(Context mContext, Api api) {
        this.mContext = mContext;
        this.api = api;
    }

    public void getFaceMergeImage(String targetImage,String templateImage,String actionType) {
        api.getFaceMergeImage(templateImage,targetImage)
                .compose(RxUtil.io_main())
                .map(RxUtil.applyApiResult())
                .subscribe(new Observer<EffectImageBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(EffectImageBean effectAgeBean) {
                        IFaceMergeView attachedView = getAttachedView();
                        if (attachedView != null) {
                            attachedView.onResultEffectImage(effectAgeBean.getImage(),actionType);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        IFaceMergeView attachedView = getAttachedView();
                        if (attachedView != null) {
                            attachedView.onResultEffectImage("",actionType);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void getTemplateConfig(String function) {

       String type = "";
        if(TextUtils.equals(function,FunctionBean.ID_CHANGE_BACKGROUND)){
            type = TYPE_TEMPLATE_BACKGROUND;
        }else if(TextUtils.equals(function,FunctionBean.ID_CHANGE_ANIMALFACE)){
            type = TYPE_TEMPLATE_ANIMALFACE;
        }else if(TextUtils.equals(function,FunctionBean.ID_CHANGE_ANIMAL)){
            type = TYPE_TEMPLATE_ANIMAL;
        }else if(TextUtils.equals(function,FunctionBean.ID_CHANGE_CUSTOMS)){
            type = TYPE_TEMPLATE_CUSTOMS;
        }else if(TextUtils.equals(function,FunctionBean.ID_CHANGE_HAIR)){
            type = TYPE_TEMPLATE_HAIR;
        }else if(TextUtils.equals(function,FunctionBean.ID_CHANGE_CLOTHES)){
            type = TYPE_TEMPLATE_CLOTHES;
        }else if(TextUtils.equals(function,FunctionBean.ID_DETECTION_PAST)){
            type = TYPE_TEMPLATE_PAST;
        }else if(TextUtils.equals(function,FunctionBean.ID_DETECTION_BABY)){
            type = TYPE_TEMPLATE_BABYFACE;
        }

        api.getTemplateConfig(type)
                .compose(RxUtil.io_main())
                .map(RxUtil.applyApiResult())
                .subscribe(new Observer<TemplatesConfigBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TemplatesConfigBean templatesConfigBean) {
                        IFaceMergeView attachedView = getAttachedView();
                        if (attachedView != null) {
                            attachedView.onResultTemplatesConfigBean(templatesConfigBean);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        IFaceMergeView attachedView = getAttachedView();
                        if (attachedView != null) {
                            attachedView.onResultTemplatesConfigBean(new TemplatesConfigBean());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
