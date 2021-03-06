package com.beige.camera.advertisement.dagger;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.beige.camera.advertisement.TTAdManagerHolder;
import com.beige.camera.common.BuildConfig;
import com.beige.camera.common.dagger.component.CommonComponent;
import com.beige.camera.common.dagger.component.CommonComponentHolder;
import com.beige.camera.common.utils.ProcessUtil;
import com.qq.e.comm.managers.GDTADManager;

import java.lang.reflect.Method;

public class AdComponentHolder {

    private static AdComponent component;

    public static void init() {

        CommonComponent commonComponent = CommonComponentHolder.getCommonComponent();
        component = DaggerAdComponent.builder()
                .commonComponent(commonComponent)
                .build();

        Application application = commonComponent.getApplication();
        if (ProcessUtil.isMainProcessChecked(application)) {


            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                    m.invoke(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            GDTADManager.getInstance().initWith(application, BuildConfig.AD_GDT_APP_ID);
            //穿山甲SDK初始化
            //强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
            TTAdManagerHolder.init(application);
            //如果明确某个进程不会使用到广告SDK，可以只针对特定进程初始化广告SDK的content
            //if (PROCESS_NAME_XXXX.equals(processName)) {
            //   TTAdManagerHolder.init(this)
            //}
        }
    }

    public static AdComponent getComponent() {
        return component;
    }
}
