package com.beige.camera.advertisement.api;

import com.beige.camera.advertisement.api.bean.AdConfigBean;
import com.beige.camera.common.base.bean.ApiResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AdApiService {

    /**
     * 获取广告配置
     *
     * @param type welcome
     * @return
     */
    @GET("api/v1/config/advertise")
    Observable<ApiResult<AdConfigBean>> getAdConfig(@Query("type") String type);


}
