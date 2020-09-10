package com.beige.camera.common.helper.activitylife;

public interface AppShowListener {

    /**
     * 应用第一次进入前台
     */
    void onAppEnterForeground();

    /**
     * 从后台返回前台
     */
    void onAppReturnForeground();

    /**
     * 进入后台
     * @param
     */
    void onAppEnterBackground();
}
