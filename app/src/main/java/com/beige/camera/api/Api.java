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

import com.beige.camera.bean.EffectAgeBean;
import com.beige.camera.bean.EffectImageBean;
import com.beige.camera.bean.TemplatesConfigBean;
import com.beige.camera.bean.VersionInfoBean;
import com.beige.camera.common.base.bean.ApiResult;

import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * https://github.com/JustWayward/BookReader
 *
 * @author yuyh.
 * @date 2016/8/3.
 */
public class Api {

    public static Api instance;

    private ApiService service;

    public Api(Retrofit retrofit) {
        service = retrofit.create(ApiService.class);
    }

    public Observable<ApiResult<VersionInfoBean>> checkVersion() {
        return service.checkVersion();
    }

    public Observable<ApiResult<EffectAgeBean>> getEffectAge(String imageUrl) {
        return service.getEffectAge(imageUrl);
    }

    public Observable<ApiResult<EffectImageBean>> getFaceEditAttr(String imageUrl, String actionType) {
        return service.getFaceEditAttr(imageUrl, actionType);
    }

    public Observable<ApiResult<EffectImageBean>> getImageStyleTrans(String imageUrl, String option) {
        return service.getImageStyleTrans(imageUrl, option);
    }

    public Observable<ApiResult<EffectImageBean>> getImageSelieAnime(String imageUrl) {
        return service.getImageSelieAnime(imageUrl);
    }

    public Observable<ApiResult<EffectImageBean>> getFaceMergeImage(String templateImage, String targetImage) {
        return service.faceMerge(templateImage, targetImage);
    }

    public Observable<ApiResult<EffectImageBean>> bodySeg(String image) {
        return service.bodySeg(image);
    }

    public Observable<ApiResult<TemplatesConfigBean>> getTemplateConfig(String type) {
        return service.getTemplateConfig(type);
    }


}
