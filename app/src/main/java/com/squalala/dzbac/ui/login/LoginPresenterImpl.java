package com.squalala.dzbac.ui.login;

import com.squalala.dzbac.data.api.ApiResponse.Login;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.interactors.LoginInteractor;
import com.squalala.dzbac.utils.Sha1;

import java.util.Locale;


public class LoginPresenterImpl implements LoginPresenter, OnLoginFinishedListener {

    private LoginView loginView;
    private LoginInteractor loginInteractor;
    private MainPreferences mainPreferences;

    public LoginPresenterImpl(LoginView loginView, LoginInteractor loginInteractor,
    		MainPreferences mainPreferences) {
        this.loginView = loginView;
        this.loginInteractor = loginInteractor;
        this.mainPreferences = mainPreferences;
    }

    @Override public void validateCredentials(String email, String password) {
        loginView.showProgress();
        loginInteractor.login(email, password, this);
    }

    @Override public void onEmailError() {
        loginView.setEmailError();
        loginView.hideProgress();
    }

    @Override public void onPasswordError() {
        loginView.setPasswordError();
        loginView.hideProgress();
    }


    @Override
    public void onServerError() {
        loginView.hideProgress();
        loginView.showMessage("Le serveur est actuellement indisponible réessayer plus tard :D");
    }

    @Override public void onLogin(Login login, String email, String password) {
    	
		mainPreferences.setApiKey(Sha1.encrypt(email.toLowerCase(Locale.getDefault()) + password));
		mainPreferences.setPseudo(login.pseudo);
		mainPreferences.setIdUser(login.id_membre);
		mainPreferences.setEmailUser(email.toLowerCase(Locale.getDefault()));
        mainPreferences.setPasswordUser(password);

        if (mainPreferences.getLevelUser() == 0) {
            loginView.setBannedError();
            loginView.hideProgress();
        }
        else
            loginView.navigateToMainScreen();
    }

	@Override
	public void onLoginError() {
		loginView.setLoginError();
		loginView.hideProgress();
	}

	@Override
	public void onEmailInvalidError() {
		loginView.setEmailInvalidError();
		loginView.hideProgress();
	}
}
