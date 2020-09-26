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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.beige.camera.R;
import com.beige.camera.common.base.BaseActivity;
import com.beige.camera.common.router.PageIdentity;
import com.beige.camera.common.utils.ImageUtils;
import com.beige.camera.common.utils.MsgUtils;
import com.beige.camera.common.utils.imageloader.BitmapUtil;
import com.beige.camera.common.view.loadding.CustomDialog;
import com.beige.camera.contract.IEffectImageView;
import com.beige.camera.utils.AdHelper;


@Route(path = PageIdentity.APP_BEAUTYVSEFFECT)
public class BeautyVsEffectActivity extends BaseActivity implements IEffectImageView {

    public String bannerAdType = "bannerAdType";
    public String rewardedAdType = "rewardedAdType";
    public String fullScreenVideoType = "fullScreenVideoType";

    private ImageView icBack;
    private TextView tvTitle;
    private TextView btnSave;
    private TextView btnShare;

    private ImageView mIv_perview_left;
    private ImageView mIv_champion_left;
    private ImageView mIv_perview_right;
    private ImageView mIv_champion_right;
    private TextView mTv_score_left;
    private TextView mTv_score_right;
    private TextView mTv_score_left_eye;
    private ProgressBar mProgressbar_left_eye;
    private TextView mTv_score_right_eye;
    private ProgressBar mProgressbar_right_eye;
    private TextView mTv_score_left_nose;
    private ProgressBar mProgressbar_left_nose;
    private TextView mTv_score_right_nose;
    private ProgressBar mProgressbar_right_nose;
    private TextView mTv_score_left_mouth;
    private ProgressBar mProgressbar_left_mouth;
    private TextView mTv_score_right_mouth;
    private ProgressBar mProgressbar_right_mouth;
    private TextView mTv_score_left_laugh;
    private ProgressBar mProgressbar_left_laugh;
    private TextView mTv_score_right_laugh;
    private ProgressBar mProgressbar_right_laugh;
    private TextView mTv_score_left_skin;
    private ProgressBar mProgressbar_left_skin;
    private TextView mTv_score_right_skin;
    private ProgressBar mProgressbar_right_skin;
    private ConstraintLayout clSaveImage;
    private FrameLayout adContainer;
    private AdHelper adHelper;

    @Autowired(name = "image_path")
    String imagePath;

    @Autowired(name = "image_path_vs")
    String imagePathVS;


    private CustomDialog mCustomDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupActivityComponent() {
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_beautyvs_effect;
    }

    @Override
    protected void onResume() {
        super.onResume();
        adHelper.showBannerAdView(bannerAdType, adContainer);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void initViews() {
        icBack = findViewById(R.id.ic_back);
        tvTitle = findViewById(R.id.tv_title);
        btnSave = findViewById(R.id.btn_save);
        btnShare = findViewById(R.id.btn_share);
        mIv_perview_left = (ImageView) findViewById(R.id.iv_perview_left);
        mIv_champion_left = (ImageView) findViewById(R.id.iv_champion_left);
        mIv_perview_right = (ImageView) findViewById(R.id.iv_perview_right);
        mIv_champion_right = (ImageView) findViewById(R.id.iv_champion_right);
        mTv_score_left = (TextView) findViewById(R.id.tv_score_left);
        mTv_score_right = (TextView) findViewById(R.id.tv_score_right);
        mTv_score_left_eye = (TextView) findViewById(R.id.tv_score_left_eye);
        mProgressbar_left_eye = (ProgressBar) findViewById(R.id.progressbar_left_eye);
        mTv_score_right_eye = (TextView) findViewById(R.id.tv_score_right_eye);
        mProgressbar_right_eye = (ProgressBar) findViewById(R.id.progressbar_right_eye);
        mTv_score_left_nose = (TextView) findViewById(R.id.tv_score_left_nose);
        mProgressbar_left_nose = (ProgressBar) findViewById(R.id.progressbar_left_nose);
        mTv_score_right_nose = (TextView) findViewById(R.id.tv_score_right_nose);
        mProgressbar_right_nose = (ProgressBar) findViewById(R.id.progressbar_right_nose);
        mTv_score_left_mouth = (TextView) findViewById(R.id.tv_score_left_mouth);
        mProgressbar_left_mouth = (ProgressBar) findViewById(R.id.progressbar_left_mouth);
        mTv_score_right_mouth = (TextView) findViewById(R.id.tv_score_right_mouth);
        mProgressbar_right_mouth = (ProgressBar) findViewById(R.id.progressbar_right_mouth);
        mTv_score_left_laugh = (TextView) findViewById(R.id.tv_score_left_laugh);
        mProgressbar_left_laugh = (ProgressBar) findViewById(R.id.progressbar_left_laugh);
        mTv_score_right_laugh = (TextView) findViewById(R.id.tv_score_right_laugh);
        mProgressbar_right_laugh = (ProgressBar) findViewById(R.id.progressbar_right_laugh);
        mTv_score_left_skin = (TextView) findViewById(R.id.tv_score_left_skin);
        mProgressbar_left_skin = (ProgressBar) findViewById(R.id.progressbar_left_skin);
        mTv_score_right_skin = (TextView) findViewById(R.id.tv_score_right_skin);
        mProgressbar_right_skin = (ProgressBar) findViewById(R.id.progressbar_right_skin);
        clSaveImage = findViewById(R.id.cl_save_image);
        adContainer = findViewById(R.id.fl_ad_container);
        adHelper = new AdHelper();

    }

    @Override
    public void initData() {
    }

    @Override
    public void configViews() {

        BitmapUtil.loadImageCircle(this, imagePath, R.drawable.bg_shape_gender_gray, mIv_perview_left);
        BitmapUtil.loadImageCircle(this, imagePathVS, R.drawable.bg_shape_gender_gray, mIv_perview_right);
        int tScoreLeft = 0;
        int tScoreRight = 0;
        for (int i = 0; i < 5; i++) {
            int scoreLeft = randomScore();
            int scoreRight = randomScore();
            tScoreLeft = tScoreLeft + scoreLeft;
            tScoreRight = tScoreRight + scoreRight;

            switch (i) {
                case 0:
                    mTv_score_left_eye.setText(scoreLeft + "");
                    mProgressbar_left_eye.setProgress(scoreLeft);
                    mTv_score_right_eye.setText(scoreRight + "");
                    mProgressbar_right_eye.setProgress(scoreRight);
                    break;
                case 1:
                    mTv_score_left_nose.setText(scoreLeft + "");
                    mProgressbar_left_nose.setProgress(scoreLeft);
                    mTv_score_right_nose.setText(scoreRight + "");
                    mProgressbar_right_nose.setProgress(scoreRight);
                    break;
                case 2:
                    mTv_score_left_mouth.setText(scoreLeft + "");
                    mProgressbar_left_mouth.setProgress(scoreLeft);
                    mTv_score_right_mouth.setText(scoreRight + "");
                    mProgressbar_right_mouth.setProgress(scoreRight);
                    break;
                case 3:
                    mTv_score_left_laugh.setText(scoreLeft + "");
                    mProgressbar_left_laugh.setProgress(scoreLeft);
                    mTv_score_right_laugh.setText(scoreRight + "");
                    mProgressbar_right_laugh.setProgress(scoreRight);
                    break;
                case 4:
                    mTv_score_left_skin.setText(scoreLeft + "");
                    mProgressbar_left_skin.setProgress(scoreLeft);
                    mTv_score_right_skin.setText(scoreRight + "");
                    mProgressbar_right_skin.setProgress(scoreRight);
                    break;
            }

        }

        if (tScoreLeft >tScoreRight) {
            mIv_champion_left.setVisibility(View.VISIBLE);
            mIv_champion_right.setVisibility(View.GONE);
        }else{
            mIv_champion_left.setVisibility(View.GONE);
            mIv_champion_right.setVisibility(View.VISIBLE);
        }

        mTv_score_left.setText(tScoreLeft/5 + "");
        mTv_score_right.setText(tScoreRight/5 + "");

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

        adHelper.playRewardedVideo(BeautyVsEffectActivity.this, rewardedAdType, new AdHelper.PlayRewardedAdCallback() {
            @Override
            public void onDismissed(int action) {
            }
            @Override
            public void onFail() {
            }
        });

    }


    private int randomScore() {
        return (int) (10 + Math.random() * (100 - 10 + 1));
    }


    @Nullable
    @Override
    public String getPageName() {
        return PageIdentity.APP_BEAUTYVSEFFECT;
    }


    @Override
    public void onResultEffectImage(String image, String actionType) {
        if (TextUtils.isEmpty(image)) {
            MsgUtils.showToastCenter(BeautyVsEffectActivity.this, "图片处理失败");
        }
    }

    @Override
    public void onResultAge(String age) {

    }


    private void saveImage(ViewGroup view){

        adHelper.playRewardedVideo(BeautyVsEffectActivity.this, rewardedAdType, new AdHelper.PlayRewardedAdCallback() {
            @Override
            public void onDismissed(int action) {
                Bitmap bitmap = ImageUtils.getBitmapByView(view);//contentLly是布局文件
                ImageUtils.saveImageToGallery(BeautyVsEffectActivity.this, bitmap, System.currentTimeMillis() + ".jpg", new ImageUtils.CallBack() {
                    @Override
                    public void onStart() {
                        mCustomDialog = CustomDialog.instance(BeautyVsEffectActivity.this);
                        mCustomDialog.show();
                    }

                    @Override
                    public void onSuccess() {
                        MsgUtils.showToastCenter(BeautyVsEffectActivity.this,"图片保存成功，请在相册中点击分享");
                        if (mCustomDialog != null) {
                            mCustomDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFail() {
                        MsgUtils.showToastCenter(BeautyVsEffectActivity.this,"图片保存失败");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCustomDialog != null) {
            mCustomDialog.dismiss();
            mCustomDialog = null;
        }
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

        adHelper.playFullScreenVideoAd(this, fullScreenVideoType, new AdHelper.PlayRewardedAdCallback() {
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
