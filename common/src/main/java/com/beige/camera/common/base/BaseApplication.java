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
package com.beige.camera.common.base;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.beige.camera.common.BuildConfig;
import com.beige.camera.common.helper.activitylife.ActivityLifeManager;
import com.beige.camera.common.constant.Constant;
import com.beige.camera.common.utils.DeviceUtils;
import com.beige.camera.common.utils.MmkvUtil;
import com.beige.camera.common.utils.PackageUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;


/**
 * @author zhangning.
 * @date 2016/8/3.
 */
public abstract class BaseApplication extends Application {

    private static BaseApplication sInstance;
    public static volatile long start;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        //qapp使用统一桥接
        if (isMainProcess()) {
            ActivityLifeManager.get().init(this);
            initAnalysis(this);
            initBugly(this);
        }
    }

    protected abstract boolean isMainProcess();

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        start = System.currentTimeMillis();
        sInstance = this;
        initMmkvUtil();
    }

    public static BaseApplication getsInstance() {
        return sInstance;
    }

    protected void initMmkvUtil() {
        MmkvUtil.init(this);
    }


    /**
     * bugly初始化
     *
     * @param context application
     */
    public static void initBugly(Context context) {
        try {
            CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
            String device = DeviceUtils.getDevice();
            String deviceInfo = "";
            if (!TextUtils.isEmpty(device)) {
                deviceInfo = "Device:" + device;
            }
            strategy.setAppVersion(PackageUtils.getVersionName(context));
            strategy.setAppChannel(PackageUtils.getChannelName(context));
            strategy.setDeviceID(DeviceUtils.getIMEI(context));
            CrashReport.initCrashReport(context, Constant.BUGLY_APPID, BuildConfig.DEBUG, strategy);

        } catch (Throwable e) {
        }
    }


    /**
     * umeng初始化
     *
     * @param context application
     */
    public static void initAnalysis(Context context) {
        try {
            String chanel = PackageUtils.getChannelName(context);
            UMConfigure.setLogEnabled(BuildConfig.DEBUG);
            UMConfigure.init(context, Constant.UMENG_APPKEY, chanel, UMConfigure.DEVICE_TYPE_PHONE, null);
            // 选用LEGACY_AUTO页面采集模式
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.LEGACY_MANUAL);
        } catch (Throwable e) {
        }
    }
}
