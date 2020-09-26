package com.beige.camera.contract;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;

import com.beige.camera.advertisement.api.bean.AdConfigBean;
import com.beige.camera.bean.VersionInfoBean;
import com.zhangqiang.mvp.IView;

/**
 * @author wuyufeng 2019/10/10.
 */
public interface IHomeView extends IView, LifecycleOwner{

    void showDownloadApkDialog(VersionInfoBean mVersionInfo);

    void showAdvertiseConfig(String type, AdConfigBean adConfigBean);


}
