package com.jifen.dandan.ringtone.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.jifen.dandan.common.utils.MmkvUtil;
import com.jifen.dandan.ringtone.utils.SystemUtils;
import com.jifen.dandan.ringtone.view.FloatViewParams;
import com.jifen.dandan.ringtone.view.FloatWindowManager;
import com.jifen.dandan.ringtone.view.RingVideoView;

public class RingVideoViewManager {

    private FloatWindowManager floatWindowManager;

    private Context mContext;

    private RingVideoViewManager() {

    }

    private static class RingVideoViewManagerHolder {
        private static final RingVideoViewManager INSTANCE = new RingVideoViewManager();
    }

    public static RingVideoViewManager getInstance() {
        return RingVideoViewManagerHolder.INSTANCE;
    }


    public void showRingView(Context context, String phoneNumber) {
        Log.d("ringvideoviewmanager","showRingView");
        this.mContext = context;
        String url = MmkvUtil.getInstance().getString("RingConstants.KEY_MP4_PATH");
        if (TextUtils.isEmpty(url)
                || (floatWindowManager != null && floatWindowManager.isFloatWindowShowing())
                || RingVideoManager.getInstance().isPlaying()) {
            return;
        }
        RingVideoView ringVideoView = new RingVideoView(context, phoneNumber);
        if (floatWindowManager != null) {
            floatWindowManager.dismissFloatWindow();
            floatWindowManager = null;
        }
        floatWindowManager = new FloatWindowManager();
        floatWindowManager.showFloatWindow(context, FloatWindowManager.FW_TYPE_ALERT_WINDOW, ringVideoView, buildParams());
    }

    public void dissmissRingView() {
        RingVideoManager.getInstance().release();
        if (floatWindowManager != null) {
            floatWindowManager.dismissFloatWindow();
        }
    }

    private FloatViewParams buildParams() {
        FloatViewParams params = new FloatViewParams();
        int screenWidth = SystemUtils.getScreenWidth(mContext);
        int screenHeight = SystemUtils.getScreenHeight(mContext);
        int statusBarHeight = SystemUtils.getStatusBarHeight(mContext);

        params.width = screenWidth;
        params.height = screenHeight;
        params.x = 0;
        params.y = 0;
        params.contentWidth = screenWidth;

        params.screenWidth = screenWidth;
        params.screenHeight = screenHeight;
        params.statusBarHeight = statusBarHeight;
        return params;
    }

}
