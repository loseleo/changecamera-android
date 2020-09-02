/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jifen.dandan;

import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jifen.dandan.ringtone.dagger.AdComponentHolder;
import com.jifen.dandan.common.base.BaseApplication;
import com.jifen.dandan.common.dagger.component.CommonComponentHolder;
import com.jifen.dandan.common.helper.activitylife.ActivityLifeManager;
import com.jifen.dandan.common.helper.activitylife.AppShowListener;
import com.jifen.dandan.common.utils.ProcessUtil;
import com.jifen.dandan.dagger.MainComponentHolder;
import com.tencent.bugly.crashreport.CrashReport;

import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author zhangning.
 * @date 2016/8/3.
 */
public class MyApplication extends BaseApplication {

    private static MyApplication sInstance;
    public static long appStart;

    @Override
    protected void attachBaseContext(Context base) {
        appStart = System.currentTimeMillis();
        super.attachBaseContext(base);
        sInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (isMainProcess()) {
            MyApplication.initRouter(this);
            initComponent();
        }
        sInstance = this;
        //防止rx flow终止之后继续发射错误，导致crash
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                // 上报错误信息
                CrashReport.postCatchedException(throwable);
            }
        });
        if (isMainProcess()) {
            addAppShowListener();
        }
    }

    protected boolean isMainProcess() {
        return ProcessUtil.isMainProcessChecked(this);
    }

    public static MyApplication getInstance() {
        return sInstance;
    }

    public void initComponent() {
        CommonComponentHolder.init(this);
        MainComponentHolder.init();
        AdComponentHolder.init();
    }

    public static void initRouter(MyApplication application) {
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(application);
        //防止未设置group导致activity无法跳转的问题
//        ARouter.getInstance().build(PageIdentity.SERVICE_APP).navigation();
    }



    private void addAppShowListener() {
        ActivityLifeManager.get().addAppShowListener(new AppShowListener() {
            @Override
            public void onAppEnterForeground() {
//                HashMap<String, String> extendInfo = new HashMap<>();
//                extendInfo.put("action", "0");
//                ReportUtils.onShow("app", "app_open", extendInfo);
            }

            @Override
            public void onAppReturnForeground() {
//                HashMap<String, String> extendInfo = new HashMap<>();
//                extendInfo.put("action", "1");
//                ReportUtils.onShow("app", "app_open", extendInfo);
            }

            @Override
            public void onAppEnterBackground() {
            }
        });
    }




}
