/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.beige.camera.ringtone.permission;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.text.TextUtils;

import com.beige.camera.common.utils.LogUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Description:
 *
 * @author zhaozp
 * @since 2016-05-23
 */
public class RomUtils {
    private static final String TAG = "RomUtils";

    /**
     * 获取 emui 版本号
     *
     * @return
     */
    public static double getEmuiVersion() {
        try {
            String emuiVersion = getSystemProperty("ro.build.version.emui");
            String version = emuiVersion.substring(emuiVersion.indexOf("_") + 1);
            return Double.parseDouble(version);
        } catch (Exception e) {
            LogUtils.e(TAG, e);
        }
        return 0.0;
    }

    /**
     * 获取小米 rom 版本号，获取失败返回 -1
     *
     * @return miui rom version code, if fail , return -1
     */
    public static int getMiuiVersion() {
        String version = getSystemProperty("ro.miui.ui.version.name");
        if (version != null) {
            try {
                return Integer.parseInt(version.substring(1));
            } catch (Exception e) {
                LogUtils.e(TAG, "get miui version code error, version : " + version);
            }
        }
        return -1;
    }

    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            LogUtils.e(TAG + "Unable to read sysprop " + propName, ex);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LogUtils.e(TAG + "Exception while closing InputStream", e);
                }
            }
        }
        return line;
    }

    public static boolean checkIsOnePlusRom() {
        return Build.MANUFACTURER.contains("OnePlus");
    }

    public static boolean checkIsHuaweiRom() {
        return Build.MANUFACTURER.contains("HUAWEI");
    }

    public static boolean checkIsNova4(){
        return checkIsHuaweiRom() && !Build.MODEL.contains("HUAWEI");
    }


    public static boolean checkPlay() {
        return checkIsNova4() || checkIsSamsunRom() || isMiuiAnd5A() || isRedmiNote5A();
    }

    public static boolean isMiuiAnd5A() {
        return Build.MANUFACTURER.contains("Xiaomi") && Build.MODEL.contains("Redmi 5A");
    }

    public static boolean isRedmiNote5A() {
        return  Build.MODEL.contains("Redmi Note 5A");
    }



    /**
     * check if is miui ROM
     */
    public static boolean checkIsMiuiRom() {
        return !TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.name"));
    }

    public static boolean isVivoY71() {
        return checkIsVivoRom() && Build.MODEL.contains("Y71");
    }
    /**
     *
     * @return
     */
    public static boolean isMaiMang() {
        return checkIsHuaweiRom() && Build.MODEL.contains("RNE-AL00");
    }

    public static boolean checkIsMeizuRom() {
        //return Build.MANUFACTURER.contains("Meizu");
        String meizuFlymeOSFlag = getSystemProperty("ro.build.display.id");
        if (TextUtils.isEmpty(meizuFlymeOSFlag)) {
            return false;
        } else if (meizuFlymeOSFlag.contains("flyme") || meizuFlymeOSFlag.toLowerCase().contains("flyme")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkIs360Rom() {
        //fix issue https://github.com/zhaozepeng/FloatWindowPermission/issues/9
        return Build.MANUFACTURER.contains("QiKU")
                || Build.MANUFACTURER.contains("360");
    }

    public static boolean checkIsOppoRom() {
        //https://github.com/zhaozepeng/FloatWindowPermission/pull/26
        return Build.MANUFACTURER.contains("OPPO") || Build.MANUFACTURER.contains("oppo");
    }

    public static boolean checkIsVivoRom() {
        return Build.MANUFACTURER.contains("vivo") || Build.MANUFACTURER.contains("VIVO");
    }

    public static boolean checkIsSamsunRom() {
        return Build.MANUFACTURER.contains("samsung");
    }

    public static boolean isVivoBrand() {
        return Build.BRAND.toLowerCase().contains("vivo");
    }

    /**
     * 判断小米MIUI系统中授权管理中对应的权限授取
     *
     * @return false 存在核心的未收取的权限   true 核心权限已经全部授权
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean hasMiuiPermission(Context context) {
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int locationOp = appOpsManager.checkOp(AppOpsManager.OPSTR_FINE_LOCATION, Binder.getCallingUid(), context.getPackageName());
        if (locationOp == AppOpsManager.MODE_IGNORED) {
            return false;
        }
        return true;
    }


    /**
     * 跳转到权限设置界面
     */
    public static void getAppDetailSettingIntent(Context context) {
        try {
            // vivo 点击设置图标>加速白名单>我的app
            //      点击软件管理>软件管理权限>软件>我的app>信任该软件
            Intent appIntent = context.getPackageManager().getLaunchIntentForPackage("com.iqoo.secure");
            if (appIntent != null) {
                context.startActivity(appIntent);
                return;
            }

            // oppo 点击设置图标>应用权限管理>按应用程序管理>我的app>我信任该应用
            //      点击权限隐私>自启动管理>我的app
            appIntent = context.getPackageManager().getLaunchIntentForPackage("com.oppo.safe");
            if (appIntent != null) {
                context.startActivity(appIntent);
                return;
            }

            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 9) {
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", context.getPackageName(), null));
            } else if (Build.VERSION.SDK_INT <= 8) {
                intent.setAction(Intent.ACTION_VIEW);
                intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }



    public static void getMethod(Class<?> clazz) {
        try {
            //获取本类的所有方法，存放入数组
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                LogUtils.e("********** 方法名：" + method.getName());
                //获取本方法所有参数类型，存入数组
                Class<?>[] getTypeParameters = method.getParameterTypes();
                if (getTypeParameters.length == 0) {
                    LogUtils.e("此方法无参数");
                }
                for (Class<?> class1 : getTypeParameters) {
                    String parameterName = class1.getName();
                    LogUtils.e("参数类型：" + parameterName);
                }
                LogUtils.e("****************************");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getFieldName(Class<?> clazz, Object object) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String descriptor = Modifier.toString(field.getModifiers());//获得其属性的修饰
            descriptor = descriptor.equals("") == true ? "" : descriptor + " ";
            try {
                LogUtils.e("************ 字段 " + descriptor + field.getName() + "=" + field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断 Intent 是否有效
     *
     * @param context
     * @param intent
     * @return
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        if (intent == null) {
            return false;
        }
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    /**
     * 跳转到甚至页面
     *
     * @param context
     */
    public static boolean gotoDefaultSettingApp(Context context) {
        boolean ret = false;
        if (context != null) {
            Intent intent = new Intent("android.settings.MANAGE_DEFAULT_APPS_SETTINGS");
            if (isIntentAvailable(context, intent)) {
                context.startActivity(intent);
                ret = true;
            }
        }
        return ret;
    }
}
