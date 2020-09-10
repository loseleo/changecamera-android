package com.beige.camera.ringtone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;

import com.beige.camera.common.base.BaseActivity;
import com.beige.camera.common.utils.StatusBarConfig;
import com.beige.camera.ringtone.manager.RingToneManager;
import com.zhangqiang.visiblehelper.OnVisibilityChangeListener;


public class RingtoneTestActivity extends BaseActivity {


    private static final String INTENT_KEY_SCHEMA_URI = "schemaUri";

    private long mExitTime;
    private RingtoneTestActivity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        getVisibleHelper().addVisibilityChangeListener(new OnVisibilityChangeListener() {
            @Override
            public void onVisibilityChange(boolean isVisible) {
                if (isVisible) {
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public StatusBarConfig getStatusBarConfig() {
        return new StatusBarConfig.Builder()
                .setFitsSystemWindows(false)
                .setDarkTheme(false)
                .build();
    }

    @Override
    protected void setupActivityComponent() {
    }

    @Override
    protected void onRestoreWhenCreate(Bundle savedInstanceState) {
        super.onRestoreWhenCreate(savedInstanceState);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_ringtone_test;
    }

    @Override
    public void initViews() {

        findViewById(R.id.tv_mp3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RingToneManager.setPermission(mActivity);
//                AppNavigator.goCameraActivity(RingtoneTestActivity.this);
            }
        });

        findViewById(R.id.tv_mp4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }

    @Override
    public void configViews() {

    }

    @Override
    public void initData() {
    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Nullable
    @Override
    public String getPageName() {
        return "RingtoneTestActivity";
    }

    @Override
    public Context getApplicationContext() {
        return null;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {

    }

    CallPhoneStateListener phoneStateListener;
    TelephonyManager telephonyManager;
    /**
     * 初始化来电状态监听器
     */
    private void initPhoneStateListener() {
        if (phoneStateListener == null) {
            phoneStateListener = new CallPhoneStateListener(this);
        }
        if (telephonyManager == null) {
            telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        }
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }


}
