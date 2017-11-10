package com.squalala.dzbac.ui.ranking;

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

    private RankingView view;

    public RankingModule(RankingView view) {
        this.view = view;
    }

    @Provides
    public RankingView provideRankingView() {
        return view;
    }

    @Provides
    public RankingPresenter provideRankingPresenter(RankingView view, UserInteractor interactor, Activity activity) {
        return new RankingPresenterImpl(interactor, view, activity);
    }


}
