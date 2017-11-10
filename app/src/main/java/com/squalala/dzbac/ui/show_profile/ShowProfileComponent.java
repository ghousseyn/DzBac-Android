package com.squalala.dzbac.ui.show_profile;

import com.squalala.dzbac.ActivityModule;
import com.squalala.dzbac.ActivityScope;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.ui.dialogs.DialogModule;
import com.squalala.dzbac.ui.cards.CardModule;
import com.squalala.dzbac.ui.edit_profile.EditProfileActivity;

import dagger.Component;

/**
 * Created by Back Packer
 * Date : 30/04/15
 */
@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = {
                ShowProfileModule.class,
                CardModule.class,
                ActivityModule.class,
                DialogModule.class
        }
)
public interface ShowProfileComponent {
    void inject(ShowProfileActivity activity);
    void inject(EditProfileActivity activity);

    ShowProfilePresenter getShowProfilePresenter();

}
