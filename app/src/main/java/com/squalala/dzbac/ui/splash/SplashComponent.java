package com.squalala.dzbac.ui.splash;

import com.squalala.dzbac.ActivityModule;
import com.squalala.dzbac.ActivityScope;
import com.squalala.dzbac.AppComponent;

import dagger.Component;

/**
 * Created by Back Packer
 * Date : 30/04/15
 */
@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = {
                SplashModule.class,
                ActivityModule.class
        }
)
public interface SplashComponent {

    void inject(SplashActivity activity);

    SplashPresenter getSplashPresenter();
}
