/**
 * 
 */
package com.squalala.dzbac.ui.splash;

import com.squalala.dzbac.interactors.SplashInteractor;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : SplashPresenterImpl.java
 * Date : 20 juin 2014
 * 
 */
public class SplashPresenterImpl implements SplashPresenter, OnFinishedListener {
	
	private SplashView splashView;
	private SplashInteractor splashInteractor;
	
	public SplashPresenterImpl(SplashView splashView, SplashInteractor splashInteractor) {
		this.splashInteractor = splashInteractor;
		this.splashView = splashView;
	}

	@Override
	public void onUserNoConnected() {
		splashView.showBtnLoginSignUp();
	}

	@Override
	public void onUserConnected() {
		splashView.navigateToMainScreen();
	}

	@Override
	public void validateStateUser() {
		splashInteractor.stateUser(this);
	}

	@Override
	public void onNoConnection() {
		splashView.setConnectionError();
	}


}
