package com.beige.camera.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.beige.camera.R;
import com.beige.camera.bean.FunctionBean;
import com.beige.camera.common.base.BaseActivity;
import com.beige.camera.common.constant.Constant;
import com.beige.camera.common.router.PageIdentity;
import com.beige.camera.common.utils.ImageUtils;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.MsgUtils;
import com.beige.camera.common.utils.PackageUtils;
import com.beige.camera.common.utils.imageloader.BitmapUtil;
import com.beige.camera.common.view.loadding.CustomDialog;
import com.beige.camera.contract.IEffectImageView;
import com.beige.camera.dagger.MainComponentHolder;
import com.beige.camera.presenter.EffectImagePresenter;
import com.beige.camera.utils.AdHelper;

import javax.inject.Inject;


@Route(path = PageIdentity.APP_AGEEFFECT)
public class AgeEffectActivity extends BaseActivity implements IEffectImageView {

    private ImageView icBack;
    private ImageView ivPreview;
    private ConstraintLayout clSaveImage;
    private TextView tvAge;
    private TextView btnSave;
    private TextView btnShare;
    private ConstraintLayout layoutAdMantle;
    private FrameLayout adContainer;
    @Inject
    public EffectImagePresenter mPresenter;

    @Autowired(name = "image_path")
    String imagePath;
    AdHelper adHelper;
    CustomDialog mCustomDialog;
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
    protected void onResume() {
        super.onResume();
        adHelper.showBannerAdView(AdHelper.getBannerAdTypeById(FunctionBean.ID_DETECTION_AGE),adContainer);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_age_effect;
    }

    @Override
    public void initViews() {
        icBack = findViewById(R.id.ic_back);
        ivPreview = findViewById(R.id.iv_preview_bg);
        layoutAdMantle = findViewById(R.id.layout_ad_mantle);
        clSaveImage = findViewById(R.id.cl_save_image);
        tvAge = findViewById(R.id.tv_age);
        btnSave = findViewById(R.id.btn_save);
        btnShare = findViewById(R.id.btn_share);
        adContainer =findViewById(R.id.fl_ad_container);
        layoutAdMantle.setVisibility(View.VISIBLE);
        adHelper = new AdHelper();
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

        mPresenter.getEffectAge(imagePath);

        layoutAdMantle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adHelper.playRewardedVideo(AgeEffectActivity.this, AdHelper.getRewardedAdTypeById(FunctionBean.ID_DETECTION_AGE), new AdHelper.PlayRewardedAdCallback() {
                    @Override
                    public void onDismissed(int action) {
                        layoutAdMantle.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFail() {

                    }
                });
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

    private void saveImage(ViewGroup view){
        adHelper.playRewardedVideo(AgeEffectActivity.this, AdHelper.getRewardedAdTypeById(FunctionBean.ID_DETECTION_AGE), new AdHelper.PlayRewardedAdCallback() {
            @Override
            public void onDismissed(int action) {
                Bitmap bitmap = ImageUtils.getBitmapByView(view);//contentLly是布局文件
                ImageUtils.saveImageToGallery(AgeEffectActivity.this, bitmap, System.currentTimeMillis() + ".jpg", new ImageUtils.CallBack() {
                    @Override
                    public void onStart() {
                        mCustomDialog = CustomDialog.instance(AgeEffectActivity.this);
                        mCustomDialog.show();
                    }

                    @Override
                    public void onSuccess() {
                        MsgUtils.showToastCenter(AgeEffectActivity.this,"图片保存成功，请在相册中点击分享");
                        if (mCustomDialog != null) {
                            mCustomDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFail() {
                        MsgUtils.showToastCenter(AgeEffectActivity.this,"图片保存失败");
                        if (mCustomDialog != null) {
                            mCustomDialog.dismiss();
                        }
                    }
                });
            }
            @Override
            public void onFail() {

            }
        });
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
             age = (int) (10 + Math.random() * (120 - 10 + 1)) + "";
        }
        tvAge.setText(age + " 岁");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        if (mCustomDialog != null) {
            mCustomDialog.dismiss();
            mCustomDialog = null;
        }
    }

    public void finshActivity(){

        adHelper.playFullScreenVideoAd(this, AdHelper.getFullScreenVideoAdTypeById(FunctionBean.ID_DETECTION_AGE), new AdHelper.PlayRewardedAdCallback() {
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
