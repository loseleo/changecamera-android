package com.beige.camera.screenlock.widget.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.beige.camera.common.base.BaseDialogFragment;
import com.beige.camera.common.utils.DialogUtil;
import com.beige.camera.common.utils.PackageUtils;
import com.beige.camera.common.utils.PermissionPageUtils;
import com.beige.camera.screenlock.R;

/**
 * @author: zhanglin
 * @date: 2019/5/1
 * Copyright (c) 2019 https://www.qutoutiao.net. All rights reserved.
 */
public class LockPermissionNavigateDialog extends BaseDialogFragment {

    private  ClickCallback mClickCallback ;

    public interface ClickCallback{
        void openCallback();
        void closeCallback();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return DialogUtil.showCenterDialog(getActivity(),initView());
    }

    public void setClickCallback(ClickCallback mClickCallback){
        this.mClickCallback = mClickCallback;
    }

    private View initView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.lock_dialog_permission_navigate,null,false);
        Button btnOpen = view.findViewById(R.id.btn_open);
        ImageView ivClose = view.findViewById(R.id.iv_close);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvAppName = view.findViewById(R.id.tv_app_name);
        tvTitle.setText(getString(R.string.lock_permission_navigate_title, PackageUtils.getAppName(getActivity())));
        tvAppName.setText(PackageUtils.getAppName(getActivity()));

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionPageUtils permissionPageUtils = new PermissionPageUtils(getActivity());
                permissionPageUtils.jumpPermissionPage();
                dismiss();
                if(mClickCallback != null){
                    mClickCallback.openCallback();
                }
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if(mClickCallback != null){
                    mClickCallback.closeCallback();
                }
            }
        });
        return view;
    }


}
