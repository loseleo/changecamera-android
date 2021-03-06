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
package com.beige.camera.common.dagger.component;

import android.app.Application;
import android.content.Context;

import com.beige.camera.common.dagger.module.AppModule;
import com.beige.camera.common.dagger.module.CommonApiModule;
import com.beige.camera.common.upload.UploadFileManager;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author zhangning.
 * @date 2016/8/3.
 */
@Singleton
@Component(modules = {AppModule.class, CommonApiModule.class})
public interface CommonComponent {
    Context getContext();

    Application getApplication();

    OkHttpClient getOkHttpClient();

    Retrofit getRetrofit();

    void inject(UploadFileManager uploadFileManager);
}