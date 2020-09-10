package com.beige.camera.activity;

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
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.imageloader.BitmapUtil;
import com.beige.camera.ringtone.api.bean.AdConfigBean;
import com.beige.camera.ringtone.core.AdManager;
import com.beige.camera.ringtone.core.infoflow.InfoFlowAd;
import com.beige.camera.ringtone.core.strategy.Callback;
import com.zhangqiang.celladapter.CellRVAdapter;
import com.zhangqiang.celladapter.cell.Cell;
import com.zhangqiang.celladapter.cell.MultiCell;
import com.zhangqiang.celladapter.cell.ViewHolderBinder;
import com.zhangqiang.celladapter.vh.ViewHolder;

import java.util.ArrayList;
import java.util.List;

@Route(path = PageIdentity.APP_CARTOONEFFECT)
public class CartoonEffectActivity extends BaseActivity {

    private ImageView ivPreview;
    private ImageView icBack;
    private RecyclerView recyclerView;
    private ImageView ivNormal;
    private TextView btnSave;
    private TextView btnShare;
    private FrameLayout adContainer;
    private CellRVAdapter mAdapter = new CellRVAdapter();


    @Autowired(name = "image_path")
    String imagePath;

    private String selectId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupActivityComponent() {
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_cartoon_effect;
    }

    @Override
    public void initViews() {
        icBack = findViewById(R.id.ic_back);
        ivPreview = findViewById(R.id.iv_preview);
        recyclerView = findViewById(R.id.recyclerView);
        ivNormal = findViewById(R.id.iv_normal);
        btnSave = findViewById(R.id.btn_save);
        btnShare = findViewById(R.id.btn_share);
        adContainer = findViewById(R.id.fl_ad_container);
    }

    @Override
    public void initData() {
    }

    @Override
    public void configViews() {
        LogUtils.e("zhangning", "imagePath = " + imagePath);
        ivPreview.setImageBitmap(BitmapFactory.decodeFile(imagePath));

        ArrayList<FunctionBean> functionBeanList = new ArrayList<>();
        functionBeanList.add( new FunctionBean("1","卡通画",R.mipmap.icon_pic_katong));
        functionBeanList.add( new FunctionBean("2","彩色糖块油画",R.mipmap.icon_pic_caisetang));
        functionBeanList.add( new FunctionBean("3","奇异油画",R.mipmap.icon_pic_qiyiguo));
        functionBeanList.add( new FunctionBean("4","呐喊油画",R.mipmap.icon_pic_nahan));
        functionBeanList.add( new FunctionBean("5","哥特油画",R.mipmap.icon_pic_gete));
        functionBeanList.add( new FunctionBean("6","薰衣草",R.mipmap.icon_pic_xunyicao));
        functionBeanList.add( new FunctionBean("7","彩色铅笔",R.mipmap.icon_pic_caiqian));
        functionBeanList.add( new FunctionBean("8","铅笔",R.mipmap.icon_pic_heibaiqian));
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        recyclerView.setAdapter(mAdapter);
        List<Cell> cellList = new ArrayList<>();
        for (FunctionBean functionBean : functionBeanList) {
            MultiCell<FunctionBean> cell = new MultiCell<>(R.layout.item_cartoon_function, functionBean, new ViewHolderBinder<FunctionBean>() {
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
                            updataView();
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
                selectId = "";
                updataView();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }


    public void updataView(){
        mAdapter.notifyDataSetChanged();
    }


    @Nullable
    @Override
    public String getPageName() {
        return PageIdentity.APP_CARTOONEFFECT;
    }




    public void setAdModel(AdConfigBean adModel) {
        if (adContainer == null || adModel == null) {
            return;
        }
        adContainer.post(new Runnable() {
            @Override
            public void run() {
                AdManager.loadBigImgAd(adContainer, adModel.getCandidates(), new Callback<InfoFlowAd>() {
                    @Override
                    public void onAdLoadStart(InfoFlowAd ad) {
                    }

                    @Override
                    public void onFail(Throwable e) {

                    }
                });
            }
        });
    }


}
