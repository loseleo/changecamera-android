package com.beige.camera.common.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.airbnb.lottie.LottieAnimationView;
import com.beige.camera.common.R;

/**
 * Created by wangpeng on 2019/6/26.
 * 全局通用的加载弹窗
 */
public class MDLoadingDialog extends Dialog {

    private MDLoadingDialog customProgressDialog = null;
    private Context context;
    private int theme;
    private LottieAnimationView imageView;
    private View rootView;

    public MDLoadingDialog(Context context, boolean cancelable,
                           OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public MDLoadingDialog(Context context, int theme) {
        super(context, theme);
        customProgressDialog = this;
    }


    public MDLoadingDialog createDialog() {
        customProgressDialog.setContentView(R.layout.common_md_loading_dialog);
        Window window = customProgressDialog.getWindow();
        window.getAttributes().gravity = Gravity.CENTER;
        //init View
        rootView = customProgressDialog.findViewById(R.id.root_view);
        imageView = customProgressDialog.findViewById(R.id.animation_view);
        //防止用户误点屏幕导致loading取消，接口请求取消
        setCanceledOnTouchOutside(false);
        return customProgressDialog;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (customProgressDialog == null) {
            return;
        }
        if (hasFocus) {
            if (!imageView.isAnimating()) {
                imageView.playAnimation();
            }
        } else {
            if (imageView.isAnimating()) {
                imageView.cancelAnimation();
            }
        }
    }

    private MDLoadingDialog setMessage(String strMessage) {
        //产品确定不需要支持文字
//        if (tvMsg != null) {
//            tvMsg.setText(strMessage);
//        }

        return customProgressDialog;
    }

    public MDLoadingDialog setLoadingParams(LoadingDialogParam param) {
        setCancelable(param.isCancelable());
        setMessage(param.getMessage());
        if (param.hasBackground()) {
            rootView.setBackgroundResource(R.drawable.common_bg_loading);
        } else {
            rootView.setBackgroundResource(R.color.transparent);
        }
        return customProgressDialog;
    }


}