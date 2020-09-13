package com.beige.camera.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.FrameLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.beige.camera.BuildConfig;
import com.beige.camera.R;
import com.beige.camera.common.base.BaseActivity;
import com.beige.camera.common.router.AppNavigator;
import com.beige.camera.common.router.PageIdentity;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.MmkvUtil;
import com.beige.camera.contract.IWelcomeView;
import com.beige.camera.dagger.MainComponentHolder;
import com.beige.camera.dialog.PrivacyPoliceDialog;
import com.beige.camera.presenter.WelcomePresenter;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

@Route(path = PageIdentity.APP_WELCOME)
public class WelcomeActivity extends BaseActivity implements IWelcomeView {

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

    @SuppressLint("CheckResult")
    public void showPrivacyPolice() {
        if (!isShowPrivacyPolice()) {
            mPresenter.showSplashAD();
            Observable.timer(6, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            goHome();
                        }
                    });

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
                goNextActivity();
//                if (permission.granted) {
//                    goNextActivity();
//                } else if (!permission.shouldShowRequestPermissionRationale && (System.currentTimeMillis() - requestTime < 500)) {
//                    new PermissionPageUtils(WelcomeActivity.this).jumpPermissionPage();
//                } else {
//                    WelcomeActivity.this.finish();
//                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e("getPermissions onError");
                goNextActivity();
            }

            @Override
            public void onComplete() {
                LogUtils.e("getPermissions onComplete");
            }
        });
    }


    private void goNextActivity() {
        if (isFinishing()) {
            return;
        }
        AppNavigator.goMainActivity(WelcomeActivity.this, "", "");
        finish();
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
