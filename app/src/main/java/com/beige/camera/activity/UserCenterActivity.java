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
import com.beige.camera.common.router.AppNavigator;
import com.beige.camera.common.router.PageIdentity;
import com.beige.camera.common.view.loadding.CustomDialog;
import com.beige.camera.utils.AdHelper;
import com.zhangqiang.visiblehelper.OnVisibilityChangeListener;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


@Route(path = PageIdentity.APP_USERCENTER)
public class UserCenterActivity extends BaseActivity {

    public String bannerAdType = "bannerAdType";
    public String rewardedAdType = "rewardedAdType";

    private ConstraintLayout clClean;
    private TextView tvCleanNum;
    private ConstraintLayout clAboutus;
    private FrameLayout adContainer;

    private Disposable subscribe;
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
        new AdHelper().showBannerAdView(bannerAdType,adContainer);
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
        return R.layout.activity_user_center;
    }


    @Override
    public void initViews() {
        clClean = findViewById(R.id.cl_clean);
        tvCleanNum = findViewById(R.id.tv_clean_num);
        clAboutus = findViewById(R.id.cl_aboutus);
        adContainer =findViewById(R.id.fl_ad_container);

        findViewById(R.id.ic_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        clClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doClean();
            }
        });

        clAboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppNavigator.goAboutUsActivity(UserCenterActivity.this);
            }
        });
        setCleanNum(-1);
    }

    private void setCleanNum(int num){
        if(num == -1){
            num = (int) (10 + Math.random() * (100 - 10 + 1));
        }
        tvCleanNum.setText(num + "MB");
    }


    private void doClean(){
        CustomDialog mCustomDialog = CustomDialog.instance(UserCenterActivity.this);
        mCustomDialog.show();
        subscribe = Observable.timer(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(c -> {
                    mCustomDialog.dismiss();
                    setCleanNum(0);
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribe != null) {
            subscribe.dispose();
            subscribe = null;
        }
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
        return PageIdentity.APP_USERCENTER;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



}
