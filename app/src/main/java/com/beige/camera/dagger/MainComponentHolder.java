package com.beige.camera.dagger;

import com.beige.camera.common.dagger.component.CommonComponentHolder;

public class MainComponentHolder {

    private static MainComponent instance;

    public static void init() {
        instance = DaggerMainComponent.builder()
                .commonComponent(CommonComponentHolder.getCommonComponent())
                .build();
    }

    public static MainComponent getInstance() {
        return instance;
    }
}
