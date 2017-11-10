package com.squalala.dzbac.ui.show_item;

import com.squalala.dzbac.ActivityModule;
import com.squalala.dzbac.ActivityScope;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.data.api.MessagingService;
import com.squalala.dzbac.ui.dialogs.DialogModule;
import com.squalala.dzbac.ui.cards.CardModule;

import dagger.Component;

/**
 * Created by Back Packer
 * Date : 30/04/15
 */
@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules =  {
                ShowItemModule.class,
                CardModule.class,
                ActivityModule.class,
                DialogModule.class
        }
)
public interface ShowItemComponent {

    void inject(ShowItemActivity activity);
    void inject(ShowAnnonceActivity activity);
   // void inject(AddPostActivity activity);

    ShowItemPresenter getShowItemPresenter();

    MessagingService getMessagingService();

}
