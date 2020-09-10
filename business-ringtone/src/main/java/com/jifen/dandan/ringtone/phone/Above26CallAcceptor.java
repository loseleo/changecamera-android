package com.beige.camera.ringtone.phone;

import android.content.Context;
import android.os.Build;
import android.telecom.TelecomManager;

import com.beige.camera.common.utils.AppUtils;
import com.beige.camera.ringtone.interfaces.ICallAcceptor;
import com.beige.camera.ringtone.manager.RingVideoViewManager;

public class Above26CallAcceptor implements ICallAcceptor {

    @Override
    public void answer() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                TelecomManager telecomManager = (TelecomManager) AppUtils.getAppContext().getSystemService(Context.TELECOM_SERVICE);
                telecomManager.acceptRingingCall();
            }
        } catch (Exception e) {
            e.printStackTrace();
            RingVideoViewManager.getInstance().dissmissRingView();
        }


    }
}
