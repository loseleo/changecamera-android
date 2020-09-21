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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beige.camera.R;
import com.beige.camera.bean.VersionInfoBean;
import com.beige.camera.common.base.BaseDialogFragment;
import com.beige.camera.common.constant.Constant;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.ScreenUtils;
import com.beige.camera.common.utils.span.SpanUtils;
import com.beige.camera.common.utils.span.URLSpanWrapper;
import com.beige.camera.download.DownloadInfo;
import com.beige.camera.download.DownloadManager;
import com.beige.camera.download.DownloadObserver;
import com.zhangqiang.visiblehelper.OnVisibilityChangeListener;


public class UpdataVersionDialog extends BaseDialogFragment {

    private VersionInfoBean mVersionInfoBean;
    private ProgressBar progressbar;
    private TextView btnConfirm;

    public static UpdataVersionDialog newInstance(VersionInfoBean mVersionInfoBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("VersionInfoBean", mVersionInfoBean);
        UpdataVersionDialog dialog = new UpdataVersionDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public int getTheme() {
        return R.style.CommonDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVersionInfoBean = (VersionInfoBean) getArguments().getSerializable("VersionInfoBean");
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
        View view = inflater.inflate(R.layout.dialog_updata_version, container, false);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvContent = view.findViewById(R.id.tv_content);
        btnConfirm = view.findViewById(R.id.btn_confirm);
        TextView btnCancel = view.findViewById(R.id.btn_cancel);
        progressbar = view.findViewById(R.id.progressbar);
        String strTitle = mVersionInfoBean.getTitle();
        SpanUtils.setHtmlText(tvTitle, strTitle);
        tvContent.setText(mVersionInfoBean.getVersionMemo());
        String forceUpdate = mVersionInfoBean.getForceUpdate();
        if (TextUtils.equals("1", forceUpdate)) {
            btnCancel.setVisibility(View.GONE);
        }
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downLoad();
                if (!TextUtils.equals("1", forceUpdate)) {
                    dismiss();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
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

    private void downLoad() {

        String url = mVersionInfoBean.getUrl();

        DownloadManager.getInstance().download("https://video-ddvideo.1sapp.com/assets/apk/2020_01_18/ddvideo.apk", new DownloadObserver() {
            @Override
            public void onNext(DownloadInfo value) {
                super.onNext(value);
                long progress = value.getProgress();
                long total = value.getTotal();
                LogUtils.e("zhangning", "downLoad progress :" + progress);
                if (progressbar != null && TextUtils.equals("1", mVersionInfoBean.getForceUpdate())) {
                    int p = (int) (progress * 100 / total);
                    progressbar.setProgress(p);
                    btnConfirm.setText(p + "%");
                }
            }

            @Override
            public void onComplete() {
                super.onComplete();
                LogUtils.e("zhangning", "downLoad onComplete");
                if (TextUtils.equals("1", mVersionInfoBean.getForceUpdate())) {
                    dismiss();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                LogUtils.e("zhangning", "downLoad e:" + e.getMessage());
            }
        });
    }
}
