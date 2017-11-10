package com.squalala.dzbac.google;

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
        modules = GoogleModule.class
)
public interface GoogleComponent {

    void inject(GoogleManagerActivity activity);

    GooglePresenter getGooglePresenter();
}
