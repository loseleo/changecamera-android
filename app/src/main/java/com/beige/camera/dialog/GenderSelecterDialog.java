package com.beige.camera.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.beige.camera.R;
import com.beige.camera.common.base.BaseDialogFragment;
import com.beige.camera.common.utils.ScreenUtils;
import com.beige.camera.common.utils.span.SpanUtils;
import com.zhangqiang.visiblehelper.OnVisibilityChangeListener;


public class GenderSelecterDialog extends BaseDialogFragment {

    private OnChoiceListener onChoiceListener;

    public static GenderSelecterDialog newInstance() {
        GenderSelecterDialog dialog = new GenderSelecterDialog();
        return dialog;
    }

    public interface OnChoiceListener {

        void onGenderResurt(int gender);

    }

    @Override
    public int getTheme() {
        return R.style.CommonDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        View view = inflater.inflate(R.layout.layout_genderselect_dialog, container, false);
        ImageView ivGenderMan = view.findViewById(R.id.iv_gender_man);
        ImageView ivGenderWoman = view.findViewById(R.id.iv_gender_woman);
        ImageView ivClose = view.findViewById(R.id.iv_close);
        ivGenderMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onChoiceListener != null) {
                    onChoiceListener.onGenderResurt(1);
                }
                dismiss();
            }
        });
        ivGenderWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onChoiceListener != null) {
                    onChoiceListener.onGenderResurt(2);
                }
                dismiss();
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
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

    public void setOnChoiceListener(OnChoiceListener onChoiceListener) {
        this.onChoiceListener = onChoiceListener;
    }
}
