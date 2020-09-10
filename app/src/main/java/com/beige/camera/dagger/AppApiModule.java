package com.beige.camera.dagger;

import com.beige.camera.api.Api;
import com.beige.camera.common.dagger.scope.AppScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class AppApiModule {

    @AppScope
    @Provides
    public Api provideApi(Retrofit retrofit) {
        return new Api(retrofit);
    }
}
