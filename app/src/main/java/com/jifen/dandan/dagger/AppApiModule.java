package com.jifen.dandan.dagger;

import com.jifen.dandan.api.Api;
import com.jifen.dandan.common.dagger.scope.AppScope;

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
