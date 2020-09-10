/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.beige.camera.ringtone.permission;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.util.Log;

import com.beige.camera.common.utils.LogUtils;

import java.lang.reflect.Method;

public class QikuUtils {
    private static final String TAG = "QikuUtils";

    /**
     * 检测 360 悬浮窗权限
     */
    public static boolean checkFloatWindowPermission(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            return checkOp(context, 24); //OP_SYSTEM_ALERT_WINDOW = 24;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static boolean checkOp(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Class clazz = AppOpsManager.class;
                Method method = clazz.getDeclaredMethod("checkOp", int.class, int.class, String.class);
                return AppOpsManager.MODE_ALLOWED == (int)method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());
            } catch (Exception e) {
                LogUtils.e(TAG, Log.getStackTraceString(e));
            }
        } else {
            LogUtils.e("", "Below API 19 cannot invoke!");
        }
        return false;
    }

    /**
     * 去设置悬浮框
     * @param context
     */
    public static void gotoSetOverlay(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.android.settings", "com.android.settings.Settings$OverlaySettingsActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            intent.setClassName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity");
            if (isIntentAvailable(intent, context)) {
                context.startActivity(intent);
            } else {
                LogUtils.e(TAG, "can't open permission page with particular name, please use " +
                        "\"adb shell dumpsys activity\" command and tell me the name of the float window permission page");
            }
        }
    }

    private static boolean isIntentAvailable(Intent intent, Context context) {
        if (intent == null) {
            return false;
        }
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    /**
     * 检测后台弹出页面
     *
     * @param context
     * @return
     */
    public static boolean checkStartForeground(Context context) {
//        final int version = Build.VERSION.SDK_INT;
//        if (version >= 19) {
//            return checkOp(context, 76); //OP_START_FOREGROUND = 76;
//        }
        return true;

    }


    /**
     * 只能打开到自带安全软件
     * @param activity
     */
    public static void gotoDefaultSetting(Context activity) {
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", activity.getPackageName());
            ComponentName comp = new ComponentName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity");
            intent.setComponent(comp);
            activity.startActivity(intent);
        }catch (Exception e){
            PermissionUtils.gotoAppDetailSettingIntent(activity);
//            activity.startActivityForResult(getAppDetailSettingIntent(activity), PERMISSION_SETTING_FOR_RESULT);
        }
    }

    /**
     * 检测锁屏显示权限
     * @param context
     * @return
     */
    public static boolean checkLockedScreen(Context context) {
        return true;
    }


}
