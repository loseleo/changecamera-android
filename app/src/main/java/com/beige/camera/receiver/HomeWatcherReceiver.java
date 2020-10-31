package com.beige.camera.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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

public class HomeWatcherReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = " TAG HomeReceiver";

    public static long startTime ;

    private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    //action内的某些reason
    private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";//home键旁边的最近程序列表键
    private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";//按下home键
    private static final String SYSTEM_DIALOG_REASON_LOCK = "lock";//锁屏键
    private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";//某些三星手机的程序列表键

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
//        App app = (App) context.getApplicationContext();
        LogUtils.e(LOG_TAG, "onReceive: action: " + action);
        if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {//Action
            // android.intent.action.CLOSE_SYSTEM_DIALOGS
            String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            LogUtils.e(LOG_TAG, "reason: " + reason);

            if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) { // 短按Home键
                //可以在这里实现关闭程序操作。。。
                LogUtils.e(LOG_TAG, "homekey");
                showAD();
            } else if (SYSTEM_DIALOG_REASON_RECENT_APPS.equals(reason)) {//Home键旁边的显示最近的程序的按钮
                // 长按Home键 或者 activity切换键
                LogUtils.e(LOG_TAG, "long press home key or activity switch");
            } else if (SYSTEM_DIALOG_REASON_LOCK.equals(reason)) {  // 锁屏，似乎是没有反应，监听Intent.ACTION_SCREEN_OFF这个Action才有用
                LogUtils.e(LOG_TAG, "lock");
            } else if (SYSTEM_DIALOG_REASON_ASSIST.equals(reason)) {   // samsung 长按Home键
                LogUtils.e(LOG_TAG, "assist");
            }

        }
    }


    private void showAD(){

        if(System.currentTimeMillis() - startTime <  1000 ){
            return;
        }
        startTime = System.currentTimeMillis();
        LogUtils.i(LOG_TAG, "can show ad");

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