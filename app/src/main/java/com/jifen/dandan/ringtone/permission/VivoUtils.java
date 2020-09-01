package com.jifen.dandan.ringtone.permission;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.util.Log;

import com.jifen.dandan.common.utils.LogUtils;

import java.lang.reflect.Method;

public class VivoUtils {
    private static final String TAG = "VivoUtils";

    /**
     * @param context 跳转至权限页面
     */
    public static void goVivoManager(Context context) {
        try {
            Intent localIntent;
            if (((Build.MODEL.contains("Y85")) && (!Build.MODEL.contains("Y85A"))) || (Build.MODEL.contains("vivo Y53L"))) {
                localIntent = new Intent();
                localIntent.setClassName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.PurviewTabActivity");
                localIntent.putExtra("packagename", context.getPackageName());
                localIntent.putExtra("tabId", "1");
                context.startActivity(localIntent);
            } else {
                localIntent = new Intent();
                localIntent.setClassName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity");
                localIntent.setAction("secure.intent.action.softPermissionDetail");
                localIntent.putExtra("packagename", context.getPackageName());
                context.startActivity(localIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            goOldManager(context);

        }

    }

    private static void goOldManager(Context context) {
        try {
            Intent localIntent;
            localIntent = new Intent();
            localIntent.setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.safeguard.SoftPermissionDetailActivity"));
            localIntent.putExtra("packagename", context.getPackageName());
            context.startActivity(localIntent);
        } catch (Exception e) {
            e.printStackTrace();

            PermissionUtils.gotoAppDetailSettingIntent(context);

        }
    }

    public static boolean checkFloatWindowPermission(Context context) {
        return 0 == getFloatWindowPermission(context);
    }

    /**
     * 获取悬浮窗权限状态
     *
     * @param context
     * @return 1或其他是没有打开，0是打开，该状态的定义和{@link AppOpsManager#MODE_ALLOWED}，MODE_IGNORED等值差不多，自行查阅源码
     */
    public static int getFloatWindowPermission(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context is null");
        }
        String packageName = context.getPackageName();
        Uri uri = Uri.parse("content://com.iqoo.secure.provider.secureprovider/allowfloatwindowapp");
        String selection = "pkgname = ?";
        String[] selectionArgs = new String[]{packageName};
        Cursor cursor = context
                .getContentResolver()
                .query(uri, null, selection, selectionArgs, null);
        if (cursor != null) {
            cursor.getColumnNames();
            if (cursor.moveToFirst()) {
                int currentmode = cursor.getInt(cursor.getColumnIndex("currentlmode"));
                cursor.close();
                return currentmode;
            } else {
                cursor.close();
                return getFloatPermissionStatus2(context);
            }

        } else {
            return getFloatPermissionStatus2(context);
        }
    }


    /**
     * vivo比较新的系统获取方法
     *
     * @param context
     * @return
     */
    private static int getFloatPermissionStatus2(Context context) {
        String packageName = context.getPackageName();
        Uri uri2 = Uri.parse("content://com.vivo.permissionmanager.provider.permission/float_window_apps");
        String selection = "pkgname = ?";
        String[] selectionArgs = new String[]{packageName};
        Cursor cursor = context
                .getContentResolver()
                .query(uri2, null, selection, selectionArgs, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int currentmode = cursor.getInt(cursor.getColumnIndex("currentmode"));
                cursor.close();
                return currentmode;
            } else {
                cursor.close();
                return 0;
            }
        }
        return 0;
    }



    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static boolean checkOp(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Class clazz = AppOpsManager.class;
                Method method = clazz.getDeclaredMethod("checkOp", int.class, int.class, String.class);

//                RomUtils.getMethod(clazz);
//                RomUtils.getFieldName(clazz, manager);
                return AppOpsManager.MODE_ALLOWED == (int) method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());
            } catch (Exception e) {
                LogUtils.e(TAG, Log.getStackTraceString(e));
            }
        } else {
            LogUtils.e(TAG, "Below API 19 cannot invoke!");
        }
        return false;
    }

    /**
     * 检测后台弹出页面
     *
     * @param context
     * @return
     */
    public static boolean checkStartForeground(Context context) {
        return 0 == getVivoApplistPermissionStatus(context);

    }


    /**
     * 后台弹出页面
     *
     * @param context
     * @return
     */
    private static int getVivoApplistPermissionStatus(Context context) {
        String packageName = context.getPackageName();
        Uri uri2 = Uri.parse("content://com.vivo.permissionmanager.provider.permission/start_bg_activity");
        String selection = "pkgname = ?";
        String[] selectionArgs = new String[]{packageName};
        Cursor cursor = context
                .getContentResolver()
                .query(uri2, null, selection, selectionArgs, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int currentmode = cursor.getInt(cursor.getColumnIndex("currentstate"));
                LogUtils.e("vivoUtils = " + currentmode);
                cursor.close();
                return currentmode;
            } else {
                cursor.close();
                return 0;
            }
        }
        return 0;
    }


    /**
     * 检测锁屏显示权限
     *
     * @param context
     * @return
     */
    public static boolean checkLockedScreen(Context context) {
        return 0 == getVivoLockedScreenStatus(context);
    }

    /**
     * 锁屏显示
     *
     * @param context
     * @return
     */
    private static int getVivoLockedScreenStatus(Context context) {
        String packageName = context.getPackageName();
        Uri uri2 = Uri.parse("content://com.vivo.permissionmanager.provider.permission/control_locked_screen_action");
        String selection = "pkgname = ?";
        String[] selectionArgs = new String[]{packageName};
        Cursor cursor = context
                .getContentResolver()
                .query(uri2, null, selection, selectionArgs, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int currentmode = cursor.getInt(cursor.getColumnIndex("currentstate"));
                LogUtils.e("vivoUtils = " + currentmode);
                cursor.close();
                return currentmode;
            } else {
                cursor.close();
                return 0;
            }
        }
        return 0;
    }

    /**
     * 获取vivo显示的版本 （非android版本）
     *
     * @return
     */
    public static float getOsVersion() {
        try {
            String a = SystemProperties.getString("ro.vivo.os.build.display.id", "unkonw");
            return Float.parseFloat(a.split("_")[1]);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    /**
     * SDK 23以下
     * @param context
     */
    public static void gotSetOverly(Context context) {
        try {
            Intent localIntent;
            localIntent = new Intent();
            localIntent.setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.MainActivity"));
            context.startActivity(localIntent);

//            if (context instanceof BaseActivity) {
//                showDefaultAppView((BaseActivity) context);
//            }
        } catch (Exception e) {
            e.printStackTrace();
            goVivoManager(context);
        }
    }


//    private static void showDefaultAppView(BaseActivity context) {
//        PermissionNoticeView floatView = new PermissionNoticeView(context, null, null, "悬浮窗");
//        floatView.setPermissionTv("① 点击\"默认应用设置\"\n② 点击\"拨号\"\n③ 选择\"趣铃声\"");
//
//        FloatWindowManager floatWindowManager = new FloatWindowManager();
//        floatWindowManager.showFloatWindow(context, FloatWindowManager.FW_TYPE_ALERT_WINDOW, floatView, null);
//    }

}
