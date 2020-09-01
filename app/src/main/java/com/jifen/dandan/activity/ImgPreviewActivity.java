package com.jifen.dandan.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.jifen.dandan.R;
import com.jifen.dandan.camerautils.FileUtil;
import com.jifen.dandan.common.base.BaseActivity;
import com.jifen.dandan.common.router.PageIdentity;
import com.jifen.dandan.common.utils.LogUtils;
import com.jifen.dandan.common.utils.imageloader.BitmapUtil;
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


    private String[] functions = new String[]{"变老相机","性别转换","童颜相机","漫画脸","动物预测","年龄检测"};
    private String selectFunction = "变老相机";



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
        ivPreview = findViewById(R.id.iv_preview);
        rvfunction = findViewById(R.id.rv_function);
        ivConfirm = findViewById(R.id.iv_confirm);
        ivRotate = findViewById(R.id.iv_rotate);
    }

    @Override
    public void initData() {
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
        for (String function : functions) {
            MultiCell<String> cell = new MultiCell<>(R.layout.item_function_preview, function, new ViewHolderBinder<String>() {
                @Override
                public void onBind(ViewHolder viewHolder, String function) {

                    ImageView ivCover = viewHolder.getView(R.id.iv_cover);
                    ImageView bgSelected = viewHolder.getView(R.id.bg_selected);
                    TextView tvTitle = viewHolder.getView(R.id.tv_title);
                    BitmapUtil.loadImageCircle(viewHolder.itemView.getContext(),"https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2058213453,278814451&fm=26&gp=0.jpg",R.mipmap.icon_selected_white, ivCover);
                    tvTitle.setText(function);
                    if (TextUtils.equals(selectFunction,function)) {
                        bgSelected.setVisibility(View.VISIBLE);
                    }else{
                        bgSelected.setVisibility(View.INVISIBLE);
                    }
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            selectFunction = function;
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

}
