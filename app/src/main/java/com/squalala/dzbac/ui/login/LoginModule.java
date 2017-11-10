package com.squalala.dzbac.ui.login;

import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.interactors.LoginInteractor;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {

    private LoginView view;
    
    public LoginModule(LoginView view) {
        this.view = view;
    }

    @Provides public LoginView provideView() {
        return view;
    }

    @Provides
    public LoginPresenter providePresenter(LoginView loginView, LoginInteractor loginInteractor,
    		MainPreferences mainPreferences) {
        return new LoginPresenterImpl(loginView, loginInteractor, mainPreferences);
    }
}
