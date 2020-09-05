package com.jifen.dandan.ringtone.interfaces;

import com.zheyun.bumblebee.common.subscribe.BaseObservable;

public class CallStateObservable extends BaseObservable<CallStateObservable.CallPhoneObserver> {

    private CallStateObservable() {
    }

    public static final CallStateObservable getInstance() {
        return SingletHolder.INSTANCE;
    }

    private static class SingletHolder {
        private static CallStateObservable INSTANCE = new CallStateObservable();
    }

    public void endCall() {
        synchronized (mObservers) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).endRingCall();
            }
        }
    }

    public void answerCall() {
        synchronized (mObservers) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).answerRingCall();
            }
        }
    }

    public interface CallPhoneObserver {
        void endRingCall();
        void answerRingCall();
    }
}
