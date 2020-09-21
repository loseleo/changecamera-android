package com.beige.camera.contract;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;

import com.beige.camera.bean.VersionInfoBean;
import com.zhangqiang.mvp.IView;


public interface ICheckVersionView extends IView, LifecycleOwner {

    void showDownloadApkDialog(Context context, VersionInfoBean mVersionInfo, boolean isAutoCheck);
}
