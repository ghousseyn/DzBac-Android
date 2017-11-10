package com.squalala.dzbac.ui.sign;

/**
 * Created by Back Packer
 * Date : 30/04/15
 */

import com.squalala.dzbac.ActivityScope;
import com.squalala.dzbac.AppComponent;

import dagger.Component;

@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = SignModule.class
)
public interface SignComponent {

    void inject(SignActivity activity);

    SignPresenter getSignPresenter();
}
