package com.jifen.dandan.ad.api;

import com.jifen.dandan.ad.api.bean.AdConfigBean;
import com.jifen.dandan.ad.api.bean.IssueGoldCoinResultBean;
import com.jifen.dandan.common.base.bean.ApiResult;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Retrofit;

public class AdApi {

    private AdApiService service;

    public AdApi(Retrofit retrofit) {
        service = retrofit.create(AdApiService.class);
    }


    public Observable<ApiResult<AdConfigBean>> getAdConfig(String type) {
        return service.getAdConfig(type);
    }

    /**
     * 发放激励视频奖励
     * @param amount  奖励金币数
     * @param prizeType  cpc奖励位置 签到位：sign 激励视频位置: incentive
     * @return
     */
    public Observable<ApiResult<IssueGoldCoinResultBean>> issueRewardVideoAdGoldCoins(String amount, String prizeType){

        Map<String,String> params = new HashMap<>();
        params.put("amount",amount);
        params.put("prize_id","500");
        params.put("prize_type",prizeType);
        return service.issueRewardVideoAdGoldCoins(params);
    }

    /**
     * 发放奖励
     * @param amount
     * @param prizeType
     * @param adId
     * @param adCode
     * @param adChannel
     * @return
     */
    public Observable<ApiResult<IssueGoldCoinResultBean>> issueGoldCoins(String amount, String prizeType,String adId,String adCode,String adChannel){
        Map<String,String> params = new HashMap<>();
        params.put("amount",amount);
        params.put("prize_type",prizeType);
        params.put("ad_id",adId);
        params.put("ad_code",adCode);
        params.put("ad_channel",adChannel);
        return service.issueGoldCoins(params);
    }
}
