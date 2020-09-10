package com.beige.camera.common.dagger.module.support;


import com.beige.camera.common.utils.LogUtils;

/**
 * @author yuyh.
 * @date 2016/12/13.
 */
public class Logger implements LoggingInterceptor.Logger {

    @Override
    public void log(String message) {
        LogUtils.i("http : " + message);
    }
}
