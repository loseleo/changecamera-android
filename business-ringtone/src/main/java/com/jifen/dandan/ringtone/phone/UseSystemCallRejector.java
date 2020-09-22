package com.beige.camera.advertisement.phone;

import android.os.Build;
import android.text.TextUtils;

import com.beige.camera.advertisement.interfaces.ICallRejector;
import com.beige.camera.advertisement.manager.RingVideoViewManager;
import com.beige.camera.advertisement.permission.RomUtils;
import com.beige.camera.advertisement.permission.SystemProperties;
import com.beige.camera.advertisement.permission.VivoUtils;

public class UseSystemCallRejector implements ICallRejector {

    public static boolean canRejector() {
        boolean ret = false;
        boolean ret2 = false;
        ;
        if ((RomUtils.checkIsVivoRom() && TextUtils.equals(SystemProperties.getString("ro.vivo.os.build.display.id"), "Funtouch OS_3.1") && Build.VERSION.SDK_INT == 25)
                | (RomUtils.isVivoBrand() && VivoUtils.getOsVersion() == 2.0 && Build.DEVICE.equals("msm8974"))) {
            ret2 = true;
        }

        if (RomUtils.isVivoBrand() && Build.MODEL.equals("vivo Y75s")) {
            ret = true;
        }
        return ret2 | ret;
    }

    @Override
    public void endCall() {
        RingVideoViewManager.getInstance().dissmissRingView();
    }
}
