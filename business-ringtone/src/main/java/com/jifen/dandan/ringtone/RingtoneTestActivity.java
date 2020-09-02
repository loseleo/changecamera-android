package com.jifen.dandan.ringtone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jifen.dandan.common.base.BaseActivity;
import com.jifen.dandan.common.router.PageIdentity;
import com.jifen.dandan.common.utils.StatusBarConfig;
import com.zhangqiang.visiblehelper.OnVisibilityChangeListener;


public class RingtoneTestActivity extends BaseActivity {

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
                int nmu = RingToneManager.setPermission(mActivity);
                if (nmu <= 0){
                    RingToneManager.getInstance().setRingtoneImpl2("");
                }
            }
        });

        findViewById(R.id.tv_mp4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RingToneManager.setPermission(mActivity);
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public String getPageName() {
        return PageIdentity.APP_HOME;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
