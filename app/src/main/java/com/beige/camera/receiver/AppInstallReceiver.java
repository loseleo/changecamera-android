package com.beige.camera.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import com.beige.camera.activity.HomeActivity;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.dialog.ADCommonDialog;

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
        ADCommonDialog commonDialog = ADCommonDialog.newInstance("AppInstallReceiver");
        commonDialog.show(((HomeActivity) activity).getSupportFragmentManager(), "adcommon_dialog");
    }

}
