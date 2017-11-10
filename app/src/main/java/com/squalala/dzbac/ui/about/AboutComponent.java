package com.squalala.dzbac.ui.about;

import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.FragmentScope;

import dagger.Component;

/**
 * Created by Back Packer
 * Date : 30/04/15
 */
@FragmentScope
@Component(
        dependencies = AppComponent.class,
        modules = AboutModule.class
)
public interface AboutComponent {

    void inject(AboutFragment fragment);

    AboutPresenter getAboutPresenter();
}
