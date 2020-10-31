package com.beige.camera.floatwindow;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.beige.camera.R;
import com.beige.camera.common.feed.bean.AdModel;
import com.beige.camera.floatwindow.basefloat.BackDiskFloatWindow;
import com.beige.camera.floatwindow.basefloat.FloatWindowParamManager;
import com.beige.camera.floatwindow.basefloat.WifiAdFloatWindow;
import com.beige.camera.floatwindow.basefloat.RomUtils;
import com.blankj.utilcode.util.AppUtils;
import com.google.common.base.Strings;

import java.util.List;

public class FloatWindowService extends Service {

    public static final String TAG = "FloatWindowService";

    private static final String NOTIFICATION_CHANNEL_ID = "FloatWindowService";
    public static final int MANAGER_NOTIFICATION_ID = 0x1001;
    public static final int HANDLER_DETECT_PERMISSION = 0x2001;

    public static final String ACTION_CHECK_PERMISSION_AND_TRY_ADD = "action_check_permission_and_try_add";
    public static final String ACTION_WIFI_AD = "action_wifi_ad";
    public static final String ACTION_FULLSCREEN_AD = "action_fullscreen_ad";

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case HANDLER_DETECT_PERMISSION:
                    if (FloatWindowParamManager.checkPermission(getApplicationContext())) {
                        //对沙雕VIVO机型特殊处理,应用处于后台检查悬浮窗权限成功才能确认真的获取了悬浮窗权限
                        if (RomUtils.isVivoRom() && AppUtils.isAppForeground()) {
                            Log.e(TAG, "悬浮窗权限检查成功，但App处于前台状态，特殊机型会允许App获取权限，特殊机型就是指Vivo这个沙雕");
                            mHandler.sendEmptyMessageDelayed(HANDLER_DETECT_PERMISSION, 500);
                            return;
                        }
                        mHandler.removeMessages(HANDLER_DETECT_PERMISSION);
                        Log.e(TAG, "悬浮窗权限检查成功");
                    } else {
                        Log.e(TAG, "悬浮窗权限检查失败");
                        mHandler.sendEmptyMessageDelayed(HANDLER_DETECT_PERMISSION, 500);
                    }
                    break;
            }
        }
    };
    private WifiAdFloatWindow wifiAdFloatWindow;
    private BackDiskFloatWindow backDiskFloatWindow;

    public FloatWindowService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        addForegroundNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = Strings.nullToEmpty(intent.getAction());
        switch (action) {
            case ACTION_CHECK_PERMISSION_AND_TRY_ADD:
                //对沙雕Vivo做特殊处理
                if (RomUtils.isVivoRom()) {
                    mHandler.sendEmptyMessageDelayed(HANDLER_DETECT_PERMISSION, 1000);
                } else {
                    mHandler.sendEmptyMessage(HANDLER_DETECT_PERMISSION);
                }
                break;
            case ACTION_WIFI_AD:
                List<AdModel> adModel = (List<AdModel>) intent.getSerializableExtra("admodel");
                showWifiAdwindow(adModel);
                break;
            case ACTION_FULLSCREEN_AD:
                showBackDiskFloatWindow((List<AdModel>) intent.getSerializableExtra("admodel"));
                break;

        }
        return START_STICKY;
    }




    @Override
    public void onDestroy() {

        if (wifiAdFloatWindow != null) {
            wifiAdFloatWindow.remove();
            wifiAdFloatWindow = null;
        }

        if (backDiskFloatWindow != null) {
            backDiskFloatWindow.remove();
            backDiskFloatWindow = null;
        }

        super.onDestroy();
    }



    private void showWifiAdwindow(List<AdModel> adModel) {
        if (wifiAdFloatWindow != null) {
            wifiAdFloatWindow.remove();
            wifiAdFloatWindow = null;
        }
        wifiAdFloatWindow = new WifiAdFloatWindow(getApplicationContext());
        wifiAdFloatWindow.show();
        wifiAdFloatWindow.setAdModel(adModel);
    }

    private void showBackDiskFloatWindow(List<AdModel> adModel) {
        if (backDiskFloatWindow != null) {
            backDiskFloatWindow.remove();
            backDiskFloatWindow = null;
        }
        backDiskFloatWindow = new BackDiskFloatWindow(getApplicationContext(),adModel);
        backDiskFloatWindow.show();
    }





    private void addForegroundNotification() {
        createNotificationChannel();

        String contentTitle = "FloatWindow";
        String contentText = "FloatWindow Check";

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_float_window)
                .setLargeIcon(((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_float_window)).getBitmap())
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent msgIntent = getStartAppIntent(getApplicationContext());
        PendingIntent mainPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                msgIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = mBuilder.setContentIntent(mainPendingIntent)
                .setAutoCancel(false).build();

        startForeground(MANAGER_NOTIFICATION_ID, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Name";
            String description = "Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setShowBadge(false);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private Intent getStartAppIntent(Context context) {
        Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(AppUtils.getAppPackageName());
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        }

        return intent;
    }
}
