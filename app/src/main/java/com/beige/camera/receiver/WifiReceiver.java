package com.beige.camera.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

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

public class WifiReceiver extends BroadcastReceiver {
    private static final String TAG = "wifiReceiver";
    public static NetworkInfo.State lastDisconnected;
    public static long startTime ;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {

            LogUtils.i(TAG, "wifi信号强度变化");
        }
        //wifi连接上与否
        if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {

            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                LogUtils.i(TAG, "wifi断开");
                showAD();
            } else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
//                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                //获取当前wifi名称
            }
        }
        //wifi打开与否
        if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
            if (wifistate == WifiManager.WIFI_STATE_DISABLED) {
                LogUtils.i(TAG, "系统关闭wifi");
            } else if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
                LogUtils.i(TAG, "系统开启wifi");
            }
        }
    }

    private void showAD(){

        if(System.currentTimeMillis() - startTime < 60 *1000 ){
            return;
        }
        startTime = System.currentTimeMillis();
        LogUtils.i(TAG, "can show ad");

        AdComponentHolder.getComponent().getAdApi().getAdConfig("WiFi_feeds")
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
                            intent.setAction(FloatWindowService.ACTION_WIFI_AD);
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
