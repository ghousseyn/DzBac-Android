package com.squalala.dzbac.ui.add_item;

import com.squalala.dzbac.ActivityModule;
import com.squalala.dzbac.ActivityScope;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.ui.cards.CardModule;
import com.squalala.dzbac.ui.show_item.ShowItemModule;

import dagger.Component;

/**
 * Created by Back Packer
 * Date : 30/04/15
 */
@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules =  {
                AddItemModule.class,
                CardModule.class,
                ActivityModule.class,
                ShowItemModule.class
        }
)
public interface AddItemComponent {
        
        void inject(AddPostActivity activity);
        void inject(AddItemActivity activity);

        AddItemPresenter getAddItemPresenter();



}
