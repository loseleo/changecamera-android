package com.jifen.dandan.common.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

/**
 * 权限请求页适配，不同手机系统跳转到不同的权限请求页
 *
 * @author: zhanglin
 * @date: 2019/5/1
 * Copyright (c) 2019 https://www.qutoutiao.net. All rights reserved.
 */
public class PermissionPageUtils {


    private Context mContext;

    public PermissionPageUtils(Context context) {
        this.mContext = context;
    }

    /**
     * 跳转到设置权限管理界面
     */
    public void jumpPermissionPage() {
        goIntentSetting();
    }


    private void goIntentSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", PackageUtils.getPackageName(mContext), null);
        intent.setData(uri);
        try {
            if (mContext.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null){
                mContext.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
