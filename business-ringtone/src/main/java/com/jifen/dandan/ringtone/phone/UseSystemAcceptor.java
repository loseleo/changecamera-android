package com.beige.camera.ringtone.phone;

import android.os.Build;

import com.beige.camera.ringtone.interfaces.ICallAcceptor;
import com.beige.camera.ringtone.manager.RingVideoViewManager;

public class UseSystemAcceptor implements ICallAcceptor {

    @Override
    public void answer() {
        RingVideoViewManager.getInstance().dissmissRingView();
    }

    /* renamed from: b */
    public static boolean canUseSystem() {
        return Build.DEVICE.equals("Coolpad8675-A");
    }
}
