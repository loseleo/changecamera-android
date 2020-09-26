package com.beige.camera.presenter;

import android.content.Context;

import com.beige.camera.advertisement.api.bean.AdConfigBean;
import com.beige.camera.api.Api;
import com.beige.camera.bean.VersionInfoBean;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.RxUtil;
import com.beige.camera.contract.IHomeView;
import com.zhangqiang.mvp.Presenter;


import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * @author huangchen@qutoutiao.net
 * @version 1.0
 * @date 06/14/2019
 */
public class HomePresenter extends Presenter<IHomeView> {

    private Api api;
    private Context mContext;


    @Inject
    public HomePresenter(Context mContext, Api api) {
        this.mContext = mContext;
        this.api = api;
    }


    public void checkVersion() {
        api.checkVersion()
                .compose(RxUtil.io_main())
                .map(RxUtil.applyApiResult())
                .subscribe(new Observer<VersionInfoBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(VersionInfoBean versionInfoBean) {
                        IHomeView attachedView = getAttachedView();
                        if (attachedView != null) {
                            attachedView.showDownloadApkDialog(versionInfoBean);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("zhangning","msg:" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    public void getAdvertiseConfig(String type) {

        api.getAdvertiseConfig(type)
                .compose(RxUtil.io_main())
                .map(RxUtil.applyApiResult())
                .subscribe(new Observer<AdConfigBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AdConfigBean adConfigBean) {
                        IHomeView attachedView = getAttachedView();
                        if (attachedView != null) {
                            attachedView.showAdvertiseConfig(type,adConfigBean);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

}
