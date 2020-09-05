package com.jifen.dandan.ringtone.phone;

import android.os.Build;

import com.jifen.dandan.ringtone.interfaces.CallStateObservable;
import com.jifen.dandan.ringtone.interfaces.ICallAcceptor;
import com.jifen.dandan.ringtone.interfaces.ICallRejector;
import com.jifen.dandan.ringtone.manager.RingVideoViewManager;


public class PhoneHolder implements ICallAcceptor, ICallRejector {
    private ICallRejector callRejector;
    private ICallAcceptor callAcceptor;

    public PhoneHolder() {
        initRejector();
        initAcceptor();
    }

    private void initRejector() {
        if (UseSystemCallRejector.canRejector()) {
            this.callRejector = new UseSystemCallRejector();
        } else if (Build.VERSION.SDK_INT >= 28) {
            this.callRejector = new Above28CallRejector();
        } else {
            this.callRejector = new CommonCallRejector();
        }
    }

    private void initAcceptor() {
        if (UseSystemAcceptor.canUseSystem()) {
            this.callAcceptor = new UseSystemAcceptor();
        } else if (Build.VERSION.SDK_INT >= 26) {
            this.callAcceptor = new Above26CallAcceptor();
        } else if (Build.VERSION.SDK_INT >= 21) {
            this.callAcceptor = new Above21CallAcceptor();
        } else {
            this.callAcceptor = new Above19CallAcceptor();
        }
    }

    @Override
    public void answer() {
        try {
            if (callAcceptor != null) {
                callAcceptor.answer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        RingVideoViewManager.getInstance().dissmissRingView();
        CallStateObservable.getInstance().answerCall();
    }

    @Override
    public void endCall() {
        try {
            if (callRejector != null) {
                callRejector.endCall();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        CallStateObservable.getInstance().endCall();
        RingVideoViewManager.getInstance().dissmissRingView();
    }


}
