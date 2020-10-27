package com.beige.camera.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.beige.camera.common.R;

/**
 * dialog relative method
 *
 * @author liyanxi
 * @date 12/28/18
 * Copyright (c) 2018 https://www.qutoutiao.net. All rights reserved.
 */
public class DialogUtil {


    /**
     * 页面弹出dialog
     *
     * @param context 上下文
     * @param view    view
     * @return
     */
    public static Dialog showCenterDialog(Context context, final View view) {
        return showCenterDialog(context, view, false);
    }

    /**
     * 页面弹出dialog
     *
     * @param context 上下文
     * @param view    view
     * @param outside 外部点击是否可取消
     * @return
     */
    public static Dialog showCenterDialog(Context context, final View view, boolean outside) {
        return showCenterDialog(context, view, outside, false, R.style.CommonDialog);
    }

    /**
     * 底部弹窗
     *
     * @param context 上下文
     * @param view    view
     * @return
     */
    public static Dialog showBottomDialog(Context context, final View view) {
        Dialog dialog = showCenterDialog(context, view, true, false, R.style.CommonBottomDialogStyle);
        Window dialogWindow = dialog.getWindow();
        //设置背景颜色
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.BOTTOM);
            //设置dialog的宽高属性
            dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        return dialog;
    }

    /**
     * 页面弹出dialog
     *
     * @param context       上下文
     * @param view          view
     * @param outside       外部点击是否可取消
     * @param isDisableBack 是否禁用物理返回键
     * @return
     */
    public static Dialog showCenterDialog(Context context, final View view, boolean outside, boolean isDisableBack) {
        return showCenterDialog(context, view, outside, isDisableBack, R.style.CommonDialog);
    }

    /**
     * 页面弹出dialog
     *
     * @param context       上下文
     * @param view          view
     * @param outside       外部点击是否可取消
     * @param isDisableBack 是否禁用物理返回键
     * @return
     */
    public static Dialog showCenterDialog(Context context, final View view, boolean outside, boolean isDisableBack, int style) {
        Dialog dialog = new Dialog(context, style);
        dialog.setContentView(view);
        dialog.setCancelable(outside);
        dialog.setCanceledOnTouchOutside(outside);
        Window dialogWindow = dialog.getWindow();
        if (isDisableBack) {
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0;
                }
            });
        }
        //设置背景颜色
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.CENTER);
            //外部点击可取消
            dialog.setCancelable(outside);
            dialog.setCanceledOnTouchOutside(outside);
        }
        return dialog;
    }


}
