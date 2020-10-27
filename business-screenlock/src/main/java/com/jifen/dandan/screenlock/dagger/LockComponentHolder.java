package com.jifen.dandan.screenlock.dagger;


import com.beige.camera.common.dagger.component.CommonComponentHolder;
import com.jifen.dandan.screenlock.dagger.compenent.DaggerLockComponent;
import com.jifen.dandan.screenlock.dagger.compenent.LockComponent;

public class LockComponentHolder {

    private static LockComponent instance;


    public static LockComponent get() {
        return instance;
    }

    public static void init() {

        instance = DaggerLockComponent.builder()
                .commonComponent(CommonComponentHolder.getCommonComponent())
                .build();

    }
}
