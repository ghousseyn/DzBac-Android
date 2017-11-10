package com.squalala.dzbac.ui.list_conversation;

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
                ListConversationModule.class,
                ActivityModule.class
        }
)
public interface ListConversationComponent {
    void inject(ListConversationActivity activity);

    ListConversationPresenter getListConversationPresenter();
}
