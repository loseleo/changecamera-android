package com.beige.camera.ringtone.core.rewardvideo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.iclicash.advlib.core.ICliBundle;
import com.iclicash.advlib.ui.front.InciteADActivity;
import com.iclicash.advlib.ui.front.InciteVideoListener;
import com.beige.camera.ringtone.core.infoflow.loader.CPCInfoFlowResourceLoader;
import com.beige.camera.ringtone.core.infoflow.loader.res.CPCResource;
import com.beige.camera.common.feed.bean.AdModel;
import com.beige.camera.ringtone.core.goldcoin.GoldCoinOwner;
import com.beige.camera.ringtone.core.goldcoin.GoldCoinUtils;
import com.beige.camera.ringtone.core.loader.ResourceLoader;

import org.json.JSONException;
import org.json.JSONObject;

public class CPCRewardVideoAd extends RewardVideoAd<CPCRewardVideoAd.CpcRewardVideoResource> {

    public CPCRewardVideoAd(AdModel adModel, Activity activity) {
        super(activity, adModel);
    }

    @NonNull
    @Override
    protected ResourceLoader<CpcRewardVideoResource> onCreateResourceLoader(AdModel adModel) {
        return new CPCInfoFlowResourceLoader<CpcRewardVideoResource>(adModel,getActivity()) {

            @NonNull
            @Override
            protected CpcRewardVideoResource convert(ICliBundle iCliBundle) {
                return new CpcRewardVideoResource(iCliBundle);
            }
        };
    }

    @Override
    protected void onSetupAdResource(Activity activity, CpcRewardVideoResource cpcRewardVideoResource) {

        AdModel adModel = getAdModel();
        ICliBundle iCliBundle = cpcRewardVideoResource.getiCliBundle();

        Bundle params = new Bundle();
        params.putString("adslotid", adModel.getAdId());//广告位id
        params.putInt("award_count", 10);//金币数目
//        params.putInt("fullscreen", 1);//金币数目
        params.putString("qk_user_id", "");//用户id
        params.putInt("resource_type", 10);//场景id,值由sdk提供,测试时可以先填10
        params.putBoolean("jump_server", true);//【可选】是否要跳过服务端通信，默认为false
        params.putInt("countdown_style", 2);//默认0：有x按钮，有文案。 1：没有x按钮，没文案 ，2:没有x按钮，有文案
        //计时器关闭按钮X秒后显示，可选（仅在countdown_style = 2时生效）
        params.putInt("showCloseTime", 30);
        params.putString("descriptions", makeDesc(adModel));// 自定义文案
        params.putInt("countdown", 30);// 自定义倒计时时长
        InciteADActivity.showInciteVideo(activity, iCliBundle, params, new InciteVideoListener() {

            boolean isVideoComplete = false;

            @Override
            public void onReward(Bundle bundle) {

            }

            @Override
            public void onVideoComplete(Bundle bundle) {
                //视频播放完成
                isVideoComplete = true;
            }

            @Override
            public void onVideoFail(Bundle bundle) {
                Log.e("onVideoFail", bundle.toString());
                //视频播放失败(未播完也算失败)
                notifyAdFail(new RuntimeException("cpc video play fail"));
            }

            @Override
            public void onADShow(Bundle bundle) {
                //广告展现
                notifyAdDisplay();
            }

            @Override
            public void onADClick(Bundle bundle) {
                //广告点击
                notifyAdClick();
            }

            @Override
            public void onADClose(Bundle bundle) {
                //广告关闭
                if (isVideoComplete) {
                    notifyReward(true);
                }
                getDismissedHelper().notifyAdDismiss();
            }
        });
    }

    public static String makeDesc(AdModel adModel) {
        JSONObject jsonObject = new JSONObject();
        //倒计时上的奖励描述
        addIfNotEmpty(jsonObject, adModel, "countdown_award_des", "可领金币");
        //关闭弹窗上的标题
        addIfNotEmpty(jsonObject, adModel, "close_dialog_title", "任务提示");
        //关闭弹窗上的提示内容
        addIfNotEmpty(jsonObject, adModel, "close_dialog_des", "观看完整广告可获得金币奖励");
        //关闭弹窗上的退出按钮的描述
        addIfNotEmpty(jsonObject, adModel, "close_dialog_exit_des", "放弃金币");
        //关闭弹窗上的继续观看按钮描述
        addIfNotEmpty(jsonObject, adModel, "close_dialog_continue_btn_des", "继续观看");
        //倒计时上的请求等待描述
        addIfNotEmpty(jsonObject, adModel, "countdown_wait_des", "金币即将到账");
        //倒计时上的请求成功描述
        addIfNotEmpty(jsonObject, adModel, "countdown_success_des", "金币已到账");
        //倒计时上的请求重复描述
        addIfNotEmpty(jsonObject, adModel, "countdown_repeat_des", "不能重复领金币");
        //倒计时上的请求失败描述
        addIfNotEmpty(jsonObject, adModel, "countdown_fail_des", "加金币失败");
        //倒计时上的亮色图片
        addIfNotEmpty(jsonObject, adModel, "countdown_icon_light_url", null);
        //倒计时上的灰色图片
        addIfNotEmpty(jsonObject, adModel, "countdown_icon_gray_url", null);
        return jsonObject.toString();
    }

    private static void addIfNotEmpty(JSONObject jsonObject, AdModel adModel, String key, String valueWhenEmpty) {

        try {
            String value = adModel.getExtra(key);
            if (!TextUtils.isEmpty(value)) {
                jsonObject.put(key, value);
            } else {
                jsonObject.put(key, valueWhenEmpty);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static class CpcRewardVideoResource extends CPCResource implements GoldCoinOwner {

        CpcRewardVideoResource(ICliBundle iCliBundle) {
            super(iCliBundle);
        }

        @Override
        public int getGoldCoin() {
            return GoldCoinUtils.getGoldCoin(getiCliBundle());
        }
    }


}
