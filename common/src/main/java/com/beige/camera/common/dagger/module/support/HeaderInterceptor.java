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
package com.beige.camera.common.dagger.module.support;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.beige.camera.common.base.BaseApplication;
import com.beige.camera.common.constant.Constant;
import com.beige.camera.common.utils.DeviceUtils;
import com.beige.camera.common.utils.NetworkUtils;
import com.beige.camera.common.utils.PackageUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Retrofit2 Header拦截器。用于保存和设置Cookies
 *
 * @author yuyh.
 * @date 16/8/6.
 */
public final class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request original = chain.request();
        Request.Builder builder = original.newBuilder();
        addHeader(builder,"Device-Code", DeviceUtils.getDevice());
        addHeader(builder,"IMEI", DeviceUtils.getIMEI(BaseApplication.getsInstance().getApplicationContext()));
        addHeader(builder,"AndroidID", DeviceUtils.getAndroidID(BaseApplication.getsInstance().getApplicationContext()));
        addHeader(builder,"Version", "" + PackageUtils.getVersionCode(BaseApplication.getsInstance().getApplicationContext()));
        addHeader(builder,"Version-Name", PackageUtils.getVersionName(BaseApplication.getsInstance().getApplicationContext()));
        addHeader(builder,"Os", Constant.AppName);
        addHeader(builder,"Network", NetworkUtils.getNetWorkType(BaseApplication.getsInstance().getApplicationContext()) + "");
        addHeader(builder,"Dtu", PackageUtils.getChannelName(BaseApplication.getsInstance().getApplicationContext()));
        addHeader(builder,"Source", Constant.AppName);
        addHeader(builder,"Os-Version", DeviceUtils.getSystemVersion());//系统版本
        addHeader(builder,"Mobile-Brand", DeviceUtils.getPhoneBRAND());//手机品牌
        addHeader(builder,"Mobile-Model", DeviceUtils.getPhoneModel());//手机型号

        Request request = builder.build();
        return chain.proceed(request);

    }

    private static void addHeader(@NonNull Request.Builder builder, @NonNull String name, @Nullable String value) {
        if (value == null) {
            value = "";
        }
        builder.addHeader(name, value);
    }
}
