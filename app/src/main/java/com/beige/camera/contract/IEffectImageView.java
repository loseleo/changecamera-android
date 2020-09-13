package com.beige.camera.contract;

import android.arch.lifecycle.LifecycleOwner;

import com.zhangqiang.mvp.IView;

public interface IEffectImageView extends IView, LifecycleOwner {

    void onResultEffectImage(String image,String actionType);

    void onResultAge(String age);

}
