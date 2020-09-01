package com.jifen.dandan.presenter;

import android.content.Context;
import com.jifen.dandan.api.Api;
import com.jifen.dandan.contract.HomeView;
import com.zhangqiang.mvp.Presenter;


import javax.inject.Inject;


/**
 * @author huangchen@qutoutiao.net
 * @version 1.0
 * @date 06/14/2019
 */
public class HomePresenter extends Presenter<HomeView> {

    private Api api;
    private Context mContext;


    @Inject
    public HomePresenter(Context mContext, Api api) {
        this.mContext = mContext;
        this.api = api;
    }


    public void getPrivacyPoliceContent() {

//        api.getPrivacyPoliceContent()
//                .compose(RxUtil.io_main())
//                .map(RxUtil.applyApiResult())
//                .subscribe(new BaseObserver<PrivacyPoliceContentBean>() {
//                    @Override
//                    public void onNext(PrivacyPoliceContentBean privacyPoliceContentBean) {
//                        super.onNext(privacyPoliceContentBean);
//                        MmkvUtil.getInstance().putString(DdConstants.PRIVACY_VERSION_SERVICE, privacyPoliceContentBean.getPrivacyVersion());
//                        MmkvUtil.getInstance().putString(DdConstants.PRIVACY_TITLE_SERVICE, privacyPoliceContentBean.getPrivacyPopupTitle());
//                        MmkvUtil.getInstance().putString(DdConstants.PRIVACY_CONTENT_SERVICE, privacyPoliceContentBean.getPrivacyPopupContent());
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        super.onError(e);
//                    }
//                });
    }

}
