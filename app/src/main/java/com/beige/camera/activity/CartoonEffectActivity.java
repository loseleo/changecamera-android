package com.beige.camera.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.zhangqiang.celladapter.CellRVAdapter;
import com.zhangqiang.celladapter.cell.Cell;
import com.zhangqiang.celladapter.cell.MultiCell;
import com.zhangqiang.celladapter.cell.ViewHolderBinder;
import com.zhangqiang.celladapter.vh.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@Route(path = PageIdentity.APP_CARTOONEFFECT)
public class CartoonEffectActivity extends BaseActivity implements IEffectImageView {

    public String bannerAdType = "bannerAdType";
    public String rewardedAdType = "rewardedAdType";

    private ImageView ivPreview;
    private ImageView icBack;
    private RecyclerView recyclerView;
    private ImageView ivNormal;
    private TextView btnSave;
    private TextView btnShare;
    private FrameLayout adContainer;
    private CellRVAdapter mAdapter = new CellRVAdapter();
    private ArrayList<FunctionBean> functionBeanList = new ArrayList<>();

    @Inject
    public EffectImagePresenter mPresenter;

    @Autowired(name = "image_path")
    String imagePath;

    private String selectId ;

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
        return R.layout.activity_cartoon_effect;
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
        ivNormal = findViewById(R.id.iv_normal);
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
        BitmapUtil.loadImage(imagePath, ivPreview);
        functionBeanList.add( new FunctionBean("selie_anime", "漫画脸", R.mipmap.icon_pic_cartoon));
        functionBeanList.add( new FunctionBean("cartoon","卡通画",R.mipmap.icon_pic_katong));
        functionBeanList.add( new FunctionBean("warm","彩色糖块油画",R.mipmap.icon_pic_caisetang));
        functionBeanList.add( new FunctionBean("mononoke","奇异油画",R.mipmap.icon_pic_qiyiguo));
        functionBeanList.add( new FunctionBean("scream","呐喊油画",R.mipmap.icon_pic_nahan));
        functionBeanList.add( new FunctionBean("gothic","哥特油画",R.mipmap.icon_pic_gete));
        functionBeanList.add( new FunctionBean("lavender","薰衣草",R.mipmap.icon_pic_xunyicao));
        functionBeanList.add( new FunctionBean("color_pencil","彩色铅笔",R.mipmap.icon_pic_caiqian));
        functionBeanList.add( new FunctionBean("pencil","铅笔",R.mipmap.icon_pic_heibaiqian));

        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        recyclerView.setAdapter(mAdapter);
        List<Cell> cellList = new ArrayList<>();
        for (FunctionBean function : functionBeanList) {
            MultiCell<FunctionBean> cell = new MultiCell<>(R.layout.item_cartoon_function, function, new ViewHolderBinder<FunctionBean>() {
                @Override
                public void onBind(ViewHolder viewHolder, FunctionBean functionBean) {
                    ImageView ivCover = viewHolder.getView(R.id.iv_cover);
                    ImageView ivSelect = viewHolder.getView(R.id.iv_select);
                    BitmapUtil.loadImage(functionBean.getDrawable(), ivCover);
                    if (TextUtils.equals(selectId,functionBean.getId())) {
                        ivSelect.setVisibility(View.VISIBLE);
                    }else{
                        ivSelect.setVisibility(View.GONE);
                    }
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            selectId = functionBean.getId();
                            mAdapter.notifyDataSetChanged();
                            getEffectImage();
                        }
                    });
                }
            });
            cellList.add(cell);
        }
        mAdapter.setDataList(cellList);


        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ivNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectId =  "";
                mAdapter.notifyDataSetChanged();
                setEffectImage();
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

    private void getEffectImage(){
        if (setEffectImage()) {
            return;
        }
        if(TextUtils.equals("selie_anime",selectId)){
            mPresenter.getImageSelieAnime(imagePath,selectId);
        }else{
            mPresenter.getImageStyleTrans(imagePath,selectId);
        }
    }

    public boolean setEffectImage(){
        if(TextUtils.isEmpty(selectId)){
            BitmapUtil.loadImage(imagePath,ivPreview);
            return true;
        }

        FunctionBean selectFunction =  getSelectFunction();
        if (selectFunction !=null && !TextUtils.isEmpty(selectFunction.getImageUrl())) {
            BitmapUtil.loadImage(selectFunction.getImageUrl(),ivPreview);
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public String getPageName() {
        return PageIdentity.APP_CARTOONEFFECT;
    }




    @Override
    public void onResultEffectImage(String image,String actionType) {
        LogUtils.e("zhangning","image = " + image);
        for (FunctionBean functionBean : functionBeanList) {
            if(TextUtils.equals(functionBean.getId(),actionType)){
                functionBean.setImageUrl(image);
                setEffectImage();
                break;
            }
        }
    }

    public FunctionBean getSelectFunction(){
        FunctionBean selectFunction= null;
        for (FunctionBean functionBean : functionBeanList) {
            if(TextUtils.equals(functionBean.getId(),selectId)){
                selectFunction  = functionBean;
                return  selectFunction;
            }
        }
        return  selectFunction;
    }


    @Override
    public void onResultAge(String age) {
    }

    private void saveImage(View view){
        Bitmap bitmap = ImageUtils.getBitmapByView(view);//contentLly是布局文件
        ImageUtils.saveImageToGallery(CartoonEffectActivity.this, bitmap, System.currentTimeMillis() + ".jpg", new ImageUtils.CallBack() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess() {
                MsgUtils.showToastCenter(CartoonEffectActivity.this,"图片保存成功，请在相册中点击分享");
            }

            @Override
            public void onFail() {
                MsgUtils.showToastCenter(CartoonEffectActivity.this,"图片保存失败");
            }
        });
    }

}
