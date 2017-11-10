package com.squalala.dzbac.ui.friends;

import com.squalala.dzbac.ActivityModule;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.FragmentScope;

import dagger.Component;

/**
 * Created by Back Packer
 * Date : 04/01/16
 */
@FragmentScope
@Component(
        dependencies = AppComponent.class,
        modules = {
                FriendsModule.class,
                ActivityModule.class
        }
)
public interface FriendsComponent {

    void inject(FriendsFragment friendsFragment);

    FriendsPresenter getFriendsPresenter();
}
