package com.jifen.dandan.ringtone.phone;

import android.os.IBinder;

import com.android.internal.telephony.ITelephony;
import com.jifen.dandan.common.utils.AppUtils;
import com.jifen.dandan.ringtone.interfaces.ICallRejector;
import com.jifen.dandan.ringtone.manager.RingVideoViewManager;

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
