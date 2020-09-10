package com.beige.camera.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.beige.camera.common.utils.imageloader.BitmapUtil;
import com.beige.camera.ringtone.api.bean.AdConfigBean;
import com.beige.camera.ringtone.core.AdManager;
import com.beige.camera.ringtone.core.infoflow.InfoFlowAd;
import com.beige.camera.ringtone.core.strategy.Callback;


@Route(path = PageIdentity.APP_AGEEFFECT)
public class AgeEffectActivity extends BaseActivity {

    private ImageView icBack;
    private ImageView ivPreview;
    private TextView tvAge;
    private TextView btnSave;
    private TextView btnShare;
    private FrameLayout adContainer;

    @Autowired(name = "image_path")
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupActivityComponent() {
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
        adContainer = findViewById(R.id.fl_ad_container);
    }

    @Override
    public void initData() {
    }

    @Override
    public void configViews() {
        LogUtils.e("zhangning", "imagePath = " + imagePath);
        BitmapUtil.loadImageRound(this,imagePath,ivPreview,R.drawable.bg_shape_gender_gray,20);
        int age = (int) (10 + Math.random() * (1000 - 10 + 1));
        tvAge.setText(age + " 岁");
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

    }



    @Nullable
    @Override
    public String getPageName() {
        return PageIdentity.APP_AGEEFFECT;
    }


    public void setAdModel(AdConfigBean adModel) {
        if (adContainer == null || adModel == null) {
            return;
        }
        adContainer.post(new Runnable() {
            @Override
            public void run() {
                AdManager.loadBigImgAd(adContainer, adModel.getCandidates(), new Callback<InfoFlowAd>() {
                    @Override
                    public void onAdLoadStart(InfoFlowAd ad) {
                    }

                    @Override
                    public void onFail(Throwable e) {

                    }
                });
            }
        });
    }


}
