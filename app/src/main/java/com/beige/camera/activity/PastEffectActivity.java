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
import com.beige.camera.bean.FunctionBean;
import com.beige.camera.bean.TemplatesConfigBean;
import com.beige.camera.common.base.BaseActivity;
import com.beige.camera.common.router.PageIdentity;
import com.beige.camera.common.utils.ImageUtils;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.MsgUtils;
import com.beige.camera.common.utils.imageloader.BitmapUtil;
import com.beige.camera.contract.IEffectImageView;
import com.beige.camera.contract.IFaceMergeView;
import com.beige.camera.dagger.MainComponentHolder;
import com.beige.camera.presenter.EffectImagePresenter;
import com.beige.camera.presenter.FaceMergePresenter;
import com.beige.camera.utils.AdHelper;

import java.util.ArrayList;

import javax.inject.Inject;


@Route(path = PageIdentity.APP_PASTEFFECT)
public class PastEffectActivity extends BaseActivity implements IFaceMergeView {

    public String bannerAdType = "bannerAdType";
    public String rewardedAdType = "rewardedAdType";

    private ImageView icBack;
    private TextView tvTitle;
    private ConstraintLayout clPreview;
    private ImageView ivPreview;
    private TextView btnSave;
    private TextView btnShare;
    private FrameLayout adContainer;
    private TextView tvDescription ;

    @Inject
    public FaceMergePresenter mPresenter;

    @Autowired(name = "image_path")
    String imagePath;

    TemplatesConfigBean.Template template;


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
        return R.layout.activity_past_effect;
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
        clPreview = findViewById(R.id.cl_preview);
        ivPreview = findViewById(R.id.iv_preview_bg);
        tvDescription = findViewById(R.id.tv_description);
        btnSave = findViewById(R.id.btn_save);
        btnShare = findViewById(R.id.btn_share);
        adContainer = findViewById(R.id.fl_ad_container);
        AdHelper.showBannerAdView(bannerAdType,adContainer);
    }

    @Override
    public void initData() {
            mPresenter.getTemplateConfig(FunctionBean.ID_DETECTION_PAST);
    }

    @Override
    public void configViews() {

        LogUtils.e("zhangning", "imagePath = " + imagePath);
        BitmapUtil.loadImageRound(this,imagePath,ivPreview,R.drawable.bg_perview_white,R.drawable.bg_perview_white,6);

        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage(clPreview);
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage(clPreview);
            }
        });

//        mPresenter.getFaceEditAttr(imagePath,actionType);
    }



    @Nullable
    @Override
    public String getPageName() {
        return PageIdentity.APP_PASTEFFECT;
    }


    @Override
    public void onResultEffectImage(String image ,String actionType) {
        if (TextUtils.isEmpty(image)) {
            MsgUtils.showToastCenter(PastEffectActivity.this,"图片处理失败");
            return;
        }
        BitmapUtil.loadImageRound(this,image,ivPreview,R.drawable.bg_perview_white,R.drawable.bg_perview_white,6);
    }

    @Override
    public void onResultTemplatesConfigBean(TemplatesConfigBean templatesConfigBean) {
        ArrayList<TemplatesConfigBean.Template> templates = templatesConfigBean.getTemplates();
        if (templates != null || templates.size() > 0) {
            template = templates.get((int) (Math.random() * templates.size()));
            mPresenter.getFaceMergeImage(imagePath,template.getImage(),FunctionBean.ID_DETECTION_PAST);
            tvDescription.setText(template.getDescription());
        } else {
            MsgUtils.showToastCenter(PastEffectActivity.this, "图片处理失败");
        }
    }


    private void saveImage(ViewGroup view){
        Bitmap bitmap = ImageUtils.getBitmapByView(view);//contentLly是布局文件
        ImageUtils.saveImageToGallery(PastEffectActivity.this, bitmap, System.currentTimeMillis() + ".jpg", new ImageUtils.CallBack() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess() {
                MsgUtils.showToastCenter(PastEffectActivity.this,"图片保存成功，请在相册中点击分享");
            }

            @Override
            public void onFail() {
                MsgUtils.showToastCenter(PastEffectActivity.this,"图片保存失败");
            }
        });
    }
}
