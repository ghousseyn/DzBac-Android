package com.squalala.dzbac.ui.comment;

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
                CommentaryModule.class,
                ActivityModule.class
        }
)
public interface CommentaryComponent {
    void inject(CommentaryActivity activity);

    CommentaryPresenter getCommentaryPresenter();
}
