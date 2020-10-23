package com.beige.camera.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.beige.camera.bean.TemplatesConfigBean;
import com.beige.camera.common.base.BaseActivity;
import com.beige.camera.common.router.PageIdentity;
import com.beige.camera.common.utils.ImageUtils;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.MsgUtils;
import com.beige.camera.common.utils.imageloader.BitmapUtil;
import com.beige.camera.common.view.BaseDragZoomImageView;
import com.beige.camera.common.view.loadding.CustomDialog;
import com.beige.camera.contract.IBodySegView;
import com.beige.camera.contract.IEffectImageView;
import com.beige.camera.dagger.MainComponentHolder;
import com.beige.camera.presenter.BodySegPresenter;
import com.beige.camera.presenter.EffectImagePresenter;
import com.beige.camera.utils.AdHelper;
import com.zhangqiang.celladapter.CellRVAdapter;
import com.zhangqiang.celladapter.cell.Cell;
import com.zhangqiang.celladapter.cell.MultiCell;
import com.zhangqiang.celladapter.cell.ViewHolderBinder;
import com.zhangqiang.celladapter.vh.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


@Route(path = PageIdentity.APP_BACKGROUNDEFFECT)
public class BackgroundEffectActivity extends BaseActivity implements IBodySegView {

    private ImageView icBack;
    private TextView tvTitle;
    private ConstraintLayout clSaveImage;
    private ImageView ivPreviewBg;
    private RecyclerView recyclerView;
    private BaseDragZoomImageView ivPreview;
    private TextView btnSave;
    private TextView btnShare;
    private FrameLayout adContainer;

    @Inject
    public BodySegPresenter mPresenter;

    @Autowired(name = "image_path")
    String imagePath;

    private String selectId;
    private CellRVAdapter mAdapter = new CellRVAdapter();
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
        return R.layout.activity_background_effect;
    }

    @Override
    protected void onResume() {
        super.onResume();
        adHelper.showBannerAdView(AdHelper.getBannerAdTypeById(FunctionBean.ID_CHANGE_BACKGROUND),adContainer);
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
        ivPreviewBg = findViewById(R.id.iv_preview_bg);
        clSaveImage = findViewById(R.id.cl_save_image);
        ivPreview = findViewById(R.id.iv_preview);
        recyclerView = findViewById(R.id.recyclerView);
        btnSave = findViewById(R.id.btn_save);
        btnShare = findViewById(R.id.btn_share);
        adContainer = findViewById(R.id.fl_ad_container);
        adHelper = new AdHelper();
    }

    @Override
    public void initData() {
        mPresenter.getTemplateConfig(FunctionBean.ID_CHANGE_BACKGROUND);
        mPresenter.bodySeg(imagePath);
    }

    @Override
    public void configViews() {

        LogUtils.e("zhangning", "imagePath = " + imagePath);
        BitmapUtil.loadImage(this,imagePath,ivPreviewBg,R.drawable.bg_perview_white,R.drawable.bg_perview_white);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(mAdapter);
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
    }



    @Nullable
    @Override
    public String getPageName() {
        return PageIdentity.APP_BACKGROUNDEFFECT;
    }


    private void saveImage(ViewGroup view){

        adHelper.playRewardedVideo(BackgroundEffectActivity.this, AdHelper.getRewardedAdTypeById(FunctionBean.ID_CHANGE_BACKGROUND), new AdHelper.PlayRewardedAdCallback() {
            @Override
            public void onDismissed(int action) {
                Bitmap bitmap = ImageUtils.getBitmapByView(view);//contentLly是布局文件
                ImageUtils.saveImageToGallery(BackgroundEffectActivity.this, bitmap, System.currentTimeMillis() + ".jpg", new ImageUtils.CallBack() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess() {
                        MsgUtils.showToastCenter(BackgroundEffectActivity.this,"图片保存成功，请在相册中点击分享");
                    }

                    @Override
                    public void onFail() {
                        MsgUtils.showToastCenter(BackgroundEffectActivity.this,"图片保存失败");
                    }
                });
            }
            @Override
            public void onFail() {

            }
        });
    }

    private void setTemplatesData(ArrayList<TemplatesConfigBean.Template> templateList) {

        List<Cell> cellList = new ArrayList<>();
        for (TemplatesConfigBean.Template template : templateList) {
            MultiCell<TemplatesConfigBean.Template> cell = new MultiCell<>(R.layout.item_cartoon_function, template, new ViewHolderBinder<TemplatesConfigBean.Template>() {
                @Override
                public void onBind(ViewHolder viewHolder, TemplatesConfigBean.Template template) {
                    ImageView ivCover = viewHolder.getView(R.id.iv_cover);
                    ImageView ivSelect = viewHolder.getView(R.id.iv_select);
                    BitmapUtil.loadImage(template.getImage(), ivCover);
                    if (TextUtils.equals(selectId, template.getName())) {
                        ivSelect.setVisibility(View.VISIBLE);
                    } else {
                        ivSelect.setVisibility(View.GONE);
                    }
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (template.isShowAD()) {
                                setEffectImage(template);
                                return;
                            }
                            adHelper.playRewardedVideo(BackgroundEffectActivity.this, AdHelper.getRewardedAdTypeById(FunctionBean.ID_CHANGE_BACKGROUND), new AdHelper.PlayRewardedAdCallback() {
                                @Override
                                public void onDismissed(int action) {
                                    setEffectImage(template);
                                }
                                @Override
                                public void onFail() {
                                }
                            });
                        }
                    });
                }
            });
            cellList.add(cell);
        }
        mAdapter.setDataList(cellList);
    }

    public void setEffectImage(TemplatesConfigBean.Template template) {
       selectId = template.getName();
       template.setShowAD(true);
       mAdapter.notifyDataSetChanged();
       BitmapUtil.loadImage(template.getImage(), ivPreviewBg);
    }

    @Override
    public void onResultEffectImage(String image) {
        if (TextUtils.isEmpty(image)) {
            MsgUtils.showToastCenter(BackgroundEffectActivity.this,"图片处理失败");
            return;
        }
        BitmapUtil.loadImage(image,ivPreview);
    }

    @Override
    public void onResultTemplatesConfigBean(TemplatesConfigBean templatesConfigBean) {
        if (templatesConfigBean.getTemplates() != null || templatesConfigBean.getTemplates().size() > 0) {
            TemplatesConfigBean.Template template = templatesConfigBean.getTemplates().get(0);
            selectId = template.getName();
            setEffectImage(template);
            setTemplatesData(templatesConfigBean.getTemplates());
        } else {
            MsgUtils.showToastCenter(BackgroundEffectActivity.this, "图片处理失败");
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

        adHelper.playFullScreenVideoAd(this, AdHelper.getFullScreenVideoAdTypeById(FunctionBean.ID_CHANGE_BACKGROUND), new AdHelper.PlayRewardedAdCallback() {
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
