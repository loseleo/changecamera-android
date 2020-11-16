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
package com.beige.camera;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.beige.camera.advertisement.dagger.AdComponentHolder;
import com.beige.camera.common.base.BaseApplication;
import com.beige.camera.common.dagger.component.CommonComponentHolder;
import com.beige.camera.common.helper.activitylife.ActivityLifeManager;
import com.beige.camera.common.helper.activitylife.AppShowListener;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.ProcessUtil;
import com.beige.camera.dagger.MainComponentHolder;
import com.beige.camera.receiver.HomeWatcherReceiver;
import com.beige.camera.receiver.WifiReceiver;
import com.beige.camera.screenlock.ScreenLockService;
import com.beige.camera.screenlock.dagger.LockComponentHolder;
import com.tencent.bugly.crashreport.CrashReport;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zhangning.
 * @date 2016/8/3.
 */
public class MyApplication extends BaseApplication {

    private static MyApplication sInstance;
    public static long appStart;
    public boolean needShowSplashAd = false;

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
        LockComponentHolder.init();
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
                LogUtils.e("zhangning","onAppEnterForeground");
            }

            @Override
            public void onAppReturnForeground() {
                LogUtils.e("zhangning","onAppReturnForeground");
                onAppAllForeground();
            }

            @Override
            public void onAppEnterBackground() {
                needShowSplashAd = true;
                onAppAllForeground();
                LogUtils.e("zhangning","onAppEnterBackground");
            }
        });
    }



    private void onAppAllForeground() {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                ScreenLockService.init(BaseApplication.getsInstance().getBaseContext());

                IntentFilter filter = new IntentFilter();
                filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
                filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
                filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                registerReceiver(new WifiReceiver(), filter);


                HomeWatcherReceiver mHomeKeyReceiver = new HomeWatcherReceiver();
                IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                registerReceiver(mHomeKeyReceiver, homeFilter);

                emitter.onNext(true);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .onErrorReturnItem(false)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean o) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
