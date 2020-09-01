package com.jifen.dandan.ad.core;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jifen.dandan.common.utils.PackageUtils;

public class CPCUtils {

    @NonNull
    public static Bundle buildCommonParams(Context context) {

        Bundle bundle = new Bundle();
        try {
            bundle.putString("qk_dtu_id", PackageUtils.getChannelName(context));
//            double[] bdLocation = LocationResolver.getBDLocation(BaseApplication.getsInstance().getApplicationContext());
//            bundle.putString("key_location_city", PreferenceUtil.getString(context, LocationResolver.KEY_LOCATION_CITY));
//            bundle.putDouble("key_latitude", bdLocation[1]);
//            bundle.putLong("key_location_time", PreferenceUtil.getLong(context, LocationResolver.KEY_LOCATION_TIME, 0));
//            bundle.putDouble("key_longitude", bdLocation[0]);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return bundle;
    }
}
