package com.beige.camera.advertisement.api;

import com.beige.camera.advertisement.api.bean.AdConfigBean;
import com.beige.camera.common.base.bean.ApiResult;

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

}
