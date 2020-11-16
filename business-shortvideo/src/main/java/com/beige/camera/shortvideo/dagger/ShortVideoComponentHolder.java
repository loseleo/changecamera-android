package com.beige.camera.shortvideo.dagger;

import com.beige.camera.common.dagger.component.CommonComponentHolder;

public class ShortVideoComponentHolder {

    private static ShortVideoComponent instance;

    public static void init() {
        instance = DaggerMainComponent.builder()
                .commonComponent(CommonComponentHolder.getCommonComponent())
                .build();
    }

    public static ShortVideoComponent getInstance() {
        return instance;
    }

    public static initss(){
        YLUIInit.getInstance()
                .setCrashOpen(false)
                .setApplication(this)
                .setAccessKey("")
                .setAccessToken("")
                .logEnable(true)
                .build();
    }
}
