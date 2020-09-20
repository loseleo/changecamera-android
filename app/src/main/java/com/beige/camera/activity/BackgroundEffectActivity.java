package com.beige.camera.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.beige.camera.R;
import com.beige.camera.common.base.BaseActivity;
import com.beige.camera.common.router.PageIdentity;
import com.beige.camera.common.utils.ImageUtils;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.MsgUtils;
import com.beige.camera.common.utils.imageloader.BitmapUtil;
import com.beige.camera.common.view.BaseDragZoomImageView;
import com.beige.camera.common.view.loadding.CustomDialog;
import com.beige.camera.contract.IEffectImageView;
import com.beige.camera.dagger.MainComponentHolder;
import com.beige.camera.presenter.EffectImagePresenter;
import com.beige.camera.utils.AdHelper;

import javax.inject.Inject;


@Route(path = PageIdentity.APP_BACKGROUNDEFFECT)
public class BackgroundEffectActivity extends BaseActivity implements IEffectImageView {

    public String bannerAdType = "bannerAdType";
    public String rewardedAdType = "rewardedAdType";

    private ImageView icBack;
    private TextView tvTitle;
    private ConstraintLayout clSaveImage;
    private ImageView ivPreviewBg;
    private BaseDragZoomImageView ivPreview;
    private TextView btnSave;
    private TextView btnShare;
    private FrameLayout adContainer;
    private TextView tvBirth ;
    private TextView tvDieage ;
    private TextView tvProfession ;

    @Inject
    public EffectImagePresenter mPresenter;

    private String effectImage = "";

    @Autowired(name = "image_path")
    String imagePath;

    private CustomDialog mCustomDialog;

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
        return R.layout.activity_background_effect;
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
        tvTitle = findViewById(R.id.tv_title);
        ivPreviewBg = findViewById(R.id.iv_preview_bg);
        clSaveImage = findViewById(R.id.cl_save_image);
        ivPreview = findViewById(R.id.iv_preview);
        tvBirth = findViewById(R.id.tv_birth_content);
        tvDieage = findViewById(R.id.tv_dieage_content);
        tvProfession = findViewById(R.id.tv_profession_content);
        btnSave = findViewById(R.id.btn_save);
        btnShare = findViewById(R.id.btn_share);
        adContainer = findViewById(R.id.fl_ad_container);
        AdHelper.showBannerAdView(bannerAdType,adContainer);
    }

    @Override
    public void initData() {
    }

    @Override
    public void configViews() {

        LogUtils.e("zhangning", "imagePath = " + imagePath);
        BitmapUtil.loadImage(this,imagePath,ivPreview,R.drawable.bg_perview_white,R.drawable.bg_perview_white);
        BitmapUtil.loadImage(this,imagePath,ivPreviewBg,R.drawable.bg_perview_white,R.drawable.bg_perview_white);
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

//        mPresenter.getFaceEditAttr(imagePath,actionType);
    }



    @Nullable
    @Override
    public String getPageName() {
        return PageIdentity.APP_BACKGROUNDEFFECT;
    }


    @Override
    public void onResultEffectImage(String image ,String actionType) {
        if (TextUtils.isEmpty(image)) {
            MsgUtils.showToastCenter(BackgroundEffectActivity.this,"图片处理失败");
            effectImage = imagePath;
        }
        effectImage = image;
        BitmapUtil.loadImageRound(this,effectImage,ivPreview,R.drawable.bg_perview_white,R.drawable.bg_perview_white,6);
    }


    @Override
    public void onResultAge(String age) {

    }


    private void saveImage(ViewGroup view){
        Bitmap bitmap = ImageUtils.getBitmapByView(view);//contentLly是布局文件
        ImageUtils.saveImageToGallery(BackgroundEffectActivity.this, bitmap, System.currentTimeMillis() + ".jpg", new ImageUtils.CallBack() {
            @Override
            public void onStart() {
                mCustomDialog = CustomDialog.instance(BackgroundEffectActivity.this);
                mCustomDialog.show();
            }

            @Override
            public void onSuccess() {
                MsgUtils.showToastCenter(BackgroundEffectActivity.this,"图片保存成功，请在相册中点击分享");
                if (mCustomDialog != null) {
                    mCustomDialog.dismiss();
                }
            }

            @Override
            public void onFail() {
                MsgUtils.showToastCenter(BackgroundEffectActivity.this,"图片保存失败");
                if (mCustomDialog != null) {
                    mCustomDialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCustomDialog != null) {
            mCustomDialog.dismiss();
            mCustomDialog = null;
        }
    }
}
