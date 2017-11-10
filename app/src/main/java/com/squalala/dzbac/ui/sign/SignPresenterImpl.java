package com.squalala.dzbac.ui.sign;

import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.interactors.SignInteractor;
import com.squalala.dzbac.utils.Sha1;

import java.util.Locale;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : SignPresenterImpl.java
 * Date : 22 juin 2014
 * 
 */
public class SignPresenterImpl implements SignPresenter, OnSignListener {
	
	private SignView signView;
	private SignInteractor signInteractor;
	private MainPreferences mainPreferences;
	
	public SignPresenterImpl(SignView signView, SignInteractor signInteractor,
			MainPreferences mainPreferences) {
		this.signView = signView;
		this.signInteractor = signInteractor;
		this.mainPreferences = mainPreferences;
	}

	@Override
	public void validateUserdata(String pseudo, String email, String password,
			String confirme_password) {
		signView.showProgress();
		signInteractor.register(pseudo, password, confirme_password, email, this);
	}

	@Override
	public void onPseudoError() {
		signView.hideProgress();
		signView.setPseudoError();
	}

	@Override
	public void onPasswordError() {
		signView.hideProgress();
		signView.setPasswordError();
	}

	@Override
	public void onPasswordDifferentError() {
		signView.hideProgress();
		signView.setPasswordDifferentError();
	}

	@Override
	public void onEmailError() {
		signView.hideProgress();
		signView.setEmailError();
	}


	@Override
	public void onPseudoNotAvailaibleError(String message) {
		signView.hideProgress();
		signView.setPseudoNotAvailaibleError(message);
	}

	@Override
	public void onEmailNotAvailaible(String message) {
		signView.hideProgress();
		signView.setEmailNotAvailaibleError(message);
	}
	
	@Override
	public void onEmailInvalid() {
		signView.hideProgress();
		signView.setEmailInvalidError();
	}


	@Override
	public void onRegisterError(String message) {
		signView.hideProgress();
		signView.showMessage(message);
	}

	@Override
	public void checkEmailUser(String email) {
		signInteractor.afterEmailChanged(email, this);
	}

	@Override
	public void checkPseudoUser(String pseudo, int count) {
		signInteractor.onPseudoChanged(pseudo, count, this);
	}

	@Override
	public void onRegistred(String idMembre, String pseudo, String email, String passwordEncr) {
		
		mainPreferences.setApiKey(Sha1.encrypt(email.toLowerCase(Locale.getDefault()) + passwordEncr));
		mainPreferences.setPseudo(pseudo);
		mainPreferences.setIdUser(idMembre);
		mainPreferences.setEmailUser(email.toLowerCase(Locale.getDefault()));
		mainPreferences.setPasswordUser(passwordEncr);
		
		signView.hideProgress();
		signView.navigateToEditProfile(mainPreferences.getIdUser());
	}

	

}
