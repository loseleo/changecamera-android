package com.beige.camera.common.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.beige.camera.common.R;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Cancellable;

public class DialogControllerImpl implements DialogController {

    private Context context;
    private MDLoadingDialog progressDialog;

    public DialogControllerImpl(Context context) {
        this.context = context;
    }

    @Override
    public Observable<DialogEvent> showLoadingDialog(@Nullable final LoadingDialogParam param) {
        hideLoadingDialog();
        clear();
        return Observable.create(new ObservableOnSubscribe<DialogEvent>() {
            @Override
            public void subscribe(final ObservableEmitter<DialogEvent> emitter) throws Exception {
                if (progressDialog == null) {
                    progressDialog = new MDLoadingDialog(context, R.style.CommonLoadingDialogStyle).createDialog();
                }

                if (param != null) {
                    progressDialog.setLoadingParams(param);
                }
                progressDialog.show();
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        emitter.onNext(DialogEvent.CANCEL);
                        emitter.onComplete();
                    }
                });
                progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        emitter.onNext(DialogEvent.DISMISS);
                        emitter.onComplete();
                    }
                });
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        clear();
                    }
                });
            }
        });
    }

    @Override
    public void hideLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public Dialog showCommentDialog(String dialogContent, String dialogTitle, String dialogCancleMessage, String dialogConfirmMessage, final String style, final DialogCallback callback) {
        View rootView = null;
        switch (style) {
            case DIALOG_SINGLE_BUTTON:
                rootView = View.inflate(context, R.layout.common_layout_dialog_single_button, null);
                break;
            case DIALOG_SINGLETITLE:
                rootView = View.inflate(context, R.layout.common_layout_dialog_single_title, null);
                break;
            case DIALOG_DOUBLE_BUTTON_HORIZONTAL:
                rootView = View.inflate(context, R.layout.common_layout_dialog_dounlebutton_horizontal, null);
                break;
            case DIALOG_DOUBLE_BUTTON_VERTICAL:
                rootView = View.inflate(context, R.layout.common_layout_dialog_dounlebutton_vertical, null);
                break;
            case DIALOG_SINGLE_BUTTON_NOCLOSE:
                rootView = View.inflate(context, R.layout.common_layout_dialog_single_button_noclose, null);
                break;
            case DIALOG_DOUBLETITLE_DOUBLE_BUTTON_NOCLOSE:
                rootView = View.inflate(context, R.layout.common_layout_dialog_doubletitle_double_button_noclose, null);
                break;

            default:
                break;
        }

        final Dialog mDialog = new Dialog(context, R.style.CommonDialog);

        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.closeOptionsMenu();
        mDialog.setContentView(rootView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mDialog.show();
        TextView dialogMessage = rootView.findViewById(R.id.tv_pushnotice_dialog_content);
        TextView dialogConfirm = rootView.findViewById(R.id.tv_pushnotice_dialog_confirm);
        Button dialogClose = rootView.findViewById(R.id.iv_pushnotice_dialog_close);
        TextView mdialogTitle = rootView.findViewById(R.id.tv_dialog_title);
        TextView mTvDialogLittleTitle = rootView.findViewById(R.id.tv_dialog_little_title);
        if (dialogMessage != null && !TextUtils.isEmpty(dialogContent)) {
            dialogMessage.setText(Html.fromHtml(dialogContent));
            dialogMessage.setMovementMethod(ScrollingMovementMethod.getInstance());
        }
        if (!TextUtils.isEmpty(dialogConfirmMessage)) {
            dialogConfirm.setText(dialogConfirmMessage);
        }
        if (dialogClose != null && !TextUtils.isEmpty(dialogCancleMessage)) {
            dialogClose.setText(dialogCancleMessage);
        }
        if (!TextUtils.isEmpty(dialogTitle)) {
            mdialogTitle.setText(dialogTitle);
        }
        if (mTvDialogLittleTitle != null) {

        }
        if (dialogClose != null) {
            dialogClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.dialogCancel();
                    }

                    mDialog.dismiss();
                }
            });
        }

        dialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.dialogConfirm();
                }
                mDialog.dismiss();
            }
        });
        return mDialog;
    }

    @Override
    public void clear() {
        if (progressDialog != null) {
            progressDialog.setOnCancelListener(null);
            progressDialog.setOnDismissListener(null);
        }
    }

    @Override
    public void destroy() {
        clear();
    }
}
