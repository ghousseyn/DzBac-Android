package com.squalala.dzbac.ui.ranking;

/**
 * Created by Back Packer
 * Date : 24/09/15
 */

import com.squalala.dzbac.ActivityModule;
import com.squalala.dzbac.AppComponent;
import com.squalala.dzbac.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(
        dependencies = AppComponent.class,
        modules = {
                RankingModule.class,
                ActivityModule.class
        }
)
public interface RankComponent {
    void inject(RankingFragment rankingFragment);

    RankingPresenter getRankingPresenter();
}
