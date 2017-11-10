package com.squalala.dzbac.ui.show_conversation;

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
                ShowConversationModule.class,
                ActivityModule.class
        }
)
public interface ShowConversationComponent {

    void inject(ShowConversationActivity activity);

    ShowConversationPresenter getShowConversationPresenter();
}
