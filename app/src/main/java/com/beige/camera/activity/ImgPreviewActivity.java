package com.beige.camera.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.beige.camera.R;
import com.beige.camera.bean.FunctionBean;
import com.beige.camera.common.router.AppNavigator;
import com.beige.camera.utils.FileUtil;
import com.beige.camera.common.base.BaseActivity;
import com.beige.camera.common.router.PageIdentity;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.imageloader.BitmapUtil;
import com.zhangqiang.celladapter.CellRVAdapter;
import com.zhangqiang.celladapter.cell.Cell;
import com.zhangqiang.celladapter.cell.MultiCell;
import com.zhangqiang.celladapter.cell.ViewHolderBinder;
import com.zhangqiang.celladapter.vh.ViewHolder;

import java.util.ArrayList;
import java.util.List;

@Route(path = PageIdentity.APP_IMGPREVIEW)
public class ImgPreviewActivity extends BaseActivity {

    private ImageView ivPreview;
    private ImageView icBack;
    private ImageView ivConfirm;
    private ImageView ivRotate;
    private RecyclerView rvfunction;
    private CellRVAdapter cellRVAdapter = new CellRVAdapter();

    @Autowired(name = "image_path")
    String imagePath;

    ArrayList<FunctionBean> functionBeanList;
    private String selectFunction = FunctionBean.ID_CHANGE_OLD;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupActivityComponent() {
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_image_preview;
    }

    @Override
    public void initViews() {
        icBack = findViewById(R.id.ic_back);
        ivPreview = findViewById(R.id.iv_preview_bg);
        rvfunction = findViewById(R.id.rv_function);
        ivConfirm = findViewById(R.id.iv_confirm);
        ivRotate = findViewById(R.id.iv_rotate);
    }

    @Override
    public void initData() {
        functionBeanList = new ArrayList<>();
        functionBeanList.add( new FunctionBean(FunctionBean.ID_CHANGE_OLD,"变老相机",R.mipmap.icon_camera_pic_old));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_CHANGE_GENDER_BOY,"性别转换",R.mipmap.icon_camera_pic_change));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_CHANGE_CHILD,"童颜相机",R.mipmap.icon_camera_pic_keds));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_CHANGE_CARTOON,"漫画脸",R.mipmap.icon_camera_pic_anime));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_DETECTION_PAST,"前世今生",R.mipmap.icon_camera_pic_pre));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_CHANGE_BACKGROUND,"换背景",R.mipmap.icon_camera_pic_bg));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_CHANGE_HAIR,"换发型",R.mipmap.icon_camera_pic_hair));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_CHANGE_CUSTOMS,"异国风情",R.mipmap.icon_camera_pic_exotic));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_CHANGE_CLOTHES,"一键换装",R.mipmap.icon_camera_pic_exotic));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_DETECTION_AGE,"年龄检测",R.mipmap.icon_camera_pic_age));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_CHANGE_ANIMAL,"动物预测",R.mipmap.icon_camera_pic_animal));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_DETECTION_BABY,"宝宝预测",R.mipmap.icon_camera_pic_baby));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_DETECTION_VS,"比比谁美",R.mipmap.icon_camera_pic_beautiful));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_CHANGE_ANIMALFACE,"动物人脸",R.mipmap.icon_camera_pic_aniface));

    }

    @Override
    public void configViews() {
        LogUtils.e("zhangning","imagePath = " + imagePath);
        ivPreview.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ivConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goNextPage();
                finish();
            }
        });

        ivRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = FileUtil.rotateBitmap(90, BitmapFactory.decodeFile(imagePath));
                imagePath = FileUtil.saveBitmap(bitmap);
                ivPreview.setImageBitmap(bitmap);
            }
        });

        rvfunction.setLayoutManager(new LinearLayoutManager(ImgPreviewActivity.this, RecyclerView.HORIZONTAL, false));
        rvfunction.setAdapter(cellRVAdapter);
        List<Cell> cellList = new ArrayList<>();
        for (FunctionBean function : functionBeanList) {
            MultiCell<FunctionBean> cell = new MultiCell<>(R.layout.item_function_preview, function, new ViewHolderBinder<FunctionBean>() {
                @Override
                public void onBind(ViewHolder viewHolder, FunctionBean function) {

                    ImageView ivCover = viewHolder.getView(R.id.iv_cover);
                    ImageView bgSelected = viewHolder.getView(R.id.bg_selected);
                    TextView tvTitle = viewHolder.getView(R.id.tv_title);
                    BitmapUtil.loadImage(function.getDrawable(), ivCover);
                    tvTitle.setText(function.getTitle());
                    if (TextUtils.equals(selectFunction,function.getId())) {
                        bgSelected.setVisibility(View.VISIBLE);
                    }else{
                        bgSelected.setVisibility(View.INVISIBLE);
                    }
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            selectFunction = function.getId();
                            cellRVAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
            cellList.add(cell);
        }
        cellRVAdapter.setDataList(cellList);

    }

    @Nullable
    @Override
    public String getPageName() {
        return PageIdentity.APP_IMGPREVIEW;
    }

    private void goNextPage(){
        AppNavigator.goImgUploadActivity(ImgPreviewActivity.this,imagePath,selectFunction);
    }

}
