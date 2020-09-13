package com.beige.camera.dagger;

import com.beige.camera.activity.AgeEffectActivity;
import com.beige.camera.activity.CartoonEffectActivity;
import com.beige.camera.activity.GenderEffectActivity;
import com.beige.camera.activity.HomeActivity;
import com.beige.camera.activity.OldEffectActivity;
import com.beige.camera.activity.WelcomeActivity;
import com.beige.camera.ringtone.dagger.ADApiModule;
import com.beige.camera.api.Api;
import com.beige.camera.common.dagger.component.CommonComponent;
import com.beige.camera.common.dagger.scope.AppScope;

import dagger.Component;

@AppScope
@Component(dependencies = CommonComponent.class,modules = {AppApiModule.class, ADApiModule.class})
public interface MainComponent {

    Api getApi();
    void inject(WelcomeActivity welcomeActivity);
    void inject(HomeActivity activity);
    void inject(AgeEffectActivity activity);
    void inject(CartoonEffectActivity activity);
    void inject(GenderEffectActivity activity);
    void inject(OldEffectActivity activity);

}