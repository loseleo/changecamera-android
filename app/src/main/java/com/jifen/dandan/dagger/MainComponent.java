/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jifen.dandan.dagger;

//import com.jifen.dandan.ad.dagger.ADApiModule;
import com.jifen.dandan.activity.HomeActivity;
import com.jifen.dandan.activity.WelcomeActivity;
import com.jifen.dandan.ad.dagger.ADApiModule;
import com.jifen.dandan.api.Api;
import com.jifen.dandan.common.dagger.component.CommonComponent;
import com.jifen.dandan.common.dagger.scope.AppScope;

        import dagger.Component;
@AppScope
@Component(dependencies = CommonComponent.class,modules = {AppApiModule.class, ADApiModule.class})
public interface MainComponent {

    Api getApi();
    void inject(WelcomeActivity welcomeActivity);
    void inject(HomeActivity activity);

}