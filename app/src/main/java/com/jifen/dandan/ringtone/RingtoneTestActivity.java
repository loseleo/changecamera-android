package com.jifen.dandan.ringtone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jifen.dandan.MyApplication;
import com.jifen.dandan.R;
import com.jifen.dandan.common.base.BaseActivity;
import com.jifen.dandan.common.router.AppNavigator;
import com.jifen.dandan.common.router.PageIdentity;
import com.jifen.dandan.common.utils.BundleUtil;
import com.jifen.dandan.common.utils.MsgUtils;
import com.jifen.dandan.common.utils.PackageUtils;
import com.jifen.dandan.common.utils.StatusBarConfig;
import com.jifen.dandan.contract.HomeView;
import com.jifen.dandan.dagger.MainComponentHolder;
import com.jifen.dandan.dialog.PrivacyPoliceDialog;
import com.jifen.dandan.presenter.HomePresenter;
import com.jifen.dandan.ringtone.permission.PermissionUtils;
import com.zhangqiang.visiblehelper.OnVisibilityChangeListener;

import java.security.Permissions;

import javax.inject.Inject;


@Route(path = PageIdentity.APP_HOME)
public class RingtoneTestActivity extends BaseActivity implements HomeView {


    private static final String INTENT_KEY_SCHEMA_URI = "schemaUri";

    private long mExitTime;
    private RingtoneTestActivity mActivity;

    @Inject
    public HomePresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("cold_start_dd", "onCreate total: " + (System.currentTimeMillis() - MyApplication.appStart));
        super.onCreate(savedInstanceState);
        mActivity = this;
        mPresenter.getPrivacyPoliceContent();
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
        return R.layout.activity_home;
    }

    @Override
    public void initViews() {

        findViewById(R.id.tv_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RingToneManager.setPermission(mActivity);
//                AppNavigator.goCameraActivity(RingtoneTestActivity.this);
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


    private String getSchemaUri(@Nullable Bundle extras) {
        return BundleUtil.getString(extras, INTENT_KEY_SCHEMA_URI, "");
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


    private void autoLinkSchemeUri(String schemaUri) {
        if (!TextUtils.isEmpty(schemaUri)) {
            AppNavigator.goActivityByUri(RingtoneTestActivity.this, schemaUri);
        }
    }

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            if (System.currentTimeMillis() - mExitTime > 2000) {
                MsgUtils.showToastCenter(RingtoneTestActivity.this, getString(R.string.double_back_tips, PackageUtils.getAppName(this)));
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
