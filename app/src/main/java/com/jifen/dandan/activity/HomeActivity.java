package com.jifen.dandan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jifen.dandan.MyApplication;
import com.jifen.dandan.R;
import com.jifen.dandan.common.base.BaseActivity;
import com.jifen.dandan.common.router.AppNavigator;
import com.jifen.dandan.common.router.PageIdentity;
import com.jifen.dandan.common.utils.BundleUtil;
import com.jifen.dandan.common.utils.MsgUtils;
import com.jifen.dandan.common.utils.PackageUtils;
import com.jifen.dandan.common.utils.StatusBarConfig;
import com.jifen.dandan.common.utils.imageloader.BitmapUtil;
import com.jifen.dandan.contract.HomeView;
import com.jifen.dandan.dagger.MainComponentHolder;
import com.jifen.dandan.presenter.HomePresenter;
import com.jifen.dandan.ringtone.RingtoneTestActivity;
import com.jifen.dandan.utils.GlideImageLoader;
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

    private String[] recommends = new String[]{"变老相机","性别转换","童颜相机","漫画脸","动物预测","年龄检测"};
    private String[] hotFunctions = new String[]{"变老相机","性别转换","童颜相机","漫画脸","动物预测","年龄检测"};


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

        rvRecommend.setLayoutManager(new GridLayoutManager(this, 4));
        rvRecommend.setAdapter(recommendAdapter);
        List<Cell> recommendCellList = new ArrayList<>();
        for (String function : recommends) {
            MultiCell<String> cell = new MultiCell<>(R.layout.item_home_recommend, function, new ViewHolderBinder<String>() {
                @Override
                public void onBind(ViewHolder viewHolder, String function) {

                    ImageView ivCover = viewHolder.getView(R.id.iv_cover);
                    TextView tvTitle = viewHolder.getView(R.id.tv_title);
                    BitmapUtil.loadImage("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2058213453,278814451&fm=26&gp=0.jpg", ivCover);
                    tvTitle.setText(function);
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
                }
            });
            recommendCellList.add(cell);
        }
        recommendAdapter.setDataList(recommendCellList);


        rvHotFunction.setLayoutManager(new GridLayoutManager(this, 2));
        rvHotFunction.setAdapter(hotFunctionAdapter);
        List<Cell> hotCellList = new ArrayList<>();
        for (String function : hotFunctions) {
            MultiCell<String> cell = new MultiCell<>(R.layout.item_home_hot_function, function, new ViewHolderBinder<String>() {
                @Override
                public void onBind(ViewHolder viewHolder, String function) {
                    ImageView ivCover = viewHolder.getView(R.id.iv_cover);
                    BitmapUtil.loadImage("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2058213453,278814451&fm=26&gp=0.jpg", ivCover);
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
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
