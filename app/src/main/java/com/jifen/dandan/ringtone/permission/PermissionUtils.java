package com.jifen.dandan.ringtone.permission;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.jifen.dandan.common.utils.LogUtils;
import com.jifen.dandan.common.utils.MsgUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;

/**
 * Created by liyong on 2018/9/21.
 */
public class PermissionUtils {

    public static final String TAG = "PermissionUtils";

    /**
     * 检查悬浮窗权限
     *
     * @param context
     * @return
     */
    public static boolean checkFloatWindowPermission(Context context) {
        //6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                return MiuiUtils.checkFloatWindowPermission(context);
            } else if (RomUtils.checkIsMeizuRom()) {
                return MeizuUtils.checkFloatWindowPermission(context);
            } else if (RomUtils.checkIsHuaweiRom()) {
                return HuaweiUtils.checkFloatWindowPermission(context);
            } else if (RomUtils.checkIs360Rom()) {
                return QikuUtils.checkFloatWindowPermission(context);
            } else if (RomUtils.checkIsOppoRom()) {
                return OppoUtils.checkFloatWindowPermission(context);
            } else if (RomUtils.checkIsVivoRom()) {
                return VivoUtils.checkFloatWindowPermission(context);
            } else {
                return true;
            }
        }
        return commonROMPermissionCheck(context);
    }

    /**
     * 打开悬浮框权限
     *
     * @param context
     */
    public static void gotoCanOverly(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (RomUtils.checkIsVivoRom()) {
                if (Build.VERSION.SDK_INT == 23) {
                    VivoUtils.goVivoManager(context);
                } else if (RomUtils.isVivoY71()) {
                    PermissionUtils.gotoAppDetailSettingIntent(context);
                }
            } else {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + context.getPackageName()));
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    PermissionUtils.gotoAppDetailSettingIntent(context);
                }
            }

        } else {
            if (RomUtils.checkIsMiuiRom()) {
                MiuiUtils.toPermisstionSetting(context);
            } else if (RomUtils.checkIsMeizuRom()) {
                MeizuUtils.applyPermission(context);
            } else if (RomUtils.checkIsHuaweiRom()) {
                HuaweiUtils.gotoOverlySetPage(context);
            } else if (RomUtils.checkIs360Rom()) {
                QikuUtils.gotoSetOverlay(context);
            } else if (RomUtils.checkIsOppoRom()) {
                OppoUtils.gotoOverly(context);
            } else if (RomUtils.checkIsVivoRom()) {
                VivoUtils.gotSetOverly(context);
            } else {
                PermissionUtils.gotoAppDetailSettingIntent(context);
            }
        }
    }

    public static boolean hasPermission(@NonNull Context context, @NonNull String permission) {
        List<String> permisstions = new ArrayList<>();
        permisstions.add(permission);
        return hasPermission(context, permisstions);
    }

    /**
     * 悬浮框
     *
     * @param context
     * @return
     */
    private static boolean hasCanOverly(Context context) {
        boolean ret;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ret = Settings.canDrawOverlays(context.getApplicationContext());
        } else {
            ret = true;
        }
        return ret;
    }


    /**
     * 系统层的权限判断
     *
     * @param context     上下文
     * @param permissions 申请的权限 Manifest.permission.READ_CONTACTS
     * @return 是否有权限 ：其中有一个获取不了就是失败了
     */
    public static boolean hasPermission(@NonNull Context context, @NonNull List<String> permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;
        for (String permission : permissions) {
            try {
                String op = AppOpsManagerCompat.permissionToOp(permission);
                if (TextUtils.isEmpty(op)) continue;
                int result = AppOpsManagerCompat.noteOp(context, op, android.os.Process.myUid(), context.getPackageName());
                if (result == AppOpsManagerCompat.MODE_IGNORED) return false;
                AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                String ops = AppOpsManager.permissionToOp(permission);
                int locationOp = appOpsManager.checkOp(ops, Binder.getCallingUid(), context.getPackageName());
                if (locationOp == AppOpsManager.MODE_IGNORED) return false;
                result = ContextCompat.checkSelfPermission(context, permission);
                if (result != PackageManager.PERMISSION_GRANTED) return false;
            } catch (Exception ex) {
                LogUtils.e(TAG + "[hasPermission] error ", ex);
            }
        }
        return true;
    }

    /**
     * 跳转到权限设置界面
     *
     * @param context
     */
    public static void toPermissionSetting(Context context) {
        try {
            if (RomUtils.checkIsHuaweiRom()) {
                //华为
                HuaweiUtils.applyPermission(context);

            } else if (RomUtils.checkIsMeizuRom()) {
                //魅族
                MeizuUtils.applyPermission(context);

            } else if (RomUtils.checkIsMiuiRom()) {
                //小米
                MiuiUtils.toPermisstionSetting(context);

            } else if (RomUtils.checkIsOppoRom()) {
                //oppo
                OppoUtils.applyOppoPermission(context);

            } else if (RomUtils.checkIs360Rom()) {
                //360
                QikuUtils.gotoDefaultSetting(context);
            } else if (RomUtils.checkIsVivoRom()) {
                //vivo
                VivoUtils.goVivoManager(context);
            } else {
                gotoAppDetailSettingIntent(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
            gotoAppDetailSettingIntent(context);
        }

    }

    private static boolean commonROMPermissionCheck(Context context) {
        //最新发现魅族6.0的系统这种方式不好用，天杀的，只有你是奇葩，没办法，单独适配一下
        if (RomUtils.checkIsMeizuRom()) {
            return MeizuUtils.checkFloatWindowPermission(context);
        } else if (RomUtils.checkIsVivoRom() && (Build.VERSION.SDK_INT == 23 || RomUtils.isVivoY71())) {
            return VivoUtils.checkFloatWindowPermission(context);
        } else {
//            Boolean result = ;
//            if (Build.VERSION.SDK_INT >= 23) {
//                try {
//                    Class clazz = Settings.class;
//                    Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
//                    result = (Boolean) canDrawOverlays.invoke(null, context);
//                } catch (Exception e) {
//                    LogUtils.e(TAG, Log.getStackTraceString(e));
//                }
//            }
            return hasCanOverly(context);
        }
    }


    public static final int PERMISSION_WRITE_SDCARD = 484;

    public static boolean checkPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    public static void requestPermission(Activity activity, String permission, int requestCode) {
        if (checkPermission(activity, permission)) {
            return;
        }
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
    }

//    // 1：打开 悬浮框权限，跳转到设置页面，2：打开修改系统设置权限 跳转到设置页面 3：打开读写SDCARD 权限 弹窗
//    public static boolean isPermissionGranted(Context context, String permission) {
//        if (context == null || TextUtils.isEmpty(permission)) {
//            return false;
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
//        }
//        return true;
//    }

    /**
     * 修改系统权限
     *
     * @param context
     * @return
     */
    public static boolean hasSettingPermisson(Context context) {
        boolean ret;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ret = Settings.System.canWrite(context);
        } else {
            ret = true;
        }
        return ret;
    }

    /**
     * 通知栏权限
     *
     * @param context
     * @return
     */
    public static boolean haskNotifySetting(Context context) {
        if (context != null) {
            return NotificationManagerCompat.from(context).areNotificationsEnabled();
        }
        // areNotificationsEnabled方法的有效性官方只最低支持到API 19，低于19的仍可调用此方法不过只会返回true，即默认为用户已经开启了通知。
        return false;
    }

    /**
     * 监听通知权限
     *
     * @return
     */
    public static boolean isNotificationListenersEnabled(Context context) {
        String pkgName = context.getPackageName();
        final String flat = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 设置通知监听权限
     *
     * @param context
     * @return
     */
    public static boolean gotoNotificationAccessSetting(Context context) {
        try {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;

        } catch (ActivityNotFoundException e) {//普通情况下找不到的时候需要再特殊处理找一次
            try {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.Settings$NotificationAccessSettingsActivity");
                intent.setComponent(cn);
                intent.putExtra(":settings:show_fragment", "NotificationAccessSettings");
                context.startActivity(intent);
                return true;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            MsgUtils.showToast(context, "对不起，您的手机暂不支持");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 检查是否开启后台弹出页面权限
     *
     * @param context
     */
    public static boolean checkStartForeground(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= 19) {
                if (RomUtils.checkIsMiuiRom()) {
                    return MiuiUtils.checkStartForeground(context);
                } else if (RomUtils.checkIsMeizuRom()) {
                    return MeizuUtils.checkStartForeground(context);
                } else if (RomUtils.checkIsHuaweiRom()) {
                    return HuaweiUtils.checkStartForeground(context);
                } else if (RomUtils.checkIs360Rom()) {
                    return QikuUtils.checkStartForeground(context);
                } else if (RomUtils.checkIsOppoRom()) {
                    return OppoUtils.checkStartForeground(context);
                } else if (RomUtils.checkIsVivoRom() && Build.VERSION.SDK_INT > 23) {
                    return VivoUtils.checkStartForeground(context);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }


    /**
     * 检查是否开启锁屏显示权限
     *
     * @param context
     */
    public static boolean checkLockedScreen(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= 19) {
                if (RomUtils.checkIsMiuiRom()) {
                    return MiuiUtils.checkLockedScreen(context);
                } else if (RomUtils.checkIsMeizuRom()) {
                    return MeizuUtils.checkLockedScreen(context);
                } else if (RomUtils.checkIsHuaweiRom()) {
                    return HuaweiUtils.checkLockedScreen(context);
                } else if (RomUtils.checkIs360Rom()) {
                    return QikuUtils.checkLockedScreen(context);
                } else if (RomUtils.checkIsOppoRom()) {
                    return OppoUtils.checkLockedScreen(context);
                } else if (RomUtils.checkIsVivoRom()) {
                    return VivoUtils.checkLockedScreen(context);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * @param context
     * @return
     */
    public static boolean hasNetworkHistoryPermission(Context context) {
        if (context != null) {
            AppOpsManager appOps = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        android.os.Process.myUid(), context.getPackageName());
                if (mode == AppOpsManager.MODE_ALLOWED) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void requestReadNetworkHistoryAccess(final Context context) {
        final AppOpsManager appOps;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            appOps.startWatchingMode(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    context.getApplicationContext().getPackageName(),
                    new AppOpsManager.OnOpChangedListener() {
                        @Override
                        public void onOpChanged(String op, String packageName) {
                            int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                                    android.os.Process.myUid(), context.getPackageName());
                            if (mode != AppOpsManager.MODE_ALLOWED) {
                                return;
                            }
                            appOps.stopWatchingMode(this);
                        }
                    });
        }
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * 设置修改系统权限
     *
     * @param context
     */
    public static void gotoSettingWriteSetting(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                    Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 打开常驻通知权限
     *
     * @param context
     */
    public static void gotoSettingNotify(Context context) {
        if (context == null) {
            return;
        }

        Intent localIntent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            localIntent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            localIntent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            localIntent.putExtra("app_package", context.getPackageName());
            localIntent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.addCategory(Intent.CATEGORY_DEFAULT);
            localIntent.setData(Uri.parse("package:" + context.getPackageName()));
        } else {
            ///< 4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面,
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 19) {
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
            } else if (Build.VERSION.SDK_INT <= 18) {
                localIntent.setAction(Intent.ACTION_VIEW);
                localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
                localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
            }
        }

        try {
            context.startActivity(localIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取应用详情页面
     *
     * @return
     */
    public static void gotoAppDetailSettingIntent(Context context) {
        try {
            Intent localIntent = new Intent();
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.setAction(Intent.ACTION_VIEW);
                localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
            }
            context.startActivity(localIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * rxjava permission
     * @param activity
     * @param permissions
     * @param observer
     */
    public static void forEach(FragmentActivity activity, String[] permissions, Observer<Permission> observer) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.requestEach(permissions).subscribe(observer);
    }

}
