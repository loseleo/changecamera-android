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
package com.beige.camera.common.api;

import com.beige.camera.common.api.beans.UploadTokenModel;
import com.beige.camera.common.base.bean.ApiResult;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * https://github.com/JustWayward/BookReader
 *
 * @author yuyh.
 * @date 2016/8/3.
 */
public class CommonApi {

    public static CommonApi instance;

    private CommonApiService service;

    @Inject
    public CommonApi(Retrofit retrofit) {
        service = retrofit.create(CommonApiService.class);
    }

    public Observable<ApiResult<UploadTokenModel>> getUploadSignature() {
        return service.getUploadSignature();
    }
}
