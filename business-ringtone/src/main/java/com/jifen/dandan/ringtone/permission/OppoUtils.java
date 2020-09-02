package com.jifen.dandan.ringtone.permission;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.util.Log;

import com.jifen.dandan.common.utils.LogUtils;

import java.lang.reflect.Method;

/**
 * Description:
 *
 * @author Shawn_Dut
 * @since 2018-02-01
 */
public class OppoUtils {

    private static final String TAG = "OppoUtils";

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
                return AppOpsManager.MODE_ALLOWED == (int) method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());
            } catch (Exception e) {
                LogUtils.e(TAG, Log.getStackTraceString(e));
            }
        } else {
            LogUtils.e(TAG, "Below API 19 cannot invoke!");
        }
        return false;
    }

    public static void gotoOverly(Context context) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //com.coloros.safecenter/.sysfloatwindow.FloatWindowListActivity
            ComponentName comp = new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.sysfloatwindow.FloatWindowListActivity");//悬浮窗管理页面
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (Exception e) {
            LogUtils.e(TAG + "", e);
        }
    }

    /**
     * 系统权限页面
     */
    public static void applyOppoPermission(Context context) {
        try {
            showActivity(context, "com.coloros.phonemanager");
        } catch (Exception e1) {
            try {
                showActivity(context,"com.oppo.safe");
            } catch (Exception e2) {
                try {
                    showActivity(context,"com.coloros.oppoguardelf");
                } catch (Exception e3) {
                    try {
                        showActivity(context,"com.coloros.safecenter");

                    } catch (Exception e4) {
                        gotoDefaultSetting(context);
                    }
                }
            }
        }
    }

    /**
     * OPPO
     */
    private static void gotoDefaultSetting(Context context) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", context.getPackageName());
            ComponentName comp = new ComponentName("com.color.safecenter", "com.color.safecenter.permission.PermissionManagerActivity");
            intent.setComponent(comp);
            context.startActivity(intent);
        }catch (Exception e){
            PermissionUtils.gotoAppDetailSettingIntent(context);
        }
    }


    private static void showActivity(Context context, String action) {
        Intent intent = new Intent(action);
        context.startActivity(intent);
    }

    /**
     * 检测后台弹出页面
     *
     * @param context
     * @return
     */
    public static boolean checkStartForeground(Context context) {
        final int version = Build.VERSION.SDK_INT;
//        if (version >= 19) {
//            return checkOp(context, 76); //OP_START_FOREGROUND = 76;
//        }
        return true;

    }

    /**
     * 检测锁屏显示权限
     * @param context
     * @return
     */
    public static boolean checkLockedScreen(Context context) {
        return true;
    }

    public static boolean isA57() {
        return RomUtils.checkIsOppoRom() && (Build.MODEL.contains("A57") || Build.MODEL.contains("R9S") || Build.MODEL.contains("A59S"));
    }

}
