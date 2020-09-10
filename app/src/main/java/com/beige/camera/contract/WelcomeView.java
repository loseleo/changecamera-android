package com.beige.camera.contract;

import android.arch.lifecycle.LifecycleOwner;
import android.widget.FrameLayout;

import com.zhangqiang.mvp.IView;

public interface WelcomeView extends IView, LifecycleOwner {

    FrameLayout getAdContainer();

    void goHome();
}
