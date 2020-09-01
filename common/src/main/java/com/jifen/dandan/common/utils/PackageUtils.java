package com.jifen.dandan.common.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;

import com.jifen.dandan.common.base.BaseApplication;
import com.meituan.android.walle.ChannelInfo;
import com.meituan.android.walle.WalleChannelReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * apk安装包工具类
 */
public class PackageUtils {

    private static String sChannelName = null;

    /**
     * 获取渠道名称
     */
    public static String getChannelName(Context context) {
        if (!TextUtils.isEmpty(sChannelName)) {
            return sChannelName;
        } else {
            String chanel =  MmkvUtil.getInstance().getString("ChannelName", "");
            sChannelName = chanel;
            if (TextUtils.isEmpty(chanel)) {
                ChannelInfo channelInfo = WalleChannelReader.getChannelInfo(context);
                if (channelInfo != null) {
                    chanel = channelInfo.getChannel();
                    MmkvUtil.getInstance().putString("ChannelName", chanel);
                    return chanel;
                }

                if (TextUtils.isEmpty(chanel)) {
                    chanel = "ErrorChannel";
                }
            }
            return chanel;
        }
    }

    /**
     * 安装apk
     *
     * @param context mContext
     * @param apkPath apkPath
     */
    public static void installApk(Context context, String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(apkPath)),
                "application/vnd.android.package-archive");
        /**
         * Context中有startActivity方法，Activity继承自Context，重载了startActivity方法。如果使用
         * Activity的startActivity方法
         * ，不会有任何限制，如果使用Context的startActivity方法的话，就必须创建新的task
         * ，遇到上面那个异常的，都是因为使用了Context的startActivity方法。解决办法是，加一个flag
         * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         */
        if (!Activity.class.isAssignableFrom(context.getClass())) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 根据包名、activity名启动应用
     *
     * @param context     mContext
     * @param packageName packageName
     * @param className   className
     */
    public static void startApk(Context context, String packageName, String className) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        ComponentName cn = new ComponentName(packageName, className);
        intent.setComponent(cn);

        if (!Activity.class.isAssignableFrom(context.getClass())) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        context.startActivity(intent);
    }


    /**
     * 根据包名，设置应用的启动页面的Intent，然后获取这个Intent的信息�?
     *
     * @param context     mContext
     * @param packageName packageName
     * @return List<ResolveInfo>
     */
    public static List<ResolveInfo> findActivitiesForPackage(Context context, String packageName) {
        final PackageManager pm = context.getPackageManager();
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mainIntent.setPackage(packageName);
        final List<ResolveInfo> apps = pm.queryIntentActivities(mainIntent, 0);
        return apps != null ? apps : new ArrayList<ResolveInfo>();
    }


    /**
     * 判读apk是否已经安装过了
     *
     * @param context        mContext
     * @param apkPackageName apkPackageName
     * @return boolean
     */
    public static boolean isInstalledApk(Context context, String apkPackageName) {

        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> appList = packageManager.getInstalledPackages(0);

        for (int i = 0; i < appList.size(); i++) {
            PackageInfo pinfo = appList.get(i);

            if (pinfo.applicationInfo.packageName.equals(apkPackageName)) {
                return true;
            }

        }
        return false;
    }

    /**
     * 得到当前版本
     *
     * @param context mContext
     * @return 当前版本
     */
    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 得到当前版本名称
     *
     * @param context mContext
     * @return 当前版本
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据Apk的路径获取包名
     *
     * @param context mContext
     * @param apkPath apkPath
     * @return
     */
    public static String getApkPackageName(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if (info == null) {
            return null;
        }
        ApplicationInfo appInfo = info.applicationInfo;
        String appName = pm.getApplicationLabel(appInfo).toString();
        String packageName = appInfo.packageName; // 得到安装包名�?
        String version = info.versionName; // 得到版本信息
        // Drawable icon = pm.getApplicationIcon(appInfo);//得到图标信息
        return packageName;
    }

    /**
     * 返回包名
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        if (context != null) {
            String packageName = context.getPackageName();
            if (!TextUtils.isEmpty(packageName)) {
                return packageName;
            }
        }

        return "com.jifen.dandan";
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "应用程序";
    }


    /**
     * 重新打开app。让app回到前台
     *
     * @param context
     */
    public static void launchApp(Context context) {
        if (context == null) {
            return;
        }
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(PackageUtils.getPackageName(context));
            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取装机应用（非系统应用）
     *
     * @return
     */
    public static List<String> getApps() {
        List<String> list = new ArrayList<>();
        try {
            PackageManager pm = BaseApplication.getsInstance().getPackageManager();
            if (pm == null) {
                return null;
            }
            List<PackageInfo> packages = pm.getInstalledPackages(0);
            if (packages == null || packages.size() == 0) {
                return null;
            }
            for (PackageInfo packageInfo : packages) {
                // 判断系统//非系统应用
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    // 非系统应用
                    String packageName = packageInfo.packageName;
                    list.add(packageName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
