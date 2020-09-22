/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.beige.camera.advertisement.permission;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.MsgUtils;

import java.lang.reflect.Method;

public class MiuiUtils {
    private static final String TAG = "MiuiUtils";

    /**
     * 获取小米 rom 版本号，获取失败返回 -1
     *
     * @return miui rom version code, if fail , return -1
     */
    public static int getMiuiVersion() {
        String version = RomUtils.getSystemProperty("ro.miui.ui.version.name");
        if (version != null) {
            try {
                return Integer.parseInt(version.substring(1));
            } catch (Exception e) {
                LogUtils.e(TAG, "get miui version code error, version : " + version);
                LogUtils.e(TAG, Log.getStackTraceString(e));
            }
        }
        return -1;
    }

    /**
     * 检测 miui 悬浮窗权限
     */
    public static boolean checkFloatWindowPermission(Context context) {
        final int version = Build.VERSION.SDK_INT;

        if (version >= 19) {
            return checkOp(context, 24); //OP_SYSTEM_ALERT_WINDOW = 24;
        } else {
//            if ((context.getApplicationInfo().flags & 1 << 27) == 1) {
//                return true;
//            } else {
//                return false;
//            }
            return true;
        }
    }

    /**
     * 检测后台弹出页面
     *
     * @param context
     * @return
     */
    public static boolean checkStartForeground(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            boolean ret = getStatusByOp(context, 10021);
            LogUtils.e(" checkStartForeground =  " + ret + " op 76 = ");
            return ret; //OP_START_FOREGROUND = 76;
        }
        return true;
    }

    /**
     * 检测锁屏显示权限
     * @param context
     * @return
     */
    public static boolean checkLockedScreen(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            boolean ret = getStatusByOp(context, 10020);
            LogUtils.e(" checkStartForeground =  " + ret + " op 76 = ");
            return ret; //OP_START_FOREGROUND = 76;
        }
        return true;
    }

    private static boolean getStatusByOp(Context context, int op) {
        try {
            AppOpsManager ops = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ops = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            }
            Method method = ops.getClass().getMethod("checkOpNoThrow", new Class[]
                    {
                            int.class, int.class, String.class
                    }
            );
            Integer result = (Integer) method.invoke(ops, op, Binder.getCallingUid(), context.getPackageName());
            return result == AppOpsManager.MODE_ALLOWED;
        } catch (Exception e) {
            Log.e(TAG, "not support", e);
        }
        return false;
    }



    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static boolean checkOp(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Class clazz = AppOpsManager.class;
                Method method = clazz.getDeclaredMethod("checkOp", int.class, int.class, String.class);
                return AppOpsManager.MODE_ALLOWED == (int) method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());
            } catch (Exception e) {
                LogUtils.e(TAG, Log.getStackTraceString(e));
            }
        } else {
            LogUtils.e(TAG, "Below API 19 cannot invoke!");
        }
        return false;
    }

    private static boolean isIntentAvailable(Intent intent, Context context) {
        if (intent == null) {
            return false;
        }
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }


    public static void toPermisstionSetting(Context context){
        try {
            int rom = getMiuiVersion();
            Intent intent = null;
            if (5 == rom) {
                Uri packageURI = Uri.parse("package:" + context.getApplicationInfo().packageName);
                intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
            } else if (rom == 6 || rom == 7) {
                intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                intent.putExtra("extra_pkgname", context.getPackageName());
            } else if (rom > 8) {
                intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
                intent.putExtra("extra_pkgname", context.getPackageName());
            } else {
                RomUtils.getAppDetailSettingIntent(context);
                return;
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            MsgUtils.showToastCenter(context, "请到系统设置打开权限");
        }

    }

}
