package com.beige.camera.advertisement.dagger;

import com.beige.camera.advertisement.api.AdApi;
import com.beige.camera.common.dagger.component.CommonComponent;
import com.beige.camera.common.dagger.scope.AppScope;

import dagger.Component;

@AppScope
@Component(dependencies = CommonComponent.class,modules = ADApiModule.class)
public interface AdComponent {

    AdApi getAdApi();
}
