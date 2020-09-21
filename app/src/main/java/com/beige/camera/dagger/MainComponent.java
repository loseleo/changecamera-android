package com.beige.camera.dagger;

import com.beige.camera.activity.AgeEffectActivity;
import com.beige.camera.activity.AnimalEffectActivity;
import com.beige.camera.activity.AnimalFaceEffectActivity;
import com.beige.camera.activity.BabyEffectActivity;
import com.beige.camera.activity.BackgroundEffectActivity;
import com.beige.camera.activity.CartoonEffectActivity;
import com.beige.camera.activity.FaceEditEffectActivity;
import com.beige.camera.activity.FaceMegreEffectActivity;
import com.beige.camera.activity.HomeActivity;
import com.beige.camera.activity.OldEffectActivity;
import com.beige.camera.activity.PastEffectActivity;
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
    void inject(FaceEditEffectActivity activity);
    void inject(OldEffectActivity activity);
    void inject(PastEffectActivity activity);
    void inject(BackgroundEffectActivity activity);
    void inject(FaceMegreEffectActivity activity);
    void inject(AnimalFaceEffectActivity activity);
    void inject(AnimalEffectActivity activity);
    void inject(BabyEffectActivity activity);

}