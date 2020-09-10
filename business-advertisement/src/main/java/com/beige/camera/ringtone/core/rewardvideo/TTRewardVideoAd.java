package com.beige.camera.ringtone.core.rewardvideo;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.bykv.vk.openvk.TTAppDownloadListener;
import com.bykv.vk.openvk.TTRdVideoObject;
import com.bykv.vk.openvk.TTVfConstant;
import com.bykv.vk.openvk.TTVfManager;
import com.bykv.vk.openvk.TTVfNative;
import com.bykv.vk.openvk.VfSlot;
import com.beige.camera.common.base.BaseApplication;
import com.beige.camera.common.feed.bean.AdModel;
import com.beige.camera.ringtone.TTAdManagerHolder;
import com.beige.camera.ringtone.core.loader.ResourceLoader;
import com.beige.camera.common.utils.JsonUtils;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.ScreenUtils;

import java.util.HashMap;
import java.util.Map;


public class TTRewardVideoAd extends RewardVideoAd<TTRewardVideoAd.TTRewardVideoResource> {

    public TTRewardVideoAd(AdModel adModel, Activity activity) {
        super(activity,adModel);
    }

    @NonNull
    @Override
    protected ResourceLoader<TTRewardVideoResource> onCreateResourceLoader(AdModel adModel) {
        return new TTRewardVideoResourceLoader(adModel, getActivity());
    }

    @Override
    protected void onSetupAdResource(Activity activity, TTRewardVideoResource ttRewardVideoResource) {
        TTRdVideoObject ttRewardVideoAd = ttRewardVideoResource.getTtRewardVideoAd();
        ttRewardVideoAd.setRdVrInteractionListener(new TTRdVideoObject.RdVrInteractionListener() {
            boolean isVideoComplete;

            @Override
            public void onShow() {
                LogUtils.e("RewardVideo", "onAdShow");
                notifyAdDisplay();
            }

            @Override
            public void onVideoBarClick() {
                LogUtils.e("RewardVideo", "onAdVideoBarClick");
                notifyAdClick();
            }

            @Override
            public void onClose() {
                LogUtils.e("RewardVideo", "onAdClose");
                if (isVideoComplete) {
                    notifyReward(true);
                }
                getDismissedHelper().notifyAdDismiss();
            }

            @Override
            public void onVideoComplete() {
                LogUtils.e("RewardVideo", "onVideoComplete");
                isVideoComplete = true;
            }

            @Override
            public void onVideoError() {
                LogUtils.e("RewardVideo", "onVideoError");
                notifyAdFail(new RuntimeException("load tt video fail:onVideoError"));
            }

            //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
            @Override
            public void onRdVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
                LogUtils.e("RewardVideo", "verify:" + rewardVerify + " amount:" + rewardAmount + " name:" + rewardName);
            }

            @Override
            public void onSkippedVideo() {

            }
        });
        ttRewardVideoAd.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
                //  mHasShowDownloadActive = false;
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                LogUtils.d("ysj", "下载中，点击下载区域暂停");
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                //LogUtils.e( "下载暂停，点击下载区域继续", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                //    LogUtils.e( "下载失败，点击下载区域重新下载", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                //LogUtils.e( "下载失败，点击下载区域重新下载", Toast.LENGTH_LONG);
            }

            @Override
            public void onInstalled(String fileName, String appName) {
                //LogUtils.e( "安装完成，点击下载区域打开", Toast.LENGTH_LONG);
            }
        });
        //如果是缓存，则把当前缓存时间存起来。
//                if (finalIsCache) {
//                    cachedTime = System.currentTimeMillis();
//                    return;
//                }
        //播放激励视频广告
        ttRewardVideoAd.showRdVideoVr(activity);
    }

    static class TTRewardVideoResource {

        private TTRdVideoObject ttRewardVideoAd;

        TTRewardVideoResource(TTRdVideoObject ttRewardVideoAd) {
            this.ttRewardVideoAd = ttRewardVideoAd;
        }

        TTRdVideoObject getTtRewardVideoAd() {
            return ttRewardVideoAd;
        }
    }

    private static class TTRewardVideoResourceLoader extends ResourceLoader<TTRewardVideoResource> {

        private Activity activity;

        TTRewardVideoResourceLoader(AdModel adModel, Activity activity) {
            super(adModel);
            this.activity = activity;
        }

        @Override
        protected void onLoad(AdModel adModel) {

            //step1:初始化sdk
            TTVfManager ttAdManager = TTAdManagerHolder.get();
            //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
            TTAdManagerHolder.get().requestPermissionIfNecessary(activity);
            //step3:创建TTAdNative对象,用于调用广告请求接口
            TTVfNative mTTAdNative = ttAdManager.createVfNative(BaseApplication.getsInstance().getApplicationContext());

            Map<String, String> data = new HashMap<>();
            data.put("adCode", adModel.getAdCode());
            data.put("adId", adModel.getAdId());
            data.put("adChannel", adModel.getAdChannel());
            VfSlot adSlot = new VfSlot.Builder()
                    .setCodeId(adModel.getAdId())
                    .setSupportDeepLink(true)
                    .setImageAcceptedSize(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight())
//                    .setUserID(UserInfoManager.getMemberId())//用户id,必传参数
                    .setMediaExtra(JsonUtils.mapToJsonString(data)) //附加参数，ad_video_pause.png`可选
                    .setOrientation(TTVfConstant.VERTICAL) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                    .build();

            mTTAdNative.loadRdVideoVr(adSlot, new TTVfNative.RdVideoVfListener() {
                @Override
                public void onError(int code, String message) {
                    LogUtils.e("RewardVideo onError=", message);
                    notifyFail(new RuntimeException("load tt reward video ad fail:" + code + ";" + message));
                }

                //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
                @Override
                public void onRdVideoVrLoad(TTRdVideoObject ad) {
                    notifySuccess(new TTRewardVideoResource(ad));
                    LogUtils.e("RewardVideo", "onRewardVideoAdLoad");
//                mttRewardVideoAd.setShowDownLoadBar(false);
                }

                //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
                @Override
                public void onRdVideoCached() {
                    LogUtils.d("RewardVideo", "onRewardVideoCached");
                }

            });
        }
    }
}
