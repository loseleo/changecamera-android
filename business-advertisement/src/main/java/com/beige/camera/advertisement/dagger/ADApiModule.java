package com.beige.camera.advertisement.dagger;

import com.beige.camera.advertisement.api.AdApi;
import com.beige.camera.common.dagger.scope.AppScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ADApiModule {

    @AppScope
    @Provides
    AdApi provideADApi(Retrofit retrofit) {
        return new AdApi(retrofit);
    }
}
