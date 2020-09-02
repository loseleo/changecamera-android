package com.jifen.dandan.ringtone.dagger;

import com.jifen.dandan.ringtone.api.AdApi;
import com.jifen.dandan.common.dagger.component.CommonComponent;
import com.jifen.dandan.common.dagger.scope.AppScope;

import dagger.Component;

@AppScope
@Component(dependencies = CommonComponent.class,modules = ADApiModule.class)
public interface AdComponent {

    AdApi getAdApi();
}
