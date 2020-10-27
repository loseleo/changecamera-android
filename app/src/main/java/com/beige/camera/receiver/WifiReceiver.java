package com.beige.camera.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.beige.camera.R;
import com.beige.camera.activity.HomeActivity;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.dialog.ADWifiDialog;

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

        if(System.currentTimeMillis() - startTime < 2000 ){
            return;
        }
        startTime = System.currentTimeMillis();
        Activity activity = HomeActivity.mHomeActivity;
        if(activity == null){
            LogUtils.i(TAG, "topActivity is null");
            return;
        }
        LogUtils.i(TAG, "can show ad");

        ADWifiDialog commonDialog = ADWifiDialog.newInstance("WifiReceiver");
        commonDialog.setTvTitle("温馨提示");
        commonDialog.setTvBtnConfirm("继续使用");
        commonDialog.setTvBtnCancel("切换网络");
        commonDialog.setTvContent("检查到当前Wi-Fi状态不加，是否要切换到移动网络");
        commonDialog.setIcon(R.mipmap.icon_wifi_error);
        commonDialog.setOnChoiceListener(new ADWifiDialog.OnChoiceListener() {
            @Override
            public void onAgree() {

            }

            @Override
            public void onDisagree() {
                Intent intent = null;
                //判断手机系统的版本  即API大于10 就是3.0或以上版本
                if(android.os.Build.VERSION.SDK_INT>10){
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                }else{
                    intent = new Intent();
                    ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                activity.startActivity(intent);

            }
        });
        commonDialog.show(((HomeActivity) activity).getSupportFragmentManager(), "adwifi_dialog");

    }
}
