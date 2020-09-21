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

import com.alibaba.android.arouter.facade.annotation.Route;
import com.beige.camera.R;
import com.beige.camera.bean.FunctionBean;
import com.beige.camera.bean.TemplatesConfigBean;
import com.beige.camera.common.base.BaseActivity;
import com.beige.camera.common.router.PageIdentity;
import com.beige.camera.common.utils.ImageUtils;
import com.beige.camera.common.utils.MsgUtils;
import com.beige.camera.common.utils.imageloader.BitmapUtil;
import com.beige.camera.common.view.loadding.CustomDialog;
import com.beige.camera.contract.IEffectImageView;
import com.beige.camera.contract.IFaceMergeView;
import com.beige.camera.dagger.MainComponentHolder;
import com.beige.camera.presenter.EffectImagePresenter;
import com.beige.camera.presenter.FaceMergePresenter;
import com.beige.camera.utils.AdHelper;

import java.util.ArrayList;

import javax.inject.Inject;


@Route(path = PageIdentity.APP_BABYEFFECT)
public class BabyEffectActivity extends BaseActivity implements IFaceMergeView {

    public String bannerAdType = "bannerAdType";
    public String rewardedAdType = "rewardedAdType";

    private ImageView icBack;
    private TextView tvTitle;
    private ImageView ivPreview;
    private TextView btnSave;
    private TextView btnShare;
    private FrameLayout adContainer;
    private ConstraintLayout clSaveImage ;
    private ConstraintLayout clPbabyBoy ;
    private ConstraintLayout clPbabyGiry;
    private String  imageUrlboy = "";
    private String  imageUrlgirl = "";
    private boolean selectBoy = true;

    private  int selectDrawable ;
    private CustomDialog mCustomDialog;

    @Inject
    public FaceMergePresenter mPresenter;

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
        return R.layout.activity_baby_effect;
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
        clSaveImage = findViewById(R.id.cl_save_image);
        ivPreview = findViewById(R.id.iv_preview_bg);
        btnSave = findViewById(R.id.btn_save);
        btnShare = findViewById(R.id.btn_share);
        clPbabyBoy = findViewById(R.id.cl_pbaby_boy);
        clPbabyGiry = findViewById(R.id.cl_pbaby_giry);
        adContainer = findViewById(R.id.fl_ad_container);
        AdHelper.showBannerAdView(bannerAdType,adContainer);
    }

    @Override
    public void initData() {
        mPresenter.getTemplateConfig(FunctionBean.ID_DETECTION_BABY);
    }

    @Override
    public void configViews() {

        selectDrawable = (int) (Math.random()*8);
        setPerview();
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

        clPbabyBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectBoy = true;
                setPerview();
            }
        });

        clPbabyGiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectBoy = false;
                setPerview();
            }
        });
    }

    private void setPerview(){
        if (selectBoy) {
            clPbabyBoy.setBackground(getResources().getDrawable(R.drawable.bg_perview_white_stroke));
            clPbabyGiry.setBackground(getResources().getDrawable(R.drawable.bg_perview_white));
            BitmapUtil.loadImageRound(this,imageUrlboy,ivPreview,R.drawable.bg_perview_white,R.drawable.bg_perview_white,6);
        }else{
            clPbabyGiry.setBackground(getResources().getDrawable(R.drawable.bg_perview_white_stroke));
            clPbabyBoy.setBackground(getResources().getDrawable(R.drawable.bg_perview_white));
            BitmapUtil.loadImageRound(this,imageUrlgirl,ivPreview,R.drawable.bg_perview_white,R.drawable.bg_perview_white,6);
        }
    }


    @Nullable
    @Override
    public String getPageName() {
        return PageIdentity.APP_PASTEFFECT;
    }


    @Override
    public void onResultEffectImage(String image ,String actionType) {
        if (TextUtils.isEmpty(image)) {
            MsgUtils.showToastCenter(BabyEffectActivity.this,"图片处理失败");
        }
//        BitmapUtil.loadImageRound(this,effectImage,ivPreview,R.drawable.bg_perview_white,R.drawable.bg_perview_white,6);
    }

    @Override
    public void onResultTemplatesConfigBean(TemplatesConfigBean templatesConfigBean) {
        ArrayList<TemplatesConfigBean.Template> templates = templatesConfigBean.getTemplates();
        int size = templates.size();
        if (templates != null || size > 0) {
            int half = size / 2;
            imageUrlboy = templates.get((int) (Math.random() * half)).getImage();
            imageUrlgirl = templates.get((int)(half + Math.random() * (size - half + 1))).getImage();
            setPerview();
        } else {
            MsgUtils.showToastCenter(BabyEffectActivity.this, "图片处理失败");
        }
    }


    private void saveImage(ViewGroup view){
        Bitmap bitmap = ImageUtils.getBitmapByView(view);//contentLly是布局文件
        ImageUtils.saveImageToGallery(BabyEffectActivity.this, bitmap, System.currentTimeMillis() + ".jpg", new ImageUtils.CallBack() {
            @Override
            public void onStart() {
                mCustomDialog = CustomDialog.instance(BabyEffectActivity.this);
                mCustomDialog.show();
            }

            @Override
            public void onSuccess() {
                MsgUtils.showToastCenter(BabyEffectActivity.this,"图片保存成功，请在相册中点击分享");
                if (mCustomDialog != null) {
                    mCustomDialog.dismiss();
                }
            }

            @Override
            public void onFail() {
                MsgUtils.showToastCenter(BabyEffectActivity.this,"图片保存失败");
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
