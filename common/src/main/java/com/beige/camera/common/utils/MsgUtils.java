package com.beige.camera.common.utils;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beige.camera.common.R;
import com.tencent.bugly.crashreport.CrashReport;

import java.lang.ref.WeakReference;


/**
 * 壳工程里面的MsgUtils和ToastCompat有crash问题
 */
public class MsgUtils {

    private static WeakReference<Toast> mLastToastRef;

    private static Toast getLastToast() {
        return mLastToastRef == null ? null : mLastToastRef.get();
    }

    private static void setLastToast(Toast toast) {
        mLastToastRef = new WeakReference<>(toast);
    }

    public enum Type {
        SUCCESS, WARNING, ERROR
    }

    /**
     * 默认显示位置在屏幕下方
     *
     * @param context
     * @param text    提示文字
     */
    public static void showToast(Context context, String text) {
        showToast(context, text, Type.SUCCESS);
    }

    public static void showToast(final Context context, String text, Type type) {
        showToast(context, text, type, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, ScreenUtils.dpToPx(74));
    }

    public static void showToastCenter(Context context, String text) {
        showToast(context, text, Gravity.CENTER, 0, 0);
    }

    public static void showToastCenter(Context context, String text, int length) {
        if (context == null) {
            LogUtils.w("context is null");
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.view_toast, null);
        ImageView imgView = view.findViewById(R.id.vtoast_img);
        imgView.setVisibility(View.GONE);
        TextView textContent = view.findViewById(R.id.vtoast_content);
        textContent.setGravity(Gravity.CENTER);
        textContent.setText(text);
        showToast(context, view, length, Gravity.CENTER, 0, 0);
    }

    public static void showToast(Context context, String text, int gravity, final int xOffset, final int yOffset) {
        showToast(context, text, Type.SUCCESS, gravity, xOffset, yOffset);
    }

    /**
     * @param context
     * @param context
     * @param text    提示文字
     * @param type    类型
     * @param gravity 显示位置
     * @param xOffset X偏移
     * @param yOffset Y偏移
     */
    public static void showToast(final Context context, String text, Type type, final int gravity, final int xOffset, final int yOffset) {
        if (context == null) {
            LogUtils.w("context is null");
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.view_toast, null);
        ImageView imgView = view.findViewById(R.id.vtoast_img);
        imgView.setVisibility(View.GONE);
        TextView textContent = view.findViewById(R.id.vtoast_content);
        textContent.setGravity(Gravity.CENTER);
        textContent.setText(text);
        switch (type) {
            case ERROR:
                imgView.setVisibility(View.VISIBLE);
                imgView.setImageResource(R.mipmap.icon_toast_warning);
                break;
            default:
                break;
        }

        showToast(context, view, Toast.LENGTH_SHORT, gravity, xOffset, yOffset);
    }


    public static void showToast(final Context context, final View view, final int duration, final int gravity, final int xOffset, final int yOffset) {
        ThreadUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {

                try {
                    Toast lastToast = getLastToast();
                    if (lastToast != null) {
                        lastToast.cancel();
                    }

                    if (view != null && view.getParent() != null) {
                        ((ViewGroup) view.getParent()).removeView(view);
                    }
                    Toast toast = new Toast(context);
                    toast.setGravity(gravity, xOffset, yOffset);
                    toast.setDuration(duration);
                    toast.setView(view);
                    toast.show();

                    setLastToast(toast);
                } catch (Exception e) {
                    CrashReport.postCatchedException(e);
                    e.printStackTrace();
                    Log.e("qk", "toast..." + e);
                }
            }
        });
    }

}
