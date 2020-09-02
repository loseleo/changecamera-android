package com.jifen.dandan.ringtone.dagger;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.jifen.dandan.ringtone.CPCICliFactoryHolder;
import com.jifen.dandan.ringtone.TTAdManagerHolder;
import com.jifen.dandan.common.dagger.component.CommonComponent;
import com.jifen.dandan.common.dagger.component.CommonComponentHolder;
import com.jifen.dandan.common.utils.ProcessUtil;

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

            CPCICliFactoryHolder.getInstance().init(application);

            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                    m.invoke(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
