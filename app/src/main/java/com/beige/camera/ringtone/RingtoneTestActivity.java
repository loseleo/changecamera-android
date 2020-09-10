package com.beige.camera.ringtone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.beige.camera.MyApplication;
import com.beige.camera.R;
import com.beige.camera.common.base.BaseActivity;
import com.beige.camera.common.router.AppNavigator;
import com.beige.camera.common.router.PageIdentity;
import com.beige.camera.common.utils.BundleUtil;
import com.beige.camera.common.utils.MsgUtils;
import com.beige.camera.common.utils.PackageUtils;
import com.beige.camera.common.utils.StatusBarConfig;
import com.beige.camera.contract.HomeView;
import com.beige.camera.presenter.HomePresenter;
import com.zhangqiang.visiblehelper.OnVisibilityChangeListener;

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

//        findViewById(R.id.tv_camera).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                RingToneManager.setPermission(mActivity);
////                AppNavigator.goCameraActivity(RingtoneTestActivity.this);
//            }
//        });

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
