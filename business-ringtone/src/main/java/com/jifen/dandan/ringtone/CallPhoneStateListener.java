package com.beige.camera.ringtone;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.ringtone.manager.RingToneManager;
import com.beige.camera.ringtone.manager.RingVideoManager;
import com.beige.camera.ringtone.manager.RingVideoViewManager;

public class CallPhoneStateListener extends PhoneStateListener {


    private Context mContext;
//    private RingPhoneCallEndHelper mRingPhoneCallEndHelper;

    public CallPhoneStateListener(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCallStateChanged(int state, String phoneNumber) {
        super.onCallStateChanged(state, phoneNumber);

        LogUtils.e("PhoneCallService CallListenerService state =" + state + " phone = "+phoneNumber);
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE: // 待机，即无电话时，挂断时触发
                RingVideoViewManager.getInstance().dissmissRingView();
                RingToneManager.getInstance().stopRing();
//                if (mRingPhoneCallEndHelper != null) {
//                    mRingPhoneCallEndHelper.show();
//                }
                break;

            case TelephonyManager.CALL_STATE_RINGING: // 响铃，来电时触发
                if (!RingVideoManager.getInstance().isPlaying()) {
                    RingVideoViewManager.getInstance().showRingView(mContext, phoneNumber);
                }
//                if (UserInfoManager.hasLogin()) {
//                    mRingPhoneCallEndHelper = new RingPhoneCallEndHelper();
//                }
                break;

            case TelephonyManager.CALL_STATE_OFFHOOK: // 摘机，接听或拨出电话时触发
                RingVideoViewManager.getInstance().dissmissRingView();
                RingToneManager.getInstance().stopRing();
                break;

            default:
                break;
        }
    }
}
