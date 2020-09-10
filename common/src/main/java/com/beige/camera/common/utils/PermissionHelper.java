package com.beige.camera.common.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

public class PermissionHelper {


    public static void requestPermission(Activity activity, int requestCode, String permission) {
        PermissionHelper.requestPermissions(activity, requestCode, new String[]{permission});
    }

    public static void requestPermissions(Activity activity, int requestCode, String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(permissions, requestCode);
        }
    }

    public static boolean hasPermission(Activity activity, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = activity.checkSelfPermission(permission);
            return PackageManager.PERMISSION_GRANTED == result;
        } else {
            return true;
        }
    }
}
