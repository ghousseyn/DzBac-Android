package com.squalala.dzbac.ui.friends;

import android.app.Activity;

import com.squalala.dzbac.interactors.UserInteractor;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Back Packer
 * Date : 04/01/16
 */
@Module
public class FriendsModule {

    private FriendsView friendsView;

    public FriendsModule(FriendsView friendsView) {
        this.friendsView = friendsView;
    }

    @Provides
    public FriendsView provideFriendsView() {
        return friendsView;
    }

    @Provides
    public FriendsPresenter provideFriendsPresenter(FriendsView view, UserInteractor interactor, Activity activity) {
        return new FriendsPresenterImpl(interactor, view, activity);
    }

}
