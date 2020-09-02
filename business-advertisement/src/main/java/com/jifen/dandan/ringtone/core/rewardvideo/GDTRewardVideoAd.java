package com.jifen.dandan.ringtone.core.rewardvideo;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.jifen.dandan.common.feed.bean.AdModel;
import com.jifen.dandan.ringtone.core.loader.ResourceLoader;
import com.jifen.dandan.common.BuildConfig;
import com.qq.e.ads.rewardvideo.RewardVideoAD;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
import com.qq.e.comm.util.AdError;

public class GDTRewardVideoAd extends RewardVideoAd<GDTRewardVideoAd.GDTRewardVideoAdResource> {

    public GDTRewardVideoAd(AdModel adModel, Activity activity) {
        super(activity, adModel);
    }

    @NonNull
    @Override
    protected ResourceLoader<GDTRewardVideoAdResource> onCreateResourceLoader(AdModel adModel) {
        return new GDTRewardVideoResourceLoader(adModel, this);
    }

    @Override
    protected void onSetupAdResource(Activity activity, GDTRewardVideoAdResource gdtRewardVideoAdResource) {
        gdtRewardVideoAdResource.getRewardVideoAD().showAD();
    }


    public static class GDTRewardVideoAdResource {

        private RewardVideoAD rewardVideoAD;

        GDTRewardVideoAdResource(RewardVideoAD rewardVideoAD) {
            this.rewardVideoAD = rewardVideoAD;
        }

        RewardVideoAD getRewardVideoAD() {
            return rewardVideoAD;
        }
    }

    private static class GDTRewardVideoResourceLoader extends ResourceLoader<GDTRewardVideoAdResource> {

        private GDTRewardVideoAd gdtRewardVideoAd;
        private RewardVideoAD rewardVideoAD;

        GDTRewardVideoResourceLoader(AdModel adModel, GDTRewardVideoAd gdtRewardVideoAd) {
            super(adModel);
            this.gdtRewardVideoAd = gdtRewardVideoAd;
        }

        @Override
        protected void onLoad(AdModel adModel) {

            rewardVideoAD = new RewardVideoAD(gdtRewardVideoAd.getActivity(), BuildConfig.AD_GDT_APP_ID, adModel.getAdId(), new RewardVideoADListener() {

                /**
                 * 广告加载成功，可在此回调后进行广告展示，此时广告过期时间确定，可通过RewardVideoAD.getExpireTimestamp()获取
                 */
                @Override
                public void onADLoad() {
                    notifySuccess(new GDTRewardVideoAdResource(rewardVideoAD));
                }

                /**
                 * 视频素材缓存成功，可在此回调后进行广告展示
                 */
                @Override
                public void onVideoCached() {

                }

                /**
                 * 激励视频广告页面展示，此后RewardVideoAD.hasShown()返回true
                 */
                @Override
                public void onADShow() {
                }

                /**
                 * 激励视频广告曝光
                 */
                @Override
                public void onADExpose() {
                    gdtRewardVideoAd.notifyAdDisplay();
                }

                /**
                 * 激励视频广告激励发放
                 */
                @Override
                public void onReward() {
                    gdtRewardVideoAd.notifyReward(true);
                }

                /**
                 * 激励视频广告被点击
                 */
                @Override
                public void onADClick() {
                    gdtRewardVideoAd.notifyAdClick();
                }

                /**
                 * 广告视频素材播放完毕
                 */
                @Override
                public void onVideoComplete() {

                }

                /**
                 * 激励视频广告被关闭
                 */
                @Override
                public void onADClose() {
                    gdtRewardVideoAd.getDismissedHelper().notifyAdDismiss();
                }

                /**
                 * 广告流程出错，AdError中包含错误码和错误描述
                 * @param adError adError
                 */
                @Override
                public void onError(AdError adError) {
                    String errorMsg;
                    if (adError == null) {
                        errorMsg = "load gdt reward video ad fail";
                    } else {
                        errorMsg = "load gdt reward ad fail,code=" + adError.getErrorCode() + ";msg=" + adError.getErrorMsg();
                    }
                    notifyFail(new RuntimeException(errorMsg));
                }
            });
            rewardVideoAD.loadAD();
        }
    }
}
