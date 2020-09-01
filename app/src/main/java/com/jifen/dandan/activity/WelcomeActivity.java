package com.jifen.dandan.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.FrameLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jifen.dandan.BuildConfig;
import com.jifen.dandan.R;
import com.jifen.dandan.common.base.BaseActivity;
import com.jifen.dandan.common.router.AppNavigator;
import com.jifen.dandan.common.router.PageIdentity;
import com.jifen.dandan.common.utils.LogUtils;
import com.jifen.dandan.common.utils.MmkvUtil;
import com.jifen.dandan.common.utils.PermissionPageUtils;
import com.jifen.dandan.contract.WelcomeView;
import com.jifen.dandan.dagger.MainComponentHolder;
import com.jifen.dandan.dialog.PrivacyPoliceDialog;
import com.jifen.dandan.presenter.WelcomePresenter;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

@Route(path = PageIdentity.APP_WELCOME)
public class WelcomeActivity extends BaseActivity implements WelcomeView {

    private static final String TAG = "WelcomeActivity";
    public static final String PRIVACY_VERSION_SERVICE = "privacy_version_service";
    public static final String PRIVACY_VERSION_SHOWED = "privacy_version_showed";

    @Inject
    public WelcomePresenter mPresenter;

    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isBroughtToFront = (getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0;
        boolean needShowAd = getIntent().getBooleanExtra("needShowAd", false);
        if (!needShowAd && !isTaskRoot() || isBroughtToFront && !needShowAd) {
            finish();
            return;
        }
        showPrivacyPolice();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.attachView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.detachView();
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @Override
    public String getPageName() {
        return PageIdentity.APP_WELCOME;
    }

    @Override
    protected void setupActivityComponent() {
        MainComponentHolder.getInstance().inject(this);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initViews() {
    }

    @Override
    public void initData() {
    }

    @Override
    public void configViews() {
    }

    public void showPrivacyPolice() {
        if (!isShowPrivacyPolice()) {
//            goNextActivity();
            mPresenter.showSplashAD();
            return;
        }
        PrivacyPoliceDialog privacyPoliceDialog = PrivacyPoliceDialog.newInstance(getPageName());
        privacyPoliceDialog.setOnChoiceListener(new PrivacyPoliceDialog.OnChoiceListener() {
            @Override
            public void onAgree() {
                setPrivacyPoliceShowVersion();
                getPermissions();
            }

            @Override
            public void onDisagree() {
                finish();
            }

        });
        privacyPoliceDialog.show(getSupportFragmentManager(), "privacy_police_dialog");

    }


    private void getPermissions() {

        long requestTime = System.currentTimeMillis();
        RxPermissions rxPermissions = new RxPermissions(WelcomeActivity.this);
        rxPermissions.setLogging(BuildConfig.DEBUG);
        rxPermissions.requestEachCombined(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA).subscribe(new Observer<Permission>() {
            @Override
            public void onSubscribe(Disposable d) {
                LogUtils.e("getPermissions onSubscribe");
            }

            @Override
            public void onNext(Permission permission) {
                if (permission.granted) {
                    goNextActivity();
                } else if (!permission.shouldShowRequestPermissionRationale && (System.currentTimeMillis() - requestTime < 500)) {
                    new PermissionPageUtils(WelcomeActivity.this).jumpPermissionPage();
                } else {
                    WelcomeActivity.this.finish();
                }


            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e("getPermissions onError");
                WelcomeActivity.this.finish();
            }

            @Override
            public void onComplete() {
                LogUtils.e("getPermissions onComplete");
            }
        });
    }


    private void goNextActivity() {
        AppNavigator.goMainActivity(WelcomeActivity.this, "", "");
    }

    @Override
    public FrameLayout getAdContainer() {
        return findViewById(R.id.fl_ad_container);
    }

    @Override
    public void goHome() {
        goNextActivity();
    }

    private void setPrivacyPoliceShowVersion() {
        String privacyPoliceVersion = getPrivacyPoliceVersion();
        MmkvUtil.getInstance().putString(PRIVACY_VERSION_SHOWED, privacyPoliceVersion);
    }

    private String getPrivacyPoliceVersion() {
        String privacyVersion = MmkvUtil.getInstance().getString(PRIVACY_VERSION_SERVICE, "5");
        return privacyVersion;
    }


    private boolean isShowPrivacyPolice() {
        String servicePrivacyVersion = getPrivacyPoliceVersion();
        String showPrivacyVersion = MmkvUtil.getInstance().getString(PRIVACY_VERSION_SHOWED, "");
        LogUtils.d("servicePrivacyVersion:" + servicePrivacyVersion);
        LogUtils.d("showPrivacyVersion:" + showPrivacyVersion);
        if (!TextUtils.isEmpty(showPrivacyVersion) && TextUtils.equals(servicePrivacyVersion, showPrivacyVersion)) {
            return false;
        }
        return true;
    }

}
