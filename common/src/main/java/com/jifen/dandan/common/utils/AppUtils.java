/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jifen.dandan.common.utils;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.jifen.dandan.common.base.BaseApplication;
import com.jifen.dandan.common.constant.Constant;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

public class AppUtils {

    private static Context mContext;
    private static Thread mUiThread;
    public final static String sMd5KEY = "WEiChong03_69";

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public static void init(Context context) {
        mContext = context;
        mUiThread = Thread.currentThread();
        Constant.sScreenWidth = ScreenUtils.getScreenWidth();
        Constant.sScreenHeight = ScreenUtils.getScreenHeight();
    }



    public static Context getAppContext() {
        Context context = AppUtils.mContext;
        if (context == null) {
            context = BaseApplication.getsInstance();
        }
        return context;
    }

    public static AssetManager getAssets() {
        return mContext.getAssets();
    }

    public static Resources getResource() {
        return mContext.getResources();
    }

    public static boolean isUIThread() {
        return Thread.currentThread() == mUiThread;
    }

    public static void runOnUI(Runnable r) {
        sHandler.post(r);
    }

    public static void runOnUIDelayed(Runnable r, long delayMills) {
        sHandler.postDelayed(r, delayMills);
    }

    public static void removeRunnable(Runnable r) {
        if (r == null) {
            sHandler.removeCallbacksAndMessages(null);
        } else {
            sHandler.removeCallbacks(r);
        }
    }

    public static void goActivity(Context act, Class<?> cls) {
        goActivity(act, cls, false);
    }

    public static void goActivity(Context act, Class<?> cls, boolean newTask) {
        Intent intent = new Intent(act, cls);
        if (newTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        act.startActivity(intent);
    }

    public static String getChannel(Context context) {
        try {
            String chanel = MmkvUtil.getInstance().getString("app_chanel");
            if (TextUtils.isEmpty(chanel)) {
                ApplicationInfo info = null;
                info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                chanel = info.metaData.getString("UMENG_CHANNEL");
                MmkvUtil.getInstance().putString("app_chanel", chanel);
            }
            return chanel;
        } catch (Exception e) {
        }
        return "website";
    }

    /**
     * 获取指纹码 MD5（UserName+YYY+MM+DD） FingerPrint
     */
    public static String getFingerPrint(String param) {
        String fingerPrint = null;
        try {
            fingerPrint = getMD5Hex(param + sMd5KEY);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return fingerPrint;

    }

    public static String getMD5Hex(final String inputString) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(inputString.getBytes());

        byte[] digest = md.digest();

        return convertByteToHex(digest);
    }

    private static String convertByteToHex(byte[] byteData) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static void OpenDefaultHomeScreen(Context context) {
        String app = GetHomeScreen(context);
        if (app.indexOf(":") != -1)
            ExcuteApp(context, app.split(":")[0], app.split(":")[1]);
    }

    public static String GetHomeScreen(Context context) {
        String HomeApp = MmkvUtil.getInstance().getString("HomeApp", "");
        if (HomeApp.equals("")) {
            Intent intent2 = new Intent(Intent.ACTION_MAIN, null);
            intent2.addCategory(Intent.CATEGORY_HOME);
            List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(intent2, 0);
            for (ResolveInfo resolveInfo : apps) {
                String pname = resolveInfo.activityInfo.packageName;
                String cname = resolveInfo.activityInfo.name;
                if (pname.indexOf("mobilewindow") == -1) {
                    HomeApp = pname + ":" + cname;
                    MmkvUtil.getInstance().putString("HomeApp", HomeApp);
                    break;
                }
            }
        }

        if (HomeApp.equals(""))
            HomeApp = "Error";

        return HomeApp;
    }

    public static boolean ExcuteApp(Context context, String packName, String className) {
        return ExcuteApp(context, packName, className, "");
    }

    public static boolean ExcuteApp(Context context, String packName, String className, String para) {
        try {
            ComponentName cn = new ComponentName(packName, className);
            ActivityInfo info = context.getPackageManager().getActivityInfo(cn, 0);
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(info.applicationInfo.packageName, info.name));
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            try {
                if (para.toLowerCase(Locale.getDefault()).startsWith("http://"))
                    intent.setData(Uri.parse(para));
                // if(Launcher.getInstance(context)!=null)Launcher.getInstance(context).startActivitySafely(intent);
                intent.putExtra("Para", para);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                } catch (SecurityException e) {
                }
            } catch (Exception e) {
            }
        } catch (Exception e) {
            try {
                return ExcuteApp(context, packName);
            } catch (Exception e1) {
                return false;
            }
        }
        return true;
    }


    public static boolean ExcuteApp(Context context, String packageName) {
        try {
            if (packageName.equals(""))
                return false;
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            context.startActivity(intent);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
