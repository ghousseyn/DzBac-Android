package com.squalala.dzbac.google;

import com.squalala.dzbac.data.api.ApiResponse.Login;
import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.interactors.LoginInteractor;
import com.squalala.dzbac.interactors.SignInteractor;
import com.squalala.dzbac.utils.Sha1;

import java.util.Locale;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : GooglePresenterImpl.java
 * Date : 21 sept. 2014
 * 
 */
public class GooglePresenterImpl implements GooglePresenter, GoogleManagerListener {
	
	private GoogleView googleView;
	private SignInteractor signInteractor;
	private LoginInteractor loginInteractor;
	private MainPreferences mainPreferences;
	
	public GooglePresenterImpl(GoogleView view, LoginInteractor loginInteractor,
			SignInteractor signInteractor, MainPreferences mainPreferences) {
		googleView = view;
		this.signInteractor = signInteractor;
		this.loginInteractor = loginInteractor;
		this.mainPreferences = mainPreferences;
	}

	@Override
	public void register(String pseudo, String email,
			String idGoogle, String urlAvatar) {
		signInteractor.registerByGoogle(pseudo, email, idGoogle, urlAvatar, this);
	}

	@Override
	public void login(String email,
			String idGoogle) {
		loginInteractor.loginByGoogle(email, idGoogle, this);
	}

	@Override
	public void onServerError() {
		googleView.setError("Le serveur est actuellement indisponible réessayer plus tard :D");
	}

	@Override
	public void onRegister(String idMembre, String pseudo, String email, String idGoogle) {
		
		mainPreferences.setApiKey(Sha1.encrypt(email.toLowerCase(Locale.getDefault()) + idGoogle));
		mainPreferences.setPseudo(pseudo);
		mainPreferences.setIdUser(idMembre);
		mainPreferences.setEmailUser(email.toLowerCase(Locale.getDefault()));
		
		googleView.navigateToEditProfile(mainPreferences.getIdUser());
	}

	@Override
	public void onLogin(Login login, String email, String idGoogle) {
		
		mainPreferences.setApiKey(Sha1.encrypt(email.toLowerCase(Locale.getDefault()) + idGoogle));
		mainPreferences.setPseudo(login.pseudo);
		mainPreferences.setIdUser(login.id_membre);
		mainPreferences.setEmailUser(email.toLowerCase(Locale.getDefault()));
		
		googleView.navigateToHome();
	}

	@Override
	public void onFailRegisterOrLogin(String message) {
		googleView.setError(message);
	}

}
