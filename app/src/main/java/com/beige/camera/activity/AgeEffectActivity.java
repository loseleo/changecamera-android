package com.beige.camera.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.beige.camera.R;
import com.beige.camera.common.base.BaseActivity;
import com.beige.camera.common.router.PageIdentity;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.RxUtil;
import com.beige.camera.common.utils.imageloader.BitmapUtil;
import com.beige.camera.contract.IEffectImageView;
import com.beige.camera.dagger.MainComponentHolder;
import com.beige.camera.presenter.EffectImagePresenter;
import com.beige.camera.presenter.WelcomePresenter;
import com.beige.camera.ringtone.api.bean.AdConfigBean;
import com.beige.camera.ringtone.core.AdManager;
import com.beige.camera.ringtone.core.infoflow.InfoFlowAd;
import com.beige.camera.ringtone.core.strategy.Callback;
import com.beige.camera.ringtone.dagger.AdComponentHolder;
import com.beige.camera.utils.AdHelper;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.beige.camera.common.utils.RxUtil.io_main;


@Route(path = PageIdentity.APP_AGEEFFECT)
public class AgeEffectActivity extends BaseActivity implements IEffectImageView {

    public String bannerAdType = "bannerAdType";
    public String rewardedAdType = "rewardedAdType";
    private ImageView icBack;
    private ImageView ivPreview;
    private TextView tvAge;
    private TextView btnSave;
    private TextView btnShare;
    private FrameLayout adContainer;
    @Inject
    public EffectImagePresenter mPresenter;

    @Autowired(name = "image_path")
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupActivityComponent() {
        MainComponentHolder.getInstance().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.attachView(this);
        AdHelper.playRewardedVideo(this, rewardedAdType, new AdHelper.PlayRewardedAdCallback() {
            @Override
            public void onDismissed(int action) {

            }

            @Override
            public void onFail() {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.detachView();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_age_effect;
    }

    @Override
    public void initViews() {
        icBack = findViewById(R.id.ic_back);
        ivPreview = findViewById(R.id.iv_preview);
        tvAge = findViewById(R.id.tv_age);
        btnSave = findViewById(R.id.btn_save);
        btnShare = findViewById(R.id.btn_share);
        adContainer =findViewById(R.id.fl_ad_container);
        AdHelper.showBannerAdView(bannerAdType,adContainer);
    }

    @Override
    public void initData() {
    }

    @Override
    public void configViews() {
        LogUtils.e("zhangning", "imagePath = " + imagePath);
        BitmapUtil.loadImageRound(this, imagePath, ivPreview, R.color.gray777, 20);
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        mPresenter.getEffectAge(imagePath);

    }


    @Nullable
    @Override
    public String getPageName() {
        return PageIdentity.APP_AGEEFFECT;
    }

    @Override
    public void onResultEffectImage(String image,String actionType) {
    }

    @Override
    public void onResultAge(String age) {
        LogUtils.e("zhangning","age = " + age);
        if (TextUtils.isEmpty(age)) {
             age = (int) (10 + Math.random() * (1000 - 10 + 1)) + "";
        }
        tvAge.setText(age + " 岁");
    }
}
