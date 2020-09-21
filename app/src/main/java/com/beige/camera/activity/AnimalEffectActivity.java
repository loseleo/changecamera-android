package com.beige.camera.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.beige.camera.R;
import com.beige.camera.bean.FunctionBean;
import com.beige.camera.bean.TemplatesConfigBean;
import com.beige.camera.common.base.BaseActivity;
import com.beige.camera.common.guide.util.LogUtil;
import com.beige.camera.common.router.PageIdentity;
import com.beige.camera.common.utils.ImageUtils;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.MsgUtils;
import com.beige.camera.common.utils.RxUtil;
import com.beige.camera.common.utils.imageloader.BitmapUtil;
import com.beige.camera.common.utils.imageloader.BitmapUtil.LoadPicCallback;
import com.beige.camera.common.view.loadding.CustomDialog;
import com.beige.camera.contract.IEffectImageView;
import com.beige.camera.contract.IFaceMergeView;
import com.beige.camera.dagger.MainComponentHolder;
import com.beige.camera.presenter.FaceMergePresenter;
import com.beige.camera.ringtone.api.bean.AdConfigBean;
import com.beige.camera.ringtone.core.AdManager;
import com.beige.camera.ringtone.core.infoflow.InfoFlowAd;
import com.beige.camera.ringtone.core.strategy.Callback;
import com.beige.camera.ringtone.dagger.AdComponentHolder;
import com.beige.camera.utils.AdHelper;
import com.zhangqiang.celladapter.cell.Cell;
import com.zhangqiang.celladapter.cell.MultiCell;
import com.zhangqiang.celladapter.cell.ViewHolderBinder;
import com.zhangqiang.celladapter.vh.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.beige.camera.common.utils.RxUtil.io_main;
import static com.beige.camera.common.utils.RxUtil.viewClick;

@Route(path = PageIdentity.APP_ANIMALEFFECT)
public class AnimalEffectActivity extends BaseActivity implements IFaceMergeView {

    public String bannerAdType = "bannerAdType";
    public String rewardedAdType = "rewardedAdType";

    private ImageView icBack;
    private CardView cardview;
    private ImageView ivAnimal;
    private ImageView ivPeople;
    private SeekBar seekbar;
    private TextView btnSave;
    private TextView btnShare;
    private FrameLayout adContainer;

    @Autowired(name = "image_path")
    String imagePath;

    CustomDialog mCustomDialog;

    @Inject
    public FaceMergePresenter mPresenter;

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
        return R.layout.activity_animal_effect;
    }

    @Override
    public void initViews() {
        icBack = findViewById(R.id.ic_back);
        cardview = findViewById(R.id.cardview);
        ivAnimal = findViewById(R.id.iv_animal);
        ivPeople = findViewById(R.id.iv_people);
        seekbar = findViewById(R.id.seekbar);
        btnSave = findViewById(R.id.btn_save);
        btnShare = findViewById(R.id.btn_share);
        adContainer = findViewById(R.id.fl_ad_container);
        AdHelper.showBannerAdView(bannerAdType,adContainer);
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
    protected void onPause() {
        super.onPause();
        mPresenter.detachView();
    }

    @Override
    public void initData() {
        mPresenter.getTemplateConfig(FunctionBean.ID_CHANGE_ANIMAL);
    }

    @Override
    public void configViews() {
        LogUtils.e("zhangning", "imagePath = " + imagePath);
        ivPeople.setImageBitmap(BitmapFactory.decodeFile(imagePath));

        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage(cardview);
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage(cardview);
            }
        });
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                LogUtils.e("zhangning", "seekBar progress = " + i);
                setImageAlpha(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    private void showAutoAnimation(){
        Observable.interval(60, 60, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .take(51)
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtils.e("zhangning","Long = " + aLong);
                        int progress = (int) (aLong * 2);
                        seekbar.setProgress(progress);
                    }
                })
                .subscribe();
    }

    private void setImageAlpha(int propress){
        float alpha = (float) propress / 100;
        ivAnimal.setAlpha(alpha);
        ivPeople.setAlpha(1-alpha);
    }
    @Nullable
    @Override
    public String getPageName() {
        return PageIdentity.APP_ANIMALEFFECT;
    }



    private void saveImage(ViewGroup view){
        Bitmap bitmap = ImageUtils.getBitmapByView(view);//contentLly是布局文件
        ImageUtils.saveImageToGallery(AnimalEffectActivity.this, bitmap, System.currentTimeMillis() + ".jpg", new ImageUtils.CallBack() {
            @Override
            public void onStart() {
                mCustomDialog = CustomDialog.instance(AnimalEffectActivity.this);
                mCustomDialog.show();
            }

            @Override
            public void onSuccess() {
                MsgUtils.showToastCenter(AnimalEffectActivity.this,"图片保存成功，请在相册中点击分享");
                if (mCustomDialog != null) {
                    mCustomDialog.dismiss();
                }
            }

            @Override
            public void onFail() {
                MsgUtils.showToastCenter(AnimalEffectActivity.this,"图片保存失败");
                if (mCustomDialog != null) {
                    mCustomDialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCustomDialog != null) {
            mCustomDialog.dismiss();
            mCustomDialog =  null;
        }
    }

    @Override
    public void onResultEffectImage(String image, String actionType) {

    }

    @Override
    public void onResultTemplatesConfigBean(TemplatesConfigBean templatesConfigBean) {
        ArrayList<TemplatesConfigBean.Template> templates = templatesConfigBean.getTemplates();
        if (templates != null || templates.size() > 0) {
            TemplatesConfigBean.Template template = templates.get((int) (Math.random() * templates.size()));
            BitmapUtil.loadImageRoundByListerner(AnimalEffectActivity.this, template.getImage(), ivAnimal, R.drawable.bg_perview_white, R.drawable.bg_perview_white, 0, new LoadPicCallback() {
                @Override
                public void onSuccess() {
                    showAutoAnimation();
                }

                @Override
                public void onFaild() {

                }
            });

        } else {
            MsgUtils.showToastCenter(AnimalEffectActivity.this, "图片处理失败");
        }
    }

}
