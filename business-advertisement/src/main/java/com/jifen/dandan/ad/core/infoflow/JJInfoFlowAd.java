package com.jifen.dandan.ad.core.infoflow;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.FrameLayout;

import com.iclicash.advlib.core.AdRequestParam;
import com.iclicash.advlib.core.ICliBundle;
import com.iclicash.advlib.core.ICliFactory;
import com.iclicash.advlib.core.IMultiAdObject;
import com.iclicash.advlib.core.IMultiAdRequest;
import com.jifen.dandan.ad.core.CPCUtils;
import com.jifen.dandan.common.feed.bean.AdModel;
import com.jifen.dandan.ad.CPCICliFactoryHolder;
import com.jifen.dandan.ad.core.infoflow.video.VideoInfoOwner;
import com.jifen.dandan.ad.core.jj.JJAdResource;
import com.jifen.dandan.ad.core.jj.JJUtils;
import com.jifen.dandan.ad.core.loader.ResourceLoader;
import com.jifen.dandan.common.BuildConfig;
import com.jifen.dandan.common.utils.PackageUtils;

public class JJInfoFlowAd extends InfoFlowVideoAd<JJInfoFlowAd.JJInfoFlowAdResource> {


    public JJInfoFlowAd(FrameLayout adContainer, AdModel adModel) {
        super(adContainer, adModel);
    }

    @Override
    protected void onSetupAdResource(FrameLayout adContainer, JJInfoFlowAdResource jjInfoFlowAdResource) {
        IMultiAdObject adObject = jjInfoFlowAdResource.getAdObject();
        if (adObject != null) {
            adObject.bindView(adContainer, new IMultiAdObject.ADEventListener() {
                @Override
                public void onAdClick() {
                    notifyAdClick();
                }

                @Override
                public void onADExposed() {
                    notifyAdDisplay();
                }

                @Override
                public void onAdFailed(String errorMsg) {
                    notifyAdFail(new RuntimeException("cpc jj error:" + errorMsg));
                }
            });
        }
    }

    @NonNull
    @Override
    protected ResourceLoader<JJInfoFlowAdResource> onCreateResourceLoader(AdModel adModel) {
        return new JJResourceLoader(adModel,getAdContainer().getContext());
    }

    public static class JJInfoFlowAdResource implements JJAdResource, VideoInfoOwner {

        private IMultiAdObject adObject;
        private ICliBundle iCliBundle;

        JJInfoFlowAdResource(IMultiAdObject adObject) {
            this.adObject = adObject;
            iCliBundle = adObject.convert2ICliBundle();
        }

        @Override
        public long getVideoDuration() {
            return iCliBundle == null ? 0 : iCliBundle.video_duration;
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

    private static class JJResourceLoader extends ResourceLoader<JJInfoFlowAdResource> {

        private Context context;

        JJResourceLoader(AdModel adModel, Context context) {
            super(adModel);
            this.context = context;
        }

        @Override
        protected void onLoad(AdModel adModel) {
            ICliFactory factory = CPCICliFactoryHolder.getInstance().getICliFactory();
            //通过工厂类构造请求对象AdRequest
            IMultiAdRequest adRequest = factory.createNativeMultiAdRequest();
            //  构造AdRequest参数对象
            Bundle bundle = CPCUtils.buildCommonParams(context);
            AdRequestParam requestParam = new AdRequestParam.Builder()
                    .adslotID(adModel.getAdId())  //必选，广告位id
                    .gdtAppID(BuildConfig.AD_GDT_APP_ID) //可选，广点通appid，如果广点通参与竞价，则必选
                    .adType(AdRequestParam.ADTYPE_FEED)//必选，ADTYPE_BANNER=Banner广告; ADTYPE_INTERACTION=插屏广告; ADTYPE_FEED=信息流广告; ADTYPE_REWARD_VIDEO=激励视频广告;
                    .bannerSize(adModel.getWidth(), adModel.getHeight()) //必选
                    .extraBundle(bundle)
                    .adLoadListener(new AdRequestParam.ADLoadListener() { //必选，加载监听
                        @Override
                        public void onADLoaded(IMultiAdObject object) {
                            notifySuccess(new JJInfoFlowAdResource(object));
                        }

                        @Override
                        public void onAdFailed(String errorMsg) {
                            notifyFail(new RuntimeException("cpc jj load error:" + errorMsg));
                        }
                    })
//                        .extraBundle(extraBundle) //可选，扩展字段bundle
                    .build();
            //AdRequest请求广告
            if (adRequest != null) {
                adRequest.invokeADV(requestParam);
            }else {
                notifyFail(new RuntimeException("JJ ad request null"));
            }
        }
    }


}
