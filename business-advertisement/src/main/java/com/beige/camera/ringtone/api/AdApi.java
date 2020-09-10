package com.beige.camera.ringtone.api;

import com.beige.camera.ringtone.api.bean.AdConfigBean;
import com.beige.camera.common.base.bean.ApiResult;

import java.util.HashMap;
import java.util.Map;

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
