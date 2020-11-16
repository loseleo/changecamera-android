package com.beige.camera.shortvideo.dagger;

import com.beige.camera.common.dagger.component.CommonComponent;
import com.beige.camera.common.dagger.scope.AppScope;
import com.beige.camera.shortvideo.api.ShortVideoApi;

import dagger.Component;

@AppScope
@Component(dependencies = CommonComponent.class,modules = {ShortVideoApiModule.class})
public interface ShortVideoComponent {

    ShortVideoApi getApi();

}