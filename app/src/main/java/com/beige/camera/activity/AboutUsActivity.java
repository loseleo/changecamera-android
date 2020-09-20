package com.beige.camera.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.beige.camera.R;
import com.beige.camera.common.base.BaseActivity;
import com.beige.camera.common.constant.Constant;
import com.beige.camera.common.router.AppNavigator;
import com.beige.camera.common.router.PageIdentity;
import com.beige.camera.common.utils.PackageUtils;
import com.beige.camera.common.view.loadding.CustomDialog;
import com.beige.camera.utils.AdHelper;
import com.zhangqiang.visiblehelper.OnVisibilityChangeListener;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


@Route(path = PageIdentity.APP_ABOUTUS)
public class AboutUsActivity extends BaseActivity {


    private ConstraintLayout clPrivacyAgreement;
    private ConstraintLayout clUserAgreement;
    private TextView tvVersion;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    protected void setupActivityComponent() {

    }

    @Override
    protected void onRestoreWhenCreate(Bundle savedInstanceState) {
        super.onRestoreWhenCreate(savedInstanceState);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_about_us;
    }


    @Override
    public void initViews() {
        clPrivacyAgreement = findViewById(R.id.cl_privacy_agreement);
        clUserAgreement = findViewById(R.id.cl_user_agreement);
        tvVersion = findViewById(R.id.tv_version);
        tvVersion.setText("版本：V" + PackageUtils.getVersionName(this));
        findViewById(R.id.ic_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        clPrivacyAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppNavigator.goWebViewActivity(AboutUsActivity.this,Constant.URL_PRIVACY_POLICE);
            }
        });

        clUserAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppNavigator.goWebViewActivity(AboutUsActivity.this,Constant.URL_USER_AGREEMENT);
            }
        });
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void configViews() {
    }

    @Override
    public void initData() {
    }

    @Nullable
    @Override
    public String getPageName() {
        return PageIdentity.APP_ABOUTUS;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



}
