package com.jifen.dandan.ringtone.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jifen.dandan.common.base.BaseDialogFragment;
import com.jifen.dandan.common.utils.ScreenUtils;
import com.jifen.dandan.common.utils.span.SpanUtils;
import com.jifen.dandan.common.utils.span.URLSpanWrapper;
import com.jifen.dandan.ringtone.R;
import com.jifen.dandan.ringtone.permission.PermissionUtils;
import com.zhangqiang.visiblehelper.OnVisibilityChangeListener;


public class RingPermissionDialog extends BaseDialogFragment {

    public static final String TAG = RingPermissionDialog.class.getSimpleName();

    private Activity mActivity;
    private String pageName;
    private TextView tvTitle;
    private TextView tvContent;
    private String strContent;
    public static RingPermissionDialog newInstance(String pageName , String strContent) {
        Bundle bundle = new Bundle();
        bundle.putString("pageName",pageName);
        bundle.putString("strContent",strContent);
        RingPermissionDialog dialog = new RingPermissionDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        pageName = getArguments().getString("pageName");
        strContent = getArguments().getString("strContent");
        getVisibleHelper().addVisibilityChangeListener(new OnVisibilityChangeListener() {
            @Override
            public void onVisibilityChange(boolean isVisible) {
                if (!isVisible) {
                    return;
                }
            }
        });
    }

    @Override
    public int getTheme() {
        return R.style.CommonDialog;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_permission_ring, container, false);
        tvTitle = view.findViewById(R.id.tv_title);
        tvContent = view.findViewById(R.id.tv_content);
        view.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotToPermission();
                dismiss();
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        SpanUtils.setHtmlText(tvTitle,"轻松五步设置铃声");
        SpanUtils.setHtmlText(tvContent, strContent, new URLSpanWrapper.OnClickListener() {
            @Override
            public boolean onClick(View widget, String url) {
                return false;
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
            layoutParams.width = (int) (ScreenUtils.getScreenWidth()*0.77f);
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(layoutParams);
        }
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
    }



    public void gotToPermission() {
        try {
            if (mActivity != null) {
                //悬浮框框权限
                if (!PermissionUtils.checkFloatWindowPermission(mActivity)) {
                    PermissionUtils.gotoCanOverly(mActivity);
                    return;
                }

                //设置通知监听权限
                if (!PermissionUtils.isNotificationListenersEnabled(mActivity)) {
                    PermissionUtils.gotoNotificationAccessSetting(mActivity);
                    return;
                }

                //修改系统设置
                if (!PermissionUtils.hasSettingPermisson(mActivity)) {
                    PermissionUtils.gotoSettingWriteSetting(mActivity);
                    return;
                }

                //后台弹出页面
                if (!PermissionUtils.checkStartForeground(mActivity)) {
                    PermissionUtils.toPermissionSetting(mActivity);
//                    showBackgroundShowView("后台弹出界面", "点击\"后台弹出界面\"");
                    return;
                }

                //通知
                if (!PermissionUtils.haskNotifySetting(mActivity)) {
                    PermissionUtils.gotoSettingNotify(mActivity);
                    return;
                }

                //来电显示
                if (!PermissionUtils.checkLockedScreen(mActivity)) {
                    PermissionUtils.toPermissionSetting(mActivity);
//                    showBackgroundShowView("锁屏显示", "点击\"锁屏显示\"");
                    return;
                }
            }

        } catch (Exception e) {

        }
    }

}


