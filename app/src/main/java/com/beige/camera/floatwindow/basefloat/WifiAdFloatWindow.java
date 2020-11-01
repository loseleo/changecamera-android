package com.beige.camera.floatwindow.basefloat;

import android.content.ComponentName;
import android.content.Context;
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
public class WifiAdFloatWindow extends AbsFloatBase {

    private  Context context;

    private TextView tvTitle;
    private ImageView ivIcon;
    private TextView tvContent;
    private TextView btnConfirm;
    private TextView btnCancel;
    private FrameLayout adContainer;

    List<AdModel> adModel;
    public WifiAdFloatWindow(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void create() {
        super.create();
        mViewMode = FULLSCREEN_TOUCHABLE;
//        mViewMode = FULLSCREEN_NOT_TOUCHABLE;
        inflate(R.layout.layout_ad_wifi);

        tvTitle = findView(R.id.tv_title);
        ivIcon = findView(R.id.iv_icon);
        tvContent = findView(R.id.tv_content);
        btnConfirm = findView(R.id.btn_confirm);
        btnCancel = findView(R.id.btn_cancel);
        adContainer = findView(R.id.fl_ad_container);

        SpanUtils.setHtmlText(tvTitle, "温馨提示");
        SpanUtils.setHtmlText(tvContent, "检查到当前Wi-Fi状态不加，是否要切换到移动网络");
        SpanUtils.setHtmlText(btnConfirm, "继续使用");
        SpanUtils.setHtmlText(btnCancel, "切换网络");
        BitmapUtil.loadImage(R.mipmap.icon_wifi_error,ivIcon);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                //判断手机系统的版本  即API大于10 就是3.0或以上版本
                if(android.os.Build.VERSION.SDK_INT>10){
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                }else{
                    intent = new Intent();
                    ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                context.startActivity(intent);
                remove();
            }
        });
    }

    @Override
    protected void onAddWindowFailed(Exception e) {

    }

    public void setAdModel(List<AdModel> adModel) {
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

}
