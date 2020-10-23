/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.beige.camera.api;

import com.beige.camera.advertisement.api.bean.AdConfigBean;
import com.beige.camera.bean.EffectAgeBean;
import com.beige.camera.bean.EffectImageBean;
import com.beige.camera.bean.TemplatesConfigBean;
import com.beige.camera.bean.VersionInfoBean;
import com.beige.camera.common.base.bean.ApiResult;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * https://github.com/JustWayward/BookReader
 *
 * @author yuyh.
 * @date 2016/8/3.
 */
public interface ApiService {


    @GET("api/v1/config/version_update")
    Observable<ApiResult<VersionInfoBean>> checkVersion();

    @GET("api/v1/config/advertise")
    Observable<ApiResult<AdConfigBean>> getAdvertiseConfig(@Query("type") String type);


    @FormUrlEncoded
    @POST("api/v1/face/detect")
    Observable<ApiResult<EffectAgeBean>> getEffectAge(@Field("image") String image);


    @FormUrlEncoded
    @POST("api/v1/face/edit_attr")
    Observable<ApiResult<EffectImageBean>> getFaceEditAttr(@Field("image") String image, @Field("action_type") String actionType,@Field("age") String age);


    @FormUrlEncoded
    @POST("api/v1/image_process/style_trans")
    Observable<ApiResult<EffectImageBean>> getImageStyleTrans(@Field("image") String image, @Field("option") String option);


    @FormUrlEncoded
    @POST("api/v1/image_process/selie_anime")
    Observable<ApiResult<EffectImageBean>> getImageSelieAnime(@Field("image") String image);

    @FormUrlEncoded
    @POST("api/v1/face/merge")
    Observable<ApiResult<EffectImageBean>> faceMerge(@Field("template_image") String templateImage,@Field("target_image") String targetImage);

    @FormUrlEncoded
    @POST("api/v1/image_process/body_seg")
    Observable<ApiResult<EffectImageBean>> bodySeg(@Field("image") String templateImage);

    @GET("api/v1/config/template")
    Observable<ApiResult<TemplatesConfigBean>> getTemplateConfig(@Query("type") String templateImage);

}