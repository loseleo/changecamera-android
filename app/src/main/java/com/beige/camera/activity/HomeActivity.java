package com.beige.camera.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.beige.camera.MyApplication;
import com.beige.camera.R;
import com.beige.camera.bean.FunctionBean;
import com.beige.camera.bean.RecommendBean;
import com.beige.camera.common.base.BaseActivity;
import com.beige.camera.common.router.AppNavigator;
import com.beige.camera.common.router.PageIdentity;
import com.beige.camera.common.utils.BundleUtil;
import com.beige.camera.common.utils.MsgUtils;
import com.beige.camera.common.utils.PackageUtils;
import com.beige.camera.common.utils.StatusBarConfig;
import com.beige.camera.common.utils.imageloader.BitmapUtil;
import com.beige.camera.contract.HomeView;
import com.beige.camera.dagger.MainComponentHolder;
import com.beige.camera.presenter.HomePresenter;
import com.beige.camera.utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zhangqiang.celladapter.CellRVAdapter;
import com.zhangqiang.celladapter.cell.Cell;
import com.zhangqiang.celladapter.cell.MultiCell;
import com.zhangqiang.celladapter.cell.ViewHolderBinder;
import com.zhangqiang.celladapter.vh.ViewHolder;
import com.zhangqiang.visiblehelper.OnVisibilityChangeListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


@Route(path = PageIdentity.APP_HOME)
public class HomeActivity extends BaseActivity implements HomeView {


    private static final String INTENT_KEY_SCHEMA_URI = "schemaUri";

    private long mExitTime;

    private  Banner banner;
    private RecyclerView rvRecommend;
    private RecyclerView rvHotFunction;


    private CellRVAdapter recommendAdapter = new CellRVAdapter();
    private CellRVAdapter hotFunctionAdapter = new CellRVAdapter();

    @Inject
    public HomePresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("cold_start_dd", "onCreate total: " + (System.currentTimeMillis() - MyApplication.appStart));
        super.onCreate(savedInstanceState);
        mPresenter.getPrivacyPoliceContent();
        getVisibleHelper().addVisibilityChangeListener(new OnVisibilityChangeListener() {
            @Override
            public void onVisibilityChange(boolean isVisible) {
                if (isVisible) {
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public StatusBarConfig getStatusBarConfig() {
        return new StatusBarConfig.Builder()
                .setFitsSystemWindows(false)
                .setDarkTheme(false)
                .build();
    }

    @Override
    protected void setupActivityComponent() {
        MainComponentHolder.getInstance()
                .inject(this);
    }

    @Override
    protected void onRestoreWhenCreate(Bundle savedInstanceState) {
        super.onRestoreWhenCreate(savedInstanceState);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_home;
    }

    @Override
    public void initViews() {
        banner = findViewById(R.id.banner);
        rvRecommend = findViewById(R.id.rv_recommend);
        rvHotFunction = findViewById(R.id.rv_hot_function);
    }

    @Override
    public void configViews() {

        setBanner();

        ArrayList<RecommendBean> recommendList = new ArrayList<>();
        recommendList.add( new RecommendBean("1","相机","",R.mipmap.icon_home_kks,""));
        recommendList.add( new RecommendBean("1","广告","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2058213453,278814451&fm=26&gp=0.jpg",0,""));
        recommendList.add( new RecommendBean("1","广告","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2058213453,278814451&fm=26&gp=0.jpg",0,""));
        recommendList.add( new RecommendBean("1","广告","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2058213453,278814451&fm=26&gp=0.jpg",0,""));
        rvRecommend.setLayoutManager(new GridLayoutManager(this, 4));
        rvRecommend.setAdapter(recommendAdapter);
        List<Cell> recommendCellList = new ArrayList<>();
        for (RecommendBean recommend : recommendList) {
            MultiCell<RecommendBean> cell = new MultiCell<>(R.layout.item_home_recommend, recommend, new ViewHolderBinder<RecommendBean>() {
                @Override
                public void onBind(ViewHolder viewHolder, RecommendBean recommend) {

                    ImageView ivCover = viewHolder.getView(R.id.iv_cover);
                    TextView tvTitle = viewHolder.getView(R.id.tv_title);
                    if (recommend.getDrawable() != 0) {
//                        ivCover.setImageResource(recommend.getDrawable());
                        BitmapUtil.loadImage(recommend.getDrawable(), ivCover);
                    }

                    if(!TextUtils.isEmpty(recommend.getCover())){
                        BitmapUtil.loadImage(recommend.getCover(), ivCover);
                    }

                    tvTitle.setText(recommend.getTitle());

                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(TextUtils.equals("1",recommend.getId())){
                                AppNavigator.goCameraActivity(HomeActivity.this,"");
                            }
                        }
                    });
                }
            });
            recommendCellList.add(cell);
        }
        recommendAdapter.setDataList(recommendCellList);


        ArrayList<FunctionBean> functionBeanList = new ArrayList<>();
        functionBeanList.add( new FunctionBean(FunctionBean.ID_CHANGE_OLD,"变老相机",R.mipmap.img_home_pic_old));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_CHANGE_GENDER,"性别转换",R.mipmap.img_home_pic_change));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_CHANGE_CHILD,"童颜相机",R.mipmap.img_home_pic_keds));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_CHANGE_CARTOON,"漫画脸",R.mipmap.img_home_pic_anime));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_CHANGE_ANIMAL,"动物预测",R.mipmap.img_home_pic_animal));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_DETECTION_AGE,"年龄检测",R.mipmap.img_home_pic_age));


        rvHotFunction.setLayoutManager(new GridLayoutManager(this, 2));
        rvHotFunction.setAdapter(hotFunctionAdapter);
        List<Cell> hotCellList = new ArrayList<>();
        for (FunctionBean functionBean : functionBeanList) {
            MultiCell<FunctionBean> cell = new MultiCell<>(R.layout.item_home_hot_function, functionBean, new ViewHolderBinder<FunctionBean>() {
                @Override
                public void onBind(ViewHolder viewHolder, FunctionBean functionBean) {
                    ImageView ivCover = viewHolder.getView(R.id.iv_cover);
                    BitmapUtil.loadImage(functionBean.getDrawable(), ivCover);
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String id = functionBean.getId();
                            AppNavigator.goCameraActivity(HomeActivity.this,id);
                        }
                    });
                }
            });
            hotCellList.add(cell);
        }
        hotFunctionAdapter.setDataList(hotCellList);

    }

    @Override
    public void initData() {
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }


    private String getSchemaUri(@Nullable Bundle extras) {
        return BundleUtil.getString(extras, INTENT_KEY_SCHEMA_URI, "");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public String getPageName() {
        return PageIdentity.APP_HOME;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void autoLinkSchemeUri(String schemaUri) {
        if (!TextUtils.isEmpty(schemaUri)) {
            AppNavigator.goActivityByUri(HomeActivity.this, schemaUri);
        }
    }

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            if (System.currentTimeMillis() - mExitTime > 2000) {
                MsgUtils.showToastCenter(HomeActivity.this, getString(R.string.double_back_tips, PackageUtils.getAppName(this)));
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }




    public void setBanner(){

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1940116422,2073392104&fm=26&gp=0.jpg");
        arrayList.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3660668578,2059694256&fm=26&gp=0.jpg");
        arrayList.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1775378136,5105235&fm=26&gp=0.jpg");
        // 绑定数据
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(arrayList);
        //设置banner动画效果
//            mBanner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        //        mBanner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

}
