package com.beige.camera.shortvideo.api;

import io.reactivex.Observable;
import retrofit2.Retrofit;

public class ShortVideoApi {

    private ShortVideoApiService service;

    public ShortVideoApi(Retrofit retrofit) {
        service = retrofit.create(ShortVideoApiService.class);
    }


//    public Observable<ApiResult<AdConfigBean>> getAdConfig(String type) {
//        return service.getAdConfig(type);
//    }

}
