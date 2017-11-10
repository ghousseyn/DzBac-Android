package com.squalala.dzbac.ui.sign;

import com.squalala.dzbac.data.prefs.MainPreferences;
import com.squalala.dzbac.interactors.SignInteractor;

import dagger.Module;
import dagger.Provides;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : SignModule.java
 * Date : 22 juin 2014
 * 
 */
@Module
public class SignModule {
	
	private SignView signView;
	
	public SignModule(SignView signView) {
		this.signView = signView;
	}
	
	@Provides public SignView provideView() {
		return signView;
	}
	
	@Provides
	public SignPresenter providePresenter(SignView signView, SignInteractor signInteractor, 
			MainPreferences mainPreferences) {
		return new SignPresenterImpl(signView, signInteractor, mainPreferences);
	}

}
