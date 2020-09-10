package com.beige.camera.ringtone.phone;

import android.content.Context;
import android.os.Build;
import android.telecom.TelecomManager;

import com.beige.camera.common.utils.AppUtils;
import com.beige.camera.ringtone.interfaces.ICallRejector;

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
