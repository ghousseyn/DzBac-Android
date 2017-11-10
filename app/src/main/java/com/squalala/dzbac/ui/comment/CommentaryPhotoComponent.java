package com.squalala.dzbac.ui.comment;

import com.squalala.dzbac.ActivityModule;
import com.squalala.dzbac.ActivityScope;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.ui.cards.CardModule;

import dagger.Component;

/**
 * Created by Back Packer
 * Date : 02/05/15
 */
@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = {
                CommentaryModule.class,
                ActivityModule.class,
                // ShowItemModule.class,
                CardModule.class
        }
)
public interface CommentaryPhotoComponent {

    void inject(CommentPhotoActivity activity);

    CommentaryPresenter getCommentaryPresenter();
}
