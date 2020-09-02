package com.jifen.dandan.ringtone.dagger;

import com.jifen.dandan.ringtone.api.AdApi;
import com.jifen.dandan.common.dagger.scope.AppScope;

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
