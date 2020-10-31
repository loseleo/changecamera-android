package com.beige.camera.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.beige.camera.advertisement.api.bean.AdConfigBean;
import com.beige.camera.advertisement.dagger.AdComponentHolder;
import com.beige.camera.common.base.BaseApplication;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.RxUtil;
import com.beige.camera.floatwindow.FloatWindowService;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.beige.camera.common.utils.RxUtil.io_main;

public class AppInstallReceiver extends BroadcastReceiver {

    private static final String TAG = "AppInstallReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        PackageManager manager = context.getPackageManager();
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            LogUtils.e(TAG, "安装成功" + packageName);
            showAD();
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            LogUtils.e(TAG, "卸载成功" + packageName);
            showAD();
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            LogUtils.e(TAG, "替换成功" + packageName);
            showAD();
        }
    }
    public static long startTime ;

    private void showAD(){

        if(System.currentTimeMillis() - startTime < 1000 * 60 ){
            return;
        }
        startTime = System.currentTimeMillis();

        AdComponentHolder.getComponent().getAdApi().getAdConfig("Unload_feeds")
                .compose(io_main())
                .timeout(2000, TimeUnit.MILLISECONDS)
                .map(RxUtil.applyApiResult())
                .subscribe(new Observer<AdConfigBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(AdConfigBean adModel) {
                        LogUtils.e("TTInfoFlowAd", "adConfigBean = " + adModel.getCandidates().toString());
                        if(adModel != null && adModel.getCandidates().size() >0 ){
                            Context baseContext = BaseApplication.getsInstance().getBaseContext();
                            Intent intent = new Intent(baseContext, FloatWindowService.class);
                            intent.setAction(FloatWindowService.ACTION_FULLSCREEN_AD);
                            intent.putExtra("admodel",(Serializable) adModel.getCandidates());
                            baseContext.startService(intent);
                        }
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
