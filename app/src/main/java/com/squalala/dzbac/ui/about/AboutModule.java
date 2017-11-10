package com.squalala.dzbac.ui.about;

import com.squalala.dzbac.interactors.UserInteractor;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Back Packer
 * Date : 15/04/15
 */
@Module
public class AboutModule {

    private AboutView view;

    public AboutModule(AboutView view) {
        this.view = view;
    }

    @Provides
    public AboutView provideAboutView() {
        return view;
    }

    @Provides
    public AboutPresenter provideAboutPresenter(AboutView view, UserInteractor interactor) {
        return new AboutPresenterImpl(view, interactor);
    }


}
