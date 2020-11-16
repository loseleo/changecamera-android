package com.beige.camera.screenlock;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.beige.camera.advertisement.api.bean.AdConfigBean;
import com.beige.camera.advertisement.dagger.AdComponentHolder;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.RxUtil;
import com.beige.camera.screenlock.activity.LockReaderActivity;
import com.beige.camera.screenlock.api.Api;
import com.beige.camera.screenlock.dagger.LockComponentHolder;
import com.tencent.bugly.crashreport.CrashReport;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

import static com.beige.camera.common.utils.RxUtil.io_main;

public class ScreenLockService extends Service {


    private static WeakReference<ScreenLockService> instanceRef;
    private PublishSubject<Boolean> stopPreloadSubject = PublishSubject.create();
    public static final int PRELOAD_STATE_IDLE = 0;
    public static final int PRELOAD_STATE_LOADInG = 1;
    public static final int PRELOAD_STATE_SUCCESS = 2;
    public static final int PRELOAD_STATE_FAIL = 3;
    private int preloadState = PRELOAD_STATE_IDLE;
    private  AdConfigBean adModel;

    @Inject
    Api api;

    public static void init(Context context) {
        try {
            Intent intent = new Intent(context, ScreenLockService.class);
            context.startService(intent);
        } catch (Exception e) {
            CrashReport.postCatchedException(e);
        }
    }

    public static void stop() {

        if (instanceRef == null) {
            return;
        }
        ScreenLockService lockService = instanceRef.get();
        if (lockService == null) {
            return;
        }
        lockService.stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LockComponentHolder.get().inject(this);
        instanceRef = new WeakReference<>(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        registerReceiver(mScreenReceiver, filter);
    }

    // 注：服务被系统自动回收之后重启时，intent会为null
    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        preloadLockData();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mScreenReceiver);
    }

    private BroadcastReceiver mScreenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_OFF.equals(action)) {

                if (preloadState != PRELOAD_STATE_SUCCESS || adModel == null ) {
                    return;
                }
                Intent startIntent = new Intent(context, LockReaderActivity.class);
                startIntent.setPackage(context.getPackageName());
                startIntent.putExtra("dataList", adModel);
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    startActivity(startIntent);
                }catch (Throwable e){
                    e.printStackTrace();
                    //ignore
                }
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {

                KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                if (keyguardManager != null && keyguardManager.isKeyguardSecure()) {
                    LockReaderActivity activity = LockReaderActivity.getInstance();
                    if (activity != null) {
                        activity.finish();
                    }
                }
                preloadState = PRELOAD_STATE_IDLE;
                preloadLockData();
            }
        }
    };


    private void preloadLockData() {

        if (preloadState == PRELOAD_STATE_SUCCESS || preloadState == PRELOAD_STATE_LOADInG) {
            return;
        }
        preloadState = PRELOAD_STATE_LOADInG;
        AdComponentHolder.getComponent().getAdApi().getAdConfig("LockScreen_feeds")
                .compose(io_main())
                .timeout(2000, TimeUnit.MILLISECONDS)
                .map(RxUtil.applyApiResult())
                .subscribe(new Observer<AdConfigBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(AdConfigBean adModel) {
                        if(adModel!= null && adModel.getCandidates().size()>0){
                            ScreenLockService.this.adModel = adModel;
                            preloadState = PRELOAD_STATE_SUCCESS;
                        }else{
                            preloadState = PRELOAD_STATE_FAIL;
                        }

                        LogUtils.e("TTInfoFlowAd", "adConfigBean = " + adModel.getCandidates().toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        preloadState = PRELOAD_STATE_FAIL;
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }
}
