package com.beige.camera.advertisement.phone;

import android.os.Build;

import com.beige.camera.advertisement.interfaces.ICallAcceptor;
import com.beige.camera.advertisement.manager.RingVideoViewManager;

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
