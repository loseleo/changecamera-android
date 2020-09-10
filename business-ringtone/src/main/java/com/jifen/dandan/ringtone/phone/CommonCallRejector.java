package com.beige.camera.ringtone.phone;

import android.os.IBinder;

import com.android.internal.telephony.ITelephony;
import com.beige.camera.common.utils.AppUtils;
import com.beige.camera.ringtone.interfaces.ICallRejector;
import com.beige.camera.ringtone.manager.RingVideoViewManager;

import java.lang.reflect.Method;

public class CommonCallRejector implements ICallRejector {
    /**
     * 挂断电话，需要复制两个AIDL
     */
    @Override
    public void endCall() {
        try {
            Class clazz = AppUtils.getAppContext().getClassLoader()
                    .loadClass("android.os.ServiceManager");
            Method method = clazz.getDeclaredMethod("getService", String.class);
            IBinder iBinder = (IBinder) method.invoke(null, "phone");
            ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
            iTelephony.endCall();
        } catch (Exception e) {
            e.printStackTrace();
            RingVideoViewManager.getInstance().dissmissRingView();
        }
    }


}
