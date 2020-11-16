package com.beige.camera.screenlock.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.beige.camera.common.utils.LogUtils;


public class BatteryListener {
    private Context mContext;
    private String TAG = "BatteryListener";
    private BatteryBroadcastReceiver receiver;

    private BatteryStateListener mBatteryStateListener;

    public BatteryListener(Context context) {
        mContext = context;
        receiver = new BatteryBroadcastReceiver();
    }

    public void register(BatteryStateListener listener) {
        mBatteryStateListener = listener;
        if (receiver != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_BATTERY_CHANGED);
            filter.addAction(Intent.ACTION_BATTERY_LOW);
            filter.addAction(Intent.ACTION_BATTERY_OKAY);
            filter.addAction(Intent.ACTION_POWER_CONNECTED);
            filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
            mContext.registerReceiver(receiver, filter);
        }
    }

    public void unregister() {
        if (receiver != null) {
            mContext.unregisterReceiver(receiver);
        }
    }

    private class BatteryBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String acyion = intent.getAction();
                int level = intent.getIntExtra("level", 0);
                int scale = intent.getIntExtra("scale", 100);
                int power = level * 100 / scale;

                switch (acyion) {
                    case Intent.ACTION_BATTERY_CHANGED://电量发生改变
                        if (mBatteryStateListener != null) {
                            mBatteryStateListener.onStateChanged( level, scale,power);
                            LogUtils.i(TAG + "电量发生改变");
                        }
                        break;
                    case Intent.ACTION_BATTERY_LOW://电量低
                        if (mBatteryStateListener != null) {
                            LogUtils.i(TAG + "电量低");
                        }
                        break;
                    case Intent.ACTION_BATTERY_OKAY://电量充满
                        if (mBatteryStateListener != null) {
                            LogUtils.i(TAG + "电量充满");
                        }
                        break;
                    case Intent.ACTION_POWER_CONNECTED://接通电源
                        if (mBatteryStateListener != null) {
                            mBatteryStateListener.onStatePowerConnected(level, scale,power);
                            LogUtils.i(TAG + "接通电源");
                        }
                        break;
                    case Intent.ACTION_POWER_DISCONNECTED://拔出电源
                        if (mBatteryStateListener != null) {
                            mBatteryStateListener.onStatePowerDisconnected(level, scale,power);
                            LogUtils.i(TAG + "拔出电源");
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public interface BatteryStateListener {
        public void onStateChanged(int level, int scale, int power);

        public void onStatePowerConnected(int level, int scale, int power);

        public void onStatePowerDisconnected(int level, int scale, int power);
    }

}
