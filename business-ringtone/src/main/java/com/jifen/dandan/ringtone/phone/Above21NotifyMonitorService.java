package com.beige.camera.ringtone.phone;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;

import com.beige.platform.log.LogUtils;

import java.util.Iterator;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class Above21NotifyMonitorService extends NotificationListenerService {
    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        LogUtils.e("Above21NotifyMonitorService onListenerConnected");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        LogUtils.e("Above21NotifyMonitorService onNotificationPosted");

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        LogUtils.e("Above21NotifyMonitorService onNotificationRemoved");

    }


    public static void startMonitorService(Context context) {
        List<ActivityManager.RunningServiceInfo> runningServices;
        if (context != null) {
            ComponentName componentName = new ComponentName(context, Above21NotifyMonitorService.class);
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            boolean isRunning = false;
            if (activityManager != null && (runningServices = activityManager.getRunningServices(Integer.MAX_VALUE)) != null) {
                Iterator<ActivityManager.RunningServiceInfo> it = runningServices.iterator();
                while (it.hasNext()) {
                    ActivityManager.RunningServiceInfo next = it.next();
                    if (next.service.equals(componentName) && next.pid == Process.myPid()) {
                        isRunning = true;
                        break;
                    }
                }

                if (!isRunning) {
                    toggleNotificationListenerService(context);
                }
            }
        }
    }

    private static void toggleNotificationListenerService(Context context) {
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(context, Above21NotifyMonitorService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(new ComponentName(context, Above21NotifyMonitorService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
}
