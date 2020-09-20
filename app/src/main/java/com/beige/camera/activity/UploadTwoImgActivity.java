package com.beige.camera.activity;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.beige.camera.R;
import com.beige.camera.bean.FunctionBean;
import com.beige.camera.common.base.BaseActivity;
import com.beige.camera.common.router.AppNavigator;
import com.beige.camera.common.router.PageIdentity;
import com.beige.camera.common.upload.UploadFileManager;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.MsgUtils;
import com.beige.camera.common.utils.imageloader.BitmapUtil;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

@Route(path = PageIdentity.APP_TWOIMGUPLOAD)
public class UploadTwoImgActivity extends BaseActivity {

    public static final  int SELECT_IMG_REQUESTCODE = 8889;

    private ImageView icBack;
    private ProgressBar progressbar;
    private TextView tvProgress;
    private TextView tabTitle;
    private ImageView ivPreviewOne;
    private ImageView ivPreviewTwo;
    private ImageView ivCenter;
    private ConstraintLayout clGuid;
    private ConstraintLayout clProgress;
    private ImageView ivGuid;
    private TextView tvGuid;
    private ImageView ivGuidHand;

    @Autowired(name = "image_path")
    String imagePath;
    @Autowired(name = "function")
    String function;

    String imagePathVS;

    private ValueAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupActivityComponent() {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_twoimg_upload;
    }

    @Override
    public void initViews() {
        icBack = findViewById(R.id.ic_back);
        tabTitle = findViewById(R.id.tab_title);
        ivPreviewOne = findViewById(R.id.iv_preview_one);
        ivPreviewTwo = findViewById(R.id.iv_preview_two);
        ivCenter = findViewById(R.id.iv_center);
        clGuid = findViewById(R.id.cl_guid);
        ivGuid = findViewById(R.id.iv_guid);
        tvGuid = findViewById(R.id.tv_guid);
        ivGuidHand = findViewById(R.id.iv_guid_hand);
        clProgress = findViewById(R.id.cl_progress);
        progressbar = findViewById(R.id.progressbar);
        tvProgress = findViewById(R.id.tv_progress);
    }

    @Override
    public void initData() {
    }

    @Override
    public void configViews() {
        LogUtils.e("zhangning", "imagePath = " + imagePath);
        BitmapUtil.loadImageRound(this, imagePath, ivPreviewOne,R.color.black,R.color.black);

        if(TextUtils.isEmpty(imagePathVS)){
            showGuidAnimal(ivGuidHand);
        }
        if (TextUtils.equals(function, FunctionBean.ID_DETECTION_BABY)) {
            tabTitle.setText("宝宝预测");
            ivCenter.setImageResource(R.mipmap.icon_taoxin);
            ivGuid.setImageResource(R.mipmap.icon_head_normal);
            tvGuid.setText("你的另一半");
        } else if (TextUtils.equals(function, FunctionBean.ID_DETECTION_VS)) {
            tabTitle.setText("比比谁美");
            ivCenter.setImageResource(R.mipmap.icon_vs);
            ivGuid.setImageResource(R.mipmap.icon_head_girl);
            tvGuid.setText("你比美的对手");
        }
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        clGuid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppNavigator.goCameraActivity(UploadTwoImgActivity.this,CameraActivity.FUNCTION_IMG_TOOL,SELECT_IMG_REQUESTCODE);
            }
        });

    }

    @Nullable
    @Override
    public String getPageName() {
        return PageIdentity.APP_TWOIMGUPLOAD;
    }

    private void upLoadImage() {
        if (TextUtils.isEmpty(imagePath)) {
            return;
        }
        UploadFileManager.getInstance().uploadFile("IMG", imagePath, new UploadFileManager.UploadFileCallback() {
            @Override
            public void onProgress(long currentSize, long totalSize) {

                progressbar.post(new Runnable() {
                    @Override
                    public void run() {
                        int progress = (int) (currentSize * 100 / totalSize);
                        progressbar.setProgress(progress);
                        tvProgress.setText(progress + "%");
                    }
                });
            }

            @Override
            public void onSuccess(String ossFilePath, String eTag, String requestId) {
                Log.e("zhangning", "OssCoverPtah :" + ossFilePath);
//                submitVideoBean.setOssCoverPtah(ossFilePath);
//                submitVideoInfo(submitVideoBean);
//                goNextActivity(ossFilePath);
            }

            @Override
            public void onFailure(String requestId, String errorCode, String message) {
                MsgUtils.showToastCenter(UploadTwoImgActivity.this, "图片处理失败");
            }
        });
    }

    private void setProgress() {
        clProgress.setVisibility(View.VISIBLE);
        Observable.interval(100, 100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .take(21)
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtils.e("zhangning", "Long = " + aLong);
                        int progress = (int) (aLong * 5);
                        progressbar.setProgress(progress);
                        tvProgress.setText(progress + "%");
                        if (aLong == 20) {
                            goNextActivity(imagePath,imagePathVS);
                        }
                    }
                })
                .subscribe();
    }

    private void showGuidAnimal(View view) {

        if (animator == null) {
            animator = ValueAnimator.ofFloat(1.0f, 0.6f, 1.2f, 1.0f, 0.6f, 1.2f, 1.0f);
            animator.setDuration(6000L);//设置缩放时间
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float scale = (Float) animation.getAnimatedValue();
                    view.setScaleX(scale);
                    view.setScaleY(scale);
                }
            });
            animator.setInterpolator(new LinearInterpolator());
        }
        animator.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (resultCode == RESULT_OK && requestCode == SELECT_IMG_REQUESTCODE && data != null) {
            imagePathVS = data.getStringExtra("imagePathVS");
             if(!TextUtils.isEmpty(imagePathVS)){
                 BitmapUtil.loadImageRound(this, imagePathVS, ivPreviewTwo,R.color.black,R.color.black);
                 clGuid.setVisibility(View.GONE);
                 if (animator != null && animator.isRunning()) {
                     animator.cancel();
                     animator = null;
                 }
                 setProgress();
             }else{
                 showGuidAnimal(ivGuidHand);
             }
        }
    }
    private void goNextActivity(String imagePath,String imagePathVS) {
       if (TextUtils.equals(function, FunctionBean.ID_DETECTION_BABY)) {
            AppNavigator.goBabyEfectActivity(UploadTwoImgActivity.this,imagePath);
        } else if (TextUtils.equals(function, FunctionBean.ID_DETECTION_VS)) {
            AppNavigator.goBeautyVsEfectActivity(UploadTwoImgActivity.this,imagePath,imagePathVS);
        }
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animator != null && animator.isRunning()) {
            animator.cancel();
            animator = null;
        }
    }
}
