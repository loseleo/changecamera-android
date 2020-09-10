package com.beige.camera.common.view.dialog;

import android.app.Dialog;
import android.support.annotation.Nullable;

import io.reactivex.Observable;

public interface DialogController {
    public static final String DIALOG_SINGLE_BUTTON = "dialogsinglebutton";//只有一个按钮，一个取消叉叉图片
    public static final String DIALOG_SINGLETITLE = "dialogsingletitle";//只有一个主标题，两个按钮横向排列
    public static final String DIALOG_DOUBLE_BUTTON_HORIZONTAL = "DIALOGDOUBLEBUTTONHORIZONTAL";//两个按钮横向排列
    public static final String DIALOG_DOUBLE_BUTTON_VERTICAL = "dialogdoublebuttonvertical";//两个按钮纵向排列
    public static final String DIALOG_SINGLE_BUTTON_NOCLOSE = "dialogsinglebuttonnoclose";////只有一个按钮，且没有取消叉叉图片
    public static final String DIALOG_DOUBLETITLE_DOUBLE_BUTTON_NOCLOSE = "dialogdoubletitledoublebuttonnoclose";//两个标题两个按钮

    Observable<DialogEvent> showLoadingDialog(@Nullable LoadingDialogParam param);

    void hideLoadingDialog();

    /**
     * 通知栏开启弹框
     *
     * @param dialogContent        弹框内容
     * @param dialogTitle          弹框标题
     * @param dialogCancleMessage  弹框取消按钮内容
     * @param dialogConfirmMessage 弹框确认内容
     * @param style                弹框样式
     * @param callback
     */
    Dialog showCommentDialog(String dialogContent, String dialogTitle, String dialogCancleMessage, String dialogConfirmMessage, String style, DialogCallback callback);

    void clear();


    void destroy();

}
