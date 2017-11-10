package com.squalala.dzbac.ui.list_items;

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
        modules = {
                ListItemModule.class
        }
)
public interface PostsComponent {
    void inject(PostsFragment fragment);
    void inject(com.squalala.dzbac.ui.signalement.PostsFragment fragment);

    ListItemPresenter getListItemPresenter();
}
