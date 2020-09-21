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

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.beige.camera.MyApplication;
import com.beige.camera.R;
import com.beige.camera.bean.FunctionBean;
import com.beige.camera.bean.RecommendBean;
import com.beige.camera.bean.VersionInfoBean;
import com.beige.camera.common.base.BaseActivity;
import com.beige.camera.common.router.AppNavigator;
import com.beige.camera.common.router.PageIdentity;
import com.beige.camera.common.utils.BundleUtil;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.MsgUtils;
import com.beige.camera.common.utils.PackageUtils;
import com.beige.camera.common.utils.imageloader.BitmapUtil;
import com.beige.camera.contract.IHomeView;
import com.beige.camera.dagger.MainComponentHolder;
import com.beige.camera.dialog.GenderSelecterDialog;
import com.beige.camera.dialog.UpdataVersionDialog;
import com.beige.camera.presenter.HomePresenter;
import com.beige.camera.utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
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
public class HomeActivity extends BaseActivity implements IHomeView {


    @Autowired(name = "schemaUri")
    String schemaUri;

    private long mExitTime;
    private  Banner banner;
    private  ImageView ivMe;
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
        ivMe = findViewById(R.id.iv_me);
        banner = findViewById(R.id.banner);
        rvRecommend = findViewById(R.id.rv_recommend);
        rvHotFunction = findViewById(R.id.rv_hot_function);
        autoLinkSchemeUri(schemaUri);
    }

    @Override
    public void configViews() {
        ivMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppNavigator.goUserCenterActivity(HomeActivity.this);
            }
        });

        setBanner();
        ArrayList<RecommendBean> recommendList = new ArrayList<>();
        recommendList.add( new RecommendBean("1","相机","",R.mipmap.icon_home_kks,""));
//        recommendList.add( new RecommendBean("1","广告","http://mirage-test.oss-cn-hongkong.aliyuncs.com/01EJ399A0E0000000000000000.jpg",0,""));
//        recommendList.add( new RecommendBean("1","广告","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2058213453,278814451&fm=26&gp=0.jpg",0,""));
//        recommendList.add( new RecommendBean("1","广告","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2058213453,278814451&fm=26&gp=0.jpg",0,""));
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
        functionBeanList.add( new FunctionBean(FunctionBean.ID_CHANGE_GENDER_BOY,"性别转换",R.mipmap.img_home_pic_change));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_CHANGE_CHILD,"童颜相机",R.mipmap.img_home_pic_keds));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_CHANGE_CARTOON,"漫画脸",R.mipmap.img_home_pic_anime));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_DETECTION_PAST,"前世今生",R.mipmap.img_home_pic_past));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_CHANGE_BACKGROUND,"换背景",R.mipmap.img_home_pic_background));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_CHANGE_HAIR,"换发型",R.mipmap.img_home_pic_hair));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_CHANGE_CUSTOMS,"异国风情",R.mipmap.img_home_pic_custom));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_CHANGE_CLOTHES,"一键换装",R.mipmap.img_home_pic_custom));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_DETECTION_AGE,"年龄检测",R.mipmap.img_home_pic_age));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_CHANGE_ANIMAL,"动物预测",R.mipmap.img_home_pic_animal));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_DETECTION_BABY,"宝宝预测",R.mipmap.img_home_pic_baby));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_DETECTION_VS,"比比谁美",R.mipmap.img_home_pic_beautiful));
        functionBeanList.add( new FunctionBean(FunctionBean.ID_CHANGE_ANIMALFACE,"动物人脸",R.mipmap.img_home_pic_animalface));
        rvHotFunction.setNestedScrollingEnabled(false);
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
                            if(TextUtils.equals(FunctionBean.ID_CHANGE_GENDER_BOY,id)){
                                GenderSelecterDialog genderSelecterDialog = GenderSelecterDialog.newInstance();
                                genderSelecterDialog.setOnChoiceListener(new GenderSelecterDialog.OnChoiceListener() {
                                    @Override
                                    public void onGenderResurt(int gender) {
                                        if(gender == 1){
                                            AppNavigator.goCameraActivity(HomeActivity.this,FunctionBean.ID_CHANGE_GENDER_BOY);
                                        }else{
                                            AppNavigator.goCameraActivity(HomeActivity.this,FunctionBean.ID_CHANGE_GENDER_GIRL);
                                        }
                                    }
                                });
                                genderSelecterDialog.show(getSupportFragmentManager(), "gender_selecter_dialog");
                            }else{
                                AppNavigator.goCameraActivity(HomeActivity.this,id);
                            }
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

        ArrayList<FunctionBean> arrayList = new ArrayList<>();
//        arrayList.add( new FunctionBean(FunctionBean.ID_CHANGE_OLD,"变老相机",R.mipmap.img_home_pic_old));
//        arrayList.add( new FunctionBean(FunctionBean.ID_CHANGE_GENDER,"性别转换",R.mipmap.img_home_pic_change));
//        arrayList.add( new FunctionBean(FunctionBean.ID_CHANGE_CHILD,"童颜相机",R.mipmap.img_home_pic_keds));
        arrayList.add( new FunctionBean(FunctionBean.ID_CHANGE_CARTOON,"漫画脸",R.mipmap.banner_anime));
        arrayList.add( new FunctionBean(FunctionBean.ID_CHANGE_ANIMAL,"动物预测",R.mipmap.banner_animal));
//        arrayList.add( new FunctionBean(FunctionBean.ID_DETECTION_AGE,"年龄检测",R.mipmap.img_home_pic_age));

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
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
//                FunctionBean functionBean = arrayList.get(position);
//                AppNavigator.goCameraActivity(HomeActivity.this,functionBean.getId());

                VersionInfoBean versionInfoBean = new VersionInfoBean();
                versionInfoBean.setTitle("发现新版本");
                versionInfoBean.setForceUpdate("0");
                versionInfoBean.setVersionMemo("1,发现新版本\n2,发现新版本\n3,发现新版本\n2,发现新版本\n3,发现新版本\n2,发现新版本\n3,发现新版本");
                UpdataVersionDialog updataVersionDialog = UpdataVersionDialog.newInstance(versionInfoBean);
                updataVersionDialog.show(getSupportFragmentManager(), "updata_version_dialog");
            }
        });

    }

}
