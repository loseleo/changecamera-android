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

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

@Route(path = PageIdentity.APP_OLDEFFECT)
public class OldEffectActivity extends BaseActivity implements IEffectImageView {

    private ImageView ivPreview;
    private ImageView icBack;
    private TextView btnSave;
    private TextView btnShare;
    private FrameLayout adContainer;
    private ConstraintLayout layoutAdMantle;
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
    HashMap<String,String>  effectImageMap = new HashMap<String,String>();
    private ArrayList<String> showADList = new ArrayList<>();

    @Autowired(name = "image_path")
    String imagePath;

    private AdHelper adHelper;

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
        return R.layout.activity_oldeffect;
    }

    @Override
    protected void onResume() {
        super.onResume();
        adHelper.showBannerAdView(AdHelper.getBannerAdTypeById(FunctionBean.ID_CHANGE_OLD),adContainer);
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
        ivPreview = findViewById(R.id.iv_preview_bg);
        btnSave = findViewById(R.id.btn_save);
        btnShare = findViewById(R.id.btn_share);
        adContainer = findViewById(R.id.fl_ad_container);
        layoutAdMantle = findViewById(R.id.layout_ad_mantle);
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
        adHelper = new AdHelper();

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
                finshActivity();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage(ivPreview);
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage(ivPreview);
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
                selectAge = "50";
                setSelectAge();
            }
        });
        clSelectAge50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAge = "80";
                setSelectAge();
            }
        });

        layoutAdMantle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adHelper.playRewardedVideo(OldEffectActivity.this, AdHelper.getRewardedAdTypeById(FunctionBean.ID_CHANGE_OLD), new AdHelper.PlayRewardedAdCallback() {
                    @Override
                    public void onDismissed(int action) {
                        layoutAdMantle.setVisibility(View.GONE);
                        showADList.add(selectAge);
                    }

                    @Override
                    public void onFail() {
                        layoutAdMantle.setVisibility(View.GONE);
                    }
                });
            }
        });
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
        } else if (TextUtils.equals(selectAge, "50")) {
            selectAge40.setVisibility(View.VISIBLE);
            tvSelectAge40.setTextColor(getResources().getColor(R.color.common_color_FF390B));
        } else if (TextUtils.equals(selectAge, "80")) {
            selectAge50.setVisibility(View.VISIBLE);
            tvSelectAge50.setTextColor(getResources().getColor(R.color.common_color_FF390B));
        }
        setEffectImage();
    }

    private void setEffectImage(){
        if (TextUtils.equals(selectAge, "now")) {
            BitmapUtil.loadImage(imagePath,ivPreview);
            layoutAdMantle.setVisibility(View.GONE);
        }else{
            String effectImage = effectImageMap.get(selectAge);

            if (showADList.contains(selectAge)) {
                layoutAdMantle.setVisibility(View.GONE);
            }else{
                layoutAdMantle.setVisibility(View.VISIBLE);
            }

            if(TextUtils.isEmpty(effectImage)){
                mPresenter.getFaceEditAttr(imagePath, "TO_AGE",selectAge);
            }else{
                BitmapUtil.loadImage(effectImage,ivPreview);
            }
        }
    }

    @Override
    public void onResultEffectImage(String image, String actionType) {
        if (TextUtils.isEmpty(image)) {
            MsgUtils.showToastCenter(OldEffectActivity.this,"图片处理失败");
        }
        effectImageMap.put(selectAge,image);
        setEffectImage();
    }

    @Override
    public void onResultAge(String age) {
    }


    private void saveImage(View view){

        adHelper.playRewardedVideo(OldEffectActivity.this, AdHelper.getRewardedAdTypeById(FunctionBean.ID_CHANGE_OLD), new AdHelper.PlayRewardedAdCallback() {
            @Override
            public void onDismissed(int action) {

                Bitmap bitmap = ImageUtils.getBitmapByView(view);//contentLly是布局文件
                ImageUtils.saveImageToGallery(OldEffectActivity.this, bitmap, System.currentTimeMillis() + ".jpg", new ImageUtils.CallBack() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess() {
                        MsgUtils.showToastCenter(OldEffectActivity.this,"图片保存成功，请在相册中点击分享");
                    }

                    @Override
                    public void onFail() {
                        MsgUtils.showToastCenter(OldEffectActivity.this,"图片保存失败");
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

        adHelper.playFullScreenVideoAd(this, AdHelper.getFullScreenVideoAdTypeById(FunctionBean.ID_CHANGE_OLD), new AdHelper.PlayRewardedAdCallback() {
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
