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
import android.widget.TextView;

import com.beige.camera.R;
import com.beige.camera.common.base.BaseDialogFragment;
import com.beige.camera.common.utils.ScreenUtils;
import com.beige.camera.common.utils.span.SpanUtils;
import com.zhangqiang.visiblehelper.OnVisibilityChangeListener;


public class CommonDialog extends BaseDialogFragment {

    private OnChoiceListener onChoiceListener;
    private TextView tvTitle;
    private TextView tvContent;
    private TextView btnConfirm;
    private TextView btnCancel;

    private String pageName;
    private String strTitle;
    private String strContent;
    private String tvBtnConfirm;
    private String tvBtnCancel;

    public static CommonDialog newInstance(String pageName) {
        Bundle bundle = new Bundle();
        bundle.putString("pageName", pageName);
        CommonDialog dialog = new CommonDialog();
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
        View view = inflater.inflate(R.layout.layout_common_dialog, container, false);
        tvTitle = view.findViewById(R.id.tv_title);
        tvContent = view.findViewById(R.id.tv_content);
        btnConfirm = view.findViewById(R.id.btn_confirm);
        btnCancel = view.findViewById(R.id.btn_cancel);
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
