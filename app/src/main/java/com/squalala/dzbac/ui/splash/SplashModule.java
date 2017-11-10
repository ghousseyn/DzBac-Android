/**
 * 
 */
package com.squalala.dzbac.ui.splash;

import android.app.Activity;

import com.squalala.dzbac.ui.dialogs.LoginChoiceDialog;
import com.squalala.dzbac.ui.dialogs.SignChoiceDialog;
import com.squalala.dzbac.interactors.SplashInteractor;

import dagger.Module;
import dagger.Provides;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : SplashModule.java
 * Date : 20 juin 2014
 * 
 */

@Module
public class SplashModule {
	
	private SplashView splashView;

	public SplashModule(SplashView splashView) {
		this.splashView = splashView;
	}
	
	@Provides public SplashView provideView() {
		return splashView;
	}

	@Provides
	public SplashPresenter providePresenter(SplashView splashView, SplashInteractor splashInteractor) {
		return new SplashPresenterImpl(splashView, splashInteractor);
	}
	
	@Provides
	public SignChoiceDialog provideSignChoiceDialog(Activity activity) {
		return new SignChoiceDialog(activity);
	}
	
	@Provides
	public LoginChoiceDialog provideChoiceDialog(Activity activity) {
		return new LoginChoiceDialog(activity);
	}

}
