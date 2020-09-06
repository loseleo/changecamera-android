package com.jifen.dandan.activity;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.jifen.dandan.R;
import com.jifen.dandan.common.base.BaseActivity;
import com.jifen.dandan.common.router.PageIdentity;
import com.jifen.dandan.common.utils.LogUtils;
import com.jifen.dandan.ringtone.api.bean.AdConfigBean;
import com.jifen.dandan.ringtone.core.AdManager;
import com.jifen.dandan.ringtone.core.infoflow.InfoFlowAd;
import com.jifen.dandan.ringtone.core.strategy.Callback;
import com.zhangqiang.celladapter.CellRVAdapter;

@Route(path = PageIdentity.APP_OLDFFFECT)
public class OldEffectActivity extends BaseActivity {

    private ImageView ivPreview;
    private ImageView icBack;
    private TextView btnSave;
    private TextView btnShare;
    private FrameLayout adContainer;
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

    private CellRVAdapter cellRVAdapter = new CellRVAdapter();
    private String selectAge = "now";

    @Autowired(name = "image_path")
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupActivityComponent() {
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_oldeffect;
    }

    @Override
    public void initViews() {
        icBack = findViewById(R.id.ic_back);
        ivPreview = findViewById(R.id.iv_preview);
        btnSave = findViewById(R.id.btn_save);
        btnShare = findViewById(R.id.btn_share);
        adContainer = findViewById(R.id.fl_ad_container);
        clSelectAgeNow = findViewById(R.id.cl_select_age_now);
        clSelectAge30 = findViewById(R.id.cl_select_age_30);
        clSelectAge40 = findViewById(R.id.cl_select_age_40);
        clSelectAge50 = findViewById(R.id.cl_select_age_50);
        selectAgeNow = findViewById(R.id.select_age_now);
        selectAge30 = findViewById(R.id.select_age_30);
        selectAge40 = findViewById(R.id.select_age_40);
        selectAge50 = findViewById(R.id.select_age_50);
        tvSelectAgeNow = findViewById(R.id.tv_select_age_now);
        tvSelectAge30 = findViewById(R.id.tv_select_age_30);
        tvSelectAge40 = findViewById(R.id.tv_select_age_40);
        tvSelectAge50 = findViewById(R.id.tv_select_age_50);
    }

    @Override
    public void initData() {
    }

    @Override
    public void configViews() {
        LogUtils.e("zhangning", "imagePath = " + imagePath);
        ivPreview.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                selectAge = "40";
                setSelectAge();
            }
        });
        clSelectAge50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAge = "50";
                setSelectAge();
            }
        });

    }

    @Nullable
    @Override
    public String getPageName() {
        return PageIdentity.APP_OLDFFFECT;
    }


    private void setSelectAge(){
        selectAgeNow.setVisibility(View.INVISIBLE);
        selectAge30.setVisibility(View.INVISIBLE);
        selectAge40.setVisibility(View.INVISIBLE);
        selectAge50.setVisibility(View.INVISIBLE);
        tvSelectAgeNow.setTextColor(getResources().getColor(R.color.black));
        tvSelectAge30.setTextColor(getResources().getColor(R.color.black));
        tvSelectAge40.setTextColor(getResources().getColor(R.color.black));
        tvSelectAge50.setTextColor(getResources().getColor(R.color.black));
        if (TextUtils.equals(selectAge ,"now")) {
            selectAgeNow.setVisibility(View.VISIBLE);
            tvSelectAgeNow.setTextColor(getResources().getColor(R.color.common_color_FF390B));
        }else if(TextUtils.equals(selectAge ,"30")){
            selectAge30.setVisibility(View.VISIBLE);
            tvSelectAge30.setTextColor(getResources().getColor(R.color.common_color_FF390B));
        }else if(TextUtils.equals(selectAge ,"40")){
            selectAge40.setVisibility(View.VISIBLE);
            tvSelectAge40.setTextColor(getResources().getColor(R.color.common_color_FF390B));
        }else if(TextUtils.equals(selectAge ,"50")){
            selectAge50.setVisibility(View.VISIBLE);
            tvSelectAge50.setTextColor(getResources().getColor(R.color.common_color_FF390B));
        }
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
