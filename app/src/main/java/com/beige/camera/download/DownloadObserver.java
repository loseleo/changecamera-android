package com.beige.camera.download;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.beige.camera.common.base.BaseApplication;
import com.beige.camera.common.constant.Constant;
import com.beige.camera.common.utils.FileUtils;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by zs
 * Date：2018年 09月 12日
 * Time：13:50
 * —————————————————————————————————————
 * About: 观察者
 * —————————————————————————————————————
 */
public abstract class DownloadObserver implements Observer<DownloadInfo> {

    public Disposable d;//可以用于取消注册的监听者
    public DownloadInfo downloadInfo;

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
    }

    @Override
    public void onNext(DownloadInfo value) {
        this.downloadInfo = value;
        downloadInfo.setDownloadStatus(DownloadInfo.DOWNLOAD);
//        EventBus.getDefault().post(downloadInfo);
    }

    @Override
    public void onError(Throwable e) {
//        Log.d("My_Log","onError");
        if (DownloadManager.getInstance().getDownloadUrl(downloadInfo.getUrl())){
            DownloadManager.getInstance().pauseDownload(downloadInfo.getUrl());
            downloadInfo.setDownloadStatus(DownloadInfo.DOWNLOAD_ERROR);
//            EventBus.getDefault().post(downloadInfo);
        }else{
            downloadInfo.setDownloadStatus(DownloadInfo.DOWNLOAD_PAUSE);
//            EventBus.getDefault().post(downloadInfo);
        }

    }

    @Override
    public void onComplete() {
//        Log.d("My_Log","onComplete");
        if (downloadInfo != null){
            downloadInfo.setDownloadStatus(DownloadInfo.DOWNLOAD_OVER);
//            EventBus.getDefault().post(downloadInfo);
            String path = Constant.FILE_PATH_DOWNLOAD + "/" + downloadInfo.getFileName();
            FileUtils.installApk(BaseApplication.getsInstance().getBaseContext(),path);
        }
    }


}
