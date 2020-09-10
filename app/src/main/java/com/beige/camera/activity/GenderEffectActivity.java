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
import com.beige.camera.bean.FunctionBean;
import com.beige.camera.common.base.BaseActivity;
import com.beige.camera.common.router.AppNavigator;
import com.beige.camera.common.router.PageIdentity;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.imageloader.BitmapUtil;
import com.beige.camera.ringtone.api.bean.AdConfigBean;
import com.beige.camera.ringtone.core.AdManager;
import com.beige.camera.ringtone.core.infoflow.InfoFlowAd;
import com.beige.camera.ringtone.core.strategy.Callback;


@Route(path = PageIdentity.APP_GENDEREFFECT)
public class GenderEffectActivity extends BaseActivity {

    private ImageView icBack;
    private TextView tvTitle;
    private ImageView ivEffect;
    private ImageView ivNormal;
    private TextView btnSave;
    private TextView btnShare;
    private FrameLayout adContainer;

    @Autowired(name = "image_path")
    String imagePath;
    @Autowired(name = "function")
    String function;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupActivityComponent() {
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_gender_effect;
    }

    @Override
    public void initViews() {
        icBack = findViewById(R.id.ic_back);
        tvTitle = findViewById(R.id.tv_title);
        ivEffect = findViewById(R.id.iv_effect);
        ivNormal = findViewById(R.id.iv_normal);
        btnSave = findViewById(R.id.btn_save);
        btnShare = findViewById(R.id.btn_share);
        adContainer = findViewById(R.id.fl_ad_container);
    }

    @Override
    public void initData() {
    }

    @Override
    public void configViews() {
        if (TextUtils.equals(function,FunctionBean.ID_CHANGE_GENDER)) {
            tvTitle.setText("性别转换");
        }else if (TextUtils.equals(function,FunctionBean.ID_CHANGE_CHILD)) {
            tvTitle.setText("童颜相机");
        }
        LogUtils.e("zhangning", "imagePath = " + imagePath);
        BitmapUtil.loadImageCircle(this,imagePath,R.drawable.bg_shape_gender_gray,ivNormal);
        BitmapUtil.loadImageCircle(this,imagePath,R.drawable.bg_shape_gender_gray,ivEffect);
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
        return PageIdentity.APP_GENDEREFFECT;
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
