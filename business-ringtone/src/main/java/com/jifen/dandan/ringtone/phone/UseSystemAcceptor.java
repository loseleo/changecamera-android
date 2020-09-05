package com.jifen.dandan.ringtone.phone;

import android.os.Build;

import com.jifen.dandan.ringtone.interfaces.ICallAcceptor;
import com.jifen.dandan.ringtone.manager.RingVideoViewManager;

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
