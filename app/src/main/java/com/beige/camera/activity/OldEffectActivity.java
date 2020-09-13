package com.beige.camera.activity;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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
import com.beige.camera.common.utils.MsgUtils;
import com.beige.camera.common.utils.RxUtil;
import com.beige.camera.common.utils.imageloader.BitmapUtil;
import com.beige.camera.contract.IEffectImageView;
import com.beige.camera.dagger.MainComponentHolder;
import com.beige.camera.presenter.EffectImagePresenter;
import com.beige.camera.ringtone.api.bean.AdConfigBean;
import com.beige.camera.ringtone.core.AdManager;
import com.beige.camera.ringtone.core.infoflow.InfoFlowAd;
import com.beige.camera.ringtone.core.strategy.Callback;
import com.beige.camera.ringtone.dagger.AdComponentHolder;
import com.beige.camera.utils.AdHelper;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.beige.camera.common.utils.RxUtil.io_main;

@Route(path = PageIdentity.APP_OLDEFFECT)
public class OldEffectActivity extends BaseActivity implements IEffectImageView {

    public String bannerAdType = "bannerAdType";
    public String rewardedAdType = "rewardedAdType";

    private ImageView ivPreview;
    private ImageView icBack;
    private TextView btnSave;
    private TextView btnShare;
    private FrameLayout adContainer;
    private ConstraintLayout clSelectAgeNow;
    private ConstraintLayout clSelectAge30;
    private ConstraintLayout clSelectAge40;
    private ConstraintLayout clSelectAge50;
    private ImageView selectAgeNow;
    private ImageView selectAge30;
    private ImageView selectAge40;
    private ImageView selectAge50;

    private TextView tvSelectAgeNow;
    private TextView tvSelectAge30;
    private TextView tvSelectAge40;
    private TextView tvSelectAge50;

    @Inject
    public EffectImagePresenter mPresenter;

    private String selectAge = "now";
    private String effectImage = "";

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
    public int getLayoutResId() {
        return R.layout.activity_oldeffect;
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
    public void initViews() {
        icBack = findViewById(R.id.ic_back);
        ivPreview = findViewById(R.id.iv_preview);
        btnSave = findViewById(R.id.btn_save);
        btnShare = findViewById(R.id.btn_share);
        adContainer = findViewById(R.id.fl_ad_container);
        clSelectAgeNow = findViewById(R.id.cl_select_age_now);
        clSelectAge30 = findViewById(R.id.cl_select_age_30);
        clSelectAge40 = findViewById(R.id.cl_select_age_40);
        clSelectAge50 = findViewById(R.id.cl_select_age_50);
        selectAgeNow = findViewById(R.id.select_age_now);
        selectAge30 = findViewById(R.id.select_age_30);
        selectAge40 = findViewById(R.id.select_age_40);
        selectAge50 = findViewById(R.id.iv_select);
        tvSelectAgeNow = findViewById(R.id.tv_select_age_now);
        tvSelectAge30 = findViewById(R.id.tv_select_age_30);
        tvSelectAge40 = findViewById(R.id.tv_select_age_40);
        tvSelectAge50 = findViewById(R.id.tv_select_age_50);
        AdHelper.showBannerAdView(bannerAdType,adContainer);
    }

    @Override
    public void initData() {
    }

    @Override
    public void configViews() {
        LogUtils.e("zhangning", "imagePath = " + imagePath);
        setEffectImage();
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

        clSelectAgeNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAge = "now";
                setSelectAge();
            }
        });
        clSelectAge30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAge = "30";
                setSelectAge();
            }
        });
        clSelectAge40.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAge = "40";
                setSelectAge();
            }
        });
        clSelectAge50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAge = "50";
                setSelectAge();
            }
        });

        mPresenter.getFaceEditAttr(imagePath, "TO_OLD");

    }

    @Nullable
    @Override
    public String getPageName() {
        return PageIdentity.APP_OLDEFFECT;
    }


    private void setSelectAge() {
        selectAgeNow.setVisibility(View.INVISIBLE);
        selectAge30.setVisibility(View.INVISIBLE);
        selectAge40.setVisibility(View.INVISIBLE);
        selectAge50.setVisibility(View.INVISIBLE);
        tvSelectAgeNow.setTextColor(getResources().getColor(R.color.black));
        tvSelectAge30.setTextColor(getResources().getColor(R.color.black));
        tvSelectAge40.setTextColor(getResources().getColor(R.color.black));
        tvSelectAge50.setTextColor(getResources().getColor(R.color.black));
        if (TextUtils.equals(selectAge, "now")) {
            selectAgeNow.setVisibility(View.VISIBLE);
            tvSelectAgeNow.setTextColor(getResources().getColor(R.color.common_color_FF390B));
        } else if (TextUtils.equals(selectAge, "30")) {
            selectAge30.setVisibility(View.VISIBLE);
            tvSelectAge30.setTextColor(getResources().getColor(R.color.common_color_FF390B));
        } else if (TextUtils.equals(selectAge, "40")) {
            selectAge40.setVisibility(View.VISIBLE);
            tvSelectAge40.setTextColor(getResources().getColor(R.color.common_color_FF390B));
        } else if (TextUtils.equals(selectAge, "50")) {
            selectAge50.setVisibility(View.VISIBLE);
            tvSelectAge50.setTextColor(getResources().getColor(R.color.common_color_FF390B));
        }
        setEffectImage();
    }

    private void setEffectImage(){
        if (TextUtils.equals(selectAge, "now")) {
            BitmapUtil.loadImage(imagePath,ivPreview);
        }else{
            BitmapUtil.loadImage(effectImage,ivPreview);
        }
    }

    @Override
    public void onResultEffectImage(String image, String actionType) {
        if (TextUtils.isEmpty(image)) {
            MsgUtils.showToastCenter(OldEffectActivity.this,"图片处理失败");
            effectImage = imagePath;
        }
        effectImage = image;
        setEffectImage();
    }

    @Override
    public void onResultAge(String age) {
    }


}
