package com.beige.camera.ringtone.core.rewardvideo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.iclicash.advlib.core.AdRequestParam;
import com.iclicash.advlib.core.ICliBundle;
import com.iclicash.advlib.core.ICliFactory;
import com.iclicash.advlib.core.IMultiAdObject;
import com.iclicash.advlib.core.IMultiAdRequest;
import com.beige.camera.ringtone.core.CPCUtils;
import com.beige.camera.common.feed.bean.AdModel;
import com.beige.camera.ringtone.CPCICliFactoryHolder;
import com.beige.camera.ringtone.core.jj.JJAdResource;
import com.beige.camera.ringtone.core.jj.JJUtils;
import com.beige.camera.ringtone.core.loader.ResourceLoader;
import com.beige.camera.common.BuildConfig;

public class JJRewardVideoAd extends RewardVideoAd<JJRewardVideoAd.JJResource> {


    public JJRewardVideoAd(Activity activity, AdModel adModel) {
        super(activity, adModel);
    }

    @Override
    protected void onSetupAdResource(Activity activity, JJResource jjResource) {
        IMultiAdObject adObject = jjResource.getAdObject();
        adObject.showRewardVideo(activity);
    }

    @NonNull
    @Override
    protected ResourceLoader<JJResource> onCreateResourceLoader(AdModel adModel) {
        return new JJResourceLoader(adModel,this);
    }

    public static class JJResource implements JJAdResource {

        private IMultiAdObject adObject;
        private ICliBundle iCliBundle;

        JJResource(IMultiAdObject adObject) {
            this.adObject = adObject;
            iCliBundle = adObject.convert2ICliBundle();
        }

        IMultiAdObject getAdObject() {
            return adObject;
        }

        public ICliBundle getiCliBundle() {
            return iCliBundle;
        }

        @Override
        public String getSuccessChannel() {
            return JJUtils.getSuccessChannel(iCliBundle);
        }

        @Override
        public String getSuccessAdId() {
            return JJUtils.getSuccessAdId(iCliBundle);
        }
    }


    private static class JJResourceLoader extends ResourceLoader<JJResource> {

        private JJRewardVideoAd rewardVideoAd;

        JJResourceLoader(AdModel adModel, JJRewardVideoAd rewardVideoAd) {
            super(adModel);
            this.rewardVideoAd = rewardVideoAd;
        }

        @Override
        protected void onLoad(AdModel adModel) {
            Bundle extraBundle = CPCUtils.buildCommonParams(rewardVideoAd.getActivity());
            extraBundle.putBoolean("jump_server", true);//【可选】是否要跳过服务端通信，默认为false
            extraBundle.putInt("countdown_style", 0);//【可选】倒计时样式，默认为0
            extraBundle.putInt("countdown", 30);// 自定义倒计时时长
//            extraBundle.putInt("fullscreen", 1);//金币数目
            extraBundle.putString("descriptions", CPCRewardVideoAd.makeDesc(adModel));// 自定义文案
            AdRequestParam requestParam = new AdRequestParam.Builder()
                    .adslotID(adModel.getAdId())  //必选
                    .gdtAppID(BuildConfig.AD_GDT_APP_ID) //可选，广点通appid，如果广点通参与竞价，则必选
                    .adType(AdRequestParam.ADTYPE_REWARD_VIDEO) //必选，请注意：adType为ADTYPE_REWARD_VIDEO
                    .bannerSize(adModel.getWidth(), adModel.getHeight()) //必选
                    .adLoadListener(new AdRequestParam.ADLoadListener() { //必选，加载监听
                        @Override
                        public void onADLoaded(IMultiAdObject object) {
                            if (object != null) {
                                notifySuccess(new JJResource(object));
                            } else {
                                notifyFail(new RuntimeException("cpc jj reward ad fail: obj null"));
                            }
                        }

                        @Override
                        public void onAdFailed(String errorMsg) {
                            notifyFail(new RuntimeException("cpc jj reward ad fail:" + errorMsg));
                        }
                    })
                    .adRewardVideoListener(new AdRequestParam.ADRewardVideoListener() {//必选，激励视频回调监听
                        boolean isVideoComplete;

                        @Override
                        public void onAdShow(Bundle bundle) {
                            rewardVideoAd.notifyAdDisplay();
                        }

                        @Override
                        public void onAdClick(Bundle bundle) {
                            rewardVideoAd.notifyAdClick();
                        }

                        @Override
                        public void onAdClose(Bundle bundle) {
                            if (isVideoComplete) {
                                rewardVideoAd.notifyReward(true);
                            }
                            rewardVideoAd.getDismissedHelper().notifyAdDismiss();
                        }

                        @Override
                        public void onVideoComplete(Bundle bundle) {
                            isVideoComplete = true;
                        }

                        @Override
                        public void onVideoError(Bundle bundle) {
                            notifyFail(new RuntimeException("cpc jj video error"));
                        }

                        @Override
                        public void onReward(Bundle bundle) {
                        }

                        @Override
                        public void onSkippedVideo(Bundle bundle) {
                        }
                    })
                    .extraBundle(extraBundle) //可选，扩展字段bundle
                    .build();

            ICliFactory iCliFactory = CPCICliFactoryHolder.getInstance().getICliFactory();
            IMultiAdRequest multiAdRequest = iCliFactory.createNativeMultiAdRequest();
            if (multiAdRequest != null) {
                multiAdRequest.invokeADV(requestParam);
            }else {
                notifyFail(new RuntimeException("multiAdRequest null"));
            }
        }
    }
}
