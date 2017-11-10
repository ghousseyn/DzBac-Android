package com.squalala.dzbac.ui.list_followers;

import android.app.Activity;


import com.squalala.dzbac.interactors.UserInteractor;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Back Packer
 * Date : 24/09/15
 */
@Module
public class RankingModule {

    private ListFollowerView view;

    public RankingModule(ListFollowerView view) {
        this.view = view;
    }

    @Provides
    public ListFollowerView provideRankingView() {
        return view;
    }

    @Provides
    public ListFollowerPresenter provideRankingPresenter(ListFollowerView view, UserInteractor interactor, Activity activity) {
        return new ListFollowerPresenterImpl(interactor, view, activity);
    }


}
