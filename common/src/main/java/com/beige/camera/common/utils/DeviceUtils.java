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
package com.beige.camera.common.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;

import java.util.UUID;

/**
 * @author zhangning.
 * @date 16/4/9.
 */
public class DeviceUtils {

    private static final String TAG = DeviceUtils.class.getSimpleName();
    private static final String CMCC_ISP = "46000";//中国移动
    private static final String CMCC2_ISP = "46002";//中国移动
    private static final String CU_ISP = "46001";//中国联通
    private static final String CT_ISP = "46003";//中国电信

    private static String deviceId;
    private static String IMEI;

    public static void mockDeviceId(String id) {
        deviceId = id;
    }

    public static String getDevice() {
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = MmkvUtil.getInstance().getString("UNIQUESTR");
        }
        return deviceId;
    }

    /**
     * 获取唯一标志
     *
     * @param context mContext
     * @return String
     */
    public static void getUniqueLabel(Context context) {

        String uniqueStr = MmkvUtil.getInstance().getString("UNIQUESTR");
        if (!TextUtils.isEmpty(uniqueStr)) {
            return;
        }
        if (TextUtils.isEmpty(uniqueStr)) {
            uniqueStr = getIMEI(context);
        }
        if (TextUtils.isEmpty(uniqueStr)) {
            uniqueStr = getAndroidID(context);
        }
        if (TextUtils.isEmpty(uniqueStr)) {
            uniqueStr = getMacAddress(context);
        }
        if (TextUtils.isEmpty(uniqueStr)) {
            uniqueStr = getSimSerialNumber(context);
        }
        if (TextUtils.isEmpty(uniqueStr)) {
            uniqueStr = getUUID();
        }
        MmkvUtil.getInstance().putString("UNIQUESTR", uniqueStr);
    }

    /**
     * 获取系统版本
     *
     * @return 系统版本
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取设备的系统版本号
     */
    public static int getDeviceSDK() {
        int sdk = Build.VERSION.SDK_INT;
        return sdk;
    }


    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机品牌
     *
     * @return 手机品牌
     */
    public static String getPhoneBRAND() {
        return android.os.Build.BRAND;
    }


    public static String getIMSI(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String IMSI = telephonyManager.getSubscriberId();
            return IMSI;
        }
        return "";
    }

    public static String getIMEI(Context context) {
        if (TextUtils.isEmpty(IMEI)) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (telephonyManager != null) {
                    IMEI = telephonyManager.getDeviceId();
                }
            }
        }
        return IMEI;
    }

    /**
     * 获取AndroidID
     *
     * @param context
     * @return
     */
    public static String getAndroidID(Context context) {
        return Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getSimSerialNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return null;
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return tm.getSimSerialNumber();
        }
        return null;
    }

    public static String getUUID() {

        String uuid = MmkvUtil.getInstance().getString("uuid");
        if (TextUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            MmkvUtil.getInstance().putString("uuid", uuid);
        }
        return uuid;
    }

    /**
     * 获取手机网络运营商类型
     *
     * @param context
     * @return
     */
    public static String getPhoneISP(Context context) {
        if (context == null) {
            return "";
        }
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String teleCompany = "";
        String np = manager.getNetworkOperator();

        if (np != null) {
            if (np.equals(CMCC_ISP) || np.equals(CMCC2_ISP)) {
                teleCompany = "中国移动";
            } else if (np.startsWith(CU_ISP)) {
                teleCompany = "中国联通";
            } else if (np.startsWith(CT_ISP)) {
                teleCompany = "中国电信";
            }
        }
        return teleCompany;
    }

    /**
     * 获取屏幕信息
     *
     * @param context
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm;
    }

    /**
     * 获取/打印屏幕信息
     *
     * @param context
     * @return
     */
    public static DisplayMetrics printDisplayInfo(Context context) {
        DisplayMetrics dm = getDisplayMetrics(context);
        StringBuilder sb = new StringBuilder();
        sb.append("\ndensity         :").append(dm.density);
        sb.append("\ndensityDpi      :").append(dm.densityDpi);
        sb.append("\nheightPixels    :").append(dm.heightPixels);
        sb.append("\nwidthPixels     :").append(dm.widthPixels);
        sb.append("\nscaledDensity   :").append(dm.scaledDensity);
        sb.append("\nxdpi            :").append(dm.xdpi);
        sb.append("\nydpi            :").append(dm.ydpi);
        return dm;
    }

    /**
     * 获取系统当前可用内存大小
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static String getAvailMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
    }


    /**
     * 获取 MAC 地址
     * 须配置android.permission.ACCESS_WIFI_STATE权限
     */
    public static String getMacAddress(Context context) {
        //wifi mac地址
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String mac = info.getMacAddress();
        return mac;
    }

    /**
     * 获取 开机时间
     */
    public static String getBootTimeString() {
        long ut = SystemClock.elapsedRealtime() / 1000;
        int h = (int) ((ut / 3600));
        int m = (int) ((ut / 60) % 60);
        return h + ":" + m;
    }
}
