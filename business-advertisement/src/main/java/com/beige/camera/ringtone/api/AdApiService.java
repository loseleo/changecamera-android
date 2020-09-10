package com.beige.camera.ringtone.api;

import com.beige.camera.ringtone.api.bean.AdConfigBean;
import com.beige.camera.common.base.bean.ApiResult;

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
    @GET("api/v1/config/advertise")
    Observable<ApiResult<AdConfigBean>> getAdConfig(@Query("type") String type);


}
