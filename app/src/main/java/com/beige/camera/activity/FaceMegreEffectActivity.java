package com.beige.camera.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
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
import com.beige.camera.presenter.FaceMergePresenter;
import com.beige.camera.utils.AdHelper;
import com.zhangqiang.celladapter.CellRVAdapter;
import com.zhangqiang.celladapter.cell.Cell;
import com.zhangqiang.celladapter.cell.MultiCell;
import com.zhangqiang.celladapter.cell.ViewHolderBinder;
import com.zhangqiang.celladapter.vh.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@Route(path = PageIdentity.APP_FACEMEGREEFFECT)
public class FaceMegreEffectActivity extends BaseActivity implements IFaceMergeView {

    public String bannerAdType = "bannerAdType";
    public String rewardedAdType = "rewardedAdType";

    private ImageView ivPreview;
    private ImageView icBack;
    private RecyclerView recyclerView;
    private TextView btnSave;
    private TextView btnShare;
    private FrameLayout adContainer;
    private CellRVAdapter mAdapter = new CellRVAdapter();

    @Inject
    public FaceMergePresenter mPresenter;

    @Autowired(name = "image_path")
    String imagePath;

    @Autowired(name = "function")
    String function;

    private String selectId = "normal";

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
        return R.layout.activity_facemegre_effect;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.detachView();
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
    public void initViews() {
        icBack = findViewById(R.id.ic_back);
        ivPreview = findViewById(R.id.iv_preview_bg);
        recyclerView = findViewById(R.id.recyclerView);
        btnSave = findViewById(R.id.btn_save);
        btnShare = findViewById(R.id.btn_share);
        adContainer = findViewById(R.id.fl_ad_container);
        AdHelper.showBannerAdView(bannerAdType, adContainer);
    }

    @Override
    public void initData() {
        mPresenter.getTemplateConfig(function);
    }

    @Override
    public void configViews() {

        LogUtils.e("zhangning", "imagePath = " + imagePath);
        BitmapUtil.loadImage(imagePath, ivPreview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(mAdapter);
        ArrayList<TemplatesConfigBean.Template> templateList = new ArrayList<>();
        TemplatesConfigBean.Template normalTemplate = new TemplatesConfigBean.Template("normal", imagePath);
        normalTemplate.setImageEffect(imagePath);
        templateList.add(normalTemplate);
        setTemplatesData(templateList);
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
    }

    private void setTemplatesData(ArrayList<TemplatesConfigBean.Template> templateList) {

        TemplatesConfigBean.Template normalTemplate = new TemplatesConfigBean.Template("normal", imagePath);
        normalTemplate.setImageEffect(imagePath);
        templateList.add(0, normalTemplate);

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
                            selectId = template.getName();
                            mAdapter.notifyDataSetChanged();
                            setEffectImage(template);
                        }
                    });
                }
            });
            cellList.add(cell);
        }
        mAdapter.setDataList(cellList);
    }


    public boolean setEffectImage(TemplatesConfigBean.Template template) {
        if (!TextUtils.isEmpty(template.getImageEffect())) {
            BitmapUtil.loadImage(template.getImageEffect(), ivPreview);
            return true;
        }
        mPresenter.getFaceMergeImage(imagePath,template.getImage(),template.getName());
        return false;
    }

    public TemplatesConfigBean.Template getTemplateByName(String name) {
        int dataCount = mAdapter.getDataCount();
        for (int i = 0; i < dataCount; i++) {
            MultiCell cell = (MultiCell) mAdapter.getDataAt(i);
            TemplatesConfigBean.Template data = (TemplatesConfigBean.Template) cell.getData();
            if (TextUtils.equals(data.getName(), name)) {
                return data;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public String getPageName() {
        return PageIdentity.APP_FACEMEGREEFFECT;
    }


    private void saveImage(View view) {
        Bitmap bitmap = ImageUtils.getBitmapByView(view);//contentLly是布局文件
        ImageUtils.saveImageToGallery(FaceMegreEffectActivity.this, bitmap, System.currentTimeMillis() + ".jpg", new ImageUtils.CallBack() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess() {
                MsgUtils.showToastCenter(FaceMegreEffectActivity.this, "图片保存成功，请在相册中点击分享");
            }

            @Override
            public void onFail() {
                MsgUtils.showToastCenter(FaceMegreEffectActivity.this, "图片保存失败");
            }
        });
    }

    @Override
    public void onResultEffectImage(String image, String actionType) {
        if(!TextUtils.isEmpty(image)){
            getTemplateByName(actionType).setImageEffect(image);
            setEffectImage(getTemplateByName(selectId));
        }else{
            MsgUtils.showToastCenter(FaceMegreEffectActivity.this, "图片处理失败");
        }
    }

    @Override
    public void onResultTemplatesConfigBean(TemplatesConfigBean templatesConfigBean) {
        if (templatesConfigBean.getTemplates() != null || templatesConfigBean.getTemplates().size() > 0) {
            setTemplatesData(templatesConfigBean.getTemplates());
        } else {
            MsgUtils.showToastCenter(FaceMegreEffectActivity.this, "图片处理失败");
        }
    }
}
