package com.beige.camera.advertisement.phone;

import android.content.Context;
import android.os.Build;
import android.telecom.TelecomManager;

import com.beige.camera.common.utils.AppUtils;
import com.beige.camera.advertisement.interfaces.ICallRejector;

public class Above28CallRejector implements ICallRejector {
    @Override
    public void endCall() {
        if (Build.VERSION.SDK_INT >= 28) {
            TelecomManager telecomManager = (TelecomManager) AppUtils.getAppContext().getSystemService(Context.TELECOM_SERVICE);
            if (telecomManager != null) {
//                telecomManager.endCall();
            }
        }

    }
}
