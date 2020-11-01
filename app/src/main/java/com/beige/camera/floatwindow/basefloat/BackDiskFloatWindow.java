package com.beige.camera.floatwindow.basefloat;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.beige.camera.R;
import com.beige.camera.advertisement.core.AdManager;
import com.beige.camera.advertisement.core.SimpleAdListener;
import com.beige.camera.advertisement.core.infoflow.InfoFlowAd;
import com.beige.camera.advertisement.core.strategy.Callback;
import com.beige.camera.common.feed.bean.AdModel;
import com.beige.camera.common.utils.imageloader.BitmapUtil;
import com.beige.camera.common.utils.span.SpanUtils;

import java.util.List;

/**
 * @author sun on 2018/12/26.
 */
public class BackDiskFloatWindow extends AbsFloatBase {

    private  Context context;

    private ImageView ivClose;
    private FrameLayout adContainer;

    List<AdModel> adModel;
    public BackDiskFloatWindow(Context context, List<AdModel> adModel) {
        super(context);
        this.context = context;
        this.adModel = adModel;
        AdManager.loadInfoFlowAd(adContainer, adModel, new Callback<InfoFlowAd>() {
            @Override
            public void onAdLoadStart(InfoFlowAd ad) {
                ad.addAdListener(new SimpleAdListener() {

                    @Override
                    public void onAdClick() {
                        super.onAdClick();
                        remove();
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();

                    }

                    @Override
                    public void onAdFail(Throwable e) {
                        super.onAdFail(e);

                    }
                });
            }

            @Override
            public void onFail(Throwable e) {

            }
        });
    }

    @Override
    public void create() {
        super.create();
        mViewMode = FULLSCREEN_TOUCHABLE;
//        mViewMode = FULLSCREEN_NOT_TOUCHABLE;
        inflate(R.layout.layout_ad_fullscreen);
        ivClose = findView(R.id.iv_close);
        adContainer = findView(R.id.fl_ad_container);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
            }
        });
    }

    @Override
    protected void onAddWindowFailed(Exception e) {

    }
}
