package com.beige.camera.shortvideo.dagger;

import com.beige.camera.common.dagger.scope.AppScope;
import com.beige.camera.shortvideo.api.ShortVideoApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ShortVideoApiModule {

    @AppScope
    @Provides
    public ShortVideoApi provideApi(Retrofit retrofit) {
        return new ShortVideoApi(retrofit);
    }
}
