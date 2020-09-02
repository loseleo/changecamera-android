package com.jifen.dandan.ringtone.api;

import com.jifen.dandan.ringtone.api.bean.AdConfigBean;
import com.jifen.dandan.ringtone.api.bean.IssueGoldCoinResultBean;
import com.jifen.dandan.common.base.bean.ApiResult;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AdApiService {

    /**
     * 获取广告配置
     *
     * @param type welcome
     * @return
     */
    @GET("config/advertise")
    Observable<ApiResult<AdConfigBean>> getAdConfig(@Query("type") String type);


    /**
     * 发放激励视频金币
     * @return
     */
    @FormUrlEncoded
    @POST("coin/cpc_incentive_video")
    Observable<ApiResult<IssueGoldCoinResultBean>> issueRewardVideoAdGoldCoins(@FieldMap Map<String,String> body);

    /**
     * 发放激励视频金币
     * @return
     */
    @FormUrlEncoded
    @POST("coin/charge")
    Observable<ApiResult<IssueGoldCoinResultBean>> issueGoldCoins(@FieldMap Map<String,String> body);
}
