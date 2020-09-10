/**
 * Copyright 2016 JustWayward Team
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.beige.camera.common.dagger.module;

import com.beige.camera.common.dagger.module.support.ErrorInterceptor;
import com.beige.camera.common.dagger.module.support.HeaderInterceptor;
import com.beige.camera.common.dagger.module.support.Logger;
import com.beige.camera.common.dagger.module.support.LoggingInterceptor;
import com.beige.camera.common.dagger.module.support.ParamsInterceptor;
import com.beige.camera.common.constant.Constant;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class CommonApiModule {

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient() {

        LoggingInterceptor logging = new LoggingInterceptor(new Logger());
        logging.setLevel(LoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true) // 失败重发
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(new ParamsInterceptor())
                .addInterceptor(new ErrorInterceptor(new ErrorInterceptor.ErrorListener() {
                    @Override
                    public void onUnknownError(String url, Throwable e) {
                        reportApiError(url,"Unknown",e.getMessage());
                    }

                    @Override
                    public void onHttpError(String url, int code, String message) {
                        reportApiError(url,"" + code,message);
                    }
                }))
                .addInterceptor(logging);
        return builder.build();
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(Constant.API_BASE_URL + "/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 添加Rx适配器
                .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
                .client(okHttpClient)
                .build();
    }

    private void reportApiError(String url, String code, String message){
//        HashMap<String, String> extendInfo = new HashMap<>();
//        extendInfo.put("url",url);
//        extendInfo.put("code",code);
//        extendInfo.put("message",message);
//        ReportUtils.onEvent("app","apiError","api",extendInfo);
    }
}