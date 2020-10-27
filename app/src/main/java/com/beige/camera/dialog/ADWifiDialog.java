package com.beige.camera.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.beige.camera.R;
import com.beige.camera.advertisement.api.bean.AdConfigBean;
import com.beige.camera.advertisement.core.AdManager;
import com.beige.camera.advertisement.core.infoflow.InfoFlowAd;
import com.beige.camera.advertisement.core.strategy.Callback;
import com.beige.camera.advertisement.dagger.AdComponentHolder;
import com.beige.camera.common.base.BaseDialogFragment;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.RxUtil;
import com.beige.camera.common.utils.ScreenUtils;
import com.beige.camera.common.utils.imageloader.BitmapUtil;
import com.beige.camera.common.utils.span.SpanUtils;
import com.zhangqiang.visiblehelper.OnVisibilityChangeListener;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.beige.camera.common.utils.RxUtil.io_main;


public class ADWifiDialog extends BaseDialogFragment {

    private OnChoiceListener onChoiceListener;
    private TextView tvTitle;
    private ImageView ivIcon;
    private TextView tvContent;
    private TextView btnConfirm;
    private TextView btnCancel;
    private FrameLayout adContainer;

    private String pageName;
    private String strTitle;
    private String strContent;
    private String tvBtnConfirm;
    private String tvBtnCancel;
    private int drawable;

    public static ADWifiDialog newInstance(String pageName) {
        Bundle bundle = new Bundle();
        bundle.putString("pageName", pageName);
        ADWifiDialog dialog = new ADWifiDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    public interface OnChoiceListener {

        void onAgree();

        void onDisagree();
    }

    @Override
    public int getTheme() {
        return R.style.CommonDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageName = getArguments().getString("pageName");
        getVisibleHelper().addVisibilityChangeListener(new OnVisibilityChangeListener() {
            @Override
            public void onVisibilityChange(boolean isVisible) {
                if (!isVisible) {
                    return;
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_ad_wifi_dialog, container, false);
        tvTitle = view.findViewById(R.id.tv_title);
        ivIcon = view.findViewById(R.id.iv_icon);
        tvContent = view.findViewById(R.id.tv_content);
        btnConfirm = view.findViewById(R.id.btn_confirm);
        btnCancel = view.findViewById(R.id.btn_cancel);
        adContainer = view.findViewById(R.id.fl_ad_container);

        if (!TextUtils.isEmpty(strTitle)) {
            SpanUtils.setHtmlText(tvTitle, strTitle);
        }
        if (!TextUtils.isEmpty(strContent)) {
            SpanUtils.setHtmlText(tvContent, strContent);
        }
        if (!TextUtils.isEmpty(tvBtnConfirm)) {
            SpanUtils.setHtmlText(btnConfirm, tvBtnConfirm);
        }
        if (!TextUtils.isEmpty(tvBtnCancel)) {
            SpanUtils.setHtmlText(btnCancel, tvBtnCancel);
        }
        BitmapUtil.loadImage(drawable,ivIcon);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onChoiceListener != null) {
                    onChoiceListener.onAgree();
                }
                dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onChoiceListener != null) {
                    onChoiceListener.onDisagree();
                }
                dismiss();
            }
        });

        AdComponentHolder.getComponent().getAdApi().getAdConfig("WiFi_feeds")
                .compose(io_main())
                .timeout(2000, TimeUnit.MILLISECONDS)
                .map(RxUtil.applyApiResult())
                .subscribe(new Observer<AdConfigBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(AdConfigBean adModel) {
                        LogUtils.e("TTInfoFlowAd", "adConfigBean = " + adModel.getCandidates().toString());
                        if (adContainer != null) {
                            adContainer.post(new Runnable() {
                                @Override
                                public void run() {
                                    AdManager.loadInfoFlowAd(adContainer, adModel.getCandidates(), new Callback<InfoFlowAd>() {
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

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        return view;
    }

    public void setTvTitle(String title) {
        this.strTitle = title;
    }

    public void setTvContent(String strContent) {
        this.strContent = strContent;
    }

    public void setTvBtnConfirm(String tvBtnConfirm) {
        this.tvBtnConfirm = tvBtnConfirm;
    }

    public void setTvBtnCancel(String tvBtnCancel) {
        this.tvBtnCancel = tvBtnCancel;
    }

    public void setIcon(int drawable) {
        this.drawable = drawable;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = (int) (ScreenUtils.getScreenWidth() * 0.77f);
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(layoutParams);
        }
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
    }

    public void setOnChoiceListener(OnChoiceListener onChoiceListener) {
        this.onChoiceListener = onChoiceListener;
    }
}
