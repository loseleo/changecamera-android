package com.beige.camera.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

@Route(path = PageIdentity.APP_IMGUPLOAD)
public class UploadImgActivity extends BaseActivity {

    private ImageView ivPreview;
    private ImageView icBack;
    private ProgressBar progressbar;
    private TextView tvProgress;

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
        return R.layout.activity_img_upload;
    }

    @Override
    public void initViews() {
        icBack = findViewById(R.id.ic_back);
        ivPreview = findViewById(R.id.iv_preview_bg);
        progressbar = findViewById(R.id.progressbar);
        tvProgress = findViewById(R.id.tv_progress);
    }

    @Override
    public void initData() {
    }

    @Override
    public void configViews() {
        LogUtils.e("zhangning", "imagePath = " + imagePath);
        BitmapUtil.loadImageCircle(this, imagePath, R.color.black, ivPreview);
//        setProgress();
        if(TextUtils.equals(function,FunctionBean.ID_CHANGE_ANIMAL) || TextUtils.equals(function,FunctionBean.ID_CHANGE_ANIMALFACE)){
            setProgress();
        }else{
            upLoadImage();
        }
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Nullable
    @Override
    public String getPageName() {
        return PageIdentity.APP_IMGUPLOAD;
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
                goNextActivity(ossFilePath);
            }

            @Override
            public void onFailure(String requestId, String errorCode, String message) {
                MsgUtils.showToastCenter(UploadImgActivity.this,"图片处理失败");
            }
        });
    }

    private void setProgress(){

        Observable.interval(100, 100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .take(21)
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        int progress = (int) (aLong * 5);
                        progressbar.setProgress(progress);
                        tvProgress.setText(progress + "%");
                        if(aLong == 20){
                            goNextActivity(imagePath);
                        }
                    }
                })
                .subscribe();
    }

    private void goNextActivity(String imagePath){

        if (TextUtils.equals(function,FunctionBean.ID_CHANGE_OLD)) {
            AppNavigator.goOldEfectActivity(UploadImgActivity.this,imagePath);
        }else if (TextUtils.equals(function,FunctionBean.ID_CHANGE_GENDER_BOY) || TextUtils.equals(function,FunctionBean.ID_CHANGE_GENDER_GIRL)) {
            AppNavigator.goGenderEffectActivity(UploadImgActivity.this,imagePath,function);
        }else if (TextUtils.equals(function,FunctionBean.ID_CHANGE_CHILD)) {
            AppNavigator.goGenderEffectActivity(UploadImgActivity.this,imagePath,function);
        }else if (TextUtils.equals(function,FunctionBean.ID_CHANGE_CARTOON)) {
            AppNavigator.goCartoonEffectActivity(UploadImgActivity.this,imagePath);
        }else if (TextUtils.equals(function,FunctionBean.ID_CHANGE_ANIMAL)) {
            AppNavigator.goAnimalEffectActivity(UploadImgActivity.this,imagePath);
        }else if (TextUtils.equals(function,FunctionBean.ID_DETECTION_AGE)) {
            AppNavigator.goAgeEffectActivity(UploadImgActivity.this,imagePath);
        }else if (TextUtils.equals(function,FunctionBean.ID_DETECTION_PAST)) {
            AppNavigator.goPastEffectActivity(UploadImgActivity.this,imagePath);
        }else  if (TextUtils.equals(function,FunctionBean.ID_CHANGE_BACKGROUND)) {
            AppNavigator.goBackgroundffectActivity(UploadImgActivity.this,imagePath);
        }else if (TextUtils.equals(function,FunctionBean.ID_CHANGE_HAIR)) {
            AppNavigator.goFaceMegreEffectActivity(UploadImgActivity.this,imagePath,function);
        }else if (TextUtils.equals(function,FunctionBean.ID_CHANGE_CLOTHES)) {
            AppNavigator.goFaceMegreEffectActivity(UploadImgActivity.this,imagePath,function);
        }else if (TextUtils.equals(function,FunctionBean.ID_CHANGE_CUSTOMS)) {
            AppNavigator.goFaceMegreEffectActivity(UploadImgActivity.this,imagePath,function);
        }else if (TextUtils.equals(function,FunctionBean.ID_CHANGE_ANIMALFACE)) {
            AppNavigator.goAnimalFaceEffectActivity(UploadImgActivity.this,imagePath);
        }
        finish();
    }

}
