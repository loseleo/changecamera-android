package com.beige.camera.common.utils;

import android.graphics.drawable.Drawable;

import com.beige.camera.common.base.BaseApplication;

public class ResourcesUtil {


    public static String getResourcesString(int stringId) {
        return BaseApplication.getsInstance().getResources().getString(stringId);
    }

    public static int getResourcesColor(int colorId) {
        return BaseApplication.getsInstance().getResources().getColor(colorId);
    }

    public static Drawable getResourcesDrawable(int resId) {
        return BaseApplication.getsInstance().getResources().getDrawable(resId);
    }
}
