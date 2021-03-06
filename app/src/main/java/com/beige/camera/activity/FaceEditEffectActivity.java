package com.beige.camera.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.beige.camera.R;
import com.beige.camera.bean.FunctionBean;
import com.beige.camera.common.base.BaseActivity;
import com.beige.camera.common.router.PageIdentity;
import com.beige.camera.common.utils.ImageUtils;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.MsgUtils;
import com.beige.camera.common.utils.imageloader.BitmapUtil;
import com.beige.camera.contract.IEffectImageView;
import com.beige.camera.dagger.MainComponentHolder;
import com.beige.camera.presenter.EffectImagePresenter;
import com.beige.camera.utils.AdHelper;

import javax.inject.Inject;


@Route(path = PageIdentity.APP_FACEEDITEFFECT)
public class FaceEditEffectActivity extends BaseActivity implements IEffectImageView {

    private ConstraintLayout clSaveImage;
    private ImageView icBack;
    private TextView tvTitle;
    private ImageView ivEffect;
    private ImageView ivNormal;
    private TextView btnSave;
    private TextView btnShare;
    private FrameLayout adContainer;

    @Inject
    public EffectImagePresenter mPresenter;

    private String effectImage = "";
    private AdHelper adHelper;
    @Autowired(name = "image_path")
    String imagePath;
    @Autowired(name = "function")
    String function;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.attachView(this);
    }

    @Override
    protected void setupActivityComponent() {
        MainComponentHolder.getInstance().inject(this);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_gender_effect;
    }

    @Override
    protected void onResume() {
        super.onResume();
        adHelper.showBannerAdView(AdHelper.getBannerAdTypeById(function),adContainer);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void initViews() {
        icBack = findViewById(R.id.ic_back);
        tvTitle = findViewById(R.id.tv_title);
        ivEffect = findViewById(R.id.iv_effect);
        ivNormal = findViewById(R.id.iv_normal);
        clSaveImage = findViewById(R.id.cl_save_image);
        btnSave = findViewById(R.id.btn_save);
        btnShare = findViewById(R.id.btn_share);
        adContainer = findViewById(R.id.fl_ad_container);
        adHelper = new AdHelper();
    }

    @Override
    public void initData() {
    }

    @Override
    public void configViews() {
        if (TextUtils.equals(function,FunctionBean.ID_CHANGE_GENDER_BOY) || TextUtils.equals(function,FunctionBean.ID_CHANGE_GENDER_GIRL)) {
            tvTitle.setText("性别转换");
        }else if (TextUtils.equals(function,FunctionBean.ID_CHANGE_CHILD)) {
            tvTitle.setText("童颜相机");
        }
        LogUtils.e("zhangning", "imagePath = " + imagePath);
        BitmapUtil.loadImageCircle(this,imagePath,R.drawable.bg_shape_gender_gray,ivNormal);
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finshActivity();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage(clSaveImage);
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage(clSaveImage);
            }
        });

        String actionType = "TO_FEMALE";
        if (TextUtils.equals(function,FunctionBean.ID_CHANGE_GENDER_BOY)) {
            actionType = "TO_FEMALE";
        }else if (TextUtils.equals(function,FunctionBean.ID_CHANGE_GENDER_GIRL)) {
            actionType = "TO_MALE";
        }else if (TextUtils.equals(function,FunctionBean.ID_CHANGE_CHILD)) {
            actionType = "TO_KID";
        }
        mPresenter.getFaceEditAttr(imagePath,actionType);
        adHelper.playRewardedVideo(FaceEditEffectActivity.this, AdHelper.getRewardedAdTypeById(function), new AdHelper.PlayRewardedAdCallback() {
            @Override
            public void onDismissed(int action) {
            }
            @Override
            public void onFail() {

            }
        });

    }



    @Nullable
    @Override
    public String getPageName() {
        return PageIdentity.APP_FACEEDITEFFECT;
    }


    @Override
    public void onResultEffectImage(String image ,String actionType) {
        if (TextUtils.isEmpty(image)) {
            MsgUtils.showToastCenter(FaceEditEffectActivity.this,"图片处理失败");
            effectImage = imagePath;
        }
        effectImage = image;
        BitmapUtil.loadImageCircle(this,effectImage,R.drawable.bg_shape_gender_gray,ivEffect);
    }


    @Override
    public void onResultAge(String age) {
    }


    private void saveImage(View view){

        adHelper.playRewardedVideo(FaceEditEffectActivity.this, AdHelper.getRewardedAdTypeById(function), new AdHelper.PlayRewardedAdCallback() {
            @Override
            public void onDismissed(int action) {
                Bitmap bitmap = ImageUtils.getBitmapByView(view);//contentLly是布局文件
                ImageUtils.saveImageToGallery(FaceEditEffectActivity.this, bitmap, System.currentTimeMillis() + ".jpg", new ImageUtils.CallBack() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess() {
                        MsgUtils.showToastCenter(FaceEditEffectActivity.this,"图片保存成功，请在相册中点击分享");
                    }

                    @Override
                    public void onFail() {
                        MsgUtils.showToastCenter(FaceEditEffectActivity.this,"图片保存失败");
                    }
                });
            }
            @Override
            public void onFail() {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finshActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void finshActivity(){

        adHelper.playFullScreenVideoAd(this, AdHelper.getFullScreenVideoAdTypeById(function), new AdHelper.PlayRewardedAdCallback() {
            @Override
            public void onDismissed(int action) {
                finish();
            }

            @Override
            public void onFail() {
                finish();
            }
        });
    }
}
