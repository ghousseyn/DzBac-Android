package com.squalala.dzbac.ui.main;

import com.squalala.dzbac.ActivityModule;
import com.squalala.dzbac.ActivityScope;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.ui.dialogs.DialogModule;

import dagger.Component;

/**
 * Created by Back Packer
 * Date : 30/04/15
 */
@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = {
                MainModule.class,
                ActivityModule.class,
                DialogModule.class
        }
)
public interface MainComponent {
    void inject(MainActivity activity);

    MainPresenter getMainPresenter();


   // PostsFragment getPostsFragment();
}
