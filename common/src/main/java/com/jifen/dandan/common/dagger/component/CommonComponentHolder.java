package com.jifen.dandan.common.dagger.component;

import android.app.Application;

import com.jifen.dandan.common.dagger.module.AppModule;
import com.jifen.dandan.common.dagger.module.CommonApiModule;

public class CommonComponentHolder {

    private static CommonComponent commonComponent;

    public static void init(Application application){

        commonComponent =  DaggerCommonComponent.builder()
                .appModule(new AppModule(application))
                .commonApiModule(new CommonApiModule())
                .build();
    }

    public static CommonComponent getCommonComponent() {
        return commonComponent;
    }
}
