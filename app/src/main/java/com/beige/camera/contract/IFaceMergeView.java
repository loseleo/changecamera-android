package com.beige.camera.contract;

import android.arch.lifecycle.LifecycleOwner;

import com.beige.camera.bean.TemplatesConfigBean;
import com.zhangqiang.mvp.IView;

public interface IFaceMergeView extends IView, LifecycleOwner {

    void onResultEffectImage(String image,String actionType);

    void onResultTemplatesConfigBean(TemplatesConfigBean templatesConfigBean);

}
