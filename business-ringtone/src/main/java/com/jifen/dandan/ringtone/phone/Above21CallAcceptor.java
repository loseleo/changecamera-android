package com.jifen.dandan.ringtone.phone;

import android.content.ComponentName;
import android.content.Context;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;

import com.jifen.dandan.common.utils.AppUtils;
import com.jifen.dandan.ringtone.interfaces.ICallAcceptor;
import com.jifen.dandan.ringtone.manager.RingVideoViewManager;

import java.util.List;


public class Above21CallAcceptor implements ICallAcceptor {
    private static final String mTelecom = "com.android.server.telecom";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void answer() {
        try {
            MediaSessionManager mediaSessionManager = (MediaSessionManager) AppUtils.getAppContext().getSystemService(Context.MEDIA_SESSION_SERVICE);
            if (mediaSessionManager != null) {
                List<MediaController> mediaControllers = mediaSessionManager.getActiveSessions(new ComponentName(AppUtils.getAppContext(), Above21NotifyMonitorService.class));
                if (mediaControllers != null && mediaControllers.size() > 0) {
                    for (MediaController next : mediaControllers) {
                        if (next != null && mTelecom.equals(next.getPackageName())) {
                            next.dispatchMediaButtonEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable th) {
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RingVideoViewManager.getInstance().dissmissRingView();
            }
        },200);
//        HandlerUtils.postDelayed(() -> RingVideoViewManager.getInstance().dissmissRingView(), 200);
    }
}